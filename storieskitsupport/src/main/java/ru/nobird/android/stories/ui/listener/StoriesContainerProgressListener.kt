package ru.nobird.android.stories.ui.listener

import android.support.v4.view.ViewPager
import ru.nobird.android.stories.ui.custom.StoryView
import ru.nobird.android.stories.ui.extension.resumeCurrentStory

class StoriesContainerProgressListener(
    private val storiesViewPager: ViewPager,
    private val onComplete: () -> Unit
) : StoryView.StoryProgressListener {
    override fun onNext() {
        if (storiesViewPager.currentItem == (storiesViewPager.adapter?.count ?: 0) - 1) {
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