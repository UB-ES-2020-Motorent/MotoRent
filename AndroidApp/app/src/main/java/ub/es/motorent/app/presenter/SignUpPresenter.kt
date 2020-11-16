package ub.es.motorent.app.presenter

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.view.SignUpActivity

class SignUpPresenter (private val activity: SignUpActivity) {

    // Initialize Firebase Auth
    private var auth: FirebaseAuth = Firebase.auth

    fun createAccount(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    CommonFunctions.saveTokenToSharedPref(activity)
                    //val token = CommonFunctions.getTokenFromSharedPref(activity)
                    val user = UserDB.registerUser(email, "EXEMPLE_A_CANVIAR", 0)
                    UserDB.updateUserInfoInDataBase(8,email,"YOKSE",0,"nogger","black","nigeria",1234,"notengosoynigeriano")
                    activity.toast("user: $user")
                    activity.toast("Authentication success.")
                } else {
                    activity.toast(task.exception?.message.toString())
                }
            }
    }

    fun checkPassword(password1:String, password2: String): Boolean {
        if(password1 == password2){
            if(password1.length>5){
                if(!checkNumberInString(password1)) {
                    activity.customToast("La contrasenya ha de contenir com a mínim un número.", Toast.LENGTH_LONG).show()
                    return false;
                }
            }else{
                activity.customToast("La contrasenya ha d'incloure 6 caràcters com a mínim.", Toast.LENGTH_LONG).show()
                return false;
            }
        }else{
            activity.customToast("Les contrasenyes no coincideixen.", Toast.LENGTH_LONG).show()
            return false;
        }
        return true
    }

    private fun checkNumberInString(password:String): Boolean {
        for (character in password){
            if(character.isDigit()){
                return true
            }
        }
        return false
    }


    companion object {
        private const val TAG = "SignUpPresenter"
    }

}