package com.fyp.barcodebasedpaymentsystem.all.activity.qrcode

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.Dashboard
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

import kotlinx.android.synthetic.main.activity_qrcode_generator.*
class QRCodeGeneratorActivity : AppCompatActivity() {

    private lateinit var Qrcode: ImageView
    private var mProductname : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_generator)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (intent.hasExtra(Constants.prod_name)) {
            mProductname = intent.getStringExtra(Constants.prod_name)!!
            Log.i("Productname: ", mProductname)
        }

        generateQRcode(mProductname)

        exitButton.setOnClickListener {
            val intent = Intent(this@QRCodeGeneratorActivity, Dashboard::class.java) //TODO change this
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    fun generateQRcode(productname: String){

        Qrcode = findViewById(R.id.ivQRCode)
        //val data = productname

        val size = 512

        val bits = QRCodeWriter().encode(productname,BarcodeFormat.QR_CODE,size,size)

        val bmp = Bitmap.createBitmap(size,size,Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
            Qrcode.setImageBitmap(bmp)
        }



    }