package ru.nobird.android.stories.model

import android.os.Parcelable

interface StoryPart : Parcelable {
    val duration: Long
    val cover: String
}
