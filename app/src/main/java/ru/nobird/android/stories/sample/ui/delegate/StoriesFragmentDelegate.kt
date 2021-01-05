package ru.nobird.android.stories.sample.ui.delegate

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import ru.nobird.android.stories.sample.databinding.ActivityStoriesBinding
import ru.nobird.android.stories.sample.ui.delegate.part.PlainStoryPartViewDelegate
import ru.nobird.android.stories.ui.custom.DismissableLayout
import ru.nobird.android.stories.ui.delegate.StoriesFragmentDelegateBase
import ru.nobird.android.stories.ui.delegate.StoryPartViewDelegate

class StoriesFragmentDelegate(
    private val fragment: Fragment,
    binding: ActivityStoriesBinding
) : StoriesFragmentDelegateBase(fragment) {
    override val dismissableLayout: DismissableLayout =
        binding.content

    override val storiesViewPager: ViewPager =
        binding.storiesPager

    override val arguments: Bundle =
        fragment.arguments ?: Bundle.EMPTY

    override val storyPartDelegates: List<StoryPartViewDelegate> =
        listOf(PlainStoryPartViewDelegate())

    override fun onClose() {
        fragment.activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.remove(fragment)
            ?.commitNow()
    }
}