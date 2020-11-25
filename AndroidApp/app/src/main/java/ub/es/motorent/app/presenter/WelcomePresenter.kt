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


        fun navigationPath() {
        return if((auth.currentUser != null) && (activity.autoLogin())){
            val token = CommonFunctions.getUIDFromFirebase()
            UserDB.getUserByIdOrGoogleToken (null, token) {
                if (token != it?.google_token ?: true || it == null){
                    Log.w(TAG, "User not in DataBase... Redirecting to login")
                    activity.goToNextActivity(LoginActivity::class.java)
                } else {
                    Log.i(TAG, "user: $it")
                    CommonFunctions.saveUserInfoToSharedPref(it, activity)
                    activity.goToNextActivity(MapsActivity::class.java)
                }
            }
        }else{
            CommonFunctions.deleteUserInfoFromSharedPref(activity)
            activity.goToNextActivity(LoginActivity::class.java)
        }
    }

    companion object {
        private const val TAG = "SignUpPresenter"
    }

}