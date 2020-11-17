package ub.es.motorent.app.view

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.R
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.model.UserJson
import ub.es.motorent.app.presenter.WelcomePresenter
import java.util.*
import kotlin.concurrent.schedule

class WelcomeActivity : FullScreenActivity() {

    private lateinit var presenter: WelcomePresenter
    private val REQUEST_PERMISSION_FINE_LOCATION = 1

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "fluxControl"

    var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        presenter = WelcomePresenter(this)

        showPhoneStatePermission()

    }

    override fun onPause() {
        timer.cancel()
        super.onPause()
    }

    private fun showPhoneStatePermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_FINE_LOCATION);
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_FINE_LOCATION)
            }
        } else {
            timer.schedule(3000) {
                startActivity(presenter.navigationPath())
                finish()
            }
        }
    }

    private fun showExplanation(title: String, message: String, permission: String, permissionRequestCode: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener {
                dialog, id -> requestPermission(permission, permissionRequestCode)
                })
        builder.create().show()
    }

    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
    }

    @Override
    override fun onRequestPermissionsResult(requestCode : Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (!grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                startActivity(presenter.navigationPath())
                finish()
            }
        }
    }

    fun autoLogin() :Boolean{
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        if(sharedPref.getBoolean("autoLog",true) == true){
            return true
        }else{
            return false
        }
    }

}