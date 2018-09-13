package ru.nobird.android.stories.sample.model

import android.os.Parcel
import android.os.Parcelable
import ru.nobird.android.stories.model.StoryPart

class PlainStoryPart(
        duration: Long,
        cover: String
) : StoryPart(duration, cover), Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(duration)
        parcel.writeString(cover)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<PlainStoryPart> {
        override fun createFromParcel(parcel: Parcel) = PlainStoryPart(
                parcel.readLong(),
                parcel.readString()!!
        )

        override fun newArray(size: Int) =
                arrayOfNulls<PlainStoryPart>(size)
    }
}