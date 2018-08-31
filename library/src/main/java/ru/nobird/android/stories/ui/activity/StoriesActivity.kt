package ru.nobird.android.stories.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nobird.android.stories.R

class StoriesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)
    }
}