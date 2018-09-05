package ru.nobird.android.stories.ui.custom

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import ru.nobird.android.stories.ui.animation.SupportViewPropertyAnimator

class DismissableLayout
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
    private var isCompleteMovement = false

    private var rollbackAnimation: AnimatorSet? = null

    var onReady: (() -> Unit)? = null
    var onDismiss: (() -> Unit)? = null

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

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                rollbackAnimation?.cancel()
                startX = event.x
                startY = event.y
                parent.requestDisallowInterceptTouchEvent(true)
                intercepted = false
                isCompleteMovement = true
            }

            MotionEvent.ACTION_MOVE -> {
                if (!isCompleteMovement) return

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

                isCompleteMovement = false
            }

            MotionEvent.ACTION_UP -> {
                if (Math.abs(translationY) > quarterHeight) {
                    onDismiss?.invoke()
                } else {
                    playRollbackAnimation()
                }

                parent.requestDisallowInterceptTouchEvent(false)
                isCompleteMovement = false
            }
        }
    }

    fun playEnterAnimation(startBounds: Rect) {
        isEnabled = false

        val offset = Point()
        val targetBounds = Rect()
        getGlobalVisibleRect(targetBounds, offset)

        startBounds.offset(-offset.x, -offset.y)
        targetBounds.offset(-offset.x, -offset.y)

        translationX = startBounds.left + startBounds.width() / 2 - pivotX
        translationY = startBounds.top + startBounds.height() / 2 - pivotY

        val scale = Math.min(startBounds.width().toFloat() / targetBounds.width(), startBounds.height().toFloat() / targetBounds.height())

        scaleX = scale
        scaleY = scale

        visibility = View.VISIBLE

        SupportViewPropertyAnimator(this)
                .setInterpolator(overshootInterpolator)
                .setDuration(ANIMATION_DURATION_MS)
                .scaleX(1f)
                .scaleY(1f)
                .translationX(0f)
                .translationY(0f)
                .withEndAction(Runnable {
                    isEnabled = true
                })
                .start()
    }

    fun playExitAnimation(targetBounds: Rect, onAnimationEnd: (() -> Unit)? = null) {
        isEnabled = false

        val offset = intArrayOf(0, 0)
        (parent as? View)?.getLocationOnScreen(offset)

        targetBounds.offset(-offset[0], -offset[1])

        val scale = Math.min(targetBounds.width() / width.toFloat(), targetBounds.height() / height.toFloat())

        SupportViewPropertyAnimator(this)
                .setDuration(ANIMATION_DURATION_MS / 2)
                .scaleX(scale)
                .scaleY(scale)
                .translationX(targetBounds.left + targetBounds.width() / 2 - pivotX)
                .translationY(targetBounds.top + targetBounds.height() / 2 - pivotY)
                .withEndAction(Runnable {
                    onAnimationEnd?.invoke()
                })
                .start()
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