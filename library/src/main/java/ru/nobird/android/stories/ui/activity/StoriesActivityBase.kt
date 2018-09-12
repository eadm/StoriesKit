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
import ru.nobird.android.stories.ui.delegate.PlainStoryPartViewDelegate

abstract class StoriesActivityBase : AppCompatActivity() {
    private var position = 0

    private lateinit var key: String

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stories: List<Story> = intent.getParcelableArrayListExtra(SharedTransitionIntentBuilder.EXTRA_STORIES)
                ?: emptyList()
        key = intent.getStringExtra(SharedTransitionIntentBuilder.EXTRA_KEY) ?: ""

        storiesPager.adapter = StoriesPagerAdapter(stories, object : StoryView.StoryProgressListener {
            override fun onNext() {
                storiesPager.currentItem++
            }

            override fun onPrev() {
                storiesPager.currentItem--
            }
        }, listOf(PlainStoryPartViewDelegate()))
        storiesPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                SharedTransitionsManager.getTransitionDelegate(key)?.onPositionChanged(position)
                this@StoriesActivityBase.position = position
                storiesPager.findViewWithTag<StoryView>(position)?.resume()
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
            val view = SharedTransitionsManager.getTransitionDelegate(key)?.getSharedView(position)!!
            val bounds = Rect()
            view?.getGlobalVisibleRect(bounds)
            content.playExitAnimation(view) {
                view?.visibility = View.VISIBLE
                finish()
            }
        }

        position = if (savedInstanceState == null) {
            intent.getIntExtra(SharedTransitionIntentBuilder.EXTRA_POSITION, 0)
        } else {
            storiesPager.currentItem
        }

        storiesPager.setCurrentItem(position, false)
        SharedTransitionsManager.getTransitionDelegate(key)?.onPositionChanged(position)
    }

    private fun runEnterAnimation() {
        val view = SharedTransitionsManager.getTransitionDelegate(key)?.getSharedView(position) ?: return
        content.playEnterAnimation(view)
    }

    override fun onPause() {
        if (isFinishing) {
            overridePendingTransition(0, 0)
        }
        super.onPause()
    }
}