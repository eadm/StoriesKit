package ru.nobird.android.stories.ui.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.ui.custom.StoryView
import ru.nobird.android.stories.ui.delegate.StoryPartViewDelegate

class StoryAdapter(
        private val storyView: StoryView,
        internal val story: Story,
        private val storyPartDelegates: List<StoryPartViewDelegate>
): PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

    override fun getCount() =
            story.parts.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val part = story.parts[position]
        val delegate = storyPartDelegates.find { it.isForViewType(part) }
            ?: throw IllegalStateException("No delegate for storyPart = $part")

        return delegate.onBindView(storyView, container, position, part).also(container::addView)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}