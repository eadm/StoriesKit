package ru.nobird.android.stories.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class StoryProgressView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    var totalPart: Int = 0
    var currentPart: Int = 0

    var gap: Float = 4f
    var radius: Float = 2f

    private var currentPartProgress = 0f
    private var isPaused = false

    private val rect = RectF()
    private val disabledElementPaint = Paint().apply {
        isAntiAlias = true
        color = 0x88FFFFFF.toInt()
    }

    private val enabledElementPaint = Paint().apply {
        isAntiAlias = true
        color = 0xFFFFFFFF.toInt()
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

            val primaryPaint = if (i >= currentPart) disabledElementPaint else enabledElementPaint
            canvas.drawRoundRect(rect, radius, radius, primaryPaint)

            if (i == currentPart) {
                rect.right = rect.left + elementWidth * currentPartProgress
                canvas.drawRoundRect(rect, radius, radius, enabledElementPaint)
            }
        }
    }

    fun pause() {

    }

    fun resume() {

    }
}