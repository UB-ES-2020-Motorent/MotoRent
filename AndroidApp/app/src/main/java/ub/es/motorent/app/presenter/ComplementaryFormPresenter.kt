package ub.es.motorent.app.presenter

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.awaitAll
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.model.UserDB.getUserByIdOrGoogleToken
import ub.es.motorent.app.model.UserInfo
import ub.es.motorent.app.view.ComplementaryFormActivity
import ub.es.motorent.app.view.FullScreenActivity

private var auth: FirebaseAuth = Firebase.auth

class ComplementaryFormPresenter (var activity: ComplementaryFormActivity): FullScreenActivity(){

    //possible implementació? o millor comprovar despres del login que te les dades del formulari introduides?
    fun eliminarUserActual(){
        val user = Firebase.auth.currentUser!!
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    customToast("S'ha eliminat l'usuari actual per no completar el registre", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun updateUserInfo(name: String, country: String, idCard: String, surname: String) {
        val userInfo = CommonFunctions.loadUserInfoFromSharedPref(activity)
        if (userInfo == null){
            Log.e(TAG, "UserInfo in shared pref == null")
        } else {
            UserDB.updateUserInfoInDataBase(userInfo.id!!, name = name, surname = surname, country = country, national_id_document = idCard) {
                CommonFunctions.saveUserInfoToSharedPref(it!!.user, activity)
            }

            activity.goToMap()
        }
    }

    fun getUserInfo(): UserInfo? {
        val userInfo = CommonFunctions.loadUserInfoFromSharedPref(activity)
        if (userInfo != null) {
            return userInfo
        }
        return null
    }

    fun getNameAndSurname () : List<String?> {//List<String?>?{
        val user = CommonFunctions.loadUserInfoFromSharedPref(activity)
        //return if (user != null) listOf(user.name, user.surname) else null
        return if (user != null) listOf(user.name, user.surname) else emptyList()
    }

    companion object{
        private const val TAG = "ComplFormPres"
    }
}