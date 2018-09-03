package ru.nobird.android.stories.sample

import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import ru.nobird.android.stories.ui.activity.StoriesActivityBase

class MainActivity : AppCompatActivity() {
    companion object {
        val colors = intArrayOf(0xFFFFCCAA.toInt(), 0xFFAACCFF.toInt())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionButton1.setBackgroundColor(colors[0])
        actionButton2.setBackgroundColor(colors[1])

        actionButton1.setOnClickListener {
            startActivity(Intent(this, StoriesActivity::class.java).apply {
                putExtra(StoriesActivityBase.EXTRA_COLORS, colors)

                val rectF = Rect()
                val offset = Point()
//                val array = intArrayOf(0, 0)
                it.getGlobalVisibleRect(rectF)

//                rectF.left = array[0]
//                rectF.top = array[1]
//                rectF.right = rectF.left + it.width
//                rectF.bottom = rectF.top + it.height


                Log.d(javaClass.canonicalName, "rect = $rectF")
                putExtra(StoriesActivityBase.EXTRA_CORE_VIEW_POSITION, rectF)
            })
        }

        actionButton2.setOnClickListener {
            startActivity(Intent(this, StoriesActivity::class.java).apply {
                putExtra(StoriesActivityBase.EXTRA_COLORS, colors)
            })
        }
    }
}
