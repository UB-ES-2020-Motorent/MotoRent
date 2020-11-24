package ub.es.motorent

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import ub.es.motorent.app.view.LoginActivity
import ub.es.motorent.app.presenter.LoginPresenter

class LoginTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val presenter = LoginPresenter(activity = LoginActivity())

}