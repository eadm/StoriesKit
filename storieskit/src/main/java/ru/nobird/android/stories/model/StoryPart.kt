package ru.nobird.android.stories.model

import android.os.Parcelable

abstract class StoryPart(
        val duration: Long,
        val cover: String
) : Parcelable