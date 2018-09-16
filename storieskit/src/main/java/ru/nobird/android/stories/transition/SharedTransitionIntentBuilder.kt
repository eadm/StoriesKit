package ru.nobird.android.stories.transition

import android.content.Context
import android.content.Intent
import ru.nobird.android.stories.model.Story

object SharedTransitionIntentBuilder {
    internal const val EXTRA_KEY      = "ru.nobird.android.stories.transition.key"
    internal const val EXTRA_POSITION = "ru.nobird.android.stories.transition.position"
    internal const val EXTRA_STORIES  = "ru.nobird.android.stories.transition.stories"

    fun createIntent(
            context: Context,
            clazz: Class<*>,
            key: String,
            itemPosition: Int,
            stories: List<Story>
    ): Intent =
            Intent(context, clazz)
                    .putExtra(EXTRA_KEY, key)
                    .putExtra(EXTRA_POSITION, itemPosition)
                    .putParcelableArrayListExtra(EXTRA_STORIES, ArrayList(stories))
}