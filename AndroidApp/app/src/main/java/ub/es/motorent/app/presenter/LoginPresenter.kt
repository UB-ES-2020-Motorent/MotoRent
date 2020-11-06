package ub.es.motorent.app.presenter

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.R
import ub.es.motorent.app.view.LoginActivity
import ub.es.motorent.app.view.MapsActivity

class LoginPresenter (var activity: LoginActivity) {

    // Initialize Firebase Auth
    private var auth: FirebaseAuth = Firebase.auth

    fun signInWithEmailAndPassword(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    activity.authenticationSuccessful()
                } else {
                    activity.toast(task.exception?.message.toString())
                }
            }
    }


}