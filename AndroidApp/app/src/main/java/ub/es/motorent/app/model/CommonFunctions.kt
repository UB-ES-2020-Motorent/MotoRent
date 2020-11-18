package ub.es.motorent.app.model

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.view.*
import ub.es.motorent.R

object CommonFunctions {

    fun saveTokenToSharedPref(activity: AppCompatActivity, onResult: (token: String?) -> Unit) {
        //var token: String? = null
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    val token = task.result?.token
                    sharedPref.edit().putString("token", "$token").apply()
                    Log.i(TAG, "token = $token")
                    onResult(token)
                } else {
                    Log.i(TAG, "token not loaded")
                }
            }
        //return token
    }

    fun saveUIDToSharedPerf(): String {
        val user = Firebase.auth.currentUser
        var uid : String
        user?.uid.let {
            uid = user!!.uid
        }
        return uid
    }

    fun getTokenFromSharedPref(activity: AppCompatActivity) : String?{
        val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }

    fun saveUserInfoToSharedPref(userInfo: UserInfo, activity: AppCompatActivity){
        val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonString = gson.toJson(userInfo)
        sharedPref.edit().putString("userInfo", jsonString).apply()
    }

    fun loadUserInfoFromSharedPref(activity: AppCompatActivity): UserInfo? {
        val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val gson = Gson()
        val userString = sharedPref.getString("userInfo", null)
        return gson.fromJson(userString, UserInfo::class.java)
    }

    private const val TAG = "Firebase"

}