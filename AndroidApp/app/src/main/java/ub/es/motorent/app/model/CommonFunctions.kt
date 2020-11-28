package ub.es.motorent.app.model

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.model.LatLng
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

    fun getUIDFromFirebase(): String {
        val user = Firebase.auth.currentUser
        var uid : String
        user?.uid.let {
            uid = user!!.uid
        }
        return uid
    }

    fun getTokenFromSharedPref(activity: FragmentActivity) : String?{
        val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }

    fun saveCurrentUserCoordsToSharedPref(coords: LatLng, activity: FragmentActivity ){
        val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonString = gson.toJson(coords)
        sharedPref?.edit()?.putString("coords", jsonString)?.apply()
    }

    fun loadCurrentUserCoordsFromSharedPref(activity: FragmentActivity): LatLng? {
        val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val gson = Gson()
        val coordsString = sharedPref?.getString("coords", null)
        return gson.fromJson(coordsString, LatLng::class.java)
    }

    fun saveCurrentRentalInfoToSharedPref(rentalInfo: RentalInfo?, activity: FragmentActivity?){
        val sharedPref = activity?.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val gson = Gson()
        val jsonString = if(rentalInfo != null) gson.toJson(rentalInfo) else null
        sharedPref?.edit()?.putString("rentalInfo", jsonString)?.apply()
    }

    fun loadCurrentRentalInfoFromSharedPref(activity: FragmentActivity?): RentalInfo? {
        val sharedPref = activity?.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val gson = Gson()
        val rentalString = sharedPref?.getString("rentalInfo", null)
        return gson.fromJson(rentalString, RentalInfo::class.java)
    }

    fun loadUserInfoFromSharedPrefFragment(activity: FragmentActivity?): UserInfo? {
        val sharedPref = activity?.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val gson = Gson()
        val userString = sharedPref?.getString("userInfo", null)
        return if (userString != null) gson.fromJson(userString, UserInfo::class.java) else null
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
        return if (userString != null) gson.fromJson(userString, UserInfo::class.java) else null
    }

    fun deleteUserInfoFromSharedPref(activity: FragmentActivity) {
        val sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        sharedPref.edit().putString("userInfo", null).apply()
    }

    private const val TAG = "CommonFunctions"

}