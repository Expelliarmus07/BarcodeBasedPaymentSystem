package com.fyp.barcodebasedpaymentsystem.all.activity.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.view.*
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.CartListActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.SettingsActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.MyProductsListAdapters
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import kotlinx.android.synthetic.main.fragment_category.*
import kotlin.collections.ArrayList
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.appcompat.app.AppCompatActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.SearchListAdapter
import com.fyp.barcodebasedpaymentsystem.all.activity.categories.*
import com.google.android.material.textfield.TextInputLayout


class CategoryFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        hideProgressDialog()
        if (productsList.size > 0) {
            product_items.visibility = View.VISIBLE
            no_product_found.visibility = View.GONE

            val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,1)
            val gridLayoutManager = GridLayoutManager(activity,2)
            val linearLayoutManager = LinearLayoutManager(activity)

            product_items.layoutManager = linearLayoutManager // TODO change layout style changed from LinearLayoutManager(activity)
            product_items.setHasFixedSize(true)
            val adapterProduct = MyProductsListAdapters(requireActivity(), productsList)
            product_items.adapter = adapterProduct
        } else {
            product_items.visibility = View.GONE
            no_product_found.visibility = View.VISIBLE
        }
    }

    fun successSearchTextFromFirestore(productsList: ArrayList<Product>){

        //hideProgressDialog()
        if (productsList.size > 0){
            product_items.visibility = View.VISIBLE
            no_product_found.visibility = View.GONE
            val linearLayoutManager = LinearLayoutManager(activity)
            product_items.layoutManager = linearLayoutManager // TODO change layout style changed from LinearLayoutManager(activity)
            product_items.setHasFixedSize(true)
            val adapterSearch = SearchListAdapter(requireActivity(),productsList)
            product_items.adapter = adapterSearch
        } else {
            product_items.visibility = View.GONE
            no_product_found.visibility = View.VISIBLE
        }

    }

       private fun getProductsListFromFirestore() {
           showProgressDialog(resources.getString(R.string.please_wait))
           FirestoreClass().getProductList(this)
       }

        private fun getSearchListFromFirestore(searchText:String) {
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getSearchText(this,searchText)
    }



       override fun onResume() {
           super.onResume()
           val activity = activity as AppCompatActivity?
           val actionBar: ActionBar? = activity!!.supportActionBar
           actionBar!!.setTitle(R.string.product)
           getProductsListFromFirestore()
       }


    var items: Array<String> = arrayOf<String>(
        //"All Products",
        "Beverage",
        "Snacks",
        "Bakery" ,
        "Chilled & Frozen",
        "Cooking Ingredients",
        "Cleaning Items",
        "Grooming Items",
        "Pets",
        "Non food and Stationary")

    lateinit var option: AutoCompleteTextView

    lateinit var boxStyleN : TextInputLayout

    lateinit var adapterItems: ArrayAdapter<String>


    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val t=inflater.inflate(R.layout.fragment_category, container, false)

        boxStyleN = t.findViewById(R.id.boxStyle)
        boxStyleN.boxStrokeColor = R.color.black

        option = t.findViewById(R.id.autoCompleteText)

        adapterItems = ArrayAdapter(requireActivity(), R.layout.category_list_items,items)

        option.setAdapter(adapterItems)

        //option.isFocusable = false //set text editable or not


        option.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                //if (parent!!.getItemAtPosition(position).equals("All Products")){

                //} else{
                    if (parent.getItemAtPosition(position).equals("Beverage")) {
                        startActivity(Intent(activity, Beverage::class.java))
                    }

                    if (parent.getItemAtPosition(position).equals("Snacks")) {
                        startActivity(Intent(activity, Snacks::class.java))
                    }

                    if (parent.getItemAtPosition(position).equals("Cleaning Items")) {
                        startActivity(Intent(activity, CleaningItemsActivity::class.java))
                    }

                    if (parent.getItemAtPosition(position).equals("Grooming Items")) {
                        startActivity(Intent(activity, GroomingItems::class.java))
                    }

                    if (parent.getItemAtPosition(position).equals("Bakery")) {
                        startActivity(Intent(activity, Bakery::class.java))
                    }

                    if (parent.getItemAtPosition(position).equals("Cooking Ingredients")) {
                        startActivity(Intent(activity, cookingIngredients::class.java))
                    }

                    if (parent.getItemAtPosition(position).equals("Pets")) {
                        startActivity(Intent(activity, Pets::class.java))
                    }

                    if (parent.getItemAtPosition(position).equals("Non food and Stationary")) {
                        startActivity(Intent(activity, Nonfoodandstationary::class.java))
                    }

                    if (parent.getItemAtPosition(position).equals("Chilled & Frozen")) {
                        startActivity(Intent(activity, ChilledFrozen::class.java))
                    }

                }
            //}

        return t
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflater.inflate(R.menu.dashboard_menu, menu)
        //inflater.inflate(R.menu.my_cart_menu, menu)
        inflater.inflate(R.menu.search_button, menu)
        //inflater.inflate(R.menu.refresh, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        val searchView = item.actionView as SearchView

        when (id) {
            /*
            R.id.ic_settings1 -> {

                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }



            R.id.action_cart -> {

                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }

             */



            R.id.search_button ->{
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        val smallText = newText.lowercase()
                        getSearchListFromFirestore(smallText)

                        return false
                    }

                })

            }





        }

        return super.onOptionsItemSelected(item)

    }



}


