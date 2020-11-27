package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import ub.es.motorent.R
import android.widget.Toast
import com.hbb20.CountryCodePicker
import ub.es.motorent.app.presenter.ComplementaryFormPresenter

class ComplementaryFormActivity : FullScreenActivity(), CountryCodePicker.OnCountryChangeListener {
    private var ccp:CountryCodePicker?=null
    private var countryName:String?=null

    private lateinit var presenter: ComplementaryFormPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complementary_form)

        presenter = ComplementaryFormPresenter(this)

        ccp = findViewById(R.id.country_code_picker)
        ccp!!.setOnCountryChangeListener(this)
        ccp!!.setDefaultCountryUsingNameCode("ES")

        //De moment no fa cap comprovació dins dels camps ni res a falta de tot el tema de backend
        //ja que aqui aniria el recent usuari creat i s'afagiria tots aquests camps.

        //un cop tinguem tot lo de la base de dades ben montat, faria cada cop que es fes un intent
        //es comprovés que tota la informació d'aquesta pagina estigui dins de la DB
        //en cas que no que surtis de nou aquest formulari


        val txtName : EditText = findViewById(R.id.nomCognomFill)
        val txtIDCard : EditText = findViewById(R.id.numIdentiFill)
        val txtCreditCardName : EditText = findViewById(R.id.nomCompletFill)
        val txtCreditCardNumber : EditText = findViewById(R.id.numeroTargetaFill)
        val txtCreditCardExpirationDate : EditText = findViewById(R.id.dataCaducitatFill)
        val txtCreditCardCVV : EditText = findViewById(R.id.cvvFill)

        txtName.setText(presenter.getNameAndSurname())

        val btnRegister : Button = findViewById(R.id.signInBtn)
        btnRegister.setOnClickListener(View.OnClickListener() {
            presenter.updateUserInfo(
                txtName.text.toString(),
                ccp!!.selectedCountryName,
                txtIDCard.text.toString(),
                txtCreditCardName.text.toString(),
                txtCreditCardNumber.text.toString(),
                txtCreditCardExpirationDate.text.toString(),
                txtCreditCardCVV.text.toString()
            )
        });

    }

    override fun onBackPressed() {
        // presenter.eliminarUserActual()
    }

    override fun onCountrySelected() {
        countryName = ccp!!.selectedCountryName

        customToast("Has seleccionat: "+countryName,Toast.LENGTH_SHORT).show()
    }

    fun goToMap(){
        val intentI = Intent(this, MapsActivity::class.java)
        startActivity(intentI)
        finish()
    }
}