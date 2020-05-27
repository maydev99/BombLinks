package com.bombadu.bomblinks

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.util.*

class QrScannerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RESULT = "com.bombadu.bomblinks.EXTRA_RESULT"
    }

    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView: CodeScannerView
    private lateinit var codeResult: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)
        scannerView = findViewById(R.id.scanView)



        Permissions.check(
            this,
            Manifest.permission.CAMERA,
            "Camera Access to Scan QR Codes",
            object : PermissionHandler() {
                override fun onGranted() {
                    openQRScanner()

                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    finish()
                    super.onDenied(context, deniedPermissions)
                }
            })

    }

    private fun openQRScanner() {
        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false
        codeScanner.startPreview()


        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                codeResult = it.text
                val data = Intent().apply {
                    putExtra(EXTRA_RESULT, codeResult)

                }
                //makeAToast("Scan Result: ${it.text}")
                setResult(Activity.RESULT_OK, data)
                finish()
            }


        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                makeAToast("Camera Initialization Error: ${it.message}")
            }
        }

    }

    private fun makeAToast(tMessage: String) {
        Toast.makeText(this, tMessage, Toast.LENGTH_SHORT).show()
    }
}
