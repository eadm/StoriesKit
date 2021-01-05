package ru.nobird.android.stories.sample.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.databinding.ActivityMainBinding
import ru.nobird.android.stories.sample.model.PlainStoryPart
import ru.nobird.android.stories.sample.ui.adapter.StoriesAdapter
import ru.nobird.android.stories.transition.SharedTransitionIntentBuilder
import ru.nobird.android.stories.transition.SharedTransitionsManager
import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        val stories = arrayListOf(
            Story(
                1,
                "Story 1",
                "https://picsum.photos/1000/2000?image=992",
                listOf(
                    PlainStoryPart(duration = 15000, cover = "https://picsum.photos/1000/2000?image=992"),
                    PlainStoryPart(duration = 15000, cover = "https://picsum.photos/1000/2000?image=977")
                )
            ),
            Story(
                1,
                "Story 2",
                "https://picsum.photos/1000/2000?image=977",
                listOf(
                    PlainStoryPart(duration = 15000, cover = "https://picsum.photos/1000/2000?image=977"),
                    PlainStoryPart(duration = 15000, cover = "https://picsum.photos/1000/2000?image=992")
                )
            )
        )

        private const val SCREEN_KEY = "main_screen"
    }

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = StoriesAdapter(stories) { position, _ ->
            startActivity(SharedTransitionIntentBuilder.createIntent(
                    this, StoriesActivity::class.java, SCREEN_KEY, position, stories
            ))
        }

        binding.storiesRecycler.let {
            it.adapter = adapter
            it.itemAnimator = null
        }
    }

    override fun onStart() {
        super.onStart()
        SharedTransitionsManager.registerTransitionDelegate(SCREEN_KEY, object : SharedTransitionContainerDelegate {
            override fun getSharedView(position: Int): View {
                return (binding.storiesRecycler.findViewHolderForAdapterPosition(position) as? StoriesAdapter.ViewHolder)?.itemView!!
            }

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
