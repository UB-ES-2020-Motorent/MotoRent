package ub.es.motorent.app.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ub.es.motorent.R
import ub.es.motorent.app.presenter.ResetPswdPresenter


class ResetPswdActivity : FullScreenActivity() {

    private lateinit var presenter: ResetPswdPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pswd)

        presenter = ResetPswdPresenter(this);

        val cancelarBtn: Button = findViewById(R.id.cancelMail)
        val email : TextView = findViewById(R.id.email_fill)
        val enviarMail : TextView = findViewById(R.id.sendMail)


        enviarMail.setOnClickListener(){
            presenter.sendMail(email.text.toString())
        }

        cancelarBtn.setOnClickListener(){
            finish()
        }
    }

}