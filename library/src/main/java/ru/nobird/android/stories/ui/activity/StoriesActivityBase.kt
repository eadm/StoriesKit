package ru.nobird.android.stories.ui.activity

import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.OvershootInterpolator
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_stories.*
import ru.nobird.android.stories.ui.adapter.StoriesPagerAdapter
import ru.nobird.android.stories.ui.animation.SupportViewPropertyAnimator

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
    }

    private fun runEnterAnimation() {
        val startRect = intent.getParcelableExtra<Rect>(EXTRA_CORE_VIEW_POSITION)

        val offset = Point()
        val targetRect = Rect()
        content.getGlobalVisibleRect(targetRect, offset)
        startRect.offset(-offset.x, -offset.y)
        targetRect.offset(-offset.x, -offset.y)

        content.pivotX = 0f
        content.pivotY = 0f

        content.translationX = startRect.left.toFloat()
        content.translationY = startRect.top.toFloat()

        val scale = Math.min(startRect.width().toFloat() / targetRect.width(), startRect.height().toFloat() / targetRect.height())

        content.scaleX = scale
        content.scaleY = scale

        content.visibility = View.VISIBLE

        SupportViewPropertyAnimator(content)
                .setInterpolator(OvershootInterpolator(1.5f))
                .setDuration(400L)
                .scaleX(1f)
                .scaleY(1f)
                .translationX(0f)
                .translationY(0f)
                .start()
    }

    override fun onPause() {
        if (isFinishing) {
            overridePendingTransition(0, 0)
        }
        super.onPause()
    }
}