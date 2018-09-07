package ru.nobird.android.stories.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.view_story.view.*
import ru.nobird.android.stories.R
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.model.StoryPart
import ru.nobird.android.stories.ui.adapter.StoryAdapter

class StoryView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val progress: PartialProgressBar
    private val pager: StoryViewPager

    private val gestureDetector: GestureDetectorCompat

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

        gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(event: MotionEvent): Boolean {
                if (event.x > width / 2) {
                    progress.next()
                } else {
                    progress.prev()
                }
                return true
            }
        })

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
        gestureDetector.onTouchEvent(event)
        when(event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN ->
                progress.pause()

            MotionEvent.ACTION_UP ->
                progress.resume()
        }
    }

    fun pause() {
        progress.pause()
    }

    fun resume() {
        progress.resume()
    }

    interface StoryProgressListener {
        fun onNext()
        fun onPrev()
    }
}