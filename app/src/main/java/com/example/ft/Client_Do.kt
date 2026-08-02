package com.example.ft

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Client_Do : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: DocAdapter
    private val list = mutableListOf<MessageModel>()
    private lateinit var tvClientNoDo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_do)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val toggleButtonClientDo = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupTransportClientDo)
        val btnImportFormClientDo = findViewById<MaterialButton>(R.id.btnImportClientDo)
        val btnExportFormClientDo = findViewById<MaterialButton>(R.id.btnExportClientDo)
        toggleButtonClientDo.check(R.id.btnImportClientDo)
        btnImportFormClientDo.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnImportFormClientDo.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnExportFormClientDo.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnExportFormClientDo.setTextColor(ContextCompat.getColor(this, R.color.white))
        loadDocs("Import")

        toggleButtonClientDo.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked){
                return@addOnButtonCheckedListener
            }
            when(checkedId){

                R.id.btnImportClientDo -> {
                    btnImportFormClientDo.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnImportFormClientDo.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnExportFormClientDo.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnExportFormClientDo.setTextColor(ContextCompat.getColor(this, R.color.white))
                    loadDocs("Import")
                }

                R.id.btnExportClientDo -> {
                    btnExportFormClientDo.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnExportFormClientDo.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnImportFormClientDo.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnImportFormClientDo.setTextColor(ContextCompat.getColor(this, R.color.white))
                    loadDocs("Export")
                }
            }
        }

        rv = findViewById(R.id.rvClientDo)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false
        layoutManager.stackFromEnd = false

        rv.layoutManager = layoutManager

        adapter = DocAdapter(list)
        rv.adapter = adapter

        tvClientNoDo = findViewById(R.id.tvClientNoDo)
    }
    private fun loadDocs(type: String) {

        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("email", userEmail).get()
            .addOnSuccessListener { userSnap ->

                val name = userSnap.documents[0].getString("companyName")

                FirebaseFirestore.getInstance().collection("Jobs").document(type).collection("Reference Number")
                    .get().addOnSuccessListener { refs ->

                        list.clear()
                        adapter.updateList(emptyList())

                        var totalQueries = 0
                        var completedQueries = 0

                        for (refDoc in refs) {

                            val field = if (type == "Import") "consignee" else "shipper"

                            if (refDoc.getString(field) == name) {

                                totalQueries++   // 🔥 only matched refs count karo

                                refDoc.reference.collection("Messages").whereEqualTo("docType", "deliveryOrder").get()
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
                                    }.addOnFailureListener {
                                        Toast.makeText(this, "Error fetching the delivery orders", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        if (totalQueries == 0) {
                            updateUI()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error fetching the reference numbers", Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(this, "Error fetching the company name", Toast.LENGTH_SHORT).show()
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
            tvClientNoDo.visibility = View.VISIBLE
            rv.visibility = View.GONE
        } else {
            tvClientNoDo.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }

        rv.scrollToPosition(0)
    }
}