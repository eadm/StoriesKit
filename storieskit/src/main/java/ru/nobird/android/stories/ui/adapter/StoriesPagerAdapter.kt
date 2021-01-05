package ru.nobird.android.stories.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.minusAssign
import androidx.core.view.plusAssign
import androidx.viewpager.widget.PagerAdapter
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.ui.custom.StoryView

class StoriesPagerAdapter(
    val stories: List<Story>,
    private val delegates: List<StoryPartAdapterDelegate>,
    private val listener: StoryView.StoryProgressListener
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

    override fun getCount(): Int =
            stories.size

    override fun instantiateItem(container: ViewGroup, position: Int) =
        StoryView(container.context).also {
            it.tag = position
            it.progressListener = listener
            it.story = stories[position]
            it.delegates = delegates
            container += it
        }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container -= `object` as View
    }
}