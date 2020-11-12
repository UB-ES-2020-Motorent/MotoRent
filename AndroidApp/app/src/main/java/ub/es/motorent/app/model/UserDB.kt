package ub.es.motorent.app.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName


data class UserInfo (
    @SerializedName("id") val id: Int?,
    @SerializedName("mail") val userMail: String?,
    @SerializedName("google_token") val userGoogleToken: String?,
    @SerializedName("role") val userRole: Int?
)

object UserDB {

    fun getUsersFromDataBase(){//}: <List<UserInfo>> {
        val apiService = RestApiService()
        //return apiService.getUsers()
        apiService.getUsers()
    }

    fun registerUserToDataBase(email: String, gToken: String, role: Int = 0){
        val apiService = RestApiService()
        val userInfo = UserInfo( id = null,
            userMail = email,
            userGoogleToken = gToken,
            userRole = role )

        apiService.addUser(userInfo) {
            if (it?.id != null) {
                // it = newly added user parsed as response
                // it?.id = newly added user ID
            } else {
                Log.d("UserDB","Error registering new user")
            }
        }
    }


}