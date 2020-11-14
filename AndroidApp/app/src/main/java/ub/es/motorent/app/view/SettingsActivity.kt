package ub.es.motorent.app.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.audiofx.Virtualizer
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import ub.es.motorent.R
import ub.es.motorent.app.presenter.SettingsPresenter

class SettingsActivity : AppCompatActivity() {

    private lateinit var presenter: SettingsPresenter
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "fluxControl"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportActionBar?.hide()

        presenter = SettingsPresenter(this);

        val changePersInf : Button = findViewById(R.id.changePersonalInfBtn)
        val logOutButton : Button = findViewById(R.id.logoutBtn)
        val autoLog : CheckBox = findViewById(R.id.automaticLoginCheck)

        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        val editor = sharedPref.edit()


        if(sharedPref.contains("autoLog")){
            if(sharedPref.getBoolean("autoLog",true) == false){
                autoLog.setChecked(false)
            }else{
                autoLog.setChecked(true)
            }
        }else {
            autoLog.setChecked(true)
            editor.putBoolean("autoLog", true)
            editor.apply()
        }



        autoLog.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
               //marcar que l'user vol guardar la contrasenya
                //marcar a prefferense i posar el autologin a login activity comprovant preference
                editor.putBoolean( "autoLog", true )
                editor.apply()

            }else{
                //marcar a preference que no la vol guardar i posar la contra i el mail sempre per
                //iniciar la sessió
                editor.putBoolean( "autoLog", false )
                editor.apply()
            }

        }

        logOutButton.setOnClickListener(){
            presenter.logOutAccount()

            val i = Intent(this, WelcomeActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.putExtra("EXIT", true)
            startActivity(i)
            finish()

        }

        changePersInf.setOnClickListener(){
            val intentI = Intent(this, ComplementaryFormActivity::class.java)
            startActivity(intentI)
        }




    }

}