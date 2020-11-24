package ub.es.motorent

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import ub.es.motorent.app.view.ResetPswdActivity
import ub.es.motorent.app.presenter.ResetPswdPresenter

class ResetPswdTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val presenter = ResetPswdPresenter(activity = ResetPswdActivity())

}