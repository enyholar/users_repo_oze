package com.gideondev.githubuserrepo.test_common
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
class TestCompletable(private val autocomplete: Boolean = true) : Completable() {

    private var subscribed = false

    private val wrapped = create {
        subscribed = true
        if (autocomplete) {
            it.onComplete()
        }
    }

    override fun subscribeActual(s: CompletableObserver) {
        wrapped.subscribe(s)
    }

    fun assertSubscribed() {
        assertTrue(subscribed)
    }

    fun assertNotSubscribed() {
        assertFalse(subscribed)
    }
}