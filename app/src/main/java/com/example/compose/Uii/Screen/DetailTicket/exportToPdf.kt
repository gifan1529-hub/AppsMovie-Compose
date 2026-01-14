package com.example.compose.Uii.Screen.DetailTicket

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast

object exportToPdf {
    fun exportViewToPdf(context: Context, view: View, fileName: String) {

        // ukuran
        val pageHeight = 2750
        val pageWidth = 1220

        val pdfDocument = PdfDocument()
        // nge buat satu halaman yang udah di tentukan ukurannya
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        // buat halaman
        val page = pdfDocument.startPage(pageInfo)
        // dapet canvas sebagai area untuk pdf nya
        val canvas = page.canvas

        // mastiin kalo lebar sama seperti halaman pdf
        val measureWidth = View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY)
        // biarin tinggi mastiin suseai kebutuhannya sendiri
        val measureHeight = View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY)
        // view udah dapet ukuran
        view.measure(measureWidth, measureHeight)
        // nata posisi view halaman, di mulai dari pojok kiri atas sampai selebar dan setinggi yang sudah di tetntuin
        view.layout(0, 0, pageWidth, pageHeight)

        // nge gambar canvas
        view.draw(canvas)
        pdfDocument.finishPage(page)

        val contentValues = ContentValues().apply {
            // nentuin nama file yang ditampilin di file manager
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            // nentuin tipe file
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            // khusus android 10 atau lebih
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Memberi tahu sistem untuk menyimpan file ini di folder Downloads publik.
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
        }

        val resolver = context.contentResolver
        // minta sistem buat bikin file kosong di folder download
        // sistem akan mambuat URL untuk file yang baru dibuat
        val url = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (url != null) {
            try {
                resolver.openOutputStream(url)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                    Toast.makeText(context, "Tiket berhasil diunduh", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal mengunduh tiket", Toast.LENGTH_SHORT).show()
            }
        }
        pdfDocument.close()
    }
}