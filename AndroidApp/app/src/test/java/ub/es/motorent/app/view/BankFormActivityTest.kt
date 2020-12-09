package ub.es.motorent.app.view

import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.base.Verify.verify
import org.mockito.*;
import org.junit.Assert.*
import org.junit.Test
import ub.es.motorent.app.model.BankDataInfo
import ub.es.motorent.app.presenter.BankFormPresenter

class BankFormActivityTest {

    @Mock
    private lateinit var presenter: BankFormPresenter
    @Mock
    private lateinit var BankFormActivity: BankFormActivity

    val card_number: String = "1111111111111111"
    val card_owner: String= "Natalia"
    val card_cvv: Int = 123
    val card_expiration: String = "12/21"

    @Before
    fun setUp() {
        BankFormActivity = BankFormActivity()
        presenter = BankFormPresenter(BankFormActivity)
        var bankdatas : List<BankDataInfo>
    }

    @Test
    fun testAddCard(){
        // add card to user
        presenter.addCardToUser(card_number,card_owner,card_cvv,card_expiration)
    }

    @Test
    fun checkBankDataTest(){
        // assert if bank data is valid
        assertEquals(true, presenter.checkBankData(card_owner, card_number, card_expiration, card_cvv.toString()))
    }
}