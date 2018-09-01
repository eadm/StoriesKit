package ru.nobird.android.stories.sample

import android.os.Bundle
import ru.nobird.android.stories.ui.activity.StoriesActivityBase

class StoriesActivity : StoriesActivityBase() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)
    }
}