package ub.es.motorent.app.model


import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import java.math.BigInteger


interface RestApi {
    //@Headers("Content-Type: application/json")

    /******************** USERS ********************/
    @POST("user")
    fun addUser(
                @Query("mail") mail:String,
                @Query("google_token") google_token:String,
                @Query("role") role:Int,
                @Query("admin_code") admin_code:String?
                ): Call<UserJson>

    @GET("user")
    fun getUserByIdOrGoogleToken(
                                @Query("id") id:Int?,
                                @Query("google_token") google_token:String?,
                                @Query("admin_code") admin_code:String?
                                ): Call<UserJson>

    @PUT("user/{id}")
    fun updateUser(
                    @Path("id") id: Int,
                    @Query("name") name:String?,
                    @Query("surname") surname:String?,
                    @Query("national_id_document") national_id_document:String?,
                    @Query("country") country:String?,
                    @Query("mail") mail:String?,
                    @Query("google_token") google_token:String?,
                    @Query("role") role:Int?,
                    @Query("id_bank_data") id_bank_data:Int?,
                    @Query("admin_code") admin_code:String
                    ): Call<UserJson>

    @DELETE("user/{id}")
    fun deleteUser(
                    @Path("id") id: Int,
                    @Query("admin_code") admin_code:String
                    ): Call<UserJson>

    @GET("users")
    fun getUsers(@Query("admin_code") admin_code:String) : Call<UserList>

    /******************** MOTOS ********************/
    @POST("moto")
    fun addMoto(
                @Query("license_number") license_number:String,
                @Query("battery") battery:Int,
                @Query("longitude") longitude:Float,
                @Query("latitude") latitude:Float
                ): Call<MotoJson>

    @GET("moto/{id}")
    fun getMotoById(
                    @Path("id") id:Int?
                    ): Call<MotoJson>

    @DELETE("moto/{id}")
    fun deleteMotoById(
                        @Path("id") id: Int
                        ): Call<MotoJson>

    @GET("motos")
    fun getMotos() : Call<MotoList>

    /******************** MAPCOORDS ********************/
    @POST("mapcoord")
    fun addMapCoords(
                    @Query("from_latitude") from_latitude:Float,
                    @Query("from_longitude") from_longitude:Float,
                    @Query("to_latitude") to_latitude:Float,
                    @Query("to_longitude") to_longitude:Float,
                    @Query("admin_code") admin_code:String
                    ): Call<MapCoordJson>

    @GET("mapcoord")
    fun getMapCoordsByPair(
                            @Query("from_latitude") from_latitude:Float,
                            @Query("from_longitude") from_longitude:Float,
                            @Query("admin_code") admin_code:String
                            ): Call<MapCoordJson>

    @PUT("mapcoord")
    fun updateMapCoordsByOrigin(
                                @Query("from_latitude") from_latitude:Float,
                                @Query("from_longitude") from_longitude:Float,
                                @Query("to_latitude") to_latitude:Float?,
                                @Query("to_longitude") to_longitude:Float?,
                                @Query("admin_code") admin_code:String
                                ): Call<MapCoordJson>

    @DELETE("mapcoord")
    fun deleteMapCoordByOrigin(
                                @Query("from_latitude") from_latitude:Float,
                                @Query("from_longitude") from_longitude:Float,
                                @Query("admin_code") admin_code:String
                                ): Call<MapCoordJson>

    @GET("mapcoords")
    fun getAllMapCoords(@Query("admin_code") admin_code:String) : Call<MapCoordList>

    /******************** BANKDATA ********************/
    @POST("bankdata")
    fun addBankData(
        @Query("user_id") user_id:Int,
        @Query("card_number") card_number:BigInteger,
        @Query("card_owner") card_owner:String,
        @Query("card_cvv") card_cvv:Int,
        @Query("card_expiration") card_expiration:String
    ): Call<BankDataJson>

    @GET("bankdata")
    fun getBankDataByUIdBIdOrCardNumber(
        @Query("user_id ") user_id:Int?,
        @Query("id_bank_data ") id_bank_data:Int?,
        @Query("card_number") card_number:BigInteger?
    ): Call<BankDataJson>

    @PUT("bankdata/{id_bank_data}")
    fun updateBankDataById(
        @Path("id_bank_data") id_bank_data: Int,
        @Query("user_id") user_id:Int?,
        @Query("card_number") card_number:BigInteger?,
        @Query("card_owner") card_owner:String?,
        @Query("card_cvv") card_cvv:Int?,
        @Query("card_expiration") card_expiration:String?
    ): Call<BankDataJson>

    @DELETE("bankdata/{id_bank_data}")
    fun deleteBankDataById(
        @Path("id_bank_data") id_bank_data: Int
    ): Call<BankDataJson>

    @GET("bankdatas")
    fun getAllBankData() : Call<BankDataList>

}

class RestApiService {

    /******************** USERS ********************/
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
                            role, id_bank_data, ADMIN_CODE).enqueue(
            object : Callback<UserJson> {
                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<UserJson>, response: Response<UserJson>) {
                    val userJson = response.body()
                    Log.i("Retrofit updateUser", "onResponse(), code = ${response.code()}")
                    Log.i("Retrofit updateUser", "onResponse(), body = ${response.message()}")
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

    /******************** MOTOS ********************/
    fun getMotos(onResult: (MotoList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getMotos().enqueue(
            object : Callback<MotoList> {
                override fun onFailure(call: Call<MotoList>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getMotos")
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<MotoList>, response: Response<MotoList>) {
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

    fun getMotoById(id: Int, onResult: (MotoJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getMotoById(id).enqueue(
            object : Callback<MotoJson> {
                override fun onFailure(call: Call<MotoJson>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getMotoById")
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<MotoJson>, response: Response<MotoJson>) {
                    val motoInfo = response.body()
                    // TODO Clean log
                    Log.i("Retrofit MotoById code", response.code().toString())
                    Log.i("Retrofit getMotoById", motoInfo.toString())
                    Log.i("Retrofit getMotoById", Gson().toJson(motoInfo))
                    if (response.code() == 201 && motoInfo!=null){
                        Log.i("Retrofit getMotoById", Gson().toJson(motoInfo))
                    }else{
                        Log.i("Retrofit getMotoById", response.message())
                    }
                }
            }
        )
    }

    fun addMoto(license_number: String, battery: Int, longitude: Float, latitude: Float,
                onResult: (MotoJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addMoto(license_number, battery, longitude, latitude).enqueue(
            object : Callback<MotoJson> {
                override fun onFailure(call: Call<MotoJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<MotoJson>, response: Response<MotoJson>) {
                    val motoJson = response.body()
                    // TODO Clean log
                    Log.i("Retrofit addMoto", "onResponse(), code = ${response.code()}")
                    //MotoDB.currentMotoInfo = motoInfo
                    if (response.code() == 201 && motoJson!=null){
                        Log.i("Retrofit addMoto", Gson().toJson(motoJson))
                    }else{
                        Log.i("Retrofit addMoto", response.message())
                    }
                    onResult(motoJson)
                }
            }
        )
    }

    fun deleteMotoById(id: Int, onResult: (MotoJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteMotoById(id).enqueue(
            object : Callback<MotoJson> {
                override fun onFailure(call: Call<MotoJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<MotoJson>, response: Response<MotoJson>) {
                    val motoJson = response.body()
                    // TODO Clean log
                    Log.i("Retrofit deleteMotoById", "onResponse(), code = ${response.code()}")
                    //MotoDB.currentMotoInfo = motoInfo
                    if (response.code() == 200 && motoJson!=null){
                        Log.i("Retrofit deleteMotoById", Gson().toJson(motoJson))
                    }else{
                        Log.i("Retrofit deleteMotoById", response.message())
                    }
                    onResult(motoJson)
                }
            }
        )
    }

    /******************** MAPCOORDS ********************/
    fun getAllMapCoords(onResult: (MapCoordList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getAllMapCoords(ADMIN_CODE).enqueue(
            object : Callback<MapCoordList> {
                override fun onFailure(call: Call<MapCoordList>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getAllMapCoords")
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<MapCoordList>, response: Response<MapCoordList>) {
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

    fun getMapCoordsByPair(from_longitude: Float, from_latitude: Float, onResult: (MapCoordJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getMapCoordsByPair(from_longitude, from_latitude, ADMIN_CODE).enqueue(
            object : Callback<MapCoordJson> {
                override fun onFailure(call: Call<MapCoordJson>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getMapCoordsByPair")
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<MapCoordJson>, response: Response<MapCoordJson>) {
                    val mapCoordInfo = response.body()
                    // TODO Clean log
                    Log.i("Retrofit CoordPair code", response.code().toString())
                    Log.i("Retrofit CoordPair", mapCoordInfo.toString())
                    Log.i("Retrofit CoordPair", Gson().toJson(mapCoordInfo))
                    if (response.code() == 201 && mapCoordInfo!=null){
                        Log.i("Retrofit CoordPair", Gson().toJson(mapCoordInfo))
                    }else{
                        Log.i("Retrofit CoordPair", response.message())
                    }
                }
            }
        )
    }

    fun addMapCoords(from_longitude: Float, from_latitude: Float, to_longitude: Float, to_latitude: Float, onResult: (MapCoordJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addMapCoords(from_longitude, from_latitude, to_longitude, to_latitude, ADMIN_CODE).enqueue(
            object : Callback<MapCoordJson> {
                override fun onFailure(call: Call<MapCoordJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<MapCoordJson>, response: Response<MapCoordJson>) {
                    val mapcoordsJson = response.body()
                    // TODO Clean log
                    Log.i("Retrofit addMapCoords", "onResponse(), code = ${response.code()}")
                    //MapCoordDB.currentMapCoordsInfo = mapCoordsInfo
                    if (response.code() == 201 && mapcoordsJson!=null){
                        Log.i("Retrofit addMapCoords", Gson().toJson(mapcoordsJson))
                    }else{
                        Log.i("Retrofit addMapCoords", response.message())
                    }
                    onResult(mapcoordsJson)
                }
            }
        )
    }

    fun updateMapCoordsByOrigin(from_longitude: Float, from_latitude: Float, to_longitude: Float?, to_latitude: Float?, onResult: (MapCoordJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.updateMapCoordsByOrigin(from_longitude, from_latitude, to_longitude, to_latitude, ADMIN_CODE).enqueue(
            object : Callback<MapCoordJson> {
                override fun onFailure(call: Call<MapCoordJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<MapCoordJson>, response: Response<MapCoordJson>) {
                    val mapcoordsJson = response.body()
                    Log.i("Retrofit updateMapCoord", "onResponse(), code = ${response.code()}")
                    Log.i("Retrofit updateMapCoord", "onResponse(), body = ${response.message()}")
                    onResult(mapcoordsJson)
                }
            }
        )
    }

    fun deleteMapCoordByOrigin(from_longitude: Float, from_latitude: Float, onResult: (MapCoordJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteMapCoordByOrigin(from_longitude, from_latitude, ADMIN_CODE).enqueue(
            object : Callback<MapCoordJson> {
                override fun onFailure(call: Call<MapCoordJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<MapCoordJson>, response: Response<MapCoordJson>) {
                    val mapcoordsJson = response.body()
                    // TODO Clean log
                    Log.i("Retrofit deleteMapCoord", "onResponse(), code = ${response.code()}")
                    //MapCoordDB.currentMapCoordInfo = mapCoordInfo
                    if (response.code() == 200 && mapcoordsJson!=null){
                        Log.i("Retrofit deleteMapCoord", Gson().toJson(mapcoordsJson))
                    }else{
                        Log.i("Retrofit deleteMapCoord", response.message())
                    }
                    onResult(mapcoordsJson)
                }
            }
        )
    }

    /******************** BANKDATA ********************/
    fun getAllBankData(onResult: (BankDataList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getAllBankData().enqueue(
            object : Callback<BankDataList> {
                override fun onFailure(call: Call<BankDataList>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getAllBankData")
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<BankDataList>, response: Response<BankDataList>) {
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

    fun getBankDataByUIdBIdOrCardNumber(user_id: Int?, id_bank_data : Int?, card_number: BigInteger?, onResult: (BankDataJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getBankDataByUIdBIdOrCardNumber(user_id, id_bank_data, card_number).enqueue(
            object : Callback<BankDataJson> {
                override fun onFailure(call: Call<BankDataJson>, t: Throwable) {
                    Log.i("Retrofit", "onFailure - getBankData")
                    t.printStackTrace()
                }
                override fun onResponse( call: Call<BankDataJson>, response: Response<BankDataJson>) {
                    val bankdataInfo = response.body()
                    // TODO Clean log
                    Log.i("Retrofit getBData code", response.code().toString())
                    Log.i("Retrofit getBankData", bankdataInfo.toString())
                    Log.i("Retrofit getBankData", Gson().toJson(bankdataInfo))
                    if (response.code() == 201 && bankdataInfo!=null){
                        Log.i("Retrofit getBankData", Gson().toJson(bankdataInfo))
                    }else{
                        Log.i("Retrofit getBankData", response.message())
                    }
                }
            }
        )
    }

    fun addBankData(user_id: Int, card_number: BigInteger, card_owner: String, card_cvv: Int, card_expiration: String, onResult: (BankDataJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addBankData(user_id, card_number, card_owner, card_cvv, card_expiration).enqueue(
            object : Callback<BankDataJson> {
                override fun onFailure(call: Call<BankDataJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<BankDataJson>, response: Response<BankDataJson>) {
                    val bankdataJson = response.body()
                    // TODO Clean log
                    Log.i("Retrofit addBankData", "onResponse(), code = ${response.code()}")
                    //BankDataDB.currentBankDataInfo = bankdataInfo
                    if (response.code() == 201 && bankdataJson!=null){
                        Log.i("Retrofit addBankData", Gson().toJson(bankdataJson))
                    }else{
                        Log.i("Retrofit addBankData", response.message())
                    }
                    onResult(bankdataJson)
                }
            }
        )
    }

    fun updateBankDataById(id: Int, user_id: Int, card_number: BigInteger, card_owner: String,
                           card_cvv: Int, card_expiration: String, onResult: (BankDataJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.updateBankDataById(id, user_id, card_number, card_owner, card_cvv, card_expiration).enqueue(
            object : Callback<BankDataJson> {
                override fun onFailure(call: Call<BankDataJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<BankDataJson>, response: Response<BankDataJson>) {
                    val bankdataJson = response.body()
                    Log.i("Retrofit updateBankData", "onResponse(), code = ${response.code()}")
                    Log.i("Retrofit updateBankData", "onResponse(), body = ${response.message()}")
                    onResult(bankdataJson)
                }
            }
        )
    }

    fun deleteBankDataById(id: Int, onResult: (BankDataJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteBankDataById(id).enqueue(
            object : Callback<BankDataJson> {
                override fun onFailure(call: Call<BankDataJson>, t: Throwable) {
                    Log.i("Retrofit", t.message.toString())
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse( call: Call<BankDataJson>, response: Response<BankDataJson>) {
                    val databankJson = response.body()
                    // TODO Clean log
                    Log.i("Retrofit deleteBankData", "onResponse(), code = ${response.code()}")
                    //BankDataDB.currentDataBankInfo = databankInfo
                    if (response.code() == 200 && databankJson!=null){
                        Log.i("Retrofit deleteBankData", Gson().toJson(databankJson))
                    }else{
                        Log.i("Retrofit deleteBankData", response.message())
                    }
                    onResult(databankJson)
                }
            }
        )
    }

    companion object {
        private const val ADMIN_CODE = "admin_secret_code"
    }
}
