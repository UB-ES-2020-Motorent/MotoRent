package ub.es.motorent.app.view

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import kotlinx.android.synthetic.main.creditcard_item.view.*
import ub.es.motorent.R
import ub.es.motorent.app.model.BankDataList

class CreditCardAdapter( private val mContext: Context, val creditCards: BankDataList) : Adapter {
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

        // set info de la List<BankDataInfo>
        // agafo la primera creditCard..
        var creditCard = creditCards.bankdatas.get(0)

        itemLayout.visaNumberTextView.text = creditCard.card_number.toString()

        // si voleu es pot afegir al layout la caducitat (poso codi)
        // itemLayout.caducitatTextView.text = creditCard.card_expiration.toString()
        return itemLayout
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun hasStableIds(): Boolean {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }


}

