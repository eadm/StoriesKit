package ru.nobird.android.stories.ui.extension

import android.support.v4.view.ViewPager
import ru.nobird.android.stories.ui.custom.StoryView

internal fun ViewPager.getStoryViewAt(position: Int): StoryView? =
    findViewWithTag(position)

internal fun ViewPager.getCurrentStoryView(): StoryView? =
    findViewWithTag(currentItem)

internal fun ViewPager.pauseCurrentStory() {
    getCurrentStoryView()?.pause()
}

internal fun ViewPager.resumeCurrentStory() {
    getCurrentStoryView()?.resume()
}