package ub.es.motorent.app.model

import android.util.Log


data class UserInfo (
    var id: Int? = null,
    var id_bank_data: Int? = null,
    var national_id_document: String? = null,
    var mail: String?,
    var google_token: String?,
    var role: Int?,
    var country: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var admin_code: String? = null
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
            Log.i(TAG, it.toString())
        }
    }

    fun getUserByIdOrGoogleToken(id: Int?, google_token: String?) {
        val apiService = RestApiService()
        apiService.getUserByIdOrGoogleToken(id, google_token) {
            Log.i(TAG, it.toString())
        }
    }

    fun registerUser(email: String, gToken: String, role: Int = 0){//}: UserInfo? {
        val apiService = RestApiService()
        apiService.addUser(email, gToken, role) {
            Log.i(TAG, it.toString())
        }
    }

    fun updateUserInfoInDataBase(id: Int, email: String? = null, google_token: String? = null, role: Int? = null, name: String?, surname: String?, country: String?,
    id_bank_data: Int?, national_id_document: String?){
        val apiService = RestApiService()
        apiService.updateUser(id, name, surname, national_id_document, country, email, google_token,
                              role, id_bank_data ) {
            Log.i(TAG, it.toString())
        }
    }

    fun deleteUser(id: Int) {
        val apiService = RestApiService()
        apiService.deleteUser(id) {
            Log.i(TAG, it.toString())
        }
    }

    private const val TAG = "Retrofit UserDB"

}