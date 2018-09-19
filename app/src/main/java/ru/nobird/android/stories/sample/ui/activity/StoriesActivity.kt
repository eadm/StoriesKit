package ru.nobird.android.stories.sample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.ui.delegate.StoriesActivityDelegate

class StoriesActivity : AppCompatActivity() {
    private lateinit var storiesDelegate: StoriesActivityDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)

        storiesDelegate = StoriesActivityDelegate(this)
        storiesDelegate.onCreate(savedInstanceState)
    }

    override fun onPause() {
        storiesDelegate.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        storiesDelegate.finish()
    }
}