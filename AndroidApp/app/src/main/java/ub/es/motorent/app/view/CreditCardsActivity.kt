package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_creditcards.*
import ub.es.motorent.R
import ub.es.motorent.app.model.BankDataInfo
import ub.es.motorent.app.presenter.BankInfoFormPresenter

class CreditCardsActivity : AppCompatActivity() {
    private lateinit var presenter: BankInfoFormPresenter
    private lateinit var adapter: CreditCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creditcards)

        // comprovar aixo
        presenter = BankInfoFormPresenter(BankFormActivity())

        // obtenir targetes
        val listCreditCards =presenter.getAllCardFromUser {
            Log.i("GET CARD FROM USER", it.toString())
        }

        // type mismatch

        // adapter = CreditCardAdapter(this, listCreditCards)
        // CreditCardsListView.adapter = adapter
        val btnRegister : Button = findViewById(R.id.addCreditCard)
        btnRegister.setOnClickListener(){
            val intentI = Intent(this, BankFormActivity::class.java)
            startActivity(intentI)
        }

    }

}