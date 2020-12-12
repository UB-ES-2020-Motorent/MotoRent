package ub.es.motorent.app.model

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ub.es.motorent.app.view.MapsActivity

data class PaymentsInfo (
    var id: Int,
    var moto_id: Int,
    var user_id: Int,
    var active: Boolean,
    var book_hour: String,
    var finish_book_hour : String?,
    var finish_rental_hour : String?
)

data class PaymentsJson (
    var payment: PaymentsInfo
)
data class PaymentsList (
    var payments : List<PaymentsInfo>
)

object PaymentsDB {

    fun getAllPayments() {
        val apiService = RestApiService()
        apiService.getAllPayments() {
            Log.i(TAG, it.toString())
        }
    }

    fun getPaymentsByUserId(user_id: Int,  onResult: (PaymentsList?) -> Unit) {
        val apiService = RestApiService()
        apiService.getPaymentsByUserId(user_id) {
            Log.i(TAG, it.toString())
            onResult(it)
        }
    }

    fun getPaymentsByMotoId(moto_id: Int, onResult: (PaymentsList?) -> Unit) {
        val apiService = RestApiService()
        apiService.getPaymentsByMotoId(moto_id) {
            Log.i(TAG, it.toString())
            onResult(it)
        }
    }

    fun getPaymentById(id: Int, onResult: (PaymentsInfo?) -> Unit) {
        val apiService = RestApiService()
        apiService.getPaymentById(id) {
            Log.i(TAG, it.toString())
            onResult(it?.payment)
        }
    }

    fun addPayment(id_rental: Int, id_bank_data: Int, payment_import: Float,payment_date:String?,  onResult: (PaymentsInfo?) -> Unit){
        val apiService = RestApiService()
            apiService.addPayment(id_rental, id_bank_data, payment_import, payment_date) {
                Log.i(TAG, it.toString())
                onResult(it?.payment)
            }

    }


    private const val TAG = "Retrofit PaymentsDB"

}