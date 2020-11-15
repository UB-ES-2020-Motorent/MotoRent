package ub.es.motorent.app.model


import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*


interface RestApi {

    @GET("users")
    fun getUsers( @Query("admin_code") admin_code: String) : Call<UserList>

    @GET("user/{id}")
    fun getUserById(@Path("id") id: Int, @Query("admin_code") admin_code: String): Call<UserJson>

    //@Headers("Content-Type: application/json")
    @POST("user")
    fun addUser(@Body userData: UserInfo, @Query("admin_code") admin_code: String): Call<UserJson>

    @PUT("user/{id}")
    fun updateUser(@Path("id") id: Int, @Query("admin_code") admin_code: String, @Body userData: UserInfo): Call<UserJson>

}

class RestApiService {
    fun getUsers(onResult: (UserList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUsers(ADMIN_CODE).enqueue(
            object : Callback<UserList> {
                override fun onFailure(call: Call<UserList>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getUsers")
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<UserList>, response: Response<UserList>) {
                    val res = response.body()
                    // TODO Clean log
                    if (response.code() == 201 && res!=null){
                        Log.i("Retrofit", Gson().toJson(res))
                    }else{
                        Log.i("Retrofit non 200", Gson().toJson(res))
                    }
                }
            }
        )
        Log.i("Retrofit", "return")
        //return data
    }

    fun getUserById(id: Int, onResult: (UserJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUserById(id, ADMIN_CODE).enqueue(
            object : Callback<UserJson> {
                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getUserById")
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<UserJson>, response: Response<UserJson>) {
                    val userInfo = response.body()
                    // TODO Clean log
                    Log.i("Retrofit UserById code", response.code().toString())
                    Log.i("Retrofit getUserById", userInfo.toString())
                    Log.i("Retrofit getUserById", Gson().toJson(userInfo))
                    if (response.code() == 201 && userInfo!=null){
                        Log.i("Retrofit getUserById", Gson().toJson(userInfo))
                    }else{
                        Log.i("Retrofit getUserById", response.message())
                    }
                }
            }
        )

    }

    fun addUser(userData: UserInfo, onResult: (UserJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addUser(userData, ADMIN_CODE).enqueue(
            object : Callback<UserJson> {
                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<UserJson>, response: Response<UserJson>) {
                    val userJson = response.body()
                    // TODO Clean log
                    Log.i("Retrofit addUser", "onResponse(), code = ${response.code()}")
                    //UserDB.currentUserInfo = userInfo
                    if (response.code() == 201 && userJson!=null){
                        Log.i("Retrofit addUser", Gson().toJson(userJson))
                    }else{
                        Log.i("Retrofit addUser", response.message())
                    }
                    onResult(userJson)
                }
            }
        )
    }

    fun updateUser(id: Int, userData: UserInfo, onResult: (UserJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.updateUser(id, ADMIN_CODE, userData).enqueue(
            object : Callback<UserJson> {
                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<UserJson>, response: Response<UserJson>) {
                    val userJson = response.body()
                    Log.i("Retrofit updateUser", "onResponse(), code = ${response.code()}")
                    Log.i("Retrofit updateUser", "onResponse(), body = ${response.body()}")
                    onResult(userJson)
                }
            }
        )
    }

    companion object {
        private const val ADMIN_CODE = "admin_secret_code"
    }
}
