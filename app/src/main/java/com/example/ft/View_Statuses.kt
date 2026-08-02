package com.example.ft

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class View_Statuses : AppCompatActivity() {

    lateinit var rv: RecyclerView
    lateinit var list: ArrayList<DocumentSnapshot>
    lateinit var adapter: ShipmentAdapter

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_statuses)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val toggleButtonViewStatuses = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupTransportViewStatuses)
        val btnImportFormViewStatuses = findViewById<MaterialButton>(R.id.btnImportViewStatuses)
        val btnExportFormViewStatuses = findViewById<MaterialButton>(R.id.btnExportViewStatuses)
        toggleButtonViewStatuses.check(R.id.btnImportViewStatuses)
        btnImportFormViewStatuses.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnImportFormViewStatuses.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnExportFormViewStatuses.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnExportFormViewStatuses.setTextColor(ContextCompat.getColor(this, R.color.white))
        loadShipments("Import")

        toggleButtonViewStatuses.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked){
                return@addOnButtonCheckedListener
            }
            when(checkedId){

                R.id.btnImportViewStatuses -> {
                    btnImportFormViewStatuses.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnImportFormViewStatuses.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnExportFormViewStatuses.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnExportFormViewStatuses.setTextColor(ContextCompat.getColor(this, R.color.white))
                    loadShipments("Import")
                    }

                R.id.btnExportViewStatuses -> {
                    btnExportFormViewStatuses.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnExportFormViewStatuses.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnImportFormViewStatuses.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnImportFormViewStatuses.setTextColor(ContextCompat.getColor(this, R.color.white))
                    loadShipments("Export")
                }
            }
        }

        rv = findViewById(R.id.recyclerViewStatuses)
        rv.layoutManager = LinearLayoutManager(this)

        list = ArrayList()
        adapter = ShipmentAdapter(this, list)
        rv.adapter = adapter

    }
    private fun loadShipments(type: String) {

        val user = auth.currentUser ?: return
        val email = user.email ?: return

        db.collection("Users").whereEqualTo("email", email).get().addOnSuccessListener { result ->

                if (result.isEmpty) return@addOnSuccessListener

                val companyName = result.documents[0].id

                db.collection("Jobs").document(type).collection("Reference Number").get().addOnSuccessListener { docs ->

                        list.clear()

                        for (doc in docs) {

                            val consignee = doc.getString("consignee") ?: ""
                            val shipper = doc.getString("shipper") ?: ""

                            // 🔥 FILTER
                            if (type == "Import" && consignee != companyName) continue
                            if (type == "Export" && shipper != companyName) continue

                            list.add(doc)
                        }

                        adapter.notifyDataSetChanged()
                    }
            }
    }
}