package ru.nobird.android.stories.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.minusAssign
import androidx.core.view.plusAssign
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.view_story.view.*
import ru.nobird.android.stories.R

class StoriesPagerAdapter(private val colors: IntArray) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

    override fun getCount(): Int =
            colors.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.view_story, container, false)
        view.setBackgroundColor(colors[position])

        with(view.storyProgress) {
            totalPart = 5
            currentPart = 2
        }

//        view.setOnTouchListener { view, event ->
//
//        }

        container += view
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container -= `object` as View
    }
}