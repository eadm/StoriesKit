package ru.nobird.android.stories.sample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.nobird.android.stories.sample.R
import ru.nobird.android.stories.sample.ui.activity.MainActivity
import ru.nobird.android.stories.sample.ui.adapter.StoriesAdapter
import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate

class MainFragment : Fragment(), SharedTransitionContainerDelegate {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.activity_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = StoriesAdapter(MainActivity.stories) { position, _ ->
            val storiesFragment = StoriesFragment.newInstance(MainActivity.stories, position)
            storiesFragment.setTargetFragment(this, 999)

            fragmentManager
                ?.beginTransaction()
                ?.add(R.id.container, storiesFragment)
                ?.commitNow()
        }

        storiesRecycler.adapter = adapter
        storiesRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        storiesRecycler.itemAnimator = null


    }

    override fun getSharedView(position: Int): View? =
        (storiesRecycler.findViewHolderForAdapterPosition(position) as? StoriesAdapter.ViewHolder)?.itemView!!

    override fun onPositionChanged(position: Int) {
        storiesRecycler.layoutManager?.scrollToPosition(position)
        (storiesRecycler.adapter as StoriesAdapter).selected = position
    }
}