package com.fyp.barcodebasedpaymentsystem.all.activity.fragments

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.CashierAddProductActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.CashierSettingsActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.MyOrdersListAdapter
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Order
import kotlinx.android.synthetic.main.fragment_cashier_trans_detail.*


class CashierTransDetailFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cashier_trans_detail, container, false)
    }

    fun populateOrdersListInUI(ordersList: ArrayList<Order>){
        hideProgressDialog()

        if (ordersList.size > 0) {

            rv_my_order_items.visibility = View.VISIBLE
            tv_no_orders_found.visibility = View.GONE

            rv_my_order_items.layoutManager = LinearLayoutManager(activity)
            rv_my_order_items.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(requireActivity(), ordersList)
            rv_my_order_items.adapter = myOrdersAdapter
        } else {
            rv_my_order_items.visibility = View.GONE
            tv_no_orders_found.visibility = View.VISIBLE
        }

    }

    private fun getMyOrdersList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyOrdersList(this@CashierTransDetailFragment)
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
        val activity = activity as AppCompatActivity?
        val actionBar: ActionBar? = activity!!.supportActionBar
        val colorDrawable = ColorDrawable(resources.getColor(R.color.TopBarGreytoDG))
        actionBar!!.setBackgroundDrawable(colorDrawable)
        actionBar!!.setTitle(R.string.orderhistory)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.ic_settings1 ->{
                startActivity(Intent(activity, CashierSettingsActivity::class.java))
                return true
            }

            R.id.action_add_product ->{
                startActivity(Intent(activity, CashierAddProductActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }
}