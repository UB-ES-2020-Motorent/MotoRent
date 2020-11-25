package ub.es.motorent.app.view

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.R
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.presenter.LoginPresenter

class LoginActivity : FullScreenActivity(){

    private lateinit var presenter: LoginPresenter

    // login google
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    // Facebook
    //private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this)

        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        //if(sharedPref.getBoolean("autoLog",true) == true){
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        //}


        //val loginTwitterBtn : Button = findViewById(R.id.twitter_btn)
        //val loginFacebookBtn : Button = findViewById(R.id.facebook_btn)

        //loginFacebookBtn?.setOnClickListener { loginFacebook() }

        val btnResetPsw: Button= findViewById(R.id.recu_psw)
        btnResetPsw.setOnClickListener {
            val intentI = Intent(this, ResetPswdActivity::class.java)
            startActivity(intentI)
        }

        val lnkRegisterMail: Button = findViewById(R.id.register_mail_link)
        lnkRegisterMail.setOnClickListener {
            val intentI = Intent(this, SignUpActivity::class.java)
            startActivity(intentI)
        }

        // login email
        val txtEmail : TextView = findViewById(R.id.login_txt_email)
        val txtPassword : TextView = findViewById(R.id.login_txt_password)
        val btnRegister : Button = findViewById(R.id.login_btn)
        btnRegister.setOnClickListener(View.OnClickListener() {
            presenter.signInWithEmailAndPassword(txtEmail.text.toString(), txtPassword.text.toString())
        })

        // login Google
        val btnGoogle : Button = findViewById(R.id.google_btn)
        btnGoogle.setOnClickListener{
            signInWithGoogle()
        }

    }

    fun goToMaps(){
        toast(getString(R.string.ok_auth))
        val intentI = Intent(this, MapsActivity::class.java)
        startActivity(intentI)
    }

    fun goToForm() {
        toast("Welcome to MotoRent")
        val intentI = Intent(this, ComplementaryFormActivity::class.java)
        startActivity(intentI)
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                presenter.firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.w(TAG, "Google sign in failed", e)
                customToast(
                    getString(R.string.fail_google_auth),
                    Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
            }
        }
    }

    /*
    private fun loginFacebook() {

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult>{

                override fun onSuccess(result: LoginResult?) {
                    result?.let {
                        val facebookToken = it.accessToken
                        val credential = FacebookAuthProvider.getCredential(facebookToken.token)

                        mAuth.signInWithCredential(credential).addOnCompleteListener {
                            if(it.isSuccessful){
                                val user = mAuth.currentUser
                                //setAuth(user)

                                val intentI = Intent(this@LoginActivity, MapsActivity::class.java)
                                startActivity(intentI)
                            } else {
                                Log.w(TAG, "signInWithFAcebook:failure")
                                customImageToast(
                                    R.drawable.moto_toast, getString(R.string.fail_auth),
                                    Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
                                supportFragmentManager.beginTransaction().replace(R.id.fragment_login, LoginSignFragment()).commit()
                            }
                        }
                    }
                }

                override fun onCancel() {
                    Log.e("onCancel", "onCancelError")
                }

                override fun onError(error: FacebookException?) {
                    Log.w(TAG, "signInWithFAcebook:failure")
                    customImageToast(
                        R.drawable.moto_toast, getString(R.string.fail_auth),
                        Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_login, LoginSignFragment()).commit()
                }
            })
    }
     */

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 9001
        private const val PRIVATE_MODE = 0
        private const val PREF_NAME = "fluxControl"
    }

}