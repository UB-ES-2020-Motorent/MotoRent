package ub.es.motorent.app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.R
import ub.es.motorent.app.presenter.SignUpPresenter

class SignUpActivity : FullScreenActivity() {

    private lateinit var presenter: SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        presenter = SignUpPresenter(this);


        val txtUserName : TextView=findViewById(R.id.etx_user_name)
        val txtEmail : TextView=findViewById(R.id.etx_mail)
        val txtPassword : TextView=findViewById(R.id.etx_password)

        val btnRegister : Button = findViewById(R.id.btn_sign_in)
        btnRegister.setOnClickListener(View.OnClickListener() {
            presenter.createAccount(txtUserName.text.toString(), txtEmail.text.toString(), txtPassword.text.toString())
        });
    }

    /*
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
     */





}