package ub.es.motorent

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test
import ub.es.motorent.app.model.UserInfo
import ub.es.motorent.app.model.UserJson

class GsonTest {

    @Test
    fun fromJsonToUserInfoDoubleStruct (){
        val gson = Gson()
        val userInfo = UserInfo(1, mail = "judit@motorent.com", google_token = "5555", role = 0)
        val userJson = UserJson(user = userInfo)
        val jsonString = """{"user":{"id":1,"mail":"judit@motorent.com","google_token":"5555","role":0}}"""
        val jsonUserInfo = gson.fromJson(jsonString, UserJson::class.java)
        Assert.assertEquals(userJson, jsonUserInfo)
    }
}