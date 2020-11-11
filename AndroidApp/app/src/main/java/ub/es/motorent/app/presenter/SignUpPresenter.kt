package ub.es.motorent.app.presenter

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.app.view.SignUpActivity

class SignUpPresenter (var activity: SignUpActivity) {

    // Initialize Firebase Auth
    private var auth: FirebaseAuth = Firebase.auth


    fun createAccount(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    //updateUI(user)
                    activity.toast("Authentication success.")
                } else {
                    // If sign in fails, display a message to the user.
                    activity.toast(task.exception?.message.toString())
                    //updateUI(null)
                }
            }
    }
    fun checkPassword(password1:String, password2: String): Boolean {
        if(password1 == password2){
            if(password1.length>5){
                if(checkNumberInString(password1)){

                }else{
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

    fun checkNumberInString(password:String): Boolean {
        for (character in password){
            if(character.isDigit()){
                return true
            }
        }
        return false
    }

}