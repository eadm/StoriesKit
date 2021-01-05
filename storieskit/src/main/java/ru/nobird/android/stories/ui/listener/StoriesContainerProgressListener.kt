package ru.nobird.android.stories.ui.listener

import androidx.viewpager2.widget.ViewPager2
import ru.nobird.android.stories.ui.custom.StoryView
import ru.nobird.android.stories.ui.extension.resumeCurrentStory

class StoriesContainerProgressListener(
    private val storiesViewPager: ViewPager2,
    private val onComplete: () -> Unit
) : StoryView.StoryProgressListener {
    override fun onNext() {
        if (storiesViewPager.currentItem == (storiesViewPager.adapter?.itemCount ?: 0) - 1) {
            onComplete()
        }
        storiesViewPager.currentItem++
    }

    override fun onPrev() {
        if (storiesViewPager.currentItem == 0) {
            storiesViewPager.resumeCurrentStory()
        } else {
            storiesViewPager.currentItem--
        }
    }
}