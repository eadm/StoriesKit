package ru.nobird.android.stories.ui.delegate

import android.app.Activity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import ru.nobird.android.stories.ui.custom.DismissableLayout

abstract class StoriesActivityDelegateBase(
        private val activity: Activity
) {
    protected abstract val dismissableLayout: DismissableLayout
    protected abstract val storiesViewPager: ViewPager

    protected abstract val arguments: Bundle

    fun onCreate(savedInstanceState: Bundle?) {

    }

    fun onPause() {
        if (activity.isFinishing) {
            activity.overridePendingTransition(0, 0)
        }
    }
}