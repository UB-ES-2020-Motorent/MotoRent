package ub.es.motorent.app.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import ub.es.motorent.R
import ub.es.motorent.app.presenter.ComplementaryFormPresenter

class BankFormActivity : FullScreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_form_info)

        val name : EditText = findViewById(R.id.nomCompletFill)
        val numCard : EditText = findViewById(R.id.numeroTargetaFill)
        val caducityDate : EditText = findViewById(R.id.dataCaducitatFill)
        val cvv : EditText = findViewById(R.id.cvvFill)

        val btnRegister : Button = findViewById(R.id.signInBtn)
        btnRegister.setOnClickListener(View.OnClickListener() {
        });
    }
}