package ru.nobird.android.stories.ui.extension

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import ru.nobird.android.core.model.safeCast
import ru.nobird.android.stories.ui.custom.StoryView

internal fun ViewPager2.getStoryViewAt(position: Int): StoryView? =
    getChildAt(0)
        .safeCast<RecyclerView>()
        ?.findViewHolderForAdapterPosition(position)
        ?.itemView
        ?.safeCast()

internal fun ViewPager2.getCurrentStoryView(): StoryView? =
    getStoryViewAt(currentItem)

internal fun ViewPager2.pauseCurrentStory() {
    getCurrentStoryView()?.pause()
}

internal fun ViewPager2.resumeCurrentStory() {
    getCurrentStoryView()?.resume()
}