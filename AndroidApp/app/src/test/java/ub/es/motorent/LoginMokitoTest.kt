package ub.es.motorent

import android.content.Intent
import android.widget.Button
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.base.Verify.verify
import org.mockito.*;
import ub.es.motorent.app.presenter.LoginPresenter
import ub.es.motorent.app.view.LoginActivity
import org.junit.Assert.*
import org.junit.Rule
import ub.es.motorent.app.model.UserInfo


@RunWith(AndroidJUnit4::class)
public class LoginMokitoTest {

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
        intent = Intent()
        loginActivity = LoginActivity()
        presenter = LoginPresenter(loginActivity)

        userInfo = UserInfo(null, null, national_id_document, mail, null, null, null, name, null)
    }

    @Test
    fun checkIfNotEmptyField() {
        // assert if not empty
        assertEquals(presenter.notEmptyInfoField(mail, password), false)
    }


    @Test
    fun checkSuccessUserExists(): Boolean {
        //sign in
        presenter.signInWithEmailAndPassword(mail, password)

        //checks if the user exists
        assertEquals(true, verify(presenter.getUsers().users.contains(userInfo)))
        return true
    }

    @Test
    fun goToNextActivityTest(){
        if(checkSuccessUserExists()){
            assertEquals(true, loginActivity.goToForm())
        }
    }

    @Test
    fun checkFailureUserExists(){
        //changing userinfo parameters
        userInfo.mail = "testingfailure@motorent.com"
        //sign in
        presenter.signInWithEmailAndPassword("testingfailure@motorent.com", "")

        //checks if the user exists
        assertEquals(false, verify(presenter.getUsers().users.contains(userInfo)))
    }



}