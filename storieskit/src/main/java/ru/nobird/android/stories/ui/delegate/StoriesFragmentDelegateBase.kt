package ru.nobird.android.stories.ui.delegate

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.transition.SharedTransitionIntentBuilder
import ru.nobird.android.stories.ui.adapter.StoriesPagerAdapter
import ru.nobird.android.stories.ui.custom.DismissableLayout
import ru.nobird.android.stories.ui.extension.getStoryViewAt
import ru.nobird.android.stories.ui.extension.pauseCurrentStory
import ru.nobird.android.stories.ui.extension.resumeCurrentStory
import ru.nobird.android.stories.ui.listener.StoriesContainerPageChangeListener
import ru.nobird.android.stories.ui.listener.StoriesContainerProgressListener

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
                storiesViewPager.resumeCurrentStory()
            }

            override fun onDismiss() {
                finish()
            }
        })

        val sharedTransitionDelegate = getSharedTransitionContainerDelegate()

        if (savedInstanceState == null) {
            val position = arguments.getInt(SharedTransitionIntentBuilder.EXTRA_POSITION)
            storiesViewPager.setCurrentItem(position, false)

            dismissableLayout.visibility = View.INVISIBLE
            dismissableLayout.doOnPreDraw {
                val view = sharedTransitionDelegate?.getSharedView(storiesViewPager.currentItem)
                if (view != null) {
                    dismissableLayout.playEnterAnimation(view) {
                        sharedTransitionDelegate.onPositionChanged(position)
                        storiesViewPager.getStoryViewAt(position)?.resume()
                    }
                } else {
                    sharedTransitionDelegate?.onPositionChanged(position)
                    storiesViewPager.getStoryViewAt(position)?.resume()
                }
            }
        } else {
            sharedTransitionDelegate?.onPositionChanged(storiesViewPager.currentItem)
        }
    }

    private fun initStoriesPager() {
        storiesViewPager.adapter =
            StoriesPagerAdapter(stories, storyPartDelegates, StoriesContainerProgressListener(storiesViewPager, ::onComplete))

        storiesViewPager
            .addOnPageChangeListener(StoriesContainerPageChangeListener(storiesViewPager, ::getSharedTransitionContainerDelegate))
    }

    private fun getSharedTransitionContainerDelegate(): SharedTransitionContainerDelegate? =
        fragment.targetFragment as? SharedTransitionContainerDelegate

    fun onResume() {
        storiesViewPager.resumeCurrentStory()
    }

    fun onPause() {
        storiesViewPager.pauseCurrentStory()
    }

    /**
     * Calls when stories completed
     */
    protected open fun onComplete() {
        finish()
    }

    open fun finish() {
        val sharedTransitionDelegate = getSharedTransitionContainerDelegate()

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