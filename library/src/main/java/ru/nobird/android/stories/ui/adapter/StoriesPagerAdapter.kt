package ru.nobird.android.stories.ui.adapter

import android.view.*
import androidx.core.view.minusAssign
import androidx.core.view.plusAssign
import androidx.viewpager.widget.PagerAdapter
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.ui.custom.StoryView
import ru.nobird.android.stories.ui.delegate.PlainStoryPartViewDelegate

class StoriesPagerAdapter(
        private val stories: List<Story>,
        private val listener: StoryView.StoryProgressListener
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

    override fun getCount(): Int =
            stories.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = StoryView(container.context)

        val story = stories[position]

        view.progressListener = listener
        view.adapter = StoryAdapter(story, listOf(PlainStoryPartViewDelegate()))
        view.resume()

        container += view
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container -= `object` as View
    }
}