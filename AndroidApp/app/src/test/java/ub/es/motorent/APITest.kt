package ub.es.motorent

import android.util.Log
import com.facebook.internal.LockOnGetVariable
import org.junit.Test
import ub.es.motorent.app.model.UserDB

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class APITest {
    //@Test
    fun postUser() {
        val email = "primerPOST@test.com"
        val token = "aqui0va0el0token"
        val role = 0
        UserDB.registerUserToDataBase(email, token, role)
    }

    @Test
    fun getUsers() {
        //Log.i("test", UserDB.getUsersFromDataBase().toString())
        UserDB.getUsersFromDataBase()
    }
}
