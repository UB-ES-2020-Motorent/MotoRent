package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.protobuf.Internal
import kotlinx.android.synthetic.main.activity_creditcards.*
import ub.es.motorent.R
import ub.es.motorent.app.model.BankDataDB
import ub.es.motorent.app.model.BankDataList
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.presenter.BankFormPresenter

class CreditCardsActivity : FullScreenActivity() {
    private lateinit var adapter: CreditCardAdapter
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creditcards)

        // comprovar aixo
        listView = findViewById(R.id.CreditCardsListView)

        // obtenir targetes
        val listCreditCards = getAllCardFromUser {
            if(it != null){
                adapter = CreditCardAdapter(this, it!!)
                listView.adapter = adapter
            } else {
                this.customToast("No hi ha targetes assignades al teu usuari", Toast.LENGTH_LONG).show()
            }
        }

        val btnAddCard : Button = findViewById(R.id.addCreditCard)
        btnAddCard.setOnClickListener(){
            val intentI = Intent(this, BankFormActivity::class.java)
            startActivity(intentI)
        }

    }

    fun getAllCardFromUser(onResult: (BankDataList?) -> Unit){
        val userInfo = CommonFunctions.loadUserInfoFromSharedPref(this)
        if(userInfo != null) {
            BankDataDB.getBankDataByCardNumberOrAllCardsByUserId(userInfo.id, null) {
                onResult(it)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intentI = Intent(this, MapsActivity::class.java)
        startActivity(intentI)
        finish()
    }
}