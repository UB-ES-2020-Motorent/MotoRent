package ub.es.motorent

import android.content.Intent
import android.widget.Button
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.base.Verify.verify
import ub.es.motorent.app.presenter.LoginPresenter
import ub.es.motorent.app.view.LoginActivity
import org.junit.Assert.*
import org.junit.Rule
import ub.es.motorent.app.model.UserInfo
import android.content.Context
import bolts.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.presenter.ResetPswdPresenter
import ub.es.motorent.app.view.ResetPswdActivity
import javax.annotation.meta.When

@RunWith(MockitoJUnitRunner::class)
public class ResetPswdTest {

    @Mock
    private lateinit var presenter: ResetPswdPresenter

    @Mock
    private lateinit var resetPswdActivity: ResetPswdActivity


    val mail: String = "natalia@motorent.com"
    val password: String = "123456"
    val national_id_document: String = "12345678N"
    val name: String = "Natalia"

    @Before
    fun setUp() {
        // init mocks
        MockitoAnnotations.initMocks(this)
        resetPswdActivity = ResetPswdActivity()
        presenter = ResetPswdPresenter(resetPswdActivity)
    }

    @Test
    fun checkIfNotEmptyFieldTest() {
        // assert if not empty
        assertEquals(true, presenter.notEmptyInfoField(mail))
        assertEquals(false, presenter.notEmptyInfoField(""))
    }

    @Test
    fun verifyMailTest() {
        // checks if the email data is correct
        assertEquals(true, presenter.verifyMail(mail))
        assertEquals(false, presenter.verifyMail("testing.motorent.com"))
        assertEquals(true, presenter.verifyMail("testing@motorent.es"))
        assertEquals(false, presenter.verifyMail("testing@motorentes"))

    }
}