package ub.es.motorent.app.presenter

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.app.model.*
import ub.es.motorent.app.model.BankDataDB.getBankDataByCardNumberOrAllCardsByUserId
import java.lang.Integer.parseInt
import ub.es.motorent.app.view.BankFormActivity

class BankInfoFormPresenter(var activity: BankFormActivity){
    private var auth: FirebaseAuth = Firebase.auth

    fun addCardToUser(card_number: String, card_owner: String, card_cvv: Int, card_expiration: String) {
        val userInfo = CommonFunctions.loadUserInfoFromSharedPref(activity)
        /* user_id, card_number, card_owner, card_cvv, card_expiration*/
        if(userInfo != null) {
            BankDataDB.addBankData(userInfo.id!!, card_number.toBigInteger(), card_owner, card_cvv, card_expiration)
            Log.i("ADD BANK DATA", "successful")
        }
    }

    fun getAllCardFromUser(onResult: (BankDataList?) -> Unit){
        val userInfo = CommonFunctions.loadUserInfoFromSharedPref(activity)

        /*if(userInfo != null) {
            getBankDataByCardNumberOrAllCardsByUserId(userInfo.id, null) {
                onResult(it)
            }
        }*/
    }

    fun getCardFromUser() {

    }

    fun checkBankData(name:String, numtargeta:String, caducitat: String, cvv: String): Boolean {
        if(name.length==0){
            activity.customToast("El nom del titular no pot ser buit", Toast.LENGTH_LONG).show()
            return false
        }else if(numtargeta.length!=16){
            activity.customToast("El número de targeta no és vàlid", Toast.LENGTH_LONG).show()
            return false
        } else if (caducitat.length==0 || !caducitat.contains("/")){
            activity.customToast("La data no pot ser buida i ha de contenir el separador '/' entre mes i any", Toast.LENGTH_LONG).show()
            return false
        } else if (cvv.length!=3){
            activity.customToast("El codi de seguretat ha de ser de 3 dígits", Toast.LENGTH_LONG).show()
            return false
        } else {
            if(caducitat.split("/").get(0).length!=2 && caducitat.split("/").get(1).length!=2){
                activity.customToast("El mes i l'any s'han d'escriure amb dues xifres (MM/YY)", Toast.LENGTH_LONG).show()
                return false
            }
            var dataisvalid = true
            try {
                val datamonth = parseInt(caducitat.split("/").get(0))
                val datayear = parseInt(caducitat.split("/").get(1))
            } catch (e: NumberFormatException) {
                activity.customToast("La data ha de tenir el format MM/YY escrit en xifres", Toast.LENGTH_LONG).show()
                dataisvalid = false
            }
            return dataisvalid
        }
    }
}