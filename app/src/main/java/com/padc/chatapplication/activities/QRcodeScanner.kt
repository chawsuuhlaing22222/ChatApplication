package com.padc.chatapplication.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.ScanOptions
import com.padc.chatapplication.mvp.views.QRScannerView
import kotlinx.android.synthetic.main.activity_qrcode_scanner.*


class QRcodeScanner : AppCompatActivity(), QRScannerView{

    private lateinit var capture: CaptureManager

    companion object{
        const val IEXTRA_USERVO="user_vo"
        fun newIntent(context: Context,userVO:String):Intent{
            var intent=Intent(context,QRcodeScanner::class.java)
            intent.putExtra(IEXTRA_USERVO,userVO)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.padc.chatapplication.R.layout.activity_qrcode_scanner)

        capture = CaptureManager(this@QRcodeScanner, zxing_barcode_scanner)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.setShowMissingCameraPermissionDialog(false)
        capture.decode()

        val options = ScanOptions()
            .setOrientationLocked(false)
            .setCaptureActivity(QRcodeScanner::class.java)
            .setCameraId(0)
            .setBeepEnabled(false)
            .setBarcodeImageEnabled(true)
            .setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)

        //barcodeLauncher.launch(options)

    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        capture.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return zxing_barcode_scanner.onKeyDown(keyCode, event) || super.onKeyDown(
            keyCode,
            event
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showError(error: String) {
        showError(error)
    }


}