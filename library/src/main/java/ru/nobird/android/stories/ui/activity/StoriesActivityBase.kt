package ru.nobird.android.stories.ui.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_stories.*
import ru.nobird.android.stories.ui.adapter.StoriesPagerAdapter
import ru.nobird.android.stories.ui.custom.DismissableLayout

abstract class StoriesActivityBase : AppCompatActivity() {
    companion object {
        const val EXTRA_COLORS = "colors"
        const val EXTRA_CORE_VIEW_POSITION = "view_position"
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storiesPager.adapter = StoriesPagerAdapter(intent.getIntArrayExtra(EXTRA_COLORS) ?: intArrayOf())
        storiesPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

            }
        })

        content.visibility = View.INVISIBLE

        val preDrawListener = object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                content.viewTreeObserver.removeOnPreDrawListener(this)
                runEnterAnimation()
                return true
            }
        }
        content.viewTreeObserver.addOnPreDrawListener(preDrawListener)

        content.onDismiss = {
            content.playExitAnimation(intent.getParcelableExtra(EXTRA_CORE_VIEW_POSITION), this::finish)
        }
    }

    private fun runEnterAnimation() {
        val startBounds = intent.getParcelableExtra<Rect>(EXTRA_CORE_VIEW_POSITION)
        content.playEnterAnimation(startBounds)
    }

    override fun onPause() {
        if (isFinishing) {
            overridePendingTransition(0, 0)
        }
        super.onPause()
    }
}