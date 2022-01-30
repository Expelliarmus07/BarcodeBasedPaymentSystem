package com.fyp.barcodebasedpaymentsystem.all.activity.fragments

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.CashierSettingsActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.SettingsActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.barcode.ScanActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.qrcode.QRScanActivity
import kotlinx.android.synthetic.main.fragment_cashier_home.*
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CashierHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CashierHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cash_scan.setOnClickListener {
            val intent = Intent(activity, QRScanActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        val activity = activity as AppCompatActivity?
        val actionBar: ActionBar? = activity!!.supportActionBar
        val colorDrawable = ColorDrawable(resources.getColor(R.color.TopBarGreytoDG))
        actionBar!!.setBackgroundDrawable(colorDrawable)
        actionBar!!.setTitle(R.string.scan_qr)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cashier_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CashierHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CashierHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.ic_settings1 ->{

                startActivity(Intent(activity, CashierSettingsActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }
}