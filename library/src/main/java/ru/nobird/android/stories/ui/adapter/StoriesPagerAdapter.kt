package ru.nobird.android.stories.ui.adapter

import android.view.*
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.minusAssign
import androidx.core.view.plusAssign
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.view_story.view.*
import ru.nobird.android.stories.R

class StoriesPagerAdapter(
        private val colors: IntArray
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean =
            view == `object`

    override fun getCount(): Int =
            colors.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.view_story, container, false)
        view.setBackgroundColor(colors[position])

        with(view.storyProgress) {
            parts = longArrayOf(10000, 5000, 5000)
            currentPart = 0
            resume()
        }

        val gestureDetector = GestureDetectorCompat(container.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(event: MotionEvent): Boolean {
                if (event.x > view.width / 2) {
                    view.storyProgress.next()
                } else {
                    view.storyProgress.prev()
                }
                return true
            }
        })

        view.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            when(event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN ->
                    view.storyProgress.pause()

                MotionEvent.ACTION_UP ->
                    view.storyProgress.resume()
            }
            true
        }

        container += view
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container -= `object` as View
    }
}