package ru.nobird.android.stories.transition

import ru.nobird.android.stories.ui.delegate.SharedTransitionContainerDelegate

/**
 * Static object to provide connection between small items containers and detailed items containers
 * in order to make shared open/close transitions on any API level
 */
object SharedTransitionsManager {
    private val delegates = mutableMapOf<String, SharedTransitionContainerDelegate>()

    /**
     * Register transition delegate.
     * This method usually should be executed in onStart() lifecycle method
     * in order to correctly handle screens hierarchy.
     *
     * @param key - string key to define delegate, e.g.: screen name, user id, etc.
     */
    fun registerTransitionDelegate(key: String, delegate: SharedTransitionContainerDelegate) {
        delegates[key] = delegate
    }

    /**
     * Unregister transition delegate
     *
     * @param key - string key to define delegate, e.g.: screen name, user id, etc.
     */
    fun unregisterTransitionDelegate(key: String) {
        delegates.remove(key)
    }

    fun getTransitionDelegate(key: String): SharedTransitionContainerDelegate? =
            delegates[key]
}