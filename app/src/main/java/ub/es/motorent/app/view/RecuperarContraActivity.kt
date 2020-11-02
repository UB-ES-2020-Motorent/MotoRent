package ub.es.motorent.app.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ub.es.motorent.R

class RecuperarContraActivity : FullScreenActivity() {
    private lateinit var username : EditText
    private lateinit var enviarMail : Button
    private lateinit var cancelar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contra)
        username = findViewById(R.id.email_fill)
        enviarMail = findViewById(R.id.sendMail)
        cancelar = findViewById(R.id.cancelMail)


        enviarMail.setOnClickListener(){
            if(username.text.toString() != ""){
                val auth = FirebaseAuth.getInstance()
                var emailAddress = username.text.toString()
                auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            customToast("El mail s'ha enviat correctament",Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnCanceledListener { customToast("No tenim registrat el mail",Toast.LENGTH_LONG).show() }
            }else{
                customToast("Ompla el camp amb l'email",Toast.LENGTH_LONG).show()
            }
        }
    }




}