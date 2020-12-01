package ub.es.motorent.app.view

import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ListAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.creditcard_item.view.*
import ub.es.motorent.R
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
        itemLayout.visaNumberTextView.text = creditCard.card_number.toString()

        UserDB.getUserByIdOrGoogleToken(id = CommonFunctions.loadUserInfoFromSharedPrefWithContext(mContext)?.id) {
            Log.i("CCCCCCCCCC",it?.id_bank_data.toString())
            Log.i("FFFFFFFFFF",creditCard.id_bank_data.toString())
            if(it?.id_bank_data == creditCard.id_bank_data){
                Log.i("AAAAAAAAAAAA","XDDDDDDDDDDDDD")
                itemLayout.visaNumberTextView.setTextColor(R.color.rentMoto)
            }
            Log.i("BBBBBBBBBBB","BBBBBBBBBBBBBB")
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

