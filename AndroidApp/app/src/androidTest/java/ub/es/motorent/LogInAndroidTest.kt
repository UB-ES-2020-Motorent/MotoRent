package ub.es.motorent

import android.content.Intent
import android.provider.ContactsContract
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import bolts.Task
import com.google.firebase.auth.AuthResult
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ub.es.motorent.app.model.UserInfo
import ub.es.motorent.app.presenter.LoginPresenter
import ub.es.motorent.app.view.LoginActivity

@RunWith(AndroidJUnit4::class)
public class LogInAndroidTest {

    @get:Rule
    var ActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun itRendersCorrectly(){
        onView(ViewMatchers.withId(R.id.login_btn))
            .check(ViewAssertions.matches(withText("Iniciar Sessió")))

    }
}