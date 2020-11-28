package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import ub.es.motorent.R
import android.widget.Toast
import com.hbb20.CountryCodePicker
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.presenter.ComplementaryFormPresenter

class ComplementaryFormActivity : FullScreenActivity(), CountryCodePicker.OnCountryChangeListener {
    private var ccp:CountryCodePicker?=null
    private var countryName:String?=null

    private lateinit var presenter: ComplementaryFormPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complementary_form)

        presenter = ComplementaryFormPresenter(this)

        val userInfo = presenter.getUserInfo()
        val txtName : EditText = findViewById(R.id.nomFill)
        val txtSurname : EditText = findViewById(R.id.cognomFill)
        val txtIDCard : EditText = findViewById(R.id.numIdentiFill)


        if(userInfo != null) {
            txtName.setText(userInfo.name)
            txtSurname.setText(userInfo.surname)
            txtIDCard.setText(userInfo.national_id_document)
        }

        ccp = findViewById(R.id.country_code_picker)
        ccp!!.setOnCountryChangeListener(this)
        ccp!!.setDefaultCountryUsingNameCode("ES")

        val btnRegister : Button = findViewById(R.id.signInBtn)
        btnRegister.setOnClickListener(View.OnClickListener() {
            presenter.updateUserInfo(
                txtName.text.toString(),
                ccp!!.selectedCountryName,
                txtIDCard.text.toString(),
                txtSurname.text.toString()
            )
        });

    }

    override fun onBackPressed() {
        // presenter.eliminarUserActual()
    }

    override fun onCountrySelected() {
        countryName = ccp!!.selectedCountryName

        customToast("Has seleccionat: " + countryName,Toast.LENGTH_SHORT).show()
    }

    fun goToMap(){
        val intentI = Intent(this, MapsActivity::class.java)
        startActivity(intentI)
        finish()
    }
}