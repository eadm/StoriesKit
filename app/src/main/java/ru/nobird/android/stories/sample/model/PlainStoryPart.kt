package ru.nobird.android.stories.sample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.nobird.android.stories.model.StoryPart

@Parcelize
class PlainStoryPart(
    override val duration: Long,
    override val cover: String
) : StoryPart, Parcelable