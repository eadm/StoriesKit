package ru.nobird.android.stories.sample.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.ui.target.RoundedBitmapImageViewTarget
import ru.nobird.android.stories.ui.adapter.StoriesRecyclerViewAdapter
import kotlin.properties.Delegates

class StoriesAdapter(
        _stories: List<Story> = emptyList(),
        onStoryClicked: (Int, Story) -> Unit
) : StoriesRecyclerViewAdapter(_stories, onStoryClicked) {
    var selected: Int by Delegates.observable(-1) { _, old, new ->
        notifyItemChanged(old)
        notifyItemChanged(new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false))

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.onBind(stories[position], position)
    }

    inner class ViewHolder(root: View): StoriesRecyclerViewAdapter.StoryViewHolder(root) {
        private val title: TextView = root.findViewById(R.id.storyTitle)
        private val cover: ImageView = root.findViewById(R.id.storyCover)

        private val target = RoundedBitmapImageViewTarget(Resources.getSystem().displayMetrics.density * 8, cover)

        override fun onBind(story: Story, position: Int) {
            title.text = story.title

            Glide.with(cover)
                    .asBitmap()
                    .load(story.cover)
                    .apply(centerCropTransform())
                    .into(target)

            itemView.visibility = if (position == selected) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
        }
    }
}