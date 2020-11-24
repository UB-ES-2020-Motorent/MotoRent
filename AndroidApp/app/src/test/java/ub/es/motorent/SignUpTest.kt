package ub.es.motorent

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.junit.Assert.*
import org.junit.Test
import ub.es.motorent.app.view.SignUpActivity
import ub.es.motorent.app.presenter.SignUpPresenter


class SignUpTest {
    
    val presenter = SignUpPresenter(activity = SignUpActivity())

    @Test fun userAndMailNotEmptyTest(){
        assertEquals(presenter.userAndMailNotEmpty("Pepe","pepe@sech.com"), true )
        assertEquals(presenter.userAndMailNotEmpty("Pepe",""), false )
        assertEquals(presenter.userAndMailNotEmpty("","pepe@sech.com"), false )
        assertEquals(presenter.userAndMailNotEmpty("",""), false)
    }
    @Test fun checkPasswordTest(){
        assertEquals(presenter.checkPassword("qwerty1", "qwerty1"), true)
        assertEquals(presenter.checkPassword("qwerty", "qwerty"), false)
        assertEquals(presenter.checkPassword("qwe1", "qwe1"), false)
        assertEquals(presenter.checkPassword("qwerty1", "qwer"), false)
    }

     /*@Test fun checkNumberInStringTest(){
        assertEquals(
     }*/

}