package ru.nobird.android.stories.transition

import android.os.Bundle
import ru.nobird.android.stories.model.Story

object SharedTransitionArgumentBuilder {
    fun createArguments(stories: List<Story>, position: Int = 0): Bundle =
        Bundle(2)
            .apply {
                putInt(SharedTransitionIntentBuilder.EXTRA_POSITION, position)
                putParcelableArrayList(SharedTransitionIntentBuilder.EXTRA_STORIES, ArrayList(stories))
            }
}