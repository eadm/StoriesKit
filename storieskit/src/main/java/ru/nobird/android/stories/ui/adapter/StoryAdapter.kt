package ru.nobird.android.stories.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.minusAssign
import androidx.core.view.plusAssign
import androidx.viewpager.widget.PagerAdapter
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.ui.custom.StoryView
import ru.nobird.android.stories.ui.delegate.StoryPartViewDelegate

class StoryAdapter(
        private val storyView: StoryView,
        private val storyPartDelegates: List<StoryPartViewDelegate>,
        _story: Story
): PagerAdapter() {
    var story: Story = _story
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

    override fun getCount() =
            story.parts.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val part = story.parts[position]
        val delegate = storyPartDelegates.find { it.isForViewType(part) }
            ?: throw IllegalStateException("No delegate for storyPart = $part")

        return delegate.onBindView(storyView, container, position, part).also(container::plusAssign)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container -= (`object` as View)
    }
}