package ru.nobird.android.stories.sample.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.nobird.android.stories.sample.model.PlainStoryPart
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.ui.adapter.StoriesAdapter
import ru.nobird.android.stories.transition.SharedTransitionsManager
import ru.nobird.android.stories.transition.SharedTransitionIntentBuilder
import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate

class MainActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = StoriesAdapter(stories) { position, _ ->
            startActivity(SharedTransitionIntentBuilder.createIntent(
                    this, StoriesActivity::class.java, SCREEN_KEY, position, stories
            ))
        }

        storiesRecycler.adapter = adapter
        storiesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        storiesRecycler.itemAnimator = null
    }

    override fun onStart() {
        super.onStart()
        SharedTransitionsManager.registerTransitionDelegate(SCREEN_KEY, object : SharedTransitionContainerDelegate {
            override fun getSharedView(position: Int): View {
                return (storiesRecycler.findViewHolderForAdapterPosition(position) as? StoriesAdapter.ViewHolder)?.itemView!!
            }

            override fun onPositionChanged(position: Int) {
                storiesRecycler.layoutManager?.scrollToPosition(position)
                (storiesRecycler.adapter as StoriesAdapter).selected = position
                // adapter hide
            }
        })
    }

    override fun onStop() {
        SharedTransitionsManager.unregisterTransitionDelegate(SCREEN_KEY)
        super.onStop()
    }
}
