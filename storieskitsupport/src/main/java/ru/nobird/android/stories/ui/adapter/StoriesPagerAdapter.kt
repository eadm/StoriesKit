package ru.nobird.android.stories.ui.adapter

import android.support.v4.view.PagerAdapter
import android.view.*
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.ui.custom.StoryView
import ru.nobird.android.stories.ui.delegate.StoryPartViewDelegate

class StoriesPagerAdapter(
        val stories: List<Story>,
        private val delegates: List<StoryPartViewDelegate>,
        private val listener: StoryView.StoryProgressListener
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

    override fun getCount(): Int =
            stories.size

    override fun instantiateItem(container: ViewGroup, position: Int) =
            StoryView(container.context).apply {
                tag = position
                progressListener = listener
                adapter = StoryAdapter(this, delegates, stories[position])
                container.addView(this)
            }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}