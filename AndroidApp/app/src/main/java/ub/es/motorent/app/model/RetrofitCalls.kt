package ub.es.motorent.app.model


import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*


interface RestApi {
    //@Headers("Content-Type: application/json")
    @Multipart
    @POST("user")
    fun addUser(
                @Part("mail") mail:String,
                @Part("google_token") google_token:String,
                @Part("role") role:Int,
                @Part("admin_code") admin_code:String?
                ): Call<UserJson>

    @Multipart
    @GET("user")
    fun getUserByIdOrGoogleToken(
                    @Part("id") id:Int?,
                    @Part("google_token") google_token:String?,
                    @Part("admin_code") admin_code:String?
                    ): Call<UserJson>

    @Multipart
    @PUT("user/{id}")
    fun updateUser(
                    @Path("id") id: Int,
                    @Part("name") name:String?,
                    @Part("surname") surname:String?,
                    @Part("national_id_document") national_id_document:String?,
                    @Part("country") country:String?,
                    @Part("mail") mail:String?,
                    @Part("google_token") google_token:String?,
                    @Part("role") role:Int?,
                    @Part("id_bank_data") id_bank_data:Int?,
                    @Part("admin_code") admin_code:String
                    ): Call<UserJson>

    @Multipart
    @DELETE("user/{id}")
    fun deleteUser(
                    @Path("id") id: Int,
                    @Part("admin_code") admin_code:String
                    ): Call<UserJson>

    @Multipart
    @GET("users")
    fun getUsers(@Part("admin_code") admin_code:String) : Call<UserList>
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

    fun getUserByIdOrGoogleToken(id: Int?, google_token: String?, onResult: (UserJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUserByIdOrGoogleToken(id, google_token, ADMIN_CODE).enqueue(
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

    fun addUser(mail:String, google_token:String, role:Int, onResult: (UserJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addUser(mail, google_token, role, ADMIN_CODE).enqueue(
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

    fun updateUser(id: Int, name:String?, surname:String?, national_id_document:String?,
                   country:String?, mail:String?, google_token:String?, role:Int?,
                   id_bank_data:Int?, onResult: (UserJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.updateUser(id, name, surname, national_id_document, country, mail, google_token,
                            role, id_bank_data,ADMIN_CODE).enqueue(
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

    fun deleteUser(id: Int, onResult: (UserJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteUser(id, ADMIN_CODE).enqueue(
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
                    if (response.code() == 200 && userJson!=null){
                        Log.i("Retrofit addUser", Gson().toJson(userJson))
                    }else{
                        Log.i("Retrofit addUser", response.message())
                    }
                    onResult(userJson)
                }
            }
        )
    }

    companion object {
        private const val ADMIN_CODE = "admin_secret_code"
    }
}
