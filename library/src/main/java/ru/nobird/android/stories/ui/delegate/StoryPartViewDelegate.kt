package ru.nobird.android.stories.ui.delegate

import android.view.View
import android.view.ViewGroup
import ru.nobird.android.stories.model.StoryPart
import ru.nobird.android.stories.ui.custom.StoryView

abstract class StoryPartViewDelegate {
    abstract fun onBindView(storyView: StoryView, container: ViewGroup, position: Int, part: StoryPart): View
    abstract fun isForViewType(part: StoryPart): Boolean
}