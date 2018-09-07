package ru.nobird.android.stories.ui.adapter

import android.view.*
import androidx.core.view.minusAssign
import androidx.core.view.plusAssign
import androidx.viewpager.widget.PagerAdapter
import ru.nobird.android.stories.model.PlainStoryPart
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.ui.custom.StoryView
import ru.nobird.android.stories.ui.delegate.PlainStoryPartViewDelegate

class StoriesPagerAdapter(
        private val colors: IntArray
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

    override fun getCount(): Int =
            colors.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = StoryView(container.context)

        val story = Story(listOf(
                PlainStoryPart(duration = 5000, cover = "#FF233D4D"),
                PlainStoryPart(duration = 5000, cover = "#FFFE7F2D")
        ))

        view.adapter = StoryAdapter(story, listOf(
                PlainStoryPartViewDelegate()
        ))

        container += view
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container -= `object` as View
    }
}