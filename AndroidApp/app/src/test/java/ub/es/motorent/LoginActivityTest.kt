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
import javax.annotation.meta.When

@RunWith(MockitoJUnitRunner::class)
public class LoginActivityTest {

    @Mock
    private lateinit var presenter: LoginPresenter
    @Mock
    private lateinit var loginActivity: LoginActivity



    private lateinit var userInfo: UserInfo
    private lateinit var intent: Intent
    private lateinit var successTask: Task<AuthResult>

    val mail:String = "natalia@motorent.com"
    val password:String = "123456"
    val national_id_document:String = "12345678N"
    val name:String = "Natalia"

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        intent = Intent()
        loginActivity = LoginActivity()
        presenter = LoginPresenter(loginActivity)

        userInfo = UserInfo(null, null, national_id_document, mail, null, null, null, name, null)
    }

    @Test
    fun checkIfNotEmptyField() {
        // assert if not empty
        assertEquals(presenter.notEmptyInfoField(mail, password), true)
    }

     @Test
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