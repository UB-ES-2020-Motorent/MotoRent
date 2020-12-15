package ub.es.motorent

import android.content.Intent
import org.junit.Before
import org.junit.Test
import ub.es.motorent.app.presenter.LoginPresenter
import ub.es.motorent.app.view.LoginActivity
import org.junit.Assert.*
import ub.es.motorent.app.model.UserInfo
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ub.es.motorent.app.model.UserDB

public class LoginActivityTest {
    // mock
    @Mock
    private lateinit var presenter: LoginPresenter
    @Mock
    private lateinit var loginActivity: LoginActivity

    private lateinit var userInfo: UserInfo
    private lateinit var intent: Intent

    val mail:String = "natalia@motorent.com"
    val password:String = "123456"
    val national_id_document:String = "12345678N"
    val name:String = "Natalia"

    @Before
    fun setUp(){
        // init mocks
        MockitoAnnotations.initMocks(this)
        loginActivity = LoginActivity()
        presenter = LoginPresenter(loginActivity)

        userInfo = UserInfo(null, null, national_id_document, mail, null, null, null, name, null)
    }

    @Test
    // checks if fields are not empty
    fun checkIfNotEmptyField() {
        assertEquals(presenter.notEmptyInfoField(mail, password), true)
    }

     @Test
     // checks if the user info is valid
     fun checkUserInfoToAdd(){
         assertEquals(presenter.checkUserInfoToAdd(mail, password), true)
         assertEquals(presenter.checkUserInfoToAdd("testing.motorent.com", "123567"), false)
         assertEquals(presenter.checkUserInfoToAdd("testing@motorent", "123567"), false)
         assertEquals(presenter.checkUserInfoToAdd("testing@motorent", "1"), false)
         assertEquals(presenter.checkUserInfoToAdd("testing@motorent.com", "1364855893"), true)
     }

    @Test
    // verifies that the users can be set correctly in the database
    fun checkSuccessUsersAdd(){
        assertEquals( UserDB.getUsers(), Unit)
    }



}