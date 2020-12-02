package ub.es.motorent.app.view

import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.get
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.creditcard_item.view.*
import ub.es.motorent.R
import ub.es.motorent.app.model.BankDataDB
import ub.es.motorent.app.model.BankDataList
import ub.es.motorent.app.model.CommonFunctions
import ub.es.motorent.app.model.UserDB

class CreditCardAdapter( private val mContext: Context, val creditCards: BankDataList) :
    ListAdapter {
    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    /*
    S'hauria de fer:

    bucle targetes de l'usuari que es passa per parametre, recorrer i setejar info
    si voleu que aixo ho implementi directament kotlin fer el extends de CreditAdapter com ArrayAdapter (pot ser més eficient

     */

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // set layout de cada item
        val itemLayout = LayoutInflater.from(mContext).inflate(R.layout.creditcard_item, parent, false)
        val creditCard = creditCards.bankdatas.get(position)
        val userID = CommonFunctions.loadUserInfoFromSharedPrefWithContext(mContext)?.id

        itemLayout.PreferidaCheckBox.setOnClickListener {

            BankDataDB.getBankDataByCardNumberOrAllCardsByUserId(null, creditCard.card_number) {
                for (data in it!!.bankdatas) {
                    if(data.user_id == userID) {
                        UserDB.updateUserInfoInDataBase(userID,null,null,null,null,null,null,id_bank_data = data.id_bank_data){
                            Toast.makeText(mContext,"Nova targeta per defecte seleccionada", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            for (i in creditCards.bankdatas.indices){
                parent?.get(i)?.PreferidaCheckBox?.isChecked = false
                parent?.get(i)?.visaNumberTextView?.setTextColor(ContextCompat.getColor(mContext, R.color.browser_actions_text_color))
                parent?.get(i)?.PreferidaCheckBox?.setTextColor(ContextCompat.getColor(mContext, R.color.browser_actions_text_color))
            }
            itemLayout.PreferidaCheckBox.isChecked = true
            itemLayout.PreferidaCheckBox.setTextColor(ContextCompat.getColor(mContext, R.color.com_facebook_blue))
            itemLayout.visaNumberTextView.setTextColor(ContextCompat.getColor(mContext, R.color.darkGreen))
        }

        itemLayout.buttonEliminar.setOnClickListener {
            BankDataDB.getBankDataByCardNumberOrAllCardsByUserId(null, creditCard.card_number) {
                for (data in it!!.bankdatas) {
                    if(data.user_id == userID) {
                        BankDataDB.deleteBankDataById(data.id_bank_data, userID)
                    }
                }
                parent?.removeViewInLayout(itemLayout)
            }
            Toast.makeText(mContext,"Targeta eliminada correctament", Toast.LENGTH_LONG).show()
            val intentI = Intent(mContext, MapsActivity::class.java)
            mContext.startActivity(intentI)
        }


        itemLayout.visaNumberTextView.text = creditCard.card_number.toString()

        UserDB.getUserByIdOrGoogleToken(id = userID) {
            if(it?.id_bank_data == creditCard.id_bank_data){
                itemLayout.visaNumberTextView.setTextColor(ContextCompat.getColor(mContext, R.color.darkGreen))
                itemLayout.PreferidaCheckBox.isChecked = true
                itemLayout.PreferidaCheckBox.setTextColor(ContextCompat.getColor(mContext, R.color.com_facebook_blue))
            }
        }

        return itemLayout
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {

    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getItem(position: Int): Any {
        return creditCards.bankdatas.get(position)
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun isEnabled(p0: Int): Boolean {
        return true
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getCount(): Int {
        return creditCards.bankdatas.size
    }


}

