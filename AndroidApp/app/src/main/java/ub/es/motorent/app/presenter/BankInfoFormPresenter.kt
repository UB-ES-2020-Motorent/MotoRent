package ub.es.motorent.app.presenter

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.model.UserInfo
import ub.es.motorent.app.view.ComplementaryFormActivity
import ub.es.motorent.app.view.FullScreenActivity

class BankInfoFormPresenter(var activity: ComplementaryFormActivity): FullScreenActivity() {
    private var auth: FirebaseAuth = Firebase.auth

    fun addCardToUser() {
    }

    fun getCardFromUser() {
    }
}