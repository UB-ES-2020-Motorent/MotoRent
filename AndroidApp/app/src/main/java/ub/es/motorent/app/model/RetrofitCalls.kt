package ub.es.motorent.app.model


import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*


interface RestApi {

    @GET("users")
    //fun getUsers(@Query("api_key") key: String) : Call<List<UserInfo>>
    fun getUsers() : Call<List<UserInfo>>

    @GET("user/{id}")
    fun getUserById(@Path("id") id: Int): Call<UserInfo>

    //@Headers("Content-Type: application/json")
    @POST("user")
    fun addUser(@Body userData: UserInfo): Call<UserInfo>

}

class RestApiService {
    fun getUsers(){//}: List<UserInfo> {
        //var data : List<UserInfo>? = null

        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUsers().enqueue(
            object : Callback<List<UserInfo>> {
                override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getUsers")
                    t.printStackTrace()
                    //data = emptyList()
                }
                override fun onResponse( call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
                    val res = response.body()
                    if (response.code() == 201 && res!=null){
                        Log.i("Retrofit", Gson().toJson(res))
                        //data = res
                    }else{
                        Log.i("Retrofit non 200", Gson().toJson(res))
                        //data = emptyList()
                    }
                }
            }
        )
        Log.i("Retrofit", "return")
        //return data
    }

    fun getUserById(id: Int) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUserById(id).enqueue(
            object : Callback<UserInfo> {
                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getUserById")
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<UserInfo>, response: Response<UserInfo>) {
                    val userInfo = response.body()
                    if (response.code() == 200 && userInfo!=null){
                        Log.i("Retrofit", Gson().toJson(userInfo))
                    }else{
                        Log.i("Retrofit non 200", response.message())
                    }
                }
            }
        )

    }

    fun addUser(userData: UserInfo, onResult: (UserInfo?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addUser(userData).enqueue(
            object : Callback<UserInfo> {
                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    onResult(null)
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<UserInfo>, response: Response<UserInfo>) {
                    val userInfo = response.body()
                    if (response.code() == 200 && userInfo!=null){
                        onResult(userInfo)
                        Log.i("Retrofit", Gson().toJson(userInfo))
                    }else{
                        Log.i("Retrofit non 200", response.toString())
                    }
                }
            }
        )
    }
}
