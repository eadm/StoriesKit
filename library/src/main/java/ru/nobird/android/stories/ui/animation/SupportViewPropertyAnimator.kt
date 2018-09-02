package ru.nobird.android.stories.ui.animation

import android.animation.*
import android.view.View

class SupportViewPropertyAnimator(private val view: View) {
    private val animators = ArrayList<Animator>()
    private val set = AnimatorSet()

    private var endAction: Runnable? = null

    fun withEndAction(action: Runnable) = apply {
        endAction = action
    }

    fun setInterpolator(interpolator: TimeInterpolator) = apply {
        set.interpolator = interpolator
    }

    fun setStartDelay(duration: Long) = apply {
        set.startDelay = duration
    }

    fun setDuration(duration: Long) = apply {
        set.duration = duration
    }

    fun rotation(angle: Float) = apply {
        animators.add(ObjectAnimator.ofFloat(view, View.ROTATION, angle))
    }

    fun translationX(value: Float) = apply {
        animators.add(ObjectAnimator.ofFloat(view, View.TRANSLATION_X, value))
    }

    fun translationY(value: Float) = apply {
        animators.add(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, value))
    }

    fun alpha(value: Float) = apply {
        animators.add(ObjectAnimator.ofFloat(view, View.ALPHA, value))
    }

    fun scaleX(value: Float) = apply {
        animators.add(ObjectAnimator.ofFloat(view, View.SCALE_X, value))
    }

    fun scaleY(value: Float) = apply {
        animators.add(ObjectAnimator.ofFloat(view, View.SCALE_Y, value))
    }

    fun start() = set.apply {
        playTogether(animators)
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                endAction?.run()
            }
        })
        start()
    }
}