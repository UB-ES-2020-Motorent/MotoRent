package ub.es.motorent

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import ub.es.motorent.app.presenter.SettingsPresenter
import ub.es.motorent.app.view.SettingsActivity

class SettingsTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val presenter = SettingsPresenter(activity = SettingsActivity())

}