package ub.es.motorent

import org.junit.Before
import com.google.firebase.auth.FirebaseAuth
import org.mockito.*;
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import ub.es.motorent.app.model.BankDataDB
import ub.es.motorent.app.model.BankDataInfo
import ub.es.motorent.app.model.BankDataList
import ub.es.motorent.app.model.UserDB
import ub.es.motorent.app.presenter.BankFormPresenter
import ub.es.motorent.app.view.BankFormActivity

@RunWith(MockitoJUnitRunner::class)
class BankFormActivityTest {

    @Mock
    private lateinit var presenter: BankFormPresenter
    @Mock
    private lateinit var BankFormActivity: BankFormActivity

    val userId : Int = 1
    val card_number: String = "1111111111111111"
    val card_owner: String= "Natalia"
    val card_cvv: Int = 123
    val card_expiration: String = "12/21"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        BankFormActivity = BankFormActivity()
        presenter = BankFormPresenter(BankFormActivity)

    }

    @Test
    // checks if fields are not empty
    fun checkIfNotEmpty(){
        assertEquals(true, presenter.notEmptyInfoField(card_owner, card_number, card_expiration, card_cvv.toString()))
        assertEquals(false, presenter.notEmptyInfoField("", card_number, card_expiration, card_cvv.toString()))
        assertEquals(false, presenter.notEmptyInfoField(card_owner, "", card_expiration, card_cvv.toString()))
    }

    @Test
    // checks if the Bank Data is valid to add to the database
    fun checkBankDataIsValidTest(){
        // assert if bank data is valid
        assertEquals(true, presenter.checkBankDataIsValid(card_owner, card_number, card_expiration, card_cvv.toString()))
        assertEquals(false, presenter.checkBankDataIsValid(card_owner, card_number, card_expiration, 1232.toString()))
        assertEquals(false, presenter.checkBankDataIsValid(card_owner, card_number, "1573", card_cvv.toString()))
        assertEquals(false, presenter.checkBankDataIsValid(card_owner, "1235", card_expiration, card_cvv.toString()))
        assertEquals(false, presenter.checkBankDataIsValid("", card_number, card_expiration, card_cvv.toString()))

    }

    @Test
    // verifies that the bank data can be set correctly in the database
    fun checkSuccessBankDataAdd(){
        assertEquals(BankDataDB.getAllBankData(), Unit)
    }

    @Test
    // verifies that the bank data can be set correctly in the database (returns BankDataJson)
    fun checkSuccessGetBankData(){
        assertEquals(BankDataDB.addBankData(userId, card_number.toBigInteger(), card_owner,card_cvv, card_expiration), Unit)
        assertEquals(BankDataDB.addBankData(1, 1234444444444444.toBigInteger(), card_owner,card_cvv, card_expiration), Unit)
        assertEquals(BankDataDB.addBankData(2, card_number.toBigInteger(), card_owner,678, "12/22"), Unit)
    }

    @Test
    // verifies that the bank data can be deleted correctly (returns BankDataJson)
    fun checkSuccessDelete(){
        assertEquals(BankDataDB.deleteBankDataById(1, userId), Unit)
        assertEquals(BankDataDB.deleteBankDataById(1, 2), Unit)
        assertEquals(BankDataDB.deleteBankDataById(1, 3), Unit)
    }
    @Test
    // verifies that the bank data can be get correctly (returns BankDataJson)
    fun checkGetBankDataByCardNumberOrAllCardsByUserId(){
        assertEquals(BankDataDB.getDefaultBankDataByUidOrBid(1, 1), Unit)
    }
}