package ru.nobird.android.stories.ui.listener

import androidx.viewpager.widget.ViewPager
import ru.nobird.android.stories.ui.custom.StoryView

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
            storiesViewPager
                .findViewWithTag<StoryView>(storiesViewPager.currentItem)
                ?.resume()
        } else {
            storiesViewPager.currentItem--
        }
    }
}