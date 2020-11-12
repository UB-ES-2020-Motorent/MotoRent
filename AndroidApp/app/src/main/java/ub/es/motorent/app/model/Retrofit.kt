package ub.es.motorent.app.model


import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object URL {

    private const val isDeploy = false // Change to true for deployment

    private const val urlTest = "https://192.168.16.1:5000/"
    private const val urlDeploy = "https://motorent-deploy.herokuapp.com"

    fun getUrl() : String{
        return if (isDeploy){ urlDeploy } else { urlTest }
    }

}

interface RestApi {
    @Headers("Content-Type: application/json")
    @POST("users/")
    fun addUser(@Body userData: UserInfo): Call<UserInfo>

    @GET("users")
    //fun getUsers(@Query("api_key") key: String) : Call<List<UserInfo>>
    fun getUsers() : Call<List<UserInfo>>

    @GET("user/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<UserInfo>
}

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL.getUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}

class RestApiService {
    fun getUsers(){//}: List<UserInfo> {
        //var data : List<UserInfo>? = null

        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUsers().enqueue(
            object : Callback<List<UserInfo>> {
                override fun onFailure(call: Call<List<UserInfo>>, t: Throwable?) {
                    Log.i("Retrofit", "onFailure")
                    t?.printStackTrace()
                    //data = emptyList()
                }
                override fun onResponse( call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
                    val res = response.body()
                    if (response.code() == 200 && res!=null){
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

    fun addUser(userData: UserInfo, onResult: (UserInfo?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addUser(userData).enqueue(
            object : Callback<UserInfo> {
                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<UserInfo>, response: Response<UserInfo>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }
}
