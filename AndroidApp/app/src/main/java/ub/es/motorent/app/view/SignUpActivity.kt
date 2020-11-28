package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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
        val txtPassword2 : TextView=findViewById(R.id.etx_password2)

        val btnRegister : Button = findViewById(R.id.btn_sign_in)


        btnRegister.setOnClickListener(View.OnClickListener() {
            if (presenter.userAndMailNotEmpty(txtUserName.text.toString(), txtEmail.text.toString())){
                if(presenter.checkPassword(txtPassword.text.toString(), txtPassword2.text.toString())) {
                    presenter.createAccount(
                        txtUserName.text.toString(),
                        txtEmail.text.toString(),
                        txtPassword.text.toString()
                    )
                }
            }
        })
    }

    fun goToFormAfterRegister() {
        val intentI = Intent(this, ComplementaryFormActivity::class.java)
        startActivity(intentI)
        finish()
    }
}

    /*
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
     */

