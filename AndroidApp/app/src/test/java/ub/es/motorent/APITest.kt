package ub.es.motorent

import org.junit.Assert
import org.junit.Test
import ub.es.motorent.app.model.UserDB

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class APITest {
    @Test
    fun postUser() {
        val email = "primerPOST@test.com"
        val token = "aqui0va0el0token"
        val role = 0
        UserDB.registerUserToDataBase(email, token, role)
        Assert.assertEquals(email, UserDB.currentUserInfo?.mail)
        Assert.assertEquals(token, UserDB.currentUserInfo?.google_token)
        Assert.assertEquals(role, UserDB.currentUserInfo?.role)
    }

    @Test
    fun getUsers() {
        //Log.i("test", UserDB.getUsersFromDataBase().toString())
        UserDB.getUsersFromDataBase()
    }

}
