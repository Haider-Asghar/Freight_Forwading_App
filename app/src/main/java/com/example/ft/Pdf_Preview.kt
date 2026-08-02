package com.example.ft

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.github.barteksc.pdfviewer.PDFView
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class Pdf_Preview : AppCompatActivity() {

    private lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdf_preview)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pdfView = findViewById(R.id.pdfView)

        val url = intent.getStringExtra("url") ?: return

        loadPdf(url)
    }
    private fun loadPdf(url: String) {

        CoroutineScope(Dispatchers.IO).launch {

            try {
                val file = File.createTempFile("temp", ".pdf")

                val connection = URL(url).openConnection()
                connection.connect()
                val input = connection.getInputStream()
                val output = FileOutputStream(file)

                input.copyTo(output)
                input.close()
                output.close()

                withContext(Dispatchers.Main) {
                    pdfView.fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        .spacing(10)
                        .onError {
                            it.printStackTrace()
                        }
                        .onPageError { page, t ->
                            t.printStackTrace()
                        }
                        .load()
                }

                file.deleteOnExit()

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Pdf_Preview, "PDF load failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}