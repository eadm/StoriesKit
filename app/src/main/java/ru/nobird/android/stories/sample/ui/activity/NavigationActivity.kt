package ru.nobird.android.stories.sample.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_navigation.*
import ru.nobird.android.stories.sample.R

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        activityImplementation.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        fragmentImplementation.setOnClickListener { startActivity(Intent(this, FragmentActivity::class.java)) }
    }
}