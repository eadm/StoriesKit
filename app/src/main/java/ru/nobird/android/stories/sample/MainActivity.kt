package ru.nobird.android.stories.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.graphics.toColorInt
import kotlinx.android.synthetic.main.activity_main.*
import ru.nobird.android.stories.model.PlainStoryPart
import ru.nobird.android.stories.model.Story
import ru.nobird.android.stories.transition.SharedTransitionsManager
import ru.nobird.android.stories.transition.SharedTransitionIntentBuilder
import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate

class MainActivity : AppCompatActivity() {
    companion object {
        val stories = arrayListOf(
                Story(listOf(
                        PlainStoryPart(duration = 15000, cover = "#FF233D4D"),
                        PlainStoryPart(duration = 15000, cover = "#FFFE7F2D")
                )),
                Story(listOf(
                        PlainStoryPart(duration = 15000, cover = "#FFA1C181"),
                        PlainStoryPart(duration = 15000, cover = "#FF579C87")
                ))
        )

        private const val SCREEN_KEY = "main_screen"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionButton1.setBackgroundColor(stories[0].parts[0].cover.toColorInt())
        actionButton2.setBackgroundColor(stories[1].parts[0].cover.toColorInt())

        actionButton1.setOnClickListener {
            startActivity(SharedTransitionIntentBuilder.createIntent(
                    this, StoriesActivity::class.java, SCREEN_KEY, 0, stories
            ))
        }

        actionButton2.setOnClickListener {
            startActivity(SharedTransitionIntentBuilder.createIntent(
                    this, StoriesActivity::class.java, SCREEN_KEY, 1, stories
            ))
        }
    }

    override fun onStart() {
        super.onStart()
        SharedTransitionsManager.registerTransitionDelegate(SCREEN_KEY, object : SharedTransitionContainerDelegate {
            override fun getSharedView(position: Int): View {
                return if (position == 0) {
                    actionButton1
                } else {
                    actionButton2
                }
            }

            override fun onPositionChanged(position: Int) {
                if (position == 0) {
                    actionButton1.visibility = View.GONE
                    actionButton2.visibility = View.VISIBLE
                } else {
                    actionButton1.visibility = View.VISIBLE
                    actionButton2.visibility = View.GONE
                }
            }
        })
    }

    override fun onStop() {
        SharedTransitionsManager.unregisterTransitionDelegate(SCREEN_KEY)
        super.onStop()
    }
}
