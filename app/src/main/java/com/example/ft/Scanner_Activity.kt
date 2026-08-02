package com.example.ft

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult


class Scanner_Activity : AppCompatActivity() {

    private lateinit var scanner: GmsDocumentScanner
    private lateinit var role: String
    private var companyName: String? = null

    private val scannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->

            if (result.resultCode == RESULT_OK) {

                val resultData = GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                val pdfUri = resultData?.pdf?.uri

                if (pdfUri != null) {
                    val intent = Intent(this, Chat_Select::class.java)
                    intent.putExtra("fileUri", pdfUri.toString())
                    intent.putExtra("role", role) // ya "client"

                    if (role == "client") {
                        intent.putExtra("companyName", companyName)
                    }

                    startActivity(intent)
                    finish()
                }
            } else {
                finish() // 🔥 agar user back kare to activity band
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        role = intent.getStringExtra("role") ?: "admin"
        companyName = intent.getStringExtra("companyName")

        val options = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(10)
            .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
            .build()

        scanner = GmsDocumentScanning.getClient(options)

        startScan()
    }
    private fun startScan() {
        scanner.getStartScanIntent(this)
            .addOnSuccessListener { intentSender ->
                scannerLauncher.launch( IntentSenderRequest.Builder(intentSender).build())
            } .addOnFailureListener {
                finish()
            }
    }
}