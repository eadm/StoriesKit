package ru.nobird.android.stories.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.ui.custom.StoryView
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class StoryAdapterDelegate(
    private val storyPartDelegates: List<StoryPartAdapterDelegate>,
    private val listener: StoryView.StoryProgressListener
) : AdapterDelegate<Story, DelegateViewHolder<Story>>() {
    private val sharedViewPool = RecyclerView.RecycledViewPool() // todo

    override fun isForViewType(position: Int, data: Story): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<Story> =
        ViewHolder(StoryView(parent.context))

    private inner class ViewHolder(
        private val storyView: StoryView
    ) : DelegateViewHolder<Story>(storyView) {
        init {
            storyView.delegates = storyPartDelegates
            storyView.progressListener = listener
        }

        override fun onBind(data: Story) {
            storyView.story = data
        }
    }
}