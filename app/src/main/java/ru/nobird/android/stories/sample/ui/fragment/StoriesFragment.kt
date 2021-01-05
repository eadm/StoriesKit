package ru.nobird.android.stories.sample.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.databinding.ActivityStoriesBinding
import ru.nobird.android.stories.sample.ui.delegate.StoriesFragmentDelegate
import ru.nobird.android.stories.transition.SharedTransitionArgumentBuilder

class StoriesFragment : Fragment(R.layout.activity_stories) {
    companion object {
        fun newInstance(key: String, position: Int = 0, stories: List<Story>): Fragment =
            StoriesFragment().apply {
                arguments = SharedTransitionArgumentBuilder.createArguments(key, position, stories)
            }
    }

    private val binding by viewBinding(ActivityStoriesBinding::bind)
    private lateinit var storiesDelegate: StoriesFragmentDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        storiesDelegate = StoriesFragmentDelegate(this, binding)
        storiesDelegate.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        storiesDelegate.onResume()
    }

    override fun onPause() {
        storiesDelegate.onPause()
        super.onPause()
    }

    fun finish() {
        storiesDelegate.finish()
    }

    override fun onDestroy() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onDestroy()
    }
}