package ru.nobird.android.stories.sample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.databinding.ActivityStoriesBinding
import ru.nobird.android.stories.sample.ui.delegate.StoriesActivityDelegate

class StoriesActivity : AppCompatActivity(R.layout.activity_stories) {
    private val binding by viewBinding(ActivityStoriesBinding::bind)

    private lateinit var storiesDelegate: StoriesActivityDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)

        storiesDelegate = StoriesActivityDelegate(this, binding)
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

    override fun onBackPressed() {
        storiesDelegate.finish()
    }
}