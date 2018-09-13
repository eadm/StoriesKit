package ru.nobird.android.stories.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_stories.*
import ru.nobird.android.stories.ui.custom.DismissableLayout
import ru.nobird.android.stories.ui.delegate.StoriesActivityDelegateBase
import ru.nobird.android.stories.ui.delegate.StoryPartViewDelegate

class StoriesActivityDelegate(
        activity: AppCompatActivity
) : StoriesActivityDelegateBase(activity) {
    override val dismissableLayout: DismissableLayout =
            activity.content

    override val storiesViewPager: ViewPager =
            activity.storiesPager

    override val arguments: Bundle =
            activity.intent.extras ?: Bundle.EMPTY

    override val storyPartDelegates: List<StoryPartViewDelegate> =
            listOf(PlainStoryPartViewDelegate())
}