package com.rahulraghuwanshi.itextpdf

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.itextpdf.text.*
import com.itextpdf.text.html.WebColors
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var cell: PdfPCell
    private lateinit var imgReportLogo: Image

 //   var headColor = BaseColor(144,238,144)
    var redColor = BaseColor(255,204,203)
    var greenColor = BaseColor(144,238,144)
    var limegreen = BaseColor(50, 205, 50)

    private lateinit var mCardStmtFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val directory: File = filesDir
        mCardStmtFile = File(directory.absolutePath, "sample.pdf")

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            try {
                demoPdf();
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val btnOpen = findViewById<Button>(R.id.btn_open)
        btnOpen.setOnClickListener {
         //  openPdf()
            startActivity(Intent(this@MainActivity,PdfActivity::class.java))
        }
    }

    private fun demoPdf() {
        //Create document file
        val document = Document()
        try {
            val format = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a")
            val dateFormat = SimpleDateFormat("ddMMyyyy_HHmm")

            val directory: File = filesDir
            val file = File(directory.absolutePath, "sample.pdf")
            val outputStream = FileOutputStream(file)

//            val watermark = ContextCompat.getDrawable(this@MainActivity,R.drawable.ic_memoneet_watermark)
//            val watermarkBitmap = (watermark as BitmapDrawable).bitmap
//            val watermarkStream = ByteArrayOutputStream()
//            watermarkBitmap.compress(Bitmap.CompressFormat.PNG, 100, watermarkStream)
//            val watermarkLogo: ByteArray = watermarkStream.toByteArray()
//            imgReportLogo = Image.getInstance(watermarkLogo)
//            imgReportLogo.setAbsolutePosition(330f, 330f)

            val writer = PdfWriter.getInstance(document, outputStream)

            // add header and footer
            writer.pageEvent = WatermarkPageEvent(this@MainActivity)

            document.open()
            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("MemoNeet")
            document.addCreator("https://www.memoneet.com/")

            //Create Header table
            val header = PdfPTable(3)
            header.isSplitLate = false
            header.widthPercentage = 100f
            val fl = floatArrayOf(20f, 25f, 50f)
            header.setWidths(fl)

            //for image start
            cell = PdfPCell()
            cell.paddingLeft = 5f
            cell.paddingTop = 5f
            cell.border = Rectangle.NO_BORDER
            //Set Logo in Header Cell
            val logo =
                ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_memoneet_watermark)
            val bitmap = (logo as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val bitmapLogo: ByteArray = stream.toByteArray()
            try {
                imgReportLogo = Image.getInstance(bitmapLogo)
                imgReportLogo.setAbsolutePosition(330f, 330f)
                cell.addElement(imgReportLogo)
                header.addCell(cell)
                //image end

                cell = PdfPCell()
                cell.border = Rectangle.NO_BORDER
                cell.paddingLeft = 10f
                cell.paddingTop = 15f
                cell.addElement(Paragraph("Name :"))
                cell.addElement(Paragraph("Obtained Marks :"))
                cell.addElement(Paragraph("Total Marks :"))
                cell.addElement(Paragraph("Date :"))
                header.addCell(cell)

                cell = PdfPCell()
                cell.border = Rectangle.NO_BORDER
                cell.paddingTop = 15f
                cell.addElement(Paragraph("Sanjay Singh"))
                cell.addElement(Paragraph("570"))
                cell.addElement(Paragraph("720"))
                cell.addElement(Paragraph(format.format(Calendar.getInstance().getTime())))
                header.addCell(cell)
                //header text end

                //root table
                val rootTable = PdfPTable(1)
                rootTable.isSplitLate = false
                rootTable.widthPercentage = 100f
                cell = PdfPCell()
                cell.verticalAlignment = Element.ALIGN_CENTER
                cell.horizontalAlignment = Element.ALIGN_CENTER
                cell.colspan = 1
                cell.addElement(header)
                rootTable.addCell(cell)

                val keyTable = PdfPTable(1)
                keyTable.keepTogether = true
                keyTable.isSplitLate = false
                keyTable.isSplitRows = false
                keyTable.widthPercentage = 100f
                for (i in 1..5) {

                    val queTable = PdfPTable(1)
                    queTable.keepTogether = true
                    queTable.isSplitLate = false
                    queTable.isSplitRows = false
                    queTable.widthPercentage = 100f
//                    val wid = floatArrayOf(2f,96f,2f)
//                    queTable.setWidths(wid)

                    //for que.
                    cell = PdfPCell()
                    cell.paddingLeft = 5f
                    cell.paddingTop = 20f
                    cell.paddingBottom = 35f
                    var titleFont = Font(Font.FontFamily.TIMES_ROMAN, 18.0f, Font.BOLD, BaseColor.BLACK)
                    var titleChunk = Chunk("Que. 1 What is the name of our country?", titleFont)
                    var titleParagraph = Paragraph(titleChunk)
                    cell.addElement(titleParagraph)
                    queTable.addCell(cell)

                    //option a
                    cell = PdfPCell()
                    cell.paddingLeft = 10f
                    cell.paddingBottom = 10f
                    titleFont = Font(Font.FontFamily.TIMES_ROMAN, 15.0f, Font.NORMAL, BaseColor.BLACK)
                    cell.addElement(Paragraph(Chunk("A. Ukraine", titleFont)))
                    queTable.addCell(cell)

                    //option b
                    cell = PdfPCell()
                    cell.paddingLeft = 10f
                    cell.paddingBottom = 10f
                    cell.backgroundColor = greenColor
                    cell.addElement(Paragraph(Chunk("B. India", titleFont)))
                    if (i == 2) cell.borderColor = greenColor
                    queTable.addCell(cell)

                    //option c
                    cell = PdfPCell()
                    cell.paddingLeft = 10f
                    cell.paddingBottom = 10f
                    cell.addElement(Paragraph(Chunk("C. United State", titleFont)))
                    if (i == 2) {
                        cell.borderColor = redColor
                        cell.backgroundColor = redColor
                    }
                    queTable.addCell(cell)

                    //option d
                    cell = PdfPCell()
                    cell.paddingLeft = 10f
                    cell.paddingBottom = 10f
                    cell.addElement(Paragraph(Chunk("D. Russia", titleFont)))
                    queTable.addCell(cell)

                    //for right/wrong answer
                    cell = PdfPCell()
                    cell.paddingTop = 5f
                    val selectedAnsFont = Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD,BaseColor.BLACK)
                    cell.addElement(Paragraph(Chunk("Your Answer: B. India", selectedAnsFont)))
                    cell.border = Rectangle.NO_BORDER
                    queTable.addCell(cell)

                    cell = PdfPCell()
                    val correctFont = Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD, if (i == 2) BaseColor.RED else limegreen)
                    cell.addElement(Paragraph(Chunk("Correct Answer: B. India", correctFont)))
                    cell.border = Rectangle.NO_BORDER
                    queTable.addCell(cell)

                    cell = PdfPCell()
                    cell.colspan = 1
                    cell.border = Rectangle.NO_BORDER
                    cell.addElement(queTable)
                    keyTable.addCell(cell)

                    cell = PdfPCell(Phrase(" "))
                    cell.border = Rectangle.NO_BORDER
                    keyTable.addCell(cell)
                }

                cell = PdfPCell(Phrase(" "))
                cell.border = Rectangle.NO_BORDER
                rootTable.addCell(cell)

                cell = PdfPCell(Phrase(" "))
                cell.border = Rectangle.NO_BORDER
                rootTable.addCell(cell)

                cell = PdfPCell()
                cell.border = Rectangle.NO_BORDER
                cell.colspan = 1
                cell.addElement(keyTable)
                rootTable.addCell(cell)


//                cell = PdfPCell()
//                cell.border = Rectangle.NO_BORDER
//                cell.colspan = 6
//                cell.addElement(ftable)
//                table.addCell(cell)
                document.add(rootTable)
                Toast.makeText(
                    applicationContext,
                    "New PDF named AndroPDF" + dateFormat.format(Calendar.getInstance().getTime())
                        .toString() + ".pdf successfully generated at DOWNLOADS folder",
                    Toast.LENGTH_LONG
                ).show()
            } catch (de: DocumentException) {
                Log.e("PDFCreator", "DocumentException:$de")
            } catch (e: IOException) {
                Log.e("PDFCreator", "ioException:$e")
            } finally {
                document.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


//    @Throws(FileNotFoundException::class, DocumentException::class)
//    fun createPDF() {
//
//        //Create document file
//        val document = Document()
//        try {
//            val format = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a")
//            val dateFormat = SimpleDateFormat("ddMMyyyy_HHmm")
//     //       val cw = ContextWrapper(this@MainActivity)
////            val directory: File = cw.cacheDir
////            val file = File(directory, "RESULT.pdf")
////            val outputStream = FileOutputStream(
////                file
////            )
//            val directory: File = filesDir
//            //updating path for pdf to match with file_path.xml
//            val file = File(directory.absolutePath, "sample.pdf")
//            val outputStream = FileOutputStream(file)
//
//            val writer = PdfWriter.getInstance(document, outputStream)
//
//            //Open the document
//            document.open()
//            document.pageSize = PageSize.A4
//            document.addCreationDate()
//            document.addAuthor("MemoNeet")
//            document.addCreator("https://www.memoneet.com/")
//
//            //Create Header table
//            val header = PdfPTable(3)
//            header.widthPercentage = 100f
//            val fl = floatArrayOf(20f, 35f, 35f)
//            header.setWidths(fl)
//            cell = PdfPCell()
//            cell.border = Rectangle.NO_BORDER
//
//            //Set Logo in Header Cell
//            val logo = ContextCompat.getDrawable(this@MainActivity,R.drawable.ic_memoneet_watermark)
//            val bitmap = (logo as BitmapDrawable).bitmap
//            val stream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//            val bitmapLogo: ByteArray = stream.toByteArray()
//            try {
//                imgReportLogo = Image.getInstance(bitmapLogo)
//                imgReportLogo.setAbsolutePosition(330f, 642f)
//                cell.addElement(imgReportLogo)
//                header.addCell(cell)
//                cell = PdfPCell()
//                cell.setBorder(Rectangle.NO_BORDER)
//
//                // Heading
//                //BaseFont font = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
//                val titleFont = Font(Font.FontFamily.COURIER, 22.0f, Font.BOLD, BaseColor.BLACK)
//
//                //Creating Chunk
//                val titleChunk = Chunk("Andro PDF", titleFont)
//                //Paragraph
//                val titleParagraph = Paragraph(titleChunk)
//                cell.addElement(titleParagraph)
//                cell.addElement(Paragraph("Simple PDF Report"))
//                cell.addElement(
//                    Paragraph(
//                        "Date: " + format.format(
//                            Calendar.getInstance().getTime()
//                        )
//                    )
//                )
//                header.addCell(cell)
//                cell = PdfPCell(Paragraph(""))
//                cell.border = Rectangle.NO_BORDER
//                header.addCell(cell)
//                val pTable = PdfPTable(1)
//                pTable.widthPercentage = 100f
//                cell = PdfPCell()
//                cell.colspan = 1
//                cell.addElement(header)
//                pTable.addCell(cell)
//                val table = PdfPTable(6)
//                val columnWidth = floatArrayOf(6f, 30f, 30f, 20f, 20f, 30f)
//                table.setWidths(columnWidth)
//                cell = PdfPCell()
//                cell.backgroundColor = headColor
//                cell.colspan = 6
//                cell.addElement(pTable)
//                table.addCell(cell)
//                cell = PdfPCell(Phrase(" "))
//                cell.colspan = 6
//                table.addCell(cell)
//                cell = PdfPCell()
//                cell.colspan = 6
//                cell.backgroundColor = tableHeadColor
//                cell = PdfPCell(Phrase("#"))
//                cell.backgroundColor = tableHeadColor
//                table.addCell(cell)
//                cell = PdfPCell(Phrase("Header 1"))
//                cell.backgroundColor = tableHeadColor
//                table.addCell(cell)
//                cell = PdfPCell(Phrase("Header 2"))
//                cell.backgroundColor = tableHeadColor
//                table.addCell(cell)
//                cell = PdfPCell(Phrase("Header 3"))
//                cell.backgroundColor = tableHeadColor
//                table.addCell(cell)
//                cell = PdfPCell(Phrase("Header 4"))
//                cell.backgroundColor = tableHeadColor
//                table.addCell(cell)
//                cell = PdfPCell(Phrase("Header 5"))
//                cell.backgroundColor = tableHeadColor
//                table.addCell(cell)
//                cell = PdfPCell()
//                cell.colspan = 6
//                for (i in 1..10) {
//                    table.addCell(i.toString())
//                    table.addCell("Header 1 row $i")
//                    table.addCell("Header 2 row $i")
//                    table.addCell("Header 3 row $i")
//                    table.addCell("Header 4 row $i")
//                    table.addCell("Header 5 row $i")
//                }
//                val ftable = PdfPTable(6)
//                ftable.widthPercentage = 100f
//                val columnWidtha = floatArrayOf(30f, 10f, 30f, 10f, 30f, 10f)
//                ftable.setWidths(columnWidtha)
//                cell = PdfPCell()
//                cell.colspan = 6
//                cell.backgroundColor = tableHeadColor
//                cell = PdfPCell(Phrase("Total Number"))
//                cell.border = Rectangle.NO_BORDER
//                cell.backgroundColor = tableHeadColor
//                ftable.addCell(cell)
//                cell = PdfPCell(Phrase(""))
//                cell.border = Rectangle.NO_BORDER
//                cell.backgroundColor = tableHeadColor
//                ftable.addCell(cell)
//                cell = PdfPCell(Phrase(""))
//                cell.border = Rectangle.NO_BORDER
//                cell.backgroundColor = tableHeadColor
//                ftable.addCell(cell)
//                cell = PdfPCell(Phrase(""))
//                cell.border = Rectangle.NO_BORDER
//                cell.backgroundColor = tableHeadColor
//                ftable.addCell(cell)
//                cell = PdfPCell(Phrase(""))
//                cell.border = Rectangle.NO_BORDER
//                cell.backgroundColor = tableHeadColor
//                ftable.addCell(cell)
//                cell = PdfPCell(Phrase(""))
//                cell.border = Rectangle.NO_BORDER
//                cell.backgroundColor = tableHeadColor
//                ftable.addCell(cell)
//                cell = PdfPCell(Paragraph("Footer"))
//                cell.colspan = 6
//                ftable.addCell(cell)
//                cell = PdfPCell()
//                cell.colspan = 6
//                cell.addElement(ftable)
//                table.addCell(cell)
//                document.add(table)
//                Toast.makeText(
//                    applicationContext,
//                    "New PDF named AndroPDF" + dateFormat.format(Calendar.getInstance().getTime())
//                        .toString() + ".pdf successfully generated at DOWNLOADS folder",
//                    Toast.LENGTH_LONG
//                ).show()
//            } catch (de: DocumentException) {
//                Log.e("PDFCreator", "DocumentException:$de")
//            } catch (e: IOException) {
//                Log.e("PDFCreator", "ioException:$e")
//            } finally {
//                document.close()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

//    private fun savePDFtoInternalStorage(pdfAsBytes: ByteArray) {
//        //Save in internal memo cache
//        val directory: File = filesDir
//        // val directory: File = filesDirupdating path for pdf to match with file_path.xml
//        var outputStream: OutputStream? = null
//        try {
//            Log.e("TAG", "writing to mStmtFile")
//            outputStream = FileOutputStream(mCardStmtFile, false)
//            outputStream.write(pdfAsBytes)
//            outputStream.flush()
//            outputStream.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

    private fun openPdf() {
        val fileUri: Uri = FileProvider.getUriForFile(
            this@MainActivity,
            "com.rahulraghuwanshi.itextpdf.provider",
            mCardStmtFile
        )
        val intentShareFile = Intent(Intent.ACTION_VIEW)
        intentShareFile.setDataAndType(fileUri, "application/pdf")
        intentShareFile.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//            intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri)
//            intentShareFile.putExtra(
//                Intent.EXTRA_SUBJECT,
//                "getDescription()"
//            )
        //adding grant read permission to share internal file
        intentShareFile.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(intentShareFile)
    }
}