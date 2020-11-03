package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import ub.es.motorent.R
import ub.es.motorent.app.model.Data
import ub.es.motorent.app.model.USER_NAME
import ub.es.motorent.app.model.User
import ub.es.motorent.app.view.login.LoginSignFragment
import ub.es.motorent.app.view.login.LoginWaitFragment

const val USERS = "users"

class LoginActivity : FullScreenActivity(), LoginSignFragment.OnLoginSignListener {

    private val TAG = "LoginActivity"
    private val RC_SIGN_IN: Int = 1000

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val recu_psw_btn: Button= findViewById(R.id.recu_psw)
        val register_mail_link_btn: Button = findViewById(R.id.register_mail_link)
        ViewAdjuster.adjustViewLayoutPadding(findViewById(R.id.root))

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        ViewAdjuster.adjustView(findViewById(R.id.motorent_logo))

        supportFragmentManager.beginTransaction().replace(R.id.fragment_login, LoginSignFragment()).commit()


        recu_psw_btn.setOnClickListener {
            val intentI = Intent(this, RecuperarContraActivity::class.java)
            startActivity(intentI)
        }

        register_mail_link_btn.setOnClickListener {
            val intentI = Intent(this, SignUpActivity::class.java)
            startActivity(intentI)
        }


        // set login btn
        val txtEmail : TextView =findViewById(R.id.login_txt_email)
        val txtPassword : TextView =findViewById(R.id.login_txt_password)
        val btnRegister : Button = findViewById(R.id.login_btn)
        btnRegister.setOnClickListener(View.OnClickListener() {
            mAuth.signInWithEmailAndPassword(txtEmail.text.toString(), txtPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        toast("Authentication success.")
                    } else {
                        toast(task.exception?.message.toString())
                    }
                }
        })

    }

    override fun onStart() {
        super.onStart()
        setAuth(mAuth.currentUser)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed
                customToast(
                    getString(R.string.fail_google_auth),
                    Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
                setAuth(null)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    setAuth(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    customToast(
                        getString(R.string.fail_auth),
                        Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
                    mGoogleSignInClient.revokeAccess().addOnCompleteListener {
                        setAuth(null)
                    }
                }
            }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun setAuth(user: FirebaseUser?) {
        if (user != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_login, LoginWaitFragment()).commit()

            var intent = Intent(this, MapsActivity::class.java)
            val userDb = db.collection(USERS).document(user.uid)

            userDb.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doc = task.result!!
                    if (doc.exists()) {
                        customImageToast(
                            R.drawable.moto_toast, getString(R.string.ok_auth) + "\n" + doc.data!![USER_NAME],
                            Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
                        val myUser = doc.toObject(User::class.java)!!
                        Data.user = myUser
                    } else {
                        intent = Intent(this, WelcomeActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    customImageToast(
                        R.drawable.moto_toast, getString(R.string.imp_create_user),
                        Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_login, LoginSignFragment()).commit()
                }
            }
        }
    }

    override fun onLoginSign() {
        signIn()
    }
}