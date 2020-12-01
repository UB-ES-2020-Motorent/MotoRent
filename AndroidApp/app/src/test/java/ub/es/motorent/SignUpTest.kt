package ub.es.motorent

import org.junit.Assert.assertEquals
import org.junit.Test
import ub.es.motorent.app.presenter.SignUpPresenter
import ub.es.motorent.app.view.SignUpActivity


class SignUpTest {

    val presenter = SignUpPresenter(activity = SignUpActivity())

    @Test fun checkNumberInStringTest(){
        assertEquals(presenter.checkNumberInString("pepe1"), true )
        assertEquals(presenter.checkNumberInString("pepe"), false )
    }

    @Test fun userAndMailNotEmptyTest(){
        assertEquals(presenter.userAndMailNotEmpty("Pepe","pepe@sech.com"), true )
        assertEquals(presenter.userAndMailNotEmpty("Pepe",""), false )
        assertEquals(presenter.userAndMailNotEmpty("","pepe@sech.com"), false )
        assertEquals(presenter.userAndMailNotEmpty("",""), false)
    }
    /* MIRA RPROBLEMA AMB ELS TOASTS
    @Test fun checkPasswordTest(){
        assertEquals(presenter.checkPassword("qwerty1", "qwerty1"), true)
        assertEquals(presenter.checkPassword("qwerty", "qwerty"), false)
        assertEquals(presenter.checkPassword("qwe1", "qwe1"), false)
        assertEquals(presenter.checkPassword("qwerty1", "qwer"), false)
    }
*/
     /*@Test fun checkNumberInStringTest(){
        assertEquals(
     }*/


}