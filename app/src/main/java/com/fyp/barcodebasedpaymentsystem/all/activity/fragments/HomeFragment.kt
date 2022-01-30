package com.fyp.barcodebasedpaymentsystem.all.activity.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.CartListActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.SettingsActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.barcode.ScanActivity
import kotlinx.android.synthetic.main.fragment_home.*
import android.graphics.drawable.ColorDrawable




class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scan.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as AppCompatActivity?
        val actionBar: ActionBar? = activity!!.supportActionBar
        val colorDrawable = ColorDrawable(resources.getColor(R.color.TopBarGreytoDG))
        actionBar!!.setBackgroundDrawable(colorDrawable)
        actionBar!!.setTitle(R.string.scan_product)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        inflater.inflate(R.menu.my_cart_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.ic_settings1 ->{

                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
        }
            R.id.action_cart ->{

                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }
}