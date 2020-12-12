package ub.es.motorent

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import bolts.Task
import com.google.firebase.auth.AuthResult
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ub.es.motorent.app.model.UserInfo
import ub.es.motorent.app.view.ComplementaryFormActivity
import ub.es.motorent.app.presenter.ComplementaryFormPresenter
import ub.es.motorent.app.presenter.LoginPresenter
import ub.es.motorent.app.view.LoginActivity

class ComplementaryFormTest {
    @Mock
    private lateinit var presenter: ComplementaryFormPresenter
    @Mock
    private lateinit var complementaryFormActivity: ComplementaryFormActivity

    private lateinit var userInfo: UserInfo

    val country:String = "natalia@motorent.com"
    val id_card:String = "21"
    val name:String = "testing"
    val surname:String = "motorent"

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        complementaryFormActivity = ComplementaryFormActivity()
        presenter = ComplementaryFormPresenter(complementaryFormActivity)
    }

    @Test
    fun checkIfNotEmptyField() {
        // assert if not empty
        assertEquals(true, presenter.notEmptyInfoField(name, country, id_card, surname))
        assertEquals(false, presenter.notEmptyInfoField("", country, id_card, surname))
        assertEquals(false, presenter.notEmptyInfoField(name, "", id_card, surname))
    }
}