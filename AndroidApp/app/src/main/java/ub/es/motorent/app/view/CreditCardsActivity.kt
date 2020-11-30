package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ub.es.motorent.R

class CreditCardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_creditcards)

        val btnRegister : Button = findViewById(R.id.addCreditCard)
        btnRegister.setOnClickListener(){
            val intentI = Intent(this, BankFormActivity::class.java)
            startActivity(intentI)
        }

    }

}