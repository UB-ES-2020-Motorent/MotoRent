package ub.es.motorent.app.presenter

import android.util.Log
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
                    registerUser(email)
                    activity.toast("Authentication success.")
                } else {
                    Log.e(TAG, task.exception?.toString() ?: "error login google")
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

    private fun registerUser(email: String) {
        /*CommonFunctions.saveTokenToSharedPref(activity){token ->
            val t = token ?: ""
            UserDB.registerUser(email, t, 0) {
                if (email != it?.mail ?: true){
                    Log.w(TAG, "user not registered correctly")
                } else {
                    Log.i(TAG, "user: $it")
                    CommonFunctions.saveUserInfoToSharedPref(it!!, activity)
                    activity.goToFormAfterRegister()
                }
            }
        }*/
        val token = CommonFunctions.getUIDFromFirebase()
        UserDB.registerUser(email, token, 0) { registeredUser ->
            if (email != registeredUser?.mail ?: true){
                Log.w(TAG, "user not registered correctly")
            } else {
                Log.i(TAG, "user: $registeredUser")
                CommonFunctions.saveUserInfoToSharedPref(registeredUser!!, activity)
                UserDB.registerUser(email,token,0){}
                activity.goToFormAfterRegister()
            }
        }
    }

    companion object {
        private const val TAG = "SignUpPresenter"
    }

}