package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import ub.es.motorent.R
import ub.es.motorent.app.presenter.BankInfoFormPresenter
import ub.es.motorent.app.presenter.ComplementaryFormPresenter

class BankFormActivity : FullScreenActivity() {

    private lateinit var presenter: BankInfoFormPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_form_info)
        presenter = BankInfoFormPresenter(this)

        val name : EditText = findViewById(R.id.nomCompletFill)
        val numCard : EditText = findViewById(R.id.numeroTargetaFill)
        val caducityDate : EditText = findViewById(R.id.dataCaducitatFill)
        val cvv : EditText = findViewById(R.id.cvvFill)

        presenter.getAllCardFromUser {
            Log.i("BANK", it.toString())
        }



        val btnRegister : Button = findViewById(R.id.signInBtn)
        btnRegister.setOnClickListener(View.OnClickListener() {
            presenter.addCardToUser(numCard.text.toString(), name.text.toString(), cvv.text.toString().toInt(), caducityDate.text.toString())
        });

        val btnTornar : Button = findViewById(R.id.backBtn)
        btnTornar.setOnClickListener({
            val intentI = Intent(this, CreditCardsActivity::class.java)
            startActivity(intentI)
        });
    }
}