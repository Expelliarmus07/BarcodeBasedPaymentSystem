package com.fyp.barcodebasedpaymentsystem.all.activity.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.CartListActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.SettingsActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.CustomerReceiptAdapters
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.MyProductsListAdapters
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Order
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import com.fyp.barcodebasedpaymentsystem.all.model.User
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_customer_receipt.*
import kotlinx.android.synthetic.main.fragment_customer_receipt.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CustomerReceiptFragment : BaseFragment() {


    var formatDate= SimpleDateFormat("dd MMMM yyyy",Locale.US)

    private var mDateFrom: String = ""
    private var mDateTo:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val t=inflater.inflate(R.layout.fragment_customer_receipt, container, false)
/*
        t.button.setOnClickListener {
            val getDate: Calendar = Calendar.getInstance()
            val datepicker = DatePickerDialog(requireContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                val selectDate: Calendar = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,year)
                selectDate.set(Calendar.MONTH,month)
                selectDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val date = formatDate.format(selectDate.time)
                val giveTime = selectDate.time
                Toast.makeText(activity, "Date : $date",Toast.LENGTH_SHORT).show()
                mDateFrom = giveTime.toString()

            },getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datepicker.show()
        }

        t.button2.setOnClickListener {
            val getDate: Calendar = Calendar.getInstance()
            val datepicker = DatePickerDialog(requireContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                val selectDate: Calendar = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,year)
                selectDate.set(Calendar.MONTH,month)
                selectDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val giveTime1 = selectDate.time
                val date = formatDate.format(selectDate.time)

                Toast.makeText(activity, "Date : $date",Toast.LENGTH_SHORT).show()

                mDateTo = giveTime1.toString()

            },getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datepicker.show()
        }

 */

        return t
    }

    fun populateOrdersListInUI(productsList: ArrayList<Order>) {

        hideProgressDialog()
        if (productsList.size > 0) {
            rv_receipt.visibility = View.VISIBLE
            tv_no_products_buy.visibility = View.GONE

            val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,1)

            rv_receipt.layoutManager = LinearLayoutManager(activity) // TODO change layout style changed from LinearLayoutManager(activity)
            rv_receipt.setHasFixedSize(true)
            val adapterProduct = CustomerReceiptAdapters(requireActivity(), productsList)
            rv_receipt.adapter = adapterProduct
        } else {
            rv_receipt.visibility = View.GONE
            tv_no_products_buy.visibility = View.VISIBLE
        }
    }

    private fun getMyReceiptList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getReceipt(this@CustomerReceiptFragment)

    }

    override fun onResume() {
        super.onResume()
        getMyReceiptList()
        val activity = activity as AppCompatActivity?
        val actionBar: ActionBar? = activity!!.supportActionBar
        actionBar!!.setTitle(R.string.receipt)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        inflater.inflate(R.menu.my_cart_menu, menu)
        //inflater.inflate(R.menu.sort_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.ic_settings1 -> {

                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }

            R.id.action_cart -> {

                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }

            /*
            R.id.action_sort1 ->{
                Toast.makeText(activity,"Item selected",Toast.LENGTH_SHORT).show()
            }

             */
        }

        return super.onOptionsItemSelected(item)

    }







}