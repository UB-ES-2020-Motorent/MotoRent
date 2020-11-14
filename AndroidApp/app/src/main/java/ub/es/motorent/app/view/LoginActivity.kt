package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    internal var mTwitterAuthClient: TwitterAuthClient? = null

    private val callbackManager = CallbackManager.Factory.create()

    private var loginTwitterBtn : Button ? = null
    private var loginFacebookBtn : Button ? = null

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

        loginTwitterBtn = findViewById(R.id.twitter_btn)
        loginFacebookBtn = findViewById(R.id.facebook_btn)

        loginFacebookBtn?.setOnClickListener { loginFacebook() }
        loginTwitterBtn?.setOnClickListener{ getRequestToken() }


        recu_psw_btn.setOnClickListener {
            val intentI = Intent(this, ResetPswdActivity::class.java)
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
                        val intentI = Intent(this, MapsActivity::class.java)
                        startActivity(intentI)

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
                                setAuth(user)

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

    private fun getRequestToken() {
        val mTwitterAuthConfig = TwitterAuthConfig(
            getString(R.string.twitter_consumer_key),
            getString(R.string.twitter_consumer_secret)
        )
        val twitterConfig: TwitterConfig = TwitterConfig.Builder(this)
            .twitterAuthConfig(mTwitterAuthConfig)
            .build()
        Twitter.initialize(twitterConfig)

        mTwitterAuthClient = TwitterAuthClient()

        twitterLogin()
    }

    private fun getTwitterSession(): TwitterSession? {

        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return TwitterCore.getInstance().sessionManager.activeSession
    }

    fun twitterLogin() {
        if (getTwitterSession() == null) {
            mTwitterAuthClient!!.authorize(this, object : Callback<TwitterSession>() {
                override fun success(twitterSessionResult: Result<TwitterSession>) {
                    Toast.makeText(this@LoginActivity, "Success login with twitter", Toast.LENGTH_SHORT).show()
                    val twitterSession = twitterSessionResult.data
                    fetchTwitterEmail(twitterSession)

                }

                override fun failure(e: TwitterException) {
                    Toast.makeText(this@LoginActivity, "Failure", Toast.LENGTH_SHORT).show()
                }
            })
        } else {// the user is identificated already
            fetchTwitterEmail(getTwitterSession())
        }
    }

    fun fetchTwitterEmail(twitterSession: TwitterSession?) {
        mTwitterAuthClient?.requestEmail(twitterSession, object : Callback<String>() {
            override fun success(result: Result<String>) {
                Log.d(TAG, "twitterLogin:userId" + twitterSession!!.userId)
                Log.d(TAG, "twitterLogin:userName" + twitterSession!!.userName)
                Log.d(TAG, "twitterLogin: result.data" + result.data)

                val i = Intent(this@LoginActivity, MapsActivity::class.java)
//                val bundle = Bundle()
//                bundle.putString(Utils.FIRST_NAME, "")
//                bundle.putString(Utils.LAST_NAME, "")
//                bundle.putString(Utils.EMAIL, result.data)
//                bundle.putString(Utils.AUTH_TYPE, "TWITTER")
//                bundle.putString(Utils.TPA_TOKEN, twitterSession.userId.toString())
//                i.putExtras(bundle)
//                startActivity(i)
//                finish()

                twitter_btn!!.visibility = View.GONE
                // btnLogut!!.visibility = View.VISIBLE
                var userId = twitterSession!!.userId
                var username = twitterSession!!.userName
                var email = result.data
                var token = twitterSession.userId.toString()
                var str = "Now you are successfully login with twitter \n\n"
                var tokenStr = ""
                var usernameStr = ""
                var emailStr = ""
                if (token != null || token != "") {
                    tokenStr = "User Id : " + token + "\n\n"
                }

                if (username != null || username != "") {
                    usernameStr = "Username : " + username + "\n\n"
                }

            }


            override fun failure(exception: TwitterException?) {
                Toast.makeText(this@LoginActivity, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

     override fun onLoginSign() {
            signIn()
        }
    }