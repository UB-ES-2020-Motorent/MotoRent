package ub.es.motorent

import org.junit.Assert.*
import org.junit.Test
import ub.es.motorent.app.view.LoginActivity
import ub.es.motorent.app.presenter.LoginPresenter

class LoginTest {

    val presenter = LoginPresenter(activity = LoginActivity())

    @Test
    fun notEmptyInfoFieldTest(){
        assertEquals(presenter.notEmptyInfoField("pepe@pepe.com", "pepepe1"), true)
        assertEquals(presenter.notEmptyInfoField("", "pepepe1"), false)
        assertEquals(presenter.notEmptyInfoField("pepe@pepe.com", ""), false)
        assertEquals(presenter.notEmptyInfoField("", ""), false)
    }

}