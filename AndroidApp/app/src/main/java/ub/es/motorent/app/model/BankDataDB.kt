package ub.es.motorent.app.model

import android.util.Log
import java.math.BigInteger


data class BankDataInfo (
    var id_bank_data: Int,
    var user_id: Int,
    var card_number: BigInteger,
    var card_owner: String,
    var card_cvv: Int,
    var card_expiration: String
)

data class BankDataJson (
    var bankdata: BankDataInfo
)
data class BankDataList (
    var bankdatas : List<BankDataInfo>
)

object BankDataDB {

    fun getAllBankData() {
        val apiService = RestApiService()
        apiService.getAllBankData() {
            Log.i(TAG, it.toString())
        }
    }

    fun getBankDataByUIdBIdOrCardNumber(user_id: Int?, id_bank_data : Int?, card_number: BigInteger?) {
        val apiService = RestApiService()
        apiService.getBankDataByUIdBIdOrCardNumber(user_id, id_bank_data, card_number) {
            Log.i(TAG, it.toString())
        }
    }

    fun addBankData(user_id: Int, card_number: BigInteger, card_owner: String, card_cvv: Int, card_expiration: String){
        val apiService = RestApiService()
        apiService.addBankData(user_id, card_number, card_owner, card_cvv, card_expiration) {
            Log.i(TAG, it.toString())
        }
    }

    fun updateBankDataById(id: Int, user_id: Int, card_number: BigInteger, card_owner: String, card_cvv: Int, card_expiration: String){
        val apiService = RestApiService()
        apiService.updateBankDataById(id, user_id, card_number, card_owner, card_cvv, card_expiration) {
            Log.i(TAG, it.toString())
        }
    }

    fun deleteBankDataById(id: Int) {
        val apiService = RestApiService()
        apiService.deleteBankDataById(id) {
            Log.i(TAG, it.toString())
        }
    }

    private const val TAG = "Retrofit BankDataDB"

}