package ub.es.motorent.app.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName


data class UserInfo (
    var id: Int?,
    var mail: String,
    var google_token: String,
    var role: Int,
    var admin_code: String?
)

object UserDB {

    var currentUserInfo : UserInfo? = null

    fun getUsersFromDataBase(){//}: <List<UserInfo>> {
        val apiService = RestApiService()
        //return apiService.getUsers()
        apiService.getUsers()
    }

    fun getUserById(id: Int) {
        val apiService = RestApiService()
        apiService.getUserById(id)
    }

    fun registerUserToDataBase(email: String, gToken: String, role: Int = 0){
        val apiService = RestApiService()
        val userInfo = UserInfo( id = null,
            mail = email,
            google_token = gToken,
            role = role,
            admin_code = null
        )

        apiService.addUser(userInfo) {
            if (it?.id != null) {
                // it = newly added user parsed as response
                // it?.id = newly added user ID
                userInfo.id = it.id
                Log.i("UserDB",it.toString())
            } else {
                Log.d("UserDB","Error registering new user")
            }
            Log.i("UserDB",it.toString())
        }
    }


}