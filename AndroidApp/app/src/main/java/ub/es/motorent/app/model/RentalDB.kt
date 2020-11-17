package ub.es.motorent.app.model

import android.util.Log

data class RentalInfo (
    var id: Int,
    var moto_id: Int,
    var user_id: Int,
    var active: Boolean,
    var book_hour: String,
    var finish_book_hour : String,
    var finish_rental_hour : String
)

data class RentalJson (
    var rental: RentalInfo
)
data class RentalList (
    var rentals : List<RentalInfo>
)

object RentalDB {

    fun getAllRentals() {
        val apiService = RestApiService()
        apiService.getAllRentals() {
            Log.i(TAG, it.toString())
        }
    }

    fun getRentalById(id: Int) {
        val apiService = RestApiService()
        apiService.getRentalById(id) {
            Log.i(TAG, it.toString())
        }
    }

    fun addRental(moto_id: Int, user_id: Int){
        val apiService = RestApiService()
        apiService.addRental(moto_id, user_id) {
            Log.i(TAG, it.toString())
        }
    }

    fun updateRentalById(id: Int){
        val apiService = RestApiService()
        apiService.updateRentalById(id) {
            Log.i(TAG, it.toString())
        }
    }

    fun deleteRentalById(id: Int) {
        val apiService = RestApiService()
        apiService.deleteRentalById(id) {
            Log.i(TAG, it.toString())
        }
    }

    private const val TAG = "Retrofit RentalDB"

}