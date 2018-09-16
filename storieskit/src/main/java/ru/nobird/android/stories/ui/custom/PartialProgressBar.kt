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
import androidx.core.content.res.use
import ru.nobird.android.stories.R

class PartialProgressBar
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_GAP_DP = 2f
        const val DEFAULT_RADIUS_DP = 1f

        /**
         * Constant represents minimal time interval to consider prev item click
         */
        private const val PREV_CLICK_THRESHOLD_MS = 500L
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

    var parts: LongArray = longArrayOf()
        set(value) {
            field = value
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
        context.obtainStyledAttributes(attrs, R.styleable.PartialProgressBar).use {
            progressBackgroundColor = it.getColor(R.styleable.PartialProgressBar_progressBackgroundColor, progressBackgroundColor)
            progressForegroundColor = it.getColor(R.styleable.PartialProgressBar_progressForegroundColor, progressForegroundColor)

            gap = it.getDimension(R.styleable.PartialProgressBar_gap, gap)
            radius = it.getDimension(R.styleable.PartialProgressBar_radius, radius)
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
        }

        if (currentPart in 0 until totalParts) {
            rect.left = (elementWidth + gap) * currentPart
            rect.right = rect.left + elementWidth * currentPartProgress
            canvas.clipRect(rect) // clip current part progress bar
            rect.right = rect.left + elementWidth
            canvas.drawRoundRect(rect, radius, radius, progressForegroundPaint)
        }
    }

    fun prev() {
        val currentItemTime = currentPartProgress * parts.getOrElse(currentPart) { 0 }
        val part = if (currentItemTime > PREV_CLICK_THRESHOLD_MS) {
            currentPart
        } else {
            currentPart - 1
        }

        if (part < 0) {
            currentPart = 0
            progressListener?.onPrev()
        } else {
            currentPart = part
            progressListener?.onPositionChanged(part)
            resume()
        }

    }

    fun next() {
        val part = currentPart + 1
        if (part >= parts.size) {
            currentPart = parts.size - 1
            progressListener?.onNext()
        } else {
            currentPart = part
            progressListener?.onPositionChanged(part)
            resume()
        }
    }

    fun pause() {
        animator?.cancel()
    }

    fun resume() {
        initAnimator(currentPartProgress)
        animator?.start()
    }

    private fun initAnimator(from: Float = 0f) {
        animator?.cancel()
        currentPartProgress = from
        animator = parts.getOrNull(currentPart)?.let { partDuration ->
             ValueAnimator.ofFloat(from, 1f).apply {
                 addUpdateListener {
                     val value = animatedValue as Float
                     currentPartProgress = value
                     if (value == 1f) {
                         next()
                     }
                 }
                 interpolator = null
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