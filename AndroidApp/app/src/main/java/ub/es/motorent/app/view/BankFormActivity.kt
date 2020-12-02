package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ub.es.motorent.R
import ub.es.motorent.app.presenter.BankFormPresenter

class BankFormActivity : FullScreenActivity() {

    private lateinit var presenter: BankFormPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_form_info)
        presenter = BankFormPresenter(this)

        val name : EditText = findViewById(R.id.nomCompletFill)
        val numCard : EditText = findViewById(R.id.numeroTargetaFill)
        val caducityDate : EditText = findViewById(R.id.dataCaducitatFill)
        val cvv : EditText = findViewById(R.id.cvvFill)


        val btnRegister : Button = findViewById(R.id.signInBtn)
        btnRegister.setOnClickListener {
            if(presenter.checkBankData(name.text.toString(),numCard.text.toString(),caducityDate.text.toString(),cvv.text.toString())) {
                presenter.addCardToUser(
                    numCard.text.toString(),
                    name.text.toString(),
                    cvv.text.toString().toInt(),
                    caducityDate.text.toString()
                )
                customToast(
                    getString(R.string.succesAddCreditCard),
                    Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 100
                ).show()
                val intentI = Intent(this, MapsActivity::class.java)
                startActivity(intentI)
                finish()
            }
        };

        val btnTornar : Button = findViewById(R.id.backBtn)
        btnTornar.setOnClickListener {
            /*presenter.getAllCardFromUser {
                    Log.i("GET CARD FROM USER", it.toString())
                }*/

            val intentI = Intent(this, CreditCardsActivity::class.java)
            startActivity(intentI)
        };
    }
}