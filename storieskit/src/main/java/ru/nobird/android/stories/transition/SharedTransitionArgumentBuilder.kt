package ru.nobird.android.stories.transition

import android.os.Bundle
import ru.nobird.android.stories.model.Story

object SharedTransitionArgumentBuilder {
    fun createArguments(key: String, position: Int = 0, stories: List<Story>): Bundle =
        Bundle(3)
            .apply {
                putString(SharedTransitionIntentBuilder.EXTRA_KEY, key)
                putInt(SharedTransitionIntentBuilder.EXTRA_POSITION, position)
                putParcelableArrayList(SharedTransitionIntentBuilder.EXTRA_STORIES, ArrayList(stories))
            }
}