package ru.nobird.android.stories.ui.listener

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate
import ru.nobird.android.stories.ui.extension.getStoryViewAt
import ru.nobird.android.stories.ui.extension.resumeCurrentStory

class StoriesContainerPageChangeListener(
    private val storiesViewPager: ViewPager2,
    private val getSharedTransitionContainerDelegate: () -> SharedTransitionContainerDelegate?
) : ViewPager2.OnPageChangeCallback() {
    override fun onPageScrollStateChanged(state: Int) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            storiesViewPager.resumeCurrentStory()
        }
    }

    override fun onPageSelected(position: Int) {
        getSharedTransitionContainerDelegate()
            ?.onPositionChanged(position)

        storiesViewPager.getStoryViewAt(position)?.restartCurrentPart()
    }
}