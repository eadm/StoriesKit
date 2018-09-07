package ru.nobird.android.stories.ui.delegate

import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import ru.nobird.android.stories.model.PlainStoryPart
import ru.nobird.android.stories.model.StoryPart

class PlainStoryPartViewDelegate : StoryPartViewDelegate() {
    override fun onBindView(container: ViewGroup, part: StoryPart): View =
            View(container.context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                setBackgroundColor((part as PlainStoryPart).cover.toColorInt())
            }

    override fun isForViewType(part: StoryPart): Boolean =
            part is PlainStoryPart
}