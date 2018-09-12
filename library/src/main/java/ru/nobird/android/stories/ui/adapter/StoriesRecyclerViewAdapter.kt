package ru.nobird.android.stories.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.nobird.android.stories.model.Story

abstract class StoriesRecyclerViewAdapter(
        _stories: List<Story> = emptyList(),
        var onStoryClicked: (Int, Story) -> Unit
) : RecyclerView.Adapter<StoriesRecyclerViewAdapter.StoryViewHolder>() {
    var stories = _stories
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = stories.size

    abstract inner class StoryViewHolder(root: View): RecyclerView.ViewHolder(root) {
        init {
            root.setOnClickListener {
                if (adapterPosition in stories.indices) {
                    onStoryClicked(adapterPosition, stories[adapterPosition])
                }
            }
        }

        abstract fun onBind(story: Story, position: Int)
    }
}