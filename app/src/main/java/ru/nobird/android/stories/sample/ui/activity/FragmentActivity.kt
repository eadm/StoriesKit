package ru.nobird.android.stories.sample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.ui.fragment.MainFragment
import ru.nobird.android.stories.sample.ui.fragment.StoriesFragment

class FragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }
    }

    override fun onBackPressed() {
        supportFragmentManager
            .fragments
            .filterIsInstance<StoriesFragment>()
            .firstOrNull()
            ?.finish()
            ?: super.onBackPressed()
    }
}