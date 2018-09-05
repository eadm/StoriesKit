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
import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate

abstract class StoriesActivityBase : AppCompatActivity() {
    companion object {
        const val EXTRA_COLORS = "colors"
        const val EXTRA_COLOR_POSITION = "position"

        var sharedTransitionContainerDelegate: SharedTransitionContainerDelegate? = null
    }

    private var position = 0

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storiesPager.adapter = StoriesPagerAdapter(intent.getIntArrayExtra(EXTRA_COLORS) ?: intArrayOf())
        storiesPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                sharedTransitionContainerDelegate?.onPositionChanged(position)
                this@StoriesActivityBase.position = position
            }
        })

        content.visibility = View.INVISIBLE

        if (savedInstanceState == null) {
            val preDrawListener = object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    runEnterAnimation()
                    return true
                }
            }
            content.viewTreeObserver.addOnPreDrawListener(preDrawListener)
        } else {
            content.visibility = View.VISIBLE
        }

        content.onDismiss = {
            val view = sharedTransitionContainerDelegate?.getSharedView(position)
            val bounds = Rect()
            view?.getGlobalVisibleRect(bounds)
            content.playExitAnimation(bounds) {
                view?.visibility = View.VISIBLE
                finish()
            }
        }

        position = if (savedInstanceState == null) {
            intent.getIntExtra(EXTRA_COLOR_POSITION, 0)
        } else {
            storiesPager.currentItem
        }

        storiesPager.currentItem = position
        sharedTransitionContainerDelegate?.onPositionChanged(position)
    }

    private fun runEnterAnimation() {
        content.playEnterAnimation(getViewBounds(position))
    }

    private fun getViewBounds(position: Int) = Rect().apply {
        sharedTransitionContainerDelegate?.getSharedView(position)?.getGlobalVisibleRect(this)
    }

    override fun onPause() {
        if (isFinishing) {
            overridePendingTransition(0, 0)
        }
        super.onPause()
    }
}