package com.rahulraghuwanshi.itextpdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File


class PdfActivity : AppCompatActivity() {

    private val pdfQuality = PdfQuality.NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        var i = 0
        val img : ImageView= findViewById(R.id.img)
        openPdfWithAndroidSDK(img,i)

        val btn : Button = findViewById(R.id.btn)
        btn.setOnClickListener {
            openPdfWithAndroidSDK(img,++i)
        }
    }

    fun openPdfWithAndroidSDK(imageView: ImageView, pageNumber: Int) {
        val directory: File = filesDir
        val mCardStmtFile = File(directory.absolutePath, "sample.pdf")
        val fileUri: Uri = FileProvider.getUriForFile(
            this@PdfActivity,
            "com.rahulraghuwanshi.itextpdf.provider",
            mCardStmtFile
        )

        // We will get a page from the PDF file by calling openPage
        val fileDescriptor = ParcelFileDescriptor.open(
            mCardStmtFile,
            ParcelFileDescriptor.MODE_READ_ONLY
        )

       val mPdfRenderer = PdfRenderer(fileDescriptor);
        ///mPdfRenderer.pageCount
       val mPdfPage = mPdfRenderer.openPage(pageNumber);

        // Create a new bitmap and render the page contents on to it
        val bitmap = Bitmap.createBitmap(
            mPdfPage.width * pdfQuality.ratio,
            mPdfPage.height * pdfQuality.ratio,
            Bitmap.Config.ARGB_8888
        )

        mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Set the bitmap in the ImageView so we can view it
        imageView.setImageBitmap(bitmap);
    }
}