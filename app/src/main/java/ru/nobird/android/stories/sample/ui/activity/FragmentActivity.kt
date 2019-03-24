package ru.nobird.android.stories.sample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.ui.fragment.MainFragment

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
}