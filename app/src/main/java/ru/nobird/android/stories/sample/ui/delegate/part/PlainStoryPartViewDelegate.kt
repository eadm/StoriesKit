package ru.nobird.android.stories.sample.ui.delegate.part

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.nobird.android.stories.model.StoryPart
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.model.PlainStoryPart
import ru.nobird.android.stories.ui.adapter.StoryPartAdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class PlainStoryPartViewDelegate : StoryPartAdapterDelegate() {
    override fun isForViewType(position: Int, data: StoryPart): Boolean =
        data is PlainStoryPart

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<StoryPart> =
        ViewHolder(createView(parent, R.layout.story_part_plain))

    private class ViewHolder(view: View) : DelegateViewHolder<StoryPart>(view) {
        private val cover = view.findViewById<ImageView>(R.id.cover)

        override fun onBind(data: StoryPart) {
            Glide.with(cover)
                .load(data.cover)
                .apply(RequestOptions.centerCropTransform())
                .into(cover)
        }
    }
}