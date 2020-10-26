package ub.es.motorent.app.view

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ub.es.motorent.R
import ub.es.motorent.app.model.Data
import ub.es.motorent.app.model.USER_NAME
import ub.es.motorent.app.model.User


class WelcomeActivity : FullScreenActivity() {

    private val TAG = "WelcomeActivity"

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    private lateinit var setnameButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)

        ViewAdjuster.adjustViewLayoutPadding(findViewById(R.id.root))

        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        mAuth = FirebaseAuth.getInstance()

        db = FirebaseFirestore.getInstance()

        val uid = mAuth.currentUser!!.uid

        val emailNameTv : TextView = findViewById(R.id.usermail_tv)

        emailNameTv.text = mAuth.currentUser!!.email!!.split('@')[0]

        ViewAdjuster.adjustView(emailNameTv)

        ViewAdjuster.adjustView(findViewById(R.id.logo_img))

        val nameField: EditText = findViewById(R.id.input_name_et)
        ViewAdjuster.adjustView(nameField)

        ViewAdjuster.adjustView(findViewById(R.id.welcome_tv))
        ViewAdjuster.adjustView(findViewById(R.id.ask_name_tv))

        setnameButton = findViewById(R.id.setname_btn)
        setnameButton.setOnClickListener {
            if (nameField.text.isNotBlank()) {
                db.collection(USERS).whereEqualTo(USER_NAME, nameField.text.toString()).get().addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        if (task.result!!.documents.isEmpty()) {
                            createUser(uid, nameField.text.toString())
                        } else {
                            customToast(getString(R.string.user_name_exist),
                                Toast.LENGTH_SHORT,Gravity.TOP or
                                        Gravity.FILL_HORIZONTAL,0,200).show()
                        }
                    }
                }
            } else {
                customToast(getString(R.string.bad_user_name),
                    Toast.LENGTH_SHORT,Gravity.TOP or
                            Gravity.FILL_HORIZONTAL,0,200).show()
            }
        }
        ViewAdjuster.adjustView(setnameButton)
    }

    private fun createUser(uid: String, userName: String) {
        val user = User(userName)
        db.collection(USERS).document(uid).set(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, MapsActivity::class.java)
                customImageToast(
                    R.drawable.moto_toast, getString(R.string.user_created),
                    Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
                Data.user = user
                startActivity(intent)
                finish()
                setnameButton.isClickable = false
            } else {
                customToast(getString(R.string.fail_create_user),
                    Toast.LENGTH_SHORT, Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,100).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        // LOGOUT
        mAuth.signOut()
        mGoogleSignInClient.signOut()
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        setnameButton.isClickable = true
    }
}