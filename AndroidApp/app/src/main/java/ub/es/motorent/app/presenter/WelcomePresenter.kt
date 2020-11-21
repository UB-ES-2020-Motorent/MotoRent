package ub.es.motorent.app.presenter

import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.view.LoginActivity
import ub.es.motorent.app.view.MapsActivity
import ub.es.motorent.app.view.WelcomeActivity

class WelcomePresenter (var activity: WelcomeActivity) {

    private var auth: FirebaseAuth = Firebase.auth


    fun navigationPath() : Intent {
        return if((auth.currentUser != null) && (activity.autoLogin())){
            val token = CommonFunctions.saveUIDToSharedPerf()
            UserDB.getUserByIdOrGoogleToken (null, token) {
                if (token != it?.google_token ?: true){
                    Log.w(WelcomePresenter.TAG, "user not registered correctly")
                } else {
                    Log.i(WelcomePresenter.TAG, "user: $it")
                    CommonFunctions.saveUserInfoToSharedPref(it!!, activity)
                }
            }
            val intentI = Intent(activity, MapsActivity::class.java)
            intentI
        }else{
            val intentI = Intent(activity, LoginActivity::class.java)
            intentI
        }
    }

    companion object {
        private const val TAG = "SignUpPresenter"
    }

}