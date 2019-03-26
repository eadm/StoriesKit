package ru.nobird.android.stories.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.ui.delegate.StoriesFragmentDelegate
import ru.nobird.android.stories.transition.SharedTransitionArgumentBuilder

class StoriesFragment : Fragment() {
    companion object {
        fun newInstance(key: String, position: Int = 0, stories: List<Story>): Fragment =
            StoriesFragment().apply {
                arguments = SharedTransitionArgumentBuilder.createArguments(key, position, stories)
            }
    }

    private lateinit var storiesDelegate: StoriesFragmentDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_stories, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        storiesDelegate = StoriesFragmentDelegate(this)
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