package ub.es.motorent.app.model

import android.util.Log
import java.util.*
import kotlin.collections.ArrayList


data class MotoInfo (
    var id: Int? = null,
    var license_number: String,
    var battery: Int,
    var available: String?,
    var longitude: Float,
    var latitude: Float
)

data class MotoJson (
    var moto: MotoInfo
)
data class MotoList (
    var motos : List<MotoInfo>
)

object MotoDB {

    fun getMotos(onResult: (MotoList?) -> Unit) {
        val apiService = RestApiService()
        apiService.getMotos() {
            Log.i(TAG, it.toString())
            onResult(it)
        }
    }

    fun getMotoById(id: Int) {
        val apiService = RestApiService()
        apiService.getMotoById(id) {
            Log.i(TAG, it.toString())
        }
    }

    fun addMoto(license_number: String, battery: Int, longitude: Float, latitude: Float){
        val apiService = RestApiService()
        apiService.addMoto(license_number, battery, longitude, latitude) {
            Log.i(TAG, it.toString())
        }
    }

    fun deleteMotoById(id: Int) {
        val apiService = RestApiService()
        apiService.deleteMotoById(id) {
            Log.i(TAG, it.toString())
        }
    }

    private const val TAG = "Retrofit MotoDB"

}