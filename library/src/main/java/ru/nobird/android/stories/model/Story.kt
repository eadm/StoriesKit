package ru.nobird.android.stories.model

import android.os.Parcel
import android.os.Parcelable

data class Story(
        val parts: List<StoryPart>
) : Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(parts)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Story> {
        override fun createFromParcel(parcel: Parcel): Story {
            return Story(mutableListOf<StoryPart>().apply {
                parcel.readList(this, StoryPart::class.java.classLoader)
            })
        }

        override fun newArray(size: Int): Array<Story?> {
            return arrayOfNulls(size)
        }
    }
}