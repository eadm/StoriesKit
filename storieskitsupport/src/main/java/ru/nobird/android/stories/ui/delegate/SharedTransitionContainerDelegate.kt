package ru.nobird.android.stories.ui.delegate

import android.view.View

interface SharedTransitionContainerDelegate {
    fun getSharedView(position: Int): View?
    fun onPositionChanged(position: Int)
}