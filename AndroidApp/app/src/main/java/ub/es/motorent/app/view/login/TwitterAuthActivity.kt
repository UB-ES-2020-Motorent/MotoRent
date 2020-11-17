package ub.es.motorent.app.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.TwitterAuthProvider
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import ub.es.motorent.R

class TwitterAuthActivity: AppCompatActivity() {

    private val TAG = "TwitterAuthActivity"

    internal var mTwitterAuthClient: TwitterAuthClient? = null
    private lateinit var fbAuth: FirebaseAuth

    private val callbackManager = CallbackManager.Factory.create()

    private lateinit var loginButton: TwitterLoginButton
    private lateinit var userText: TextView
    private lateinit var statusText: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        fbAuth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)

        val mTwitterAuthConfig = TwitterAuthConfig(
            getString(R.string.twitter_consumer_key),
            getString(R.string.twitter_consumer_secret)
        )
        val twitterConfig: TwitterConfig = TwitterConfig.Builder(this)
            .twitterAuthConfig(mTwitterAuthConfig)
            .build()
        Twitter.initialize(twitterConfig)

        setContentView(R.layout.activity_twitter_auth);

        userText = findViewById(R.id.userText);
        statusText = findViewById(R.id.statusText);
        imageView = findViewById(R.id.profileImage);

        loginButton = findViewById(R.id.loginButton);

        loginButton.setCallback(object : Callback<TwitterSession?>() {
            override fun success(result: Result<TwitterSession?>?) {
                Log.d(TAG, "loginButton Callback: Success")
                if (result != null) {
                    result.data?.let { exchangeTwitterToken(it) } // comprovar aixo
                };
            }

            override fun failure(exception: TwitterException) {
                Log.d(
                    TAG, "loginButton Callback: Failure " +
                            exception.localizedMessage
                )
            }
        })

        mTwitterAuthClient = TwitterAuthClient()
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginButton.onActivityResult(requestCode, resultCode, data)
    }

    private fun exchangeTwitterToken(session: TwitterSession) {

        val credential: AuthCredential = TwitterAuthProvider.getCredential(
            session.authToken.token,
            session.authToken.secret
        )
        fbAuth.signInWithCredential(credential).addOnCompleteListener {

            if(!it.isSuccessful){
                Log.w(TAG, "signInWithFAcebook:failure")
            }
        }
    }
}