package ub.es.motorent.app.presenter

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.R
import ub.es.motorent.app.view.LoginActivity


class LoginPresenter (private val activity: LoginActivity) {

    // Initialize Firebase Auth
    private var auth: FirebaseAuth = Firebase.auth

    private fun saveTokenToSharedPref(){
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    sharedPref.edit().putString("token", task.result?.token).apply()
                } else {
                    Log.i(TAG, "token not loaded")
                }
            }
        // sharedPref.getString("token", null)  --> How to get info from shared preferences
    }

    fun signInWithEmailAndPassword(email: String, password: String){
        if(!(email.isEmpty() or password.isEmpty())) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        saveTokenToSharedPref()
                        activity.authenticationSuccessful()
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        activity.toast(task.exception?.message.toString())
                    }
                }
        }
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    activity.authenticationSuccessful()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    activity.customToast(
                        activity.getString(R.string.fail_auth),
                        Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
                }
            }
    }


    companion object {
        private const val TAG = "LoginPresenter"
    }


}