package com.fyp.barcodebasedpaymentsystem.all.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.*
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.MyOrdersListAdapter
import com.fyp.barcodebasedpaymentsystem.all.activity.barcode.BarcodeActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.categories.*
import com.fyp.barcodebasedpaymentsystem.all.activity.fragments.CashierTransDetailFragment
import com.fyp.barcodebasedpaymentsystem.all.activity.fragments.CategoryFragment
import com.fyp.barcodebasedpaymentsystem.all.activity.fragments.CustomerReceiptFragment
import com.fyp.barcodebasedpaymentsystem.all.activity.qrcode.CashierVerifyingActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.qrcode.QRCodeGeneratorActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.qrcode.QRScanActivity
import com.fyp.barcodebasedpaymentsystem.all.model.*
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_category.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
            activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if(currentUser !=null){
            currentUserID=currentUser.uid
        }

        return currentUserID
    }

    fun getCashierDetails(activity: Activity){
        mFireStore.collection(Constants.STAFF)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                if (!document.exists()) {
                    when (activity) {
                        is CashierLoginpage -> {
                            activity.errorMessage()
                        } }
                }
                else {

                Log.i(activity.javaClass.simpleName, document.toString())
                val staff = document.toObject(Staff::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.APP_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${staff.name1}"
                )
                editor.apply()

                when (activity) {
                    is CashierLoginpage -> {
                        activity.cashierLoggedInSuccess(staff)
                    }

                    is CashierSettingsActivity -> {
                        activity.staffDetailsSuccess(staff)
                    }
                }

            }
            } .addOnFailureListener { e ->
                when(activity){
                    is CashierLoginpage -> {
                        activity.hideProgressDialog()
                    }
                    is CashierSettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun getUserDetails(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                if(!document.exists()){
                    when(activity){
                        is CustomerLoginpageActivity -> {
                            activity.errorMessage()
                        }
                    }

                } else{

                Log.i(activity.javaClass.simpleName,document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.APP_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.name}"
                )
                editor.apply()

                when(activity){
                    is CustomerLoginpageActivity -> {
                      activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity ->{
                        activity.userDetailsSuccess(user)

                    }
                }

                }

            }
            .addOnFailureListener { e ->
               when(activity){
                   is CustomerLoginpageActivity -> {
                       activity.hideProgressDialog()
                   }
                   is SettingsActivity -> {
                       activity.hideProgressDialog()
                   }
               }
            }
    }

    fun updateCashierProfileData(activity: Activity, cashierHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.STAFF)
            .document(getCurrentUserID())
            .update(cashierHashMap)
            .addOnSuccessListener {
                when(activity){
                    is CashierProfileActivity -> {
                        activity.cashierProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e->
                when(activity){
                    is CashierProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }


    fun updateVerified(activity: CashierVerifyingActivity, cashierHashMap: HashMap<String, Any>, code:String){  //todo verifying
        mFireStore.collection(Constants.ORDERS)
            .document(code)
            .update(cashierHashMap)
            .addOnSuccessListener {
                activity.updateSuccess()

            }.addOnFailureListener { e->
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the verifying details.",
                    e
                )
            }

    }

    fun updateEmail(activity: ChangeEmailPassword, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                activity.EmailUpdateSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }




    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e->
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }

    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType:String){
        val sRef: StorageReference= FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "." + Constants.getFileExtention(
                activity,
                imageFileURI
            )
        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                uri ->
                Log.e("Downloadable Image URL",uri.toString())
                when(activity){
                    is UserProfileActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }

                    is CashierAddProductActivity ->{
                        activity.imageUploadSuccess(uri.toString())
                    }

                    is CashierProfileActivity ->{
                        activity.imageUploadSuccess(uri.toString())
                    }
                }
            }

        }

            .addOnFailureListener{ exception ->
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }

                    is CashierAddProductActivity -> {
                        activity.hideProgressDialog()
                    }

                    is CashierProfileActivity ->{
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }

    }

    fun uploadProductDetails(activity: CashierAddProductActivity, productInfo: Product){
        mFireStore.collection(Constants.PRODUCT)
            .document(productInfo.no)                            //TODO collection name for each item
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "ERROR WHILE UPLOADING THE PRODUCT DETAILS.",
                    e
                )
            }
    }


    fun getCheckout (activity: Activity, qrcode: String) {
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo("productname", qrcode)
            .get()
            .addOnSuccessListener { document ->

                when (activity) {
                    is CashierVerifyingActivity -> {
                        if(document.isEmpty){
                            activity.hideProgressDialog()
                            activity.finish()
                            Toast.makeText(activity,"PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }

                        for (document in document) {
                            val list = document.toObject(Order::class.java)
                            activity.setupCashUI(list)
                        }
                    }
                }

                when (activity) {
                    is QRScanActivity -> {

                        if(document.isEmpty){
                            activity.hideProgressDialog()
                            activity.finish()
                            Toast.makeText(activity,"PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }

                        for (document in document) {
                            val list = document.toObject(Order::class.java)
                            CashierVerifyingActivity().setupCashUI(list)
                        }
                    }
                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is CashierVerifyingActivity -> {
                        activity.hideProgressDialog()
                        Log.e(
                            this.javaClass.simpleName,
                            "Error while getting the order details.",
                            e
                        )
                    }

                    is QRScanActivity -> {
                        activity.hideProgressDialog()
                        Log.e(
                            this.javaClass.simpleName,
                            "Error while getting the order details.",
                            e
                        )
                    }
                }
            }
    }

    fun getProduct (activity: BarcodeActivity, barcodeId: String){

        mFireStore.collection(Constants.PRODUCT)
            .whereEqualTo("no", barcodeId)
            .get()
            .addOnSuccessListener { document ->

                if(document.isEmpty){
                    activity.hideProgressDialog()
                    activity.finish()
                    Toast.makeText(activity,"PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                Log.e(activity.javaClass.simpleName, document.toString())
                for(document in document){
                    val product2 = document.toObject(Product::class.java)
                    activity.BproductDetailsSuccess(product2)
                }
            }
            //Log.e(TAG, "${document.id} => ${document.data}")

            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(this.javaClass.simpleName, "Error while getting the product details.", e)
            }

    }

    fun getProductDetails(activity: ProductDetailsActivity, productId: String) {

        // The collection name for PRODUCTS
        mFireStore.collection(Constants.PRODUCT)
            .document(productId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                // Here we get the product details in the form of document.
                Log.e(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Product data model class.
                val product = document.toObject(Product::class.java)!!

                activity.productDetailsSuccess(product)

            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }

    fun addCartItems(activity: Activity, addToCart: CartItem) {

        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {

                when(activity){
                    is ProductDetailsActivity ->{
                        activity.addToCartSuccess()
                    }

                    is BarcodeActivity ->{
                        activity.addToCartSuccess1()
                    }
                }

                // Here call a function of base activity for transferring the result to it.

            }
            .addOnFailureListener { e ->

                when(activity){
                    is ProductDetailsActivity ->{
                        activity.hideProgressDialog()
                    }

                    is BarcodeActivity ->{
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating the document for cart item.",
                    e
                )
            }
    }

    fun getCartList(activity: Activity) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of cart items in the form of documents.
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Cart Items ArrayList.
                val list: ArrayList<CartItem> = ArrayList()

                // A for loop as per the list of documents to convert them into Cart Items ArrayList.
                for (i in document.documents) {

                    val cartItem = i.toObject(CartItem::class.java)!!
                    cartItem.id = i.id

                    list.add(cartItem)
                }
                when (activity) {
                    is CartListActivity -> {
                        activity.successCartItemsList(list)
                    }

                    is CheckoutActivity -> {
                        activity.successCartItemsList(list)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error based on the activity instance.
                when (activity) {
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName, "ERROR WHILE GETTING THE CART LIST ITEMS.", e)
            }
    }

    fun removeItemFromCart(context: Context, cart_id: String){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id) // cart id
            .delete()
            .addOnSuccessListener {
                when (context) {
                    is CartListActivity -> {
                        context.itemRemovedSuccess()
                    }
                }

            }.addOnFailureListener {
                e ->
                when (context) {
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "ERROR WHILE REMOVING ITEM FROM CART LIST!.",
                    e
                )
            }
    }

    fun updateAllDetails(activity: CheckoutActivity, cartList: ArrayList<CartItem>, code: String){
        val writeBatch = mFireStore.batch()

        // Here we will update the product stock in the products collection based to cart quantity.
        for (cartItem in cartList) {

            val productHashMap = HashMap<String, Any>()

            productHashMap[Constants.STOCK_QUANTITY] =
                (cartItem.stock_quantity.toInt() - cartItem.cart_quantity.toInt()).toString()

            val documentReference = mFireStore.collection(Constants.PRODUCT)
                .document(cartItem.product_id)

            writeBatch.update(documentReference, productHashMap)
        }

        // Delete the list of cart items
        for (cartItem in cartList) {

            val documentReference = mFireStore.collection(Constants.CART_ITEMS)
                .document(cartItem.id)
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {
            activity.allDetailsUpdatedSuccessfully(code)

        }.addOnFailureListener { e->
            // Here call a function of base activity for transferring the result to it.
            activity.hideProgressDialog()

            Log.e(activity.javaClass.simpleName, "Error while updating all the details after order placed.", e)
        }

    }

    /*
    fun getSoldProductsList(fragment: VerifiedOrderFragment){ //todo VerifiedOrderFragment
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo("verified",1)
            .get()
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Order> = ArrayList()
                for (i in document.documents) {

                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }
                fragment.successSold(list)


            }.addOnFailureListener {  e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting the orders list.", e)
            }

    }
     */



    fun getMyOrdersList(fragment: CashierTransDetailFragment){
        mFireStore.collection(Constants.ORDERS)
            .orderBy("order_datetime",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {document ->

                Log.e(fragment.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Order> = ArrayList()

                for (i in document.documents) {

                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }

                fragment.populateOrdersListInUI(list)


            }.addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting the orders list.", e)
            }

    }

    fun placeAnOrder(activity:Activity, order: Order){

        when(activity){
            is CheckoutActivity ->{
                mFireStore.collection(Constants.ORDERS)
                    .document(order.productname)   // todo changed here,check if an error occurs
                    .set(order, SetOptions.merge())
                    .addOnSuccessListener {

                        val code = order.productname
                        activity.orderPlacedSuccess(code)



                    }
                    .addOnFailureListener {e ->

                        // Hide the progress dialog if there is any error.
                        activity.hideProgressDialog()
                        Log.e(
                            activity.javaClass.simpleName,
                            "Error while placing an order.",
                            e
                        )
                    }
            }

            is QRCodeGeneratorActivity ->{
                //activity.getcode(code)
            }

        }
    }


    fun getAllProductsList(activity: Activity) {
        mFireStore.collection(Constants.PRODUCT)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                // Here we get the list of boards in the form of documents.
                Log.e("Products List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }
                when(activity){
                    is CartListActivity ->{
                        activity.successProductsListFromFireStore(productsList)
                    }
                    is CheckoutActivity ->{
                        activity.successProductsListFromFireStore(productsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                when(activity){
                    is CartListActivity ->{
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                // Hide the progress dialog if there is any error based on the base class instance.

                Log.e("Get Product List", "Error while getting all product list.", e)
            }
    }

    fun updateMyCart(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id) // cart id
            .update(itemHashMap) // A HashMap of fields which are to be updated.
            .addOnSuccessListener{
                when (context) {
                    is CartListActivity -> {
                        context.itemUpdateSuccess()
                    }
                }
            }.addOnFailureListener {e ->

                // Hide the progress dialog if there is any error.
                when (context) {
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }

                Log.e(
                    context.javaClass.simpleName,
                    "ERROR WHILE UPDATING THE CART ITEM.",
                    e
                )

            }

    }


        fun checkIfItemExistInCart(activity:Activity, productId: String) {
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID()) //TODO multiple user can add item
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.documents.toString())

                // If the document size is greater than 1 it means the product is already added to the cart.
                if (document.documents.size > 0) {

                    when(activity){
                        is ProductDetailsActivity ->{
                            activity.productExistsInCart()
                        }

                        is BarcodeActivity ->{
                            activity.productExistsInCart1()
                        }
                    }

                } else {
                    when(activity){
                        is ProductDetailsActivity ->{
                            activity.hideProgressDialog()
                        }

                        is BarcodeActivity ->{
                            activity.hideProgressDialog()
                        }
                    }

                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error.
                when(activity){
                    is ProductDetailsActivity ->{
                        activity.hideProgressDialog()
                    }

                    is BarcodeActivity ->{
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "ERROR WHILE CHECKING THE EXISTING CART LIST.",
                    e
                )
            }
    }

    fun getItemsList(activity: Activity){

        when(activity){
            is Beverage ->{
                mFireStore.collection(Constants.PRODUCT)
                    .whereEqualTo("category","beverage")
                    .get()

                    .addOnSuccessListener { document ->
                        Log.e("Products List", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id

                            productsList.add(product)
                        }
                        activity.successProductsListFromFireStore(productsList)
                    }
            }

            is Snacks ->{
                mFireStore.collection(Constants.PRODUCT)
                    .whereEqualTo("category","snacks")
                    .get()

                    .addOnSuccessListener { document ->
                        Log.e("Products List", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id

                            productsList.add(product)
                        }
                        activity.successProductsListFromFireStore(productsList)
                    }
            }

            is CleaningItemsActivity ->{
                mFireStore.collection(Constants.PRODUCT)
                    .whereEqualTo("category","cleaning items")
                    .get()

                    .addOnSuccessListener { document ->
                        Log.e("Products List", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id

                            productsList.add(product)
                        }
                        activity.successProductsListFromFireStore(productsList)
                    }
            }

            is GroomingItems ->{
                mFireStore.collection(Constants.PRODUCT)
                    .whereEqualTo("category","grooming stuffs")
                    .get()
                    .addOnSuccessListener { document ->
                        Log.e("Products List", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id

                            productsList.add(product)
                        }
                        activity.successProductsListFromFireStore(productsList)
                    }
            }

            is Bakery ->{
                mFireStore.collection(Constants.PRODUCT)
                    .whereEqualTo("category","bakery")
                    .get()
                    .addOnSuccessListener { document ->
                        Log.e("Products List", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id

                            productsList.add(product)
                        }
                        activity.successProductsListFromFireStore(productsList)
                    }
            }

            is cookingIngredients ->{
                mFireStore.collection(Constants.PRODUCT)
                    .whereEqualTo("category","cooking ingredients")
                    .get()
                    .addOnSuccessListener { document ->
                        Log.e("Products List", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id

                            productsList.add(product)
                        }
                        activity.successProductsListFromFireStore(productsList)
                    }
            }

            is Pets ->{
                mFireStore.collection(Constants.PRODUCT)
                    .whereEqualTo("category","pets")
                    .get()
                    .addOnSuccessListener { document ->
                        Log.e("Products List", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id

                            productsList.add(product)
                        }
                        activity.successProductsListFromFireStore(productsList)
                    }
            }

            is Nonfoodandstationary ->{
                mFireStore.collection(Constants.PRODUCT)
                    .whereEqualTo("category","Non food and stationary")
                    .get()
                    .addOnSuccessListener { document ->
                        Log.e("Products List", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id

                            productsList.add(product)
                        }
                        activity.successProductsListFromFireStore(productsList)
                    }
            }

            is ChilledFrozen ->{
                mFireStore.collection(Constants.PRODUCT)
                    .whereEqualTo("category","chilled & frozen")
                    .get()
                    .addOnSuccessListener { document ->
                        Log.e("Products List", document.documents.toString())
                        val productsList: ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)
                            product!!.product_id = i.id

                            productsList.add(product)
                        }
                        activity.successProductsListFromFireStore(productsList)
                    }
            }


        }
    }

    fun getProductList(fragment: Fragment){
        mFireStore.collection(Constants.PRODUCT)
            //.whereEqualTo(Constants.USER_ID, getCurrentUserID()) //check this
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for(i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when(fragment){
                    is CategoryFragment -> {
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }
            }
    }

    fun getSearchText(fragment: Fragment, searchText:String){
        mFireStore.collection(Constants.PRODUCT)
            //.whereEqualTo("search_field",searchText)
            .orderBy("search_field").startAt(searchText).endAt("$searchText\uf8ff")
            //.whereArrayContains("searchping",searchText)
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for(i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)

                }

                when(fragment){
                    is CategoryFragment -> {
                        fragment.successSearchTextFromFirestore(productsList)
                    }
                }
            }

    }

    /*
    fun getMonth(fragment: CustomerReceiptFragment, monthID:String){
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                for (i in document.documents) {

                    val month = i.toObject(Order::class.java)!!

                    val dateFormat = "MM"
                    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
                    val calendar: Calendar = Calendar.getInstance()
                    calendar.timeInMillis = month.order_datetime
                    val orderDateTime = formatter.format(calendar.time)
                    val december = orderDateTime

                    if(monthID == december){

                    }

                }


            }.addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting the orders list.", e)
            }
    }

     */

    fun getReceipt(fragment: CustomerReceiptFragment){
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            //.orderBy("order_datetime", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {document ->

                Log.e(fragment.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Order> = ArrayList()

                for (i in document.documents) {

                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }

                fragment.populateOrdersListInUI(list)


            }.addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting the orders list.", e)
            }
    }

}


