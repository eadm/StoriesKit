package ru.nobird.android.stories.ui.delegate

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewTreeObserver
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.transition.SharedTransitionIntentBuilder
import ru.nobird.android.stories.ui.adapter.StoriesPagerAdapter
import ru.nobird.android.stories.ui.custom.DismissableLayout
import ru.nobird.android.stories.ui.custom.StoryView

abstract class StoriesFragmentDelegateBase(
    private val fragment: Fragment
) {
    protected abstract val dismissableLayout: DismissableLayout
    protected abstract val storiesViewPager: ViewPager

    protected abstract val arguments: Bundle

    protected abstract val storyPartDelegates: List<StoryPartViewDelegate>

    private lateinit var stories: List<Story>

    fun onCreate(savedInstanceState: Bundle?) {
        stories = arguments.getParcelableArrayList(SharedTransitionIntentBuilder.EXTRA_STORIES)!!

        initStoriesPager()

        dismissableLayout.content = storiesViewPager
        dismissableLayout.addDismissListener(object : DismissableLayout.DismissListener {
            override fun onDragCancelled() {
                storiesViewPager.findViewWithTag<StoryView>(storiesViewPager.currentItem)?.resume()
            }

            override fun onDismiss() {
                finish()
            }
        })

        val sharedTransitionDelegate = fragment.targetFragment as? SharedTransitionContainerDelegate

        if (savedInstanceState == null) {
            val position = arguments.getInt(SharedTransitionIntentBuilder.EXTRA_POSITION)
            storiesViewPager.setCurrentItem(position, false)

            dismissableLayout.visibility = View.INVISIBLE

            val vto = dismissableLayout.viewTreeObserver
            vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (vto.isAlive) {
                        vto.removeOnPreDrawListener(this)
                    } else {
                        dismissableLayout.viewTreeObserver.removeOnPreDrawListener(this)
                    }
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
                    return true
                }
            })
        } else {
            sharedTransitionDelegate?.onPositionChanged(storiesViewPager.currentItem)
        }
    }

    private fun initStoriesPager() {
        storiesViewPager.adapter = StoriesPagerAdapter(stories, storyPartDelegates, object : StoryView.StoryProgressListener {
            override fun onNext() {
                if (storiesViewPager.currentItem == (storiesViewPager.adapter?.count ?: 0) - 1) {
                    onComplete()
                }
                storiesViewPager.currentItem++
            }

            override fun onPrev() {
                storiesViewPager.currentItem--
            }
        })

        storiesViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    storiesViewPager
                        .findViewWithTag<StoryView>(storiesViewPager.currentItem)
                        ?.resume()
                }
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                (fragment.targetFragment as? SharedTransitionContainerDelegate)
                    ?.onPositionChanged(position)

                storiesViewPager.findViewWithTag<StoryView>(position)?.restartCurrentPart()
            }
        })
    }

    fun onResume() {
        storiesViewPager
            .findViewWithTag<StoryView>(storiesViewPager.currentItem)
            ?.resume()
    }

    fun onPause() {
        storiesViewPager
            .findViewWithTag<StoryView>(storiesViewPager.currentItem)
            ?.pause()
    }

    /**
     * Calls when stories completed
     */
    protected open fun onComplete() {
        finish()
    }

    open fun finish() {
        val sharedTransitionDelegate = fragment.targetFragment as? SharedTransitionContainerDelegate

        val view = sharedTransitionDelegate?.getSharedView(storiesViewPager.currentItem)
        if (view == null) {
            sharedTransitionDelegate?.onPositionChanged(-1)
            onClose()
        } else {
            dismissableLayout.playExitAnimation(view) {
                sharedTransitionDelegate?.onPositionChanged(-1)
                onClose()
            }
        }
    }

    /**
     * Close current screen. Due to different possible navigation implementations this should be implemented by user.
     */
    protected abstract fun onClose()
}