package ru.nobird.android.stories.sample.ui.delegate.part

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.story_part_plain.view.*
import ru.nobird.android.stories.sample.model.PlainStoryPart
import ru.nobird.android.stories.model.StoryPart
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.ui.delegate.StoryPartViewDelegate

class PlainStoryPartViewDelegate : StoryPartViewDelegate() {
    override fun onBindView(container: ViewGroup, part: StoryPart): View =
            LayoutInflater.from(container.context).inflate(R.layout.story_part_plain, container, false).apply {
                part as PlainStoryPart

                Glide.with(cover)
                        .load(part.cover)
                        .apply(RequestOptions.centerCropTransform())
                        .into(cover)
            }

    override fun isForViewType(part: StoryPart): Boolean =
            part is PlainStoryPart
}