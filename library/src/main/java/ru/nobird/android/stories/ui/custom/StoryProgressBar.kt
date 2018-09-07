package ru.nobird.android.stories.ui.custom

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

class StoryProgressBar
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_GAP_DP = 2f
        const val DEFAULT_RADIUS_DP = 1f
    }

    var totalPart: Int = 0
    var currentPart: Int = 0

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

    private var currentPartProgress = 0f
    private var isPaused = false

    private val rect = RectF()
    private val progressBackgroundPaint = Paint().apply {
        isAntiAlias = true
        color = progressBackgroundColor
    }

    private val progressForegroundPaint = Paint().apply {
        isAntiAlias = true
        color = progressForegroundColor
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.StoryProgressBar).use {
            progressBackgroundColor = it.getColor(R.styleable.StoryProgressBar_progressBackgroundColor, progressBackgroundColor)
            progressForegroundColor = it.getColor(R.styleable.StoryProgressBar_progressForegroundColor, progressForegroundColor)

            gap = it.getDimension(R.styleable.StoryProgressBar_gap, gap)
            radius = it.getDimension(R.styleable.StoryProgressBar_radius, radius)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (totalPart < 1) return

        val height = height.toFloat()
        val width = width.toFloat()

        val elementWidth = (width - gap * (totalPart - 1)) / totalPart

        rect.top = 0f
        rect.bottom = height

        for (i in 0 until totalPart) {
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

    fun pause() {

    }

    fun resume() {

    }
}