package com.example.ft

import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Admin_Invoices : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: DocAdapter
    private val list = mutableListOf<MessageModel>()
    private lateinit var fileUri: Uri
    private lateinit var tvAdminNoInvoice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_invoices)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val toggleButtonAdminInvoices = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupTransportAdminInvoices)
        val btnImportFormAdminInvoices = findViewById<MaterialButton>(R.id.btnImportAdminInvoices)
        val btnExportFormAdminInvoices = findViewById<MaterialButton>(R.id.btnExportAdminInvoices)
        toggleButtonAdminInvoices.check(R.id.btnImportAdminInvoices)
        btnImportFormAdminInvoices.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnImportFormAdminInvoices.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnExportFormAdminInvoices.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnExportFormAdminInvoices.setTextColor(ContextCompat.getColor(this, R.color.white))
        loadDocs("Import")

        toggleButtonAdminInvoices.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked){
                return@addOnButtonCheckedListener
            }
            when(checkedId){

                R.id.btnImportAdminInvoices -> {
                    btnImportFormAdminInvoices.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnImportFormAdminInvoices.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnExportFormAdminInvoices.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnExportFormAdminInvoices.setTextColor(ContextCompat.getColor(this, R.color.white))
                    loadDocs("Import")
                }

                R.id.btnExportAdminInvoices -> {
                    btnExportFormAdminInvoices.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnExportFormAdminInvoices.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnImportFormAdminInvoices.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnImportFormAdminInvoices.setTextColor(ContextCompat.getColor(this, R.color.white))
                    loadDocs("Export")
                }
            }
        }

        rv = findViewById(R.id.rvDocs)

        rv.layoutManager = LinearLayoutManager(this)

        adapter = DocAdapter(list)
        rv.adapter = adapter

        tvAdminNoInvoice = findViewById(R.id.tvAdminNoInvoices)

        val btnInvoices = findViewById<Button>(R.id.btnAdminInvoices)
        btnInvoices.setOnClickListener {
            pickFile()
        }
    }
    private fun loadDocs(type: String) {

        FirebaseFirestore.getInstance().collection("Jobs").document(type)
            .collection("Reference Number").get().addOnSuccessListener { refs ->

                list.clear()
                adapter.updateList(emptyList())

                var totalQueries = 0
                var completedQueries = 0

                for (refDoc in refs) {

                    totalQueries++   // 🔥 only matched refs count karo

                    refDoc.reference.collection("Messages").whereEqualTo("docType", "invoice").get()
                        .addOnSuccessListener { msgs ->

                            for (doc in msgs) {
                                val model = MessageModel(
                                    doc.getString("sender") ?: "",
                                    doc.getString("fileName") ?: "",
                                    doc.getTimestamp("time")?.toDate()?.time ?: 0,
                                    false,
                                    "file",
                                    doc.getString("fileUrl") ?: ""
                                )
                                list.add(model)
                            }

                            completedQueries++

                            // 🔥 jab sab complete ho jayein
                            if (completedQueries == totalQueries) {
                                updateUI()
                            }
                        } .addOnFailureListener {
                            Toast.makeText(this, "Error fetching the invoices ", Toast.LENGTH_SHORT).show()
                        }
                }
                if (totalQueries == 0) {
                    updateUI()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error fetching the reference numbers", Toast.LENGTH_SHORT).show()
            }
    }
    private val picker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                fileUri = it
                askFileName()
            }
        }

    private fun pickFile() {
        picker.launch("*/*")
    }
    private fun askFileName() {

        val et = EditText(this)

        AlertDialog.Builder(this).setTitle("Enter Reference Number").setView(et).setPositiveButton("Upload") { _, _ ->
                val refNo = et.text.toString()
                uploadFile(refNo)
            }.show()
    }
    private fun uploadFile(fileName: String) {

        val storageRef = FirebaseStorage.getInstance().reference.child("invoices/${System.currentTimeMillis()}")

        storageRef.putFile(fileUri).addOnSuccessListener {

                storageRef.downloadUrl.addOnSuccessListener { url ->

                    // 🔥 IMPORTANT: correct reference match
                    val refNo = fileName // ya selected refNo

                    val type = if (refNo.startsWith("IMP")) "Import" else "Export"

                    val ref = FirebaseFirestore.getInstance().collection("Jobs").document(type)
                        .collection("Reference Number").document(refNo)

                    val map = hashMapOf(
                        "sender" to "admin",
                        "type" to "file",
                        "docType" to "invoice",
                        "fileUrl" to url.toString(),
                        "fileName" to fileName,
                        "message" to "",
                        "time" to FieldValue.serverTimestamp()
                    )

                    ref.collection("Messages").add(map) .addOnFailureListener {
                        Toast.makeText(this, "Error upload the invoice", Toast.LENGTH_SHORT).show()
                    }
                } .addOnFailureListener {
                    Toast.makeText(this, "Error URL not download of invoice", Toast.LENGTH_SHORT).show()
                }
            } .addOnFailureListener {
            Toast.makeText(this, "Error invoice not upload", Toast.LENGTH_SHORT).show()
        }
    }
    private fun groupWithDate(messages: List<MessageModel>): List<MessageModel> {

        val result = mutableListOf<MessageModel>()
        var lastDate = ""

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        for (msg in messages.sortedByDescending { it.time }) {

            val dateStr = sdf.format(Date(msg.time))

            if (dateStr != lastDate) {

                val label = when {
                    DateUtils.isToday(msg.time) -> "Today"
                    else -> dateStr
                }

                result.add(MessageModel("", label, 0, true))
                lastDate = dateStr
            }

            result.add(msg)
        }

        return result
    }
    private fun updateUI() {

        val grouped = groupWithDate(list)
        adapter.updateList(grouped)

        if (list.isEmpty()) {
            tvAdminNoInvoice.visibility = View.VISIBLE
            rv.visibility = View.GONE
        } else {
            tvAdminNoInvoice.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }

        rv.scrollToPosition(0)
    }
}