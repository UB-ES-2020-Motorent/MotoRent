package ub.es.motorent

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner
import ub.es.motorent.app.model.UserInfo
import ub.es.motorent.app.presenter.LoginPresenter
import ub.es.motorent.app.presenter.SignUpPresenter
import ub.es.motorent.app.view.LoginActivity
import ub.es.motorent.app.view.SignUpActivity

@RunWith(MockitoJUnitRunner::class)
class SignUpTest {

    @Mock
    private lateinit var presenter: SignUpPresenter
    @Mock
    private lateinit var signUpActivity: SignUpActivity

    val mail:String = "natalia@motorent.com"
    val password:String = "123456"

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        signUpActivity = SignUpActivity()
        presenter = SignUpPresenter(signUpActivity)
    }

    @Test
    // checks if the password contains a number
    fun checkNumberInStringTest(){
        assertEquals(presenter.checkNumberInString(password), true )
        assertEquals(presenter.checkNumberInString("pepe"), false )
    }

    @Test
    fun userAndMailNotEmptyTest(){
        assertEquals(presenter.userAndMailNotEmpty(mail,password), true )
        assertEquals(presenter.userAndMailNotEmpty("Pepe","pepe@sech.com"), true )
        assertEquals(presenter.userAndMailNotEmpty("Pepe",""), false )
        assertEquals(presenter.userAndMailNotEmpty("","pepe@sech.com"), false )
        assertEquals(presenter.userAndMailNotEmpty("",""), false)
    }

    @Test
    fun checkPasswordTest(){
        assertEquals(presenter.checkPasswordisValid(mail,password), true )
        assertEquals(presenter.checkPasswordisValid("qwerty1", "qwerty1"), true)
        assertEquals(presenter.checkPasswordisValid("qwerty", "qwerty"), false)
        assertEquals(presenter.checkPasswordisValid("qwe1", "qwe1"), false)
        assertEquals(presenter.checkPasswordisValid("qwerty1", "qwer"), false)
    }
}