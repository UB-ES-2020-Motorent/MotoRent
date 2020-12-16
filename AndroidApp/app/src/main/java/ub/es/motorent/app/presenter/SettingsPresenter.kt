package ub.es.motorent.app.presenter

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.app.view.SettingsActivity

// Initialize Firebase Auth
private var auth: FirebaseAuth = Firebase.auth

class SettingsPresenter(var activity: SettingsActivity) {

    fun logOutAccount(){
        auth.signOut()
    }



}