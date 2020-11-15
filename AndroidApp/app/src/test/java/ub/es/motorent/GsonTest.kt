package ub.es.motorent

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test
import ub.es.motorent.app.model.UserInfo

class GsonTest {

    @Test
    fun fromUserInfoToJson (){
        val gson = Gson()
        val userInfo = UserInfo(1, "judit@motorent.com", "5555", 0, null)
        val jsonString = gson.toJson(userInfo)
        Assert.assertEquals(jsonString, """{"id":1,"mail":"judit@motorent.com","google_token":"5555","role":0}""")//,"admin_code":null}""")
    }

    @Test
    fun fromJsonToUserInfo (){
        val gson = Gson()
        val userInfo = UserInfo(1, "judit@motorent.com", "5555", 0, null)
        val jsonString = """{"id":1,"mail":"judit@motorent.com","google_token":"5555","role":0,"surname":"Ramo"}"""
        val jsonUserInfo = gson.fromJson(jsonString, UserInfo::class.java)
        Assert.assertEquals(userInfo, jsonUserInfo)
    }
}