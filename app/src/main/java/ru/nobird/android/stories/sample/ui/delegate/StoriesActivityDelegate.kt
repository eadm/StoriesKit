package ru.nobird.android.stories.sample.ui.delegate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ru.nobird.android.stories.sample.databinding.ActivityStoriesBinding
import ru.nobird.android.stories.sample.ui.delegate.part.PlainStoryPartViewDelegate
import ru.nobird.android.stories.ui.adapter.StoryPartAdapterDelegate
import ru.nobird.android.stories.ui.custom.DismissableLayout
import ru.nobird.android.stories.ui.delegate.StoriesActivityDelegateBase

class StoriesActivityDelegate(
    activity: AppCompatActivity,
    binding: ActivityStoriesBinding
) : StoriesActivityDelegateBase(activity) {
    override val dismissableLayout: DismissableLayout =
        binding.content

    override val storiesViewPager: ViewPager2 =
        binding.storiesPager

    override val arguments: Bundle =
        activity.intent.extras ?: Bundle.EMPTY

    override val storyPartDelegates: List<StoryPartAdapterDelegate> =
        listOf(PlainStoryPartViewDelegate())
}