package ru.nobird.android.stories.sample.ui.delegate

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_stories.*
import ru.nobird.android.stories.sample.ui.delegate.part.PlainStoryPartViewDelegate
import ru.nobird.android.stories.ui.custom.DismissableLayout
import ru.nobird.android.stories.ui.delegate.StoriesFragmentDelegateBase
import ru.nobird.android.stories.ui.delegate.StoryPartViewDelegate

class StoriesFragmentDelegate(
    private val fragment: Fragment
) : StoriesFragmentDelegateBase(fragment) {
    override val dismissableLayout: DismissableLayout =
        fragment.content

    override val storiesViewPager: ViewPager =
        fragment.storiesPager

    override val arguments: Bundle =
        fragment.arguments ?: Bundle.EMPTY

    override val storyPartDelegates: List<StoryPartViewDelegate> =
        listOf(PlainStoryPartViewDelegate())

    override fun onClose() {
        fragment
            .fragmentManager
            ?.beginTransaction()
            ?.remove(fragment)
            ?.commitNow()
    }
}