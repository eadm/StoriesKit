package ru.nobird.android.stories.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ru.nobird.android.stories.ui.activity.StoriesActivityBase
import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate

class MainActivity : AppCompatActivity() {
    companion object {
        val colors = intArrayOf(0xFFFFCCAA.toInt(), 0xFFAACCFF.toInt())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionButton1.setBackgroundColor(colors[0])
        actionButton2.setBackgroundColor(colors[1])

        StoriesActivityBase.sharedTransitionContainerDelegate = object : SharedTransitionContainerDelegate {
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
        }

        actionButton1.setOnClickListener {
            startActivity(Intent(this, StoriesActivity::class.java).apply {
                putExtra(StoriesActivityBase.EXTRA_COLORS, colors)
                putExtra(StoriesActivityBase.EXTRA_COLOR_POSITION, 0)
            })
        }

        actionButton2.setOnClickListener {
            startActivity(Intent(this, StoriesActivity::class.java).apply {
                putExtra(StoriesActivityBase.EXTRA_COLORS, colors)
                putExtra(StoriesActivityBase.EXTRA_COLOR_POSITION, 1)
            })
        }
    }
}
