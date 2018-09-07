package ru.nobird.android.stories.ui.delegate

import android.view.View
import android.view.ViewGroup
import ru.nobird.android.stories.model.StoryPart

abstract class StoryPartViewDelegate {
    abstract fun onBindView(container: ViewGroup, part: StoryPart): View
    abstract fun isForViewType(part: StoryPart): Boolean
}