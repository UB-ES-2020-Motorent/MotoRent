package ub.es.motorent.app.presenter

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.app.view.LoginActivity
import ub.es.motorent.app.view.MapsActivity
import ub.es.motorent.app.view.WelcomeActivity

class WelcomePresenter (var activity: WelcomeActivity) {

    private var auth: FirebaseAuth = Firebase.auth


    fun navigationPath() : Intent {
        return if((auth.currentUser != null) && (activity.autoLogin())){
            val intentI = Intent(activity, MapsActivity::class.java)
            intentI
        }else{
            val intentI = Intent(activity, LoginActivity::class.java)
            intentI
        }
    }

}