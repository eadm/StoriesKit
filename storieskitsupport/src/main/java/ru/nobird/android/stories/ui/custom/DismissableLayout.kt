package ru.nobird.android.stories.ui.custom

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import ru.nobird.android.stories.ui.animation.SupportViewPropertyAnimator

class DismissableLayout
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        private val MIN_DELTA = Resources.getSystem().displayMetrics.density * 16

        private const val MIN_SCALE = 0.9f
        private const val ANIMATION_DURATION_MS = 500L
        private const val FADE_ANIMATION_DURATION_MS = ANIMATION_DURATION_MS / 5
    }

    private val overshootInterpolator by lazy { OvershootInterpolator(1.5f) }

    private val quarterHeight = Resources.getSystem().displayMetrics.heightPixels / 8

    private var startX = 0f
    private var startY = 0f
    private var intercepted = false
    private var isCompleteMovement = false

    private var rollbackAnimation: AnimatorSet? = null

    private val listeners = mutableListOf<DismissListener>()

    private val stubView = ImageView(context).apply {
        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        scaleType = ImageView.ScaleType.FIT_XY
        visibility = View.GONE
    }

    lateinit var content: View

    init {
        addView(stubView)
    }

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

                val wasInterceptedBefore = intercepted
                intercepted =
                        intercepted ||
                        isOwnEvent ||
                        ady > MIN_DELTA && ady > adx

                if (intercepted) {
                    if (!wasInterceptedBefore) {
                        listeners.forEach(DismissListener::onDragStarted)
                    }

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
                    listeners.forEach(DismissListener::onDismiss)
                } else {
                    playRollbackAnimation()
                    listeners.forEach(DismissListener::onDragCancelled)
                }

                parent.requestDisallowInterceptTouchEvent(false)
                isCompleteMovement = false
            }
        }
    }

    fun playEnterAnimation(startView: View, onAnimationEnd: (() -> Unit)? = null) {
        isEnabled = false

        setStabView(startView)

        val startBounds = Rect()
        startView.getGlobalVisibleRect(startBounds)

        val offset = Point()
        val targetBounds = Rect()
        getGlobalVisibleRect(targetBounds, offset)

        pivotX = width / 2f
        pivotY = height / 2f

        startBounds.offset(-offset.x, -offset.y)
        targetBounds.offset(-offset.x, -offset.y)

        translationX = startBounds.left + startBounds.width() / 2 - pivotX
        translationY = startBounds.top + startBounds.height() / 2 - pivotY

        scaleX = startBounds.width().toFloat() / targetBounds.width()
        scaleY = startBounds.height().toFloat() / targetBounds.height()

        visibility = View.VISIBLE

        stubView.alpha = 1f
        stubView.visibility = View.VISIBLE
        SupportViewPropertyAnimator(stubView)
                .setDuration(FADE_ANIMATION_DURATION_MS)
                .alpha(0f)
                .withEndAction {
                    stubView.visibility = View.GONE
                }
                .start()

        content.alpha = 0f
        content.animate()
                .setDuration(FADE_ANIMATION_DURATION_MS)
                .alpha(1f)
                .start()

        SupportViewPropertyAnimator(this)
                .setInterpolator(overshootInterpolator)
                .setDuration(ANIMATION_DURATION_MS)
                .scaleX(1f)
                .scaleY(1f)
                .translationX(0f)
                .translationY(0f)
                .withEndAction {
                    isEnabled = true
                    onAnimationEnd?.invoke()
                }
                .start()
    }

    fun playExitAnimation(targetView: View, onAnimationEnd: (() -> Unit)? = null) {
        isEnabled = false

        setStabView(targetView)

        val targetBounds = Rect()
        targetView.getGlobalVisibleRect(targetBounds)

        stubView.alpha = 0f
        stubView.visibility = View.VISIBLE
        SupportViewPropertyAnimator(stubView)
                .setStartDelay(ANIMATION_DURATION_MS - FADE_ANIMATION_DURATION_MS)
                .setDuration(FADE_ANIMATION_DURATION_MS)
                .alpha(1f)
                .withEndAction {
                    stubView.visibility = View.GONE
                }
                .start()

        content.alpha = 1f
        content.animate()
                .setStartDelay(ANIMATION_DURATION_MS - FADE_ANIMATION_DURATION_MS)
                .setDuration(FADE_ANIMATION_DURATION_MS)
                .alpha(0f)
                .start()

        SupportViewPropertyAnimator(this)
                .setDuration(ANIMATION_DURATION_MS / 2)
                .scaleX(targetBounds.width() / width.toFloat())
                .scaleY(targetBounds.height() / height.toFloat())
                .translationX(targetBounds.left + targetBounds.width() / 2 - pivotX)
                .translationY(targetBounds.top + targetBounds.height() / 2 - pivotY)
                .withEndAction {
                    onAnimationEnd?.invoke()

                }
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

    private fun setStabView(source: View) {
        val bitmap = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        source.draw(canvas)
        stubView.setImageBitmap(bitmap)
    }

    fun addDismissListener(listener: DismissListener) {
        listeners += listener
    }

    fun removeDismissListener(listener: DismissListener) {
        listeners -= listener
    }

    interface DismissListener {
        fun onDragStarted() {}
        fun onDragCancelled() {}
        fun onDismiss() {}
    }
}