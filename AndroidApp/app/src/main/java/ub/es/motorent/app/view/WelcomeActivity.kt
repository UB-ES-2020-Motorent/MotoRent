package ub.es.motorent.app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ub.es.motorent.R
import ub.es.motorent.app.presenter.WelcomePresenter
import java.util.*
import kotlin.concurrent.schedule

class WelcomeActivity : FullScreenActivity() {

    private lateinit var presenter: WelcomePresenter

    private var auth: FirebaseAuth = Firebase.auth
    var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        presenter = WelcomePresenter(this)

        timer.schedule(3000) {
            startActivity(presenter.navigationPath())
            finish()
        }
    }

    override fun onPause() {
        timer.cancel()
        super.onPause()
    }

/*


 */
}