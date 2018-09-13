package ru.nobird.android.stories.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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
}