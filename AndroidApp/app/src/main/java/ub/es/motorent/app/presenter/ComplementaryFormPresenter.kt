package ub.es.motorent.app.presenter

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.app.view.ComplementaryFormActivity
import ub.es.motorent.app.view.FullScreenActivity

class ComplementaryFormPresenter (var activity: ComplementaryFormActivity): FullScreenActivity(){

    private var auth: FirebaseAuth = Firebase.auth


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
}