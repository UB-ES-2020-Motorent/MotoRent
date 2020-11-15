package ub.es.motorent.app.model

import android.util.Log
import ub.es.motorent.app.view.FullScreenActivity


data class UserInfo (
    var id: Int? = null,
    var mail: String?,
    var google_token: String?,
    var role: Int?,
    var country: String? = null,
    var name: String? = null,
    var surname: String? = null
)

data class UserJson (
    var user: UserInfo
)
data class UserList (
    var users : List<UserInfo>
)

object UserDB {

    fun getUsers() {
        val apiService = RestApiService()
        apiService.getUsers() {
            // TODO do something
        }
    }

    fun getUserById(id: Int) {
        val apiService = RestApiService()
        apiService.getUserById(id) {
            // TODO do something
        }
    }

    fun registerUser(email: String, gToken: String?, role: Int = 0){//}: UserInfo? {
        val apiService = RestApiService()
        val userInfo = UserInfo(
            mail = email,
            google_token = gToken,
            role = role
        )
        apiService.addUser(userInfo) {
            Log.i(TAG, it.toString())
        }
    }

    fun updateUserInfoInDataBase(id: Int, email: String? = null, google_token: String? = null, role: Int? = null, name: String?, surname: String?, country: String?){
        val apiService = RestApiService()
        val userInfo = UserInfo(
            mail = email,
            google_token = google_token,
            role = role,
            country = country,
            name = name,
            surname = surname
        )
        apiService.updateUser(id, userInfo) {
            Log.i(TAG, it.toString())
        }
    }

    private const val TAG = "Retrofit UserDB"

}