package ub.es.motorent

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import ub.es.motorent.app.view.ComplementaryFormActivity
import ub.es.motorent.app.presenter.ComplementaryFormPresenter

class ComplementaryFormTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val presenter = ComplementaryFormPresenter(activity = ComplementaryFormActivity())

}