package com.fyp.barcodebasedpaymentsystem.all.activity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.SoldProductsListAdapter
import com.fyp.barcodebasedpaymentsystem.all.activity.qrcode.CashierVerifyingActivity
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Order
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import kotlinx.android.synthetic.main.fragment_verified_order.*

class VerifiedOrderFragment : BaseFragment() {

    /*
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verified_order, container, false)
    }

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }

    private fun getSoldProductsList(){
     showProgressDialog(resources.getString(R.string.please_wait))
     //FirestoreClass().getSoldProductsList(this@VerifiedOrderFragment)
    }

    fun successSold(soldProductsList:ArrayList<Order>){
        hideProgressDialog()
        if(soldProductsList.size >0){
            rv_sold_product_items.visibility= View.VISIBLE
            tv_no_products_sold.visibility= View.GONE

            rv_sold_product_items.layoutManager = LinearLayoutManager(activity)
            rv_sold_product_items.setHasFixedSize(true)

            val soldProductsListAdapter = SoldProductsListAdapter(requireActivity(), soldProductsList)
            rv_sold_product_items.adapter = soldProductsListAdapter

        }else{
            rv_sold_product_items.visibility= View.GONE
            tv_no_products_sold.visibility= View.VISIBLE
        }
    }

     */


}