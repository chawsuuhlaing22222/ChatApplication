package com.padc.chatapplication.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.padc.chatapplication.R
import kotlinx.android.synthetic.main.activity_qrcode_scanner_from_gallery.*
import java.io.FileNotFoundException
import java.io.InputStream


class QRCodeScannerFromGalleryActivity : AppCompatActivity() {

    private var imgToScanUri:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_scanner_from_gallery)

        setUpActionListener()


    }

    private fun setUpActionListener() {
        ivSelectImgFromGallery.setOnClickListener {
            val pickIntent = Intent(Intent.ACTION_PICK)
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(pickIntent, 111)
        }

        ivTapToScan.setOnClickListener {
            try {
                val inputStream: InputStream? = imgToScanUri?.let { contentResolver.openInputStream(it) }
                var bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap == null) {
                    Log.e("TAG", "uri is not a bitmap," + imgToScanUri.toString())
                    return@setOnClickListener
                }
                val width = bitmap.width
                val height = bitmap.height
                val pixels = IntArray(width * height)
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                bitmap.recycle()
                bitmap = null
                val source = RGBLuminanceSource(width, height, pixels)
                val bBitmap = BinaryBitmap(HybridBinarizer(source))
                val reader = MultiFormatReader()
               // try {
                    val result = reader.decode(bBitmap)
                    Toast.makeText(
                        this,
                        "The content of the QR image is: " + result.getText(),
                        Toast.LENGTH_SHORT
                    ).show()
               /* } catch (e: Resources.NotFoundException) {
                    Log.e("TAG", "decode exception", e)
                }*/
            } catch (e: FileNotFoundException) {
                Log.e("TAG", "can not open file" + imgToScanUri.toString(), e)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            111 -> {
                if (data == null || data.data == null) {
                    Log.e(
                        "TAG",
                        "The uri is null, probably the user cancelled the image selection process using the back button."
                    )
                    return
                }
                val uri: Uri? = data.data
                imgToScanUri=uri
                ivImageToScan.setImageURI(imgToScanUri)

            }
        }
    }
}