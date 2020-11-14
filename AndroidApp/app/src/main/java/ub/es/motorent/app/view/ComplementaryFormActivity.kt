package ub.es.motorent.app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import ub.es.motorent.R
import com.hbb20.CCPCountry
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

        val btnRegister : Button = findViewById(R.id.signInBtn)

        ccp = findViewById(R.id.country_code_picker)
        ccp!!.setOnCountryChangeListener(this)
        ccp!!.setDefaultCountryUsingNameCode("ES")

        //De moment no fa cap comprovació dins dels camps ni res a falta de tot el tema de backend
        //ja que aqui aniria el recent usuari creat i s'afagiria tots aquests camps.

        //un cop tinguem tot lo de la base de dades ben montat, faria cada cop que es fes un intent
        //es comprovés que tota la informació d'aquesta pagina estigui dins de la DB
        //en cas que no que surtis de nou aquest formulari

        btnRegister.setOnClickListener(View.OnClickListener() {

            finish()

        });

    }

    override fun onBackPressed() {
        // presenter.eliminarUserActual()
    }

    override fun onCountrySelected() {
        countryName=ccp!!.selectedCountryName

        customToast("Has seleccionat: "+countryName,Toast.LENGTH_SHORT).show()
    }
}