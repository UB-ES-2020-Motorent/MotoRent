package ub.es.motorent.app.model

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ub.es.motorent.R

object CommonFunctions {

    fun getFirebaseToken(): String? {
        var token: String? = null
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    token = task.result?.token
                } else {
                    Log.i(TAG, "token not loaded")
                }
            }
        return token
    }

    fun saveTokenToSharedPref(activity: AppCompatActivity) {
        //var token: String? = null
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    val token = task.result?.token
                    sharedPref.edit().putString("token", token).apply()
                    Log.i(TAG, "token = $token")
                } else {
                    Log.i(TAG, "token not loaded")
                }
            }
        //return token
    }

    fun getTokenFromSharedPref(activity: AppCompatActivity) : String?{
        val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }

    private const val TAG = "Firebase"

}