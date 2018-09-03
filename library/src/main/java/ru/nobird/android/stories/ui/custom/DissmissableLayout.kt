package ru.nobird.android.stories.ui.custom

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import ru.nobird.android.stories.ui.animation.SupportViewPropertyAnimator

class DissmissableLayout
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        private val MIN_DELTA = Resources.getSystem().displayMetrics.density * 16

        private const val MIN_SCALE = 0.9f
        private const val ANIMATION_DURATION_MS = 500L
    }

    private val overshootInterpolator by lazy { OvershootInterpolator(1.5f) }

    private val quarterHeight = Resources.getSystem().displayMetrics.heightPixels / 8

    private var startX = 0f
    private var startY = 0f
    private var intercepted = false

    private var rollbackAnimation: AnimatorSet? = null

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        processTouchEvent(event, false)
        return intercepted || super.onInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        processTouchEvent(event, true)
        return event.action == MotionEvent.ACTION_DOWN && isEnabled || super.onTouchEvent(event)
    }

    private fun processTouchEvent(event: MotionEvent, isOwnEvent: Boolean) {
        if (!isEnabled) return

        when(event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                rollbackAnimation?.cancel()
                startX = event.x
                startY = event.y
                parent.requestDisallowInterceptTouchEvent(true)
                intercepted = false
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - startX
                val dy = event.y - startY

                val adx = Math.abs(dx)
                val ady = Math.abs(dy)

                intercepted =
                        intercepted ||
                        isOwnEvent ||
                        ady > MIN_DELTA && ady > adx

                if (intercepted) {
                    translationY += dy

                    val scale = 1f - Math.min(1f, Math.abs(translationY) / quarterHeight) * (1f - MIN_SCALE)
                    scaleX = scale
                    scaleY = scale
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                translationY = 0f

                scaleX = 1f
                scaleY = 1f
            }

            MotionEvent.ACTION_UP -> {
//                if (translationY > quarterHeight) {
//
//                } else {
                    playRollbackAnimation()
//                }

                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
    }

    private fun playRollbackAnimation() {
        rollbackAnimation?.cancel()
        rollbackAnimation = SupportViewPropertyAnimator(this)
                .setDuration(ANIMATION_DURATION_MS)
                .setInterpolator(overshootInterpolator)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .start()
    }
}