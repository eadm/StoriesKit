package ru.nobird.android.stories.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.content.res.use
import ru.nobird.android.stories.R
import kotlin.properties.Delegates

class PartialProgressBar
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_GAP_DP = 2f
        const val DEFAULT_RADIUS_DP = 1f
    }

    var gap: Float = Resources.getSystem().displayMetrics.density * DEFAULT_GAP_DP
    var radius: Float = Resources.getSystem().displayMetrics.density * DEFAULT_RADIUS_DP

    @ColorInt
    var progressBackgroundColor: Int = 0x77FFFFFF
        set(value) {
            field = value
            progressBackgroundPaint.color = value
        }

    @ColorInt
    var progressForegroundColor: Int = 0xFFFFFFFF.toInt()
        set(value) {
            field = value
            progressForegroundPaint.color = value
        }

    var progressListener: PartialProgressListener? = null

    var parts: LongArray by Delegates.observable(longArrayOf()) {_, _, _ ->
        currentPart = 0
    }

    var currentPart: Int = 0
        set(value) {
            field = value
            currentPartProgress = 0f
            initAnimator()
        }

    private var currentPartProgress = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val rect = RectF()
    private val progressBackgroundPaint = Paint().apply {
        isAntiAlias = true
        color = progressBackgroundColor
    }

    private val progressForegroundPaint = Paint().apply {
        isAntiAlias = true
        color = progressForegroundColor
    }

    private var animator: ValueAnimator? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.StoryProgressBar).use {
            progressBackgroundColor = it.getColor(R.styleable.StoryProgressBar_progressBackgroundColor, progressBackgroundColor)
            progressForegroundColor = it.getColor(R.styleable.StoryProgressBar_progressForegroundColor, progressForegroundColor)

            gap = it.getDimension(R.styleable.StoryProgressBar_gap, gap)
            radius = it.getDimension(R.styleable.StoryProgressBar_radius, radius)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (parts.isEmpty()) return
        val totalParts = parts.size

        val height = height.toFloat()
        val width = width.toFloat()

        val elementWidth = (width - gap * (totalParts - 1)) / totalParts

        rect.top = 0f
        rect.bottom = height

        for (i in 0 until totalParts) {
            rect.left = (elementWidth + gap) * i
            rect.right = rect.left + elementWidth

            val primaryPaint = if (i >= currentPart) progressBackgroundPaint else progressForegroundPaint
            canvas.drawRoundRect(rect, radius, radius, primaryPaint)

            if (i == currentPart) {
                rect.right = rect.left + elementWidth * currentPartProgress
                canvas.drawRoundRect(rect, radius, radius, progressForegroundPaint)
            }
        }
    }

    fun prev() {
        currentPart--
        progressListener?.let {
            if (currentPart < 0) {
                it.onPrev()
            } else {
                it.onPositionChanged(currentPart)
            }
        }
        resume()
    }

    fun next() {
        currentPart++
        progressListener?.let {
            if (currentPart == parts.size) {
                it.onNext()
            } else {
                it.onPositionChanged(currentPart)
            }
        }
        resume()
    }

    fun pause() {
        animator?.cancel()
    }

    fun resume() {
        initAnimator(currentPartProgress)
        animator?.start()
    }

    private fun initAnimator(from: Float = 0f) {
        currentPartProgress = from
        animator = parts.getOrNull(currentPart)?.let { partDuration ->
             ValueAnimator.ofFloat(from, 1f).apply {
                 addUpdateListener {
                     currentPartProgress = animatedValue as Float
                 }
                 doOnEnd {
                     next()
                 }
                 duration = partDuration
            }
        }
    }

    interface PartialProgressListener {
        fun onPositionChanged(position: Int)
        fun onNext()
        fun onPrev()
    }
}