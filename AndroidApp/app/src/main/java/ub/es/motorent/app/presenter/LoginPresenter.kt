package ub.es.motorent.app.presenter

import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.R
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.model.UserInfo
import ub.es.motorent.app.model.UserList
import ub.es.motorent.app.view.LoginActivity
import ub.es.motorent.app.view.LoginWaitFragment

// Initialize Firebase Auth
private var auth: FirebaseAuth = Firebase.auth

class LoginPresenter (private val activity: LoginActivity) {

    fun signInWithEmailAndPassword(email: String, password: String){
        if(notEmptyInfoField(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        getUserFromDBAndSaveItToSP(null)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        activity.toast(task.exception?.message.toString())
                    }
                }
        }
    }

    fun checkSuccessSignIn(email: String, password: String): Boolean {
        var success : Boolean = false
        if(notEmptyInfoField(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        success = true
                    } else {
                        success = false
                    }
                }
        }
        return success
    }

    fun notEmptyInfoField(email: String, password: String): Boolean {
        return !(email.isEmpty() or password.isEmpty())
    }

    fun checkUserInfoToAdd(email: String, password: String): Boolean {
        if (email.length<6 || !email.contains("@") || !email.contains(".")){
            return false
        } else if (password.length < 6){
            return false
        }
        return true
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    getUserFromDBAndSaveItToSP(acct.email, acct.givenName, acct.familyName)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    activity.customToast(
                        activity.getString(R.string.fail_auth),
                        Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
                }
            }
    }

    fun getUserFromDBAndSaveItToSP(email: String?, name: String? = null, surname: String? = null){
        // show loading to the user while waiting for the database
        activity.supportFragmentManager.beginTransaction().replace(R.id.fragment_login, LoginWaitFragment()).commit()
        val user = CommonFunctions.loadUserInfoFromSharedPref(activity)
        val token = CommonFunctions.getUIDFromFirebase()
        if (user == null || user.google_token != token) { // SP no
            UserDB.getUserByIdOrGoogleToken (google_token = token) {
                if (token != it?.google_token ?: true || it == null){ // DB no
                    Log.w(TAG, "user not in DataBase")

                    UserDB.registerUser(email, token, 0) {registered: UserInfo? ->
                        if (registered != null) {
                            if (name != null || surname != null){
                                registered.name = name
                                registered.surname = surname
                            }
                            CommonFunctions.saveUserInfoToSharedPref(registered, activity)
                            Log.i(TAG, "POST USER added")
                            activity.goToForm()
                        } else {
                            Log.e(TAG, "POST USER return null")
                        }
                    }

                } else { // DB si
                    Log.i(TAG, "user: $it")
                    CommonFunctions.saveUserInfoToSharedPref(it, activity)
                    goToNextActivity()
                }
            }
        } else { // SP si && SP == login
            UserDB.getUserByIdOrGoogleToken (google_token = token) {
                if (token != it?.google_token ?: true || it == null) { // DB no
                    Log.w(TAG, "user not in DataBase")
                } else if (it != user){
                    CommonFunctions.saveUserInfoToSharedPref(it, activity)
                    goToNextActivity()
                } else {
                    goToNextActivity()
                }
            }
        }
    }

    fun goToNextActivity () {
        val user = CommonFunctions.loadUserInfoFromSharedPref(activity)
        if (user == null) {
            Log.w(TAG, "user in SP is null and it should not be")
        } else if (user.country == null) {
            activity.goToForm()
        } else {
            activity.goToMaps()
        }
    }

    fun getUsers(): UserList {
        return getUsers()
    }

    fun logOutAccount(){
        auth.signOut()
    }

    companion object {
        private const val TAG = "LoginPresenter"
    }


}