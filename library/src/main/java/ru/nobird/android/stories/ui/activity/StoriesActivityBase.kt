package ru.nobird.android.stories.ui.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_stories.*
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.transition.SharedTransitionsManager
import ru.nobird.android.stories.transition.SharedTransitionIntentBuilder
import ru.nobird.android.stories.ui.adapter.StoriesPagerAdapter
import ru.nobird.android.stories.ui.custom.StoryView
import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate

abstract class StoriesActivityBase : AppCompatActivity() {
    private var position = 0

    private lateinit var sharedTransitionDelegate: SharedTransitionContainerDelegate

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stories: List<Story> = intent.getParcelableArrayListExtra(SharedTransitionIntentBuilder.EXTRA_STORIES) ?: emptyList()
        val key = intent.getStringExtra(SharedTransitionIntentBuilder.EXTRA_KEY) ?: ""

        sharedTransitionDelegate = SharedTransitionsManager.getTransitionDelegate(key)!!

        storiesPager.adapter = StoriesPagerAdapter(stories, object : StoryView.StoryProgressListener {
            override fun onNext() {
                storiesPager.currentItem++
            }

            override fun onPrev() {
                storiesPager.currentItem--
            }
        })
        storiesPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                sharedTransitionDelegate.onPositionChanged(position)
                this@StoriesActivityBase.position = position
            }
        })

        content.visibility = View.INVISIBLE

        if (savedInstanceState == null) {
            content.doOnPreDraw {
                runEnterAnimation()
            }
        } else {
            content.visibility = View.VISIBLE
        }

        content.onDismiss = {
            val view = sharedTransitionDelegate.getSharedView(position)
            val bounds = Rect()
            view.getGlobalVisibleRect(bounds)
            content.playExitAnimation(bounds) {
                view.visibility = View.VISIBLE
                finish()
            }
        }

        position = if (savedInstanceState == null) {
            intent.getIntExtra(SharedTransitionIntentBuilder.EXTRA_POSITION, 0)
        } else {
            storiesPager.currentItem
        }

        storiesPager.currentItem = position
        sharedTransitionDelegate.onPositionChanged(position)
    }

    private fun runEnterAnimation() {
        content.playEnterAnimation(getViewBounds(position))
    }

    private fun getViewBounds(position: Int) = Rect().apply {
        sharedTransitionDelegate.getSharedView(position).getGlobalVisibleRect(this)
    }

    override fun onPause() {
        if (isFinishing) {
            overridePendingTransition(0, 0)
        }
        super.onPause()
    }
}