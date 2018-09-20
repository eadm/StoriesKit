package ru.nobird.android.stories.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_story.view.*
import ru.nobird.android.stories.R
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.model.StoryPart
import ru.nobird.android.stories.ui.adapter.StoryAdapter

class StoryView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        private val TAP_TIMEOUT_MS = ViewConfiguration.getTapTimeout()
        private val TOUCH_SLOP_SQUARE_PX = (8 * Resources.getSystem().displayMetrics.density).let { it * it }
    }

    private val progress: PartialProgressBar
    private val pager: StoryViewPager

    private lateinit var lastDownEvent: MotionEvent

    var progressListener: StoryProgressListener? = null

    var adapter: StoryAdapter? = null
        set(value) {
            field = value
            story = value?.story
            pager.adapter = value
        }

    private var story: Story? = null
        set(value) {
            field = value
            progress.parts = value?.parts?.map(StoryPart::duration)?.toLongArray() ?: longArrayOf()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_story, this, true)
        progress = storyProgress
        pager = storyViewPager

        progress.progressListener = object : PartialProgressBar.PartialProgressListener {
            override fun onPositionChanged(position: Int) {
                pager.setCurrentItem(position, false)
            }

            override fun onNext() {
                progressListener?.onNext()
            }

            override fun onPrev() {
                progressListener?.onPrev()
            }
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        processTouchEvent(event)
        return super.onInterceptTouchEvent(event)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        processTouchEvent(event)
        return true
    }

    private fun processTouchEvent(event: MotionEvent) {
        when(event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                lastDownEvent = MotionEvent.obtain(event)
                progress.pause()
            }

            MotionEvent.ACTION_UP -> {
                val dx = event.x - lastDownEvent.x
                val dy = event.y - lastDownEvent.y
                val dt = event.eventTime - lastDownEvent.eventTime
                if (dx * dx + dy * dy < TOUCH_SLOP_SQUARE_PX && dt < TAP_TIMEOUT_MS) {
                    if (event.x > width / 2) {
                        progress.next()
                    } else {
                        progress.prev()
                    }
                } else {
                    progress.resume()
                }
            }
        }
    }

    fun pause() {
        progress.pause()
    }

    fun resume() {
        progress.resume()
    }

    fun restart() {
        progress.currentPart = 0
        resume()
    }

    fun restartCurrentPart() {
        progress.currentPart = progress.currentPart
        resume()
    }

    interface StoryProgressListener {
        fun onNext()
        fun onPrev()
    }
}