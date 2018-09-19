package ru.nobird.android.stories.ui.delegate

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.viewpager.widget.ViewPager
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.transition.SharedTransitionIntentBuilder
import ru.nobird.android.stories.transition.SharedTransitionsManager
import ru.nobird.android.stories.ui.adapter.StoriesPagerAdapter
import ru.nobird.android.stories.ui.custom.DismissableLayout
import ru.nobird.android.stories.ui.custom.StoryView

abstract class StoriesActivityDelegateBase(
        private val activity: Activity
) {
    protected abstract val dismissableLayout: DismissableLayout
    protected abstract val storiesViewPager: ViewPager

    protected abstract val arguments: Bundle

    protected abstract val storyPartDelegates: List<StoryPartViewDelegate>

    private lateinit var key: String
    private lateinit var stories: List<Story>

    fun onCreate(savedInstanceState: Bundle?) {
        key = arguments.getString(SharedTransitionIntentBuilder.EXTRA_KEY)!!
        stories = arguments.getParcelableArrayList(SharedTransitionIntentBuilder.EXTRA_STORIES)!!

        initStoriesPager()

        dismissableLayout.content = storiesViewPager
        dismissableLayout.onDismiss = ::finish

        val sharedTransitionDelegate = SharedTransitionsManager.getTransitionDelegate(key)

        if (savedInstanceState == null) {
            val position = arguments.getInt(SharedTransitionIntentBuilder.EXTRA_POSITION)
            storiesViewPager.setCurrentItem(position, false)

            dismissableLayout.visibility = View.INVISIBLE
            dismissableLayout.doOnPreDraw {
                val view = sharedTransitionDelegate?.getSharedView(storiesViewPager.currentItem)
                if (view != null) {
                    dismissableLayout.playEnterAnimation(view) {
                        sharedTransitionDelegate.onPositionChanged(position)
                        storiesViewPager.findViewWithTag<StoryView>(position)?.resume()
                    }
                } else {
                    sharedTransitionDelegate?.onPositionChanged(position)
                    storiesViewPager.findViewWithTag<StoryView>(position)?.resume()
                }
            }
        } else {
            sharedTransitionDelegate?.onPositionChanged(storiesViewPager.currentItem)
        }
    }

    private fun initStoriesPager() {
        storiesViewPager.adapter = StoriesPagerAdapter(stories, storyPartDelegates, object : StoryView.StoryProgressListener {
            override fun onNext() {
                if (storiesViewPager.currentItem == (storiesViewPager.adapter?.count ?: 0) - 1) {
                    finish()
                }
                storiesViewPager.currentItem++
            }

            override fun onPrev() {
                storiesViewPager.currentItem--
            }
        })

        storiesViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                SharedTransitionsManager.getTransitionDelegate(key)?.onPositionChanged(position)
                storiesViewPager.findViewWithTag<StoryView>(position)?.resume()
            }
        })
    }

    fun onPause() {
        if (activity.isFinishing) {
            activity.overridePendingTransition(0, 0)
        }
    }

    open fun finish() {
        val view = SharedTransitionsManager.getTransitionDelegate(key)?.getSharedView(storiesViewPager.currentItem)
        if (view == null) {
            SharedTransitionsManager.getTransitionDelegate(key)?.onPositionChanged(-1)
            activity.finish()
        } else {
            dismissableLayout.playExitAnimation(view) {
                SharedTransitionsManager.getTransitionDelegate(key)?.onPositionChanged(-1)
                activity.finish()
            }
        }
    }
}