package ru.nobird.android.stories.sample.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nobird.android.stories.sample.databinding.ActivityNavigationBinding

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binging = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binging.root)

        binging.activityImplementation.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        binging.fragmentImplementation.setOnClickListener { startActivity(Intent(this, FragmentActivity::class.java)) }
    }
}