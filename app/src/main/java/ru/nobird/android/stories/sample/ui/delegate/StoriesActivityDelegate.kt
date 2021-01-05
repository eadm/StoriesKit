package ru.nobird.android.stories.sample.ui.delegate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import ru.nobird.android.stories.sample.databinding.ActivityStoriesBinding
import ru.nobird.android.stories.sample.ui.delegate.part.PlainStoryPartViewDelegate
import ru.nobird.android.stories.ui.custom.DismissableLayout
import ru.nobird.android.stories.ui.delegate.StoriesActivityDelegateBase
import ru.nobird.android.stories.ui.delegate.StoryPartViewDelegate

class StoriesActivityDelegate(
    activity: AppCompatActivity,
    binding: ActivityStoriesBinding
) : StoriesActivityDelegateBase(activity) {
    override val dismissableLayout: DismissableLayout =
        binding.content

    override val storiesViewPager: ViewPager =
        binding.storiesPager

    override val arguments: Bundle =
        activity.intent.extras ?: Bundle.EMPTY

    override val storyPartDelegates: List<StoryPartViewDelegate> =
        listOf(PlainStoryPartViewDelegate())
}