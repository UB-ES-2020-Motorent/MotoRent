package ub.es.motorent.app.presenter

import android.util.Log
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
                    activity.toast("Authentication failed.")
                    //updateUI(null)
                }
            }
    }

}