package com.rahulraghuwanshi.itextpdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfGState
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream


class WatermarkPageEvent(private val context: Context) : PdfPageEventHelper() {

    override fun onEndPage(writer: PdfWriter?, document: Document?) {
        super.onEndPage(writer, document)

        try {

            val logo = ContextCompat.getDrawable(context, R.drawable.memoneet_watermark)
            val bitmap = (logo as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val bitmapLogo: ByteArray = stream.toByteArray()
            val waterMarkImage: Image = Image.getInstance(bitmapLogo)

            //Get width and height of whole page
            val pdfPageWidth: Double = (document!!.pageSize.width.toDouble()) / 2
            val pdfPageHeight: Double = (document.pageSize.height.toDouble()) / 2

            // get page size and position
            val x = (document.pageSize.left + document.pageSize.right) / 2;
            val y = (document.pageSize.top + document.pageSize.bottom) / 2;

            //Set waterMarkImage on whole page
            val over = writer?.directContentUnder
            val state = PdfGState()
          //  state.setFillOpacity(0.4f)
            over?.setGState(state)
            over?.addImage(
                waterMarkImage,
                pdfPageWidth,
                0.toDouble(),
                0.toDouble(),
                pdfPageHeight,
                x - (pdfPageWidth / 2),
                y - (pdfPageHeight / 2)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}