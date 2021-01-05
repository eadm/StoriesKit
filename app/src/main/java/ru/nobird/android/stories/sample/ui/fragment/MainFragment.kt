package ru.nobird.android.stories.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.databinding.ActivityMainBinding
import ru.nobird.android.stories.sample.ui.activity.MainActivity
import ru.nobird.android.stories.sample.ui.adapter.StoriesAdapter
import ru.nobird.android.stories.transition.SharedTransitionsManager
import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate

class MainFragment : Fragment(R.layout.activity_main) {
    companion object {
        private const val SCREEN_KEY = "main_screen"
    }

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = StoriesAdapter(MainActivity.stories) { position, _ ->
            val storiesFragment =
                StoriesFragment.newInstance(SCREEN_KEY, position, MainActivity.stories)

            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.add(R.id.container, storiesFragment)
                ?.commitNow()
        }

        binding.storiesRecycler.let {
            it.adapter = adapter
            it.itemAnimator = null
        }
    }

    override fun onStart() {
        super.onStart()
        SharedTransitionsManager.registerTransitionDelegate(SCREEN_KEY, object : SharedTransitionContainerDelegate {
            override fun getSharedView(position: Int): View =
                (binding.storiesRecycler.findViewHolderForAdapterPosition(position) as? StoriesAdapter.ViewHolder)?.itemView!!

            override fun onPositionChanged(position: Int) {
                binding.storiesRecycler.layoutManager?.scrollToPosition(position)
                (binding.storiesRecycler.adapter as StoriesAdapter).selected = position
                // adapter hide
            }
        })
    }

    override fun onStop() {
        SharedTransitionsManager.unregisterTransitionDelegate(SCREEN_KEY)
        super.onStop()
    }
}