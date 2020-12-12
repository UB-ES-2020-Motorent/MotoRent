package ub.es.motorent

import android.util.Log
import org.junit.Assert
import org.junit.Test
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.model.UserInfo

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
        //val userInfo = UserDB.registerUser(email, token, role, Unit)
        //Log.println(1, "POST_USER", userInfo.toString())
        //Assert.assertEquals(userInfo, Unit)
        //Assert.assertEquals(token, userInfo?.google_token)
        //Assert.assertEquals(role, userInfo?.role)

        /*Assert.assertEquals(email, UserDB.currentUserInfo?.mail)
        Assert.assertEquals(token, UserDB.currentUserInfo?.google_token)
        Assert.assertEquals(role, UserDB.currentUserInfo?.role)
         */
    }

    @Test
    fun getUsers() {
        //Log.i("test", UserDB.getUsersFromDataBase().toString())
        UserDB.getUsers()
    }

}
