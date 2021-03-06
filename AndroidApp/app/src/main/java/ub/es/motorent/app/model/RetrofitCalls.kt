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
                @Query("mail") mail:String?,
                @Query("google_token") google_token:String,
                @Query("role") role:Int
                ): Call<UserJson>

    @GET("user")
    fun getUserByIdOrGoogleToken(
                                @Query("id") id:Int?,
                                @Query("google_token") google_token:String?
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
                    @Query("id_bank_data") id_bank_data:Int?
                    ): Call<UserJson>

    @DELETE("user/{id}")
    fun deleteUser(
                    @Path("id") id: Int
                    ): Call<UserJson>

    @GET("users")
    fun getUsers() : Call<UserList>

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

    @PUT("moto/{id}")
    fun updateMotoById(
                    @Path("id") id: Int,
                    @Query("available") available:String
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
                    @Query("to_longitude") to_longitude:Float
                    ): Call<MapCoordJson>

    @GET("mapcoord")
    fun getMapCoordsByPair(
                            @Query("from_latitude") from_latitude:Float,
                            @Query("from_longitude") from_longitude:Float
                            ): Call<MapCoordJson>

    @PUT("mapcoord")
    fun updateMapCoordsByOrigin(
                                @Query("from_latitude") from_latitude:Float,
                                @Query("from_longitude") from_longitude:Float,
                                @Query("to_latitude") to_latitude:Float?,
                                @Query("to_longitude") to_longitude:Float?
                                ): Call<MapCoordJson>

    @DELETE("mapcoord")
    fun deleteMapCoordByOrigin(
                                @Query("from_latitude") from_latitude:Float,
                                @Query("from_longitude") from_longitude:Float
                                ): Call<MapCoordJson>

    @GET("mapcoords")
    fun getAllMapCoords() : Call<MapCoordList>

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
    fun getDefaultBankDataByUidOrBid(
        @Query("user_id") user_id:Int?,
        @Query("id_bank_data ") id_bank_data:Int?,
        @Query("view_all") view_all: Boolean?
    ): Call<BankDataJson>

    @GET("bankdata")
    fun getBankDataByCardNumberOrAllCardsByUserId(
        @Query("user_id") user_id:Int?,
        @Query("card_number") card_number:BigInteger?,
        @Query("view_all") view_all: Boolean?
    ): Call<BankDataList>

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
        @Path("id_bank_data") id_bank_data: Int,
        @Query("user_id") user_id:Int
    ): Call<BankDataJson>

    @GET("bankdatas")
    fun getAllBankData() : Call<BankDataList>

    /******************** RENTALS ********************/
    @POST("rental")
    fun addRental(
        @Query("moto_id") moto_id:Int?,
        @Query("user_id") user_id:Int?
    ): Call<RentalJson>

    @GET("rental/{id}")
    fun getRentalById(
        @Path("id") id:Int
    ): Call<RentalJson>

    @GET("activerental/{user_id}")
    fun getActiveRentalByUserId(
        @Path("user_id") user_id: Int
    ): Call<RentalJson>

    @PUT("rental/{id}")
    fun updateRentalById(
        @Path("id") id: Int?,
        @Query("end_rental") end_rental:String,
        @Query("latitude") latitude:Float?,
        @Query("longitude") longitude:Float?
    ): Call<RentalJson>

    @DELETE("rental/{id}")
    fun deleteRentalById(
        @Path("id") id: Int?
    ): Call<RentalJson>

    @GET("rentals")
    fun getAllRentals() : Call<RentalList>

    /******************** INCIDENCES ********************/

    @GET("incident/{id}")
    fun getIncidentById(
        @Path("id") id: Int?
    ):Call<IncidencesJson>

    @POST("incident")
    fun addIncident(
        @Query("moto_id") moto_id:Int?,
        @Query("user_id") user_id:Int?,
        @Query("comment") comment:String

    ): Call<IncidencesJson>

    @DELETE("incident/{id}")
    fun deleteIncidentById(
        @Path("id") id: Int?
    ):Call<IncidencesJson>

    @GET("incident")
    fun getAllIncidents(

    ): Call<IncidencesList>

    /******************** PAYMENTS ********************/

    @GET("payments")
    fun getAllPayments(

    ): Call<PaymentsList>

    @GET("payments")
    fun getPaymentsByUserId(
        @Query("user_id") moto_id: Int?
    ):Call<PaymentsList>

    @GET("payments")
    fun getPaymentsByMotoId(
        @Query("moto_id") moto_id: Int?
    ):Call<PaymentsList>

    @GET("payment")
    fun getPaymentById(
        @Path("payment_id") id: Int
    ):Call<PaymentsJson>

    @POST("payment")
    fun addPayment(
        @Query("id_rental") rental_id:Int,
        @Query("id_bank_data") id_bank_data:Int,
        @Query("payment_import") payment_import:Float,
        @Query("payment_date") payment_date:String?

    ): Call<PaymentsJson>


}

class RestApiService {

    /******************** USERS ********************/
    fun getUsers(onResult: (UserList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUsers().enqueue(
            object : Callback<UserList> {
                override fun onFailure(call: Call<UserList>, t: Throwable) {
                    Log.e(TAG, "onFailure - getUsers : ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<UserList>, response: Response<UserList>) {
                    logResult(response, "getUsers: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getUserByIdOrGoogleToken(id: Int?, google_token: String?, onResult: (UserJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getUserByIdOrGoogleToken(id, google_token).enqueue(
            object : Callback<UserJson> {
                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - getUserByIdOrGoogleToken: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<UserJson>, response: Response<UserJson>) {
                    logResult(response, "getUserByIdOrGoogleToken: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun addUser(mail:String?, google_token:String, role:Int, onResult: (UserJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addUser(mail, google_token, role).enqueue(
            object : Callback<UserJson> {
                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - addUser: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<UserJson>, response: Response<UserJson>) {
                    logResult(response, "addUser: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun updateUser(id: Int, name:String?, surname:String?, national_id_document:String?,
                   country:String?, mail:String?, google_token:String?, role:Int?,
                   id_bank_data:Int?, onResult: (UserJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.updateUser(id, name, surname, national_id_document, country, mail, google_token,
                            role, id_bank_data).enqueue(
            object : Callback<UserJson> {
                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - updateUser: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<UserJson>, response: Response<UserJson>) {
                    logResult(response, "updateUser: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun deleteUser(id: Int, onResult: (UserJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteUser(id).enqueue(
            object : Callback<UserJson> {
                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - deleteUser: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<UserJson>, response: Response<UserJson>) {
                    logResult(response, "deleteUser: ")
                    onResult(response.body())
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
                    Log.e(TAG, "onFailure - getMotos", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MotoList>, response: Response<MotoList>) {
                    logResult(response, "getMotos: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getMotoById(id: Int, onResult: (MotoJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getMotoById(id).enqueue(
            object : Callback<MotoJson> {
                override fun onFailure(call: Call<MotoJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - getMotoById", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MotoJson>, response: Response<MotoJson>) {
                    logResult(response, "getMotoById: ")
                    onResult(response.body())
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
                    Log.e(TAG, "onFailure - addMoto: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MotoJson>, response: Response<MotoJson>) {
                    logResult(response, "addMoto: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun updateMotoById(id: Int, available:String, onResult: (MotoJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.updateMotoById(id, available).enqueue(
            object : Callback<MotoJson> {
                override fun onFailure(call: Call<MotoJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - updateMotoById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MotoJson>, response: Response<MotoJson>) {
                    logResult(response, "updateMotoById: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun deleteMotoById(id: Int, onResult: (MotoJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteMotoById(id).enqueue(
            object : Callback<MotoJson> {
                override fun onFailure(call: Call<MotoJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - deleteMotoById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MotoJson>, response: Response<MotoJson>) {
                    logResult(response, "deleteMotoById: ")
                    onResult(response.body())
                }
            }
        )
    }

    /******************** MAPCOORDS ********************/
    fun getAllMapCoords(onResult: (MapCoordList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getAllMapCoords().enqueue(
            object : Callback<MapCoordList> {
                override fun onFailure(call: Call<MapCoordList>, t: Throwable) {
                    Log.e(TAG, "onFailure - getAllMapCoords", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MapCoordList>, response: Response<MapCoordList>) {
                    logResult(response, "getAllMapCoords: ")
                    onResult(response.body())
                }
            }
        )
        Log.i("Retrofit", "return")
        //return data
    }

    fun getMapCoordsByPair(from_longitude: Float, from_latitude: Float, onResult: (MapCoordJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getMapCoordsByPair(from_longitude, from_latitude).enqueue(
            object : Callback<MapCoordJson> {
                override fun onFailure(call: Call<MapCoordJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - getMapCoordsByPair", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MapCoordJson>, response: Response<MapCoordJson>) {
                    logResult(response, "getMapCoordsByPair: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun addMapCoords(from_longitude: Float, from_latitude: Float, to_longitude: Float, to_latitude: Float, onResult: (MapCoordJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addMapCoords(from_longitude, from_latitude, to_longitude, to_latitude).enqueue(
            object : Callback<MapCoordJson> {
                override fun onFailure(call: Call<MapCoordJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - addMapCoords: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MapCoordJson>, response: Response<MapCoordJson>) {
                    logResult(response, "addMapCoords: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun updateMapCoordsByOrigin(from_longitude: Float, from_latitude: Float, to_longitude: Float?, to_latitude: Float?, onResult: (MapCoordJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.updateMapCoordsByOrigin(from_longitude, from_latitude, to_longitude, to_latitude).enqueue(
            object : Callback<MapCoordJson> {
                override fun onFailure(call: Call<MapCoordJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - updateMapCoordsByOrigin: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MapCoordJson>, response: Response<MapCoordJson>) {
                    logResult(response, "updateMapCoordsByOrigin: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun deleteMapCoordByOrigin(from_longitude: Float, from_latitude: Float, onResult: (MapCoordJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteMapCoordByOrigin(from_longitude, from_latitude).enqueue(
            object : Callback<MapCoordJson> {
                override fun onFailure(call: Call<MapCoordJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - deleteMapCoordByOrigin: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<MapCoordJson>, response: Response<MapCoordJson>) {
                    logResult(response, "deleteMapCoordByOrigin: ")
                    onResult(response.body())
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
                    Log.e(TAG, "onFailure - getAllBankData", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<BankDataList>, response: Response<BankDataList>) {
                    logResult(response, "getAllBankData: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getDefaultBankDataByUidOrBid(user_id: Int?, id_bank_data : Int?, onResult: (BankDataJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getDefaultBankDataByUidOrBid(user_id, id_bank_data, false).enqueue(
            object : Callback<BankDataJson> {
                override fun onFailure(call: Call<BankDataJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - getBankData", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<BankDataJson>, response: Response<BankDataJson>) {
                    logResult(response, "getBankData: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getBankDataByCardNumberOrAllCardsByUserId(user_id: Int?, card_number: BigInteger?, onResult: (BankDataList?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getBankDataByCardNumberOrAllCardsByUserId(user_id, card_number, true).enqueue(
            object : Callback<BankDataList> {
                override fun onFailure(call: Call<BankDataList>, t: Throwable) {
                    Log.e(TAG, "onFailure - getBankData", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<BankDataList>, response: Response<BankDataList>) {
                    logResult(response, "getBankData: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun addBankData(user_id: Int, card_number: BigInteger, card_owner: String, card_cvv: Int, card_expiration: String, onResult: (BankDataJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addBankData(user_id, card_number, card_owner, card_cvv, card_expiration).enqueue(
            object : Callback<BankDataJson> {
                override fun onFailure(call: Call<BankDataJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - addBankData: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<BankDataJson>, response: Response<BankDataJson>) {
                    logResult(response, "addBankData: ")
                    onResult(response.body())
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
                    Log.e(TAG, "onFailure - updateBankDataById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<BankDataJson>, response: Response<BankDataJson>) {
                    logResult(response, "updateBankDataById: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun deleteBankDataById(id_bank_data: Int, user_id: Int, onResult: (BankDataJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteBankDataById(id_bank_data,user_id).enqueue(
            object : Callback<BankDataJson> {
                override fun onFailure(call: Call<BankDataJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - deleteBankDataById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<BankDataJson>, response: Response<BankDataJson>) {
                    logResult(response, "deleteBankDataById: ")
                    onResult(response.body())
                }
            }
        )
    }

    /******************** RENTALS ********************/
    fun getAllRentals(onResult: (RentalList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getAllRentals().enqueue(
            object : Callback<RentalList> {
                override fun onFailure(call: Call<RentalList>, t: Throwable) {
                    Log.e(TAG, "onFailure - getRentals : ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<RentalList>, response: Response<RentalList>) {
                    logResult(response, "getUsers: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getRentalById(id: Int, onResult: (RentalJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getRentalById(id).enqueue(
            object : Callback<RentalJson> {
                override fun onFailure(call: Call<RentalJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - getRentalById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<RentalJson>, response: Response<RentalJson>) {
                    logResult(response, "getRentalById: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getActiveRentalByUserId(user_id: Int, onResult: (RentalJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getActiveRentalByUserId(user_id).enqueue(
            object : Callback<RentalJson> {
                override fun onFailure(call: Call<RentalJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - getActiveRentalByUserId: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<RentalJson>, response: Response<RentalJson>) {
                    logResult(response, "getActiveRentalByUserId: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun addRental(moto_id: Int?, user_id: Int?, onResult: (RentalJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addRental(moto_id, user_id).enqueue(
            object : Callback<RentalJson> {
                override fun onFailure(call: Call<RentalJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - addRental: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<RentalJson>, response: Response<RentalJson>) {
                    logResult(response, "addRental: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun updateRentalById(id: Int?, end_rental:String, latitude:Float?, longitude:Float?, onResult: (RentalJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.updateRentalById(id, end_rental, latitude, longitude).enqueue(
            object : Callback<RentalJson> {
                override fun onFailure(call: Call<RentalJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - updateRentalById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<RentalJson>, response: Response<RentalJson>) {
                    logResult(response, "updateRentalById: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun deleteRentalById(id: Int?, onResult: (RentalJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteRentalById(id).enqueue(
            object : Callback<RentalJson> {
                override fun onFailure(call: Call<RentalJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - deleteRentalById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<RentalJson>, response: Response<RentalJson>) {
                    logResult(response, "deleteRentalById: ")
                    onResult(response.body())
                }
            }
        )
    }

    /******************** INCIDENCES ********************/

    fun getIncidentById(id: Int, onResult: (IncidencesJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getIncidentById(id).enqueue(
            object : Callback<IncidencesJson> {
                override fun onFailure(call: Call<IncidencesJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - getIncidenceById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<IncidencesJson>, response: Response<IncidencesJson>) {
                    logResult(response, "getIncidenceById: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun addIncident(moto_id: Int?, user_id: Int?, comment: String, onResult: (IncidencesJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addIncident(moto_id, user_id, comment ).enqueue(
            object : Callback<IncidencesJson> {
                override fun onFailure(call: Call<IncidencesJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - addIncident: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<IncidencesJson>, response: Response<IncidencesJson>) {
                    logResult(response, "addIncident: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun deleteIncidentById(id: Int?, onResult: (IncidencesJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.deleteIncidentById(id).enqueue(
            object : Callback<IncidencesJson> {
                override fun onFailure(call: Call<IncidencesJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - deleteIncidentById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<IncidencesJson>, response: Response<IncidencesJson>) {
                    logResult(response, "deleteIncidentById: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getAllIncidents(onResult: (IncidencesList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getAllIncidents().enqueue(
            object : Callback<IncidencesList> {
                override fun onFailure(call: Call<IncidencesList>, t: Throwable) {
                    Log.e(TAG, "onFailure - getRentals : ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<IncidencesList>, response: Response<IncidencesList>) {
                    logResult(response, "getUsers: ")
                    onResult(response.body())
                }
            }
        )
    }

    /******************** PAYMENTS ********************/

    fun getAllPayments(onResult: (PaymentsList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getAllPayments().enqueue(
            object : Callback<PaymentsList> {
                override fun onFailure(call: Call<PaymentsList>, t: Throwable) {
                    Log.e(TAG, "onFailure - getAllPayments: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<PaymentsList>, response: Response<PaymentsList>) {
                    logResult(response, "getAllPayments: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getPaymentsByUserId(user_id: Int,  onResult: (PaymentsList?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getPaymentsByUserId(user_id).enqueue(
            object : Callback<PaymentsList> {
                override fun onFailure(call: Call<PaymentsList>, t: Throwable) {
                    Log.e(TAG, "onFailure - getPaymentsByUserId: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<PaymentsList>, response: Response<PaymentsList>) {
                    logResult(response, "getPaymentsByUserId: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getPaymentsByMotoId(moto_id: Int, onResult: (PaymentsList?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getPaymentsByUserId(moto_id).enqueue(
            object : Callback<PaymentsList> {
                override fun onFailure(call: Call<PaymentsList>, t: Throwable) {
                    Log.e(TAG, "onFailure - getPaymentsByMotoId: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<PaymentsList>, response: Response<PaymentsList>) {
                    logResult(response, "getPaymentsByMotoId: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun getPaymentById(id: Int, onResult: (PaymentsJson?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.getPaymentById(id).enqueue(
            object : Callback<PaymentsJson> {
                override fun onFailure(call: Call<PaymentsJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - getPaymentById: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<PaymentsJson>, response: Response<PaymentsJson>) {
                    logResult(response, "getPaymentById: ")
                    onResult(response.body())
                }
            }
        )
    }

    fun addPayment(id_rental: Int, id_bank_data: Int, payment_import: Float, payment_date:String?, onResult: (PaymentsJson?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addPayment(id_rental, id_bank_data, payment_import, payment_date).enqueue(
            object : Callback<PaymentsJson> {
                override fun onFailure(call: Call<PaymentsJson>, t: Throwable) {
                    Log.e(TAG, "onFailure - addPayment: ", t)
                    onResult(null)
                }
                override fun onResponse( call: Call<PaymentsJson>, response: Response<PaymentsJson>) {
                    logResult(response, "addPayment: ")
                    onResult(response.body())
                }
            }
        )
    }

    companion object {
        private const val TAG = "Retrofit"
    }

    fun<T> logResult(response: Response<T>, method: String?){
        val isOK = response.code() == 200 || response.code() == 201
        if (isOK){
            Log.i(TAG, method + response.message())
        }else{
            Log.w(TAG, method + response.message())
            Log.w(TAG, method + response.body().toString())
        }
    }
}
