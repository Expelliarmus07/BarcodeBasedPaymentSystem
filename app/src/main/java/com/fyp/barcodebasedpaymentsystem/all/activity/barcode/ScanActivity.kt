package com.fyp.barcodebasedpaymentsystem.all.activity.barcode

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.BaseActivity
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import kotlinx.android.synthetic.main.activity_scan.*

private const val CAMERA_REQUEST_CODE = 101

class ScanActivity : BaseActivity() {

    private lateinit var codeScanner: CodeScanner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupPermissions()
        codeScanner()
    }


    private fun codeScanner() {
        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            formats = CodeScanner.ONE_DIMENSIONAL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            scanMode = ScanMode.CONTINUOUS
            isTouchFocusEnabled = true
            //isAutoFocusEnabled = false // Whether to enable auto focus or not
            //isFlashEnabled = true // Whether to enable flash or not

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val barcode = it.text
                    //tv_textView.text= barcode

                    finish()

                    val intent = Intent(this@ScanActivity, BarcodeActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID,barcode)
                    startActivity(intent)
                }

            }


            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Scan", "Camera initialization error: ${it.message}")
                }
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need camera permission", Toast.LENGTH_SHORT).show()
                } else {
                    //success
                }
            }
        }
    }
}
