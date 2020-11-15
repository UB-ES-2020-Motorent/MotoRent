package ub.es.motorent

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import ub.es.motorent.app.model.UserDB

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class APIAndroidTest {
    @Test
    fun getUsers() {
        //Log.i("test", UserDB.getUsersFromDataBase().toString())
        UserDB.getUsers()
    }
}
