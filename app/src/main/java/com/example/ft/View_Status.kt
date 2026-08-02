package com.example.ft

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class View_Status : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_status)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val docId = intent.getStringExtra("docId") ?: return
        val type = intent.getStringExtra("type") ?: return

        loadData(docId, type)
    }
    private fun loadData(docId: String, type: String) {

        db.collection("Jobs").document(type)
            .collection("Reference Number").document(docId).get().addOnSuccessListener { doc ->

                if (!doc.exists()) return@addOnSuccessListener


                fun set(id: Int, value: String?) {
                    findViewById<TextView>(id).text = value ?: ""
                }

                // Date format
                val timestamp = doc.getTimestamp("date")
                val date = timestamp?.toDate()
                val formattedDate = if (date != null) {
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
                } else ""

                set(R.id.tvSDate, formattedDate)
                set(R.id.tvSRefNo, doc.getString("refNo"))
                set(R.id.tvSConsignee, doc.getString("consignee"))
                set(R.id.tvSShipper, doc.getString("shipper"))
                set(R.id.tvSorigin, doc.getString("origin"))
                set(R.id.tvSCity, doc.getString("city"))
                set(R.id.tvSDestination, doc.getString("destination"))
                set(R.id.tvSMaterial, doc.getString("material"))
                set(R.id.tvSCargoType, doc.getString("cargoType"))
                set(R.id.tvSWeight, doc.getString("weight"))
                set(R.id.tvSChargeableWeight, doc.getString("chargeableWeight"))
                set(R.id.tvSStatus, doc.getString("currentStatus"))
                set(R.id.tvSMode, doc.getString("mode"))
                set(R.id.tvSLine, doc.getString("line"))
                set(R.id.tvSServiceTerm, doc.getString("serviceTerm"))
                set(R.id.tvSAgent, doc.getString("agent"))
                set(R.id.tvSMAWB, doc.getString("masterAWB"))
                set(R.id.tvSHAWB, doc.getString("houseAWB"))
                set(R.id.tvSCuttOff, doc.getString("cuttOff"))
                set(R.id.tvSETD, doc.getString("etd"))
                set(R.id.tvSETA, doc.getString("eta"))
                set(R.id.tvSFreight, doc.getString("freight"))
                set(R.id.tvSExWork, doc.getString("exWork"))
                set(R.id.tvSDO, doc.getString("doCharges"))
                set(R.id.tvSTransitTime, doc.getString("transitTime"))
                set(R.id.tvSNote, doc.getString("note"))

                val lastTime = doc.getTimestamp("lastUpdate")?.toDate()
                val lastUpdate = lastTime?.let {
                    SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(it)
                } ?: ""

                set(R.id.tvSLastUpdate, lastUpdate)

                val wBlStatus = findViewById<TextView>(R.id.tvWBLStatus)
                val sBlStatus = findViewById<TextView>(R.id.tvSBLStatus)
                val wContainerType = findViewById<TextView>(R.id.tvWContainerType)
                val sContainerType = findViewById<TextView>(R.id.tvSContainerType)
                val wContainerSize = findViewById<TextView>(R.id.tvWContainerSize)
                val sContainerSize = findViewById<TextView>(R.id.tvSContainerSize)

                val mode = doc.getString("mode")
                if(mode == "Sea"){
                    wBlStatus.visibility = View.VISIBLE
                    sBlStatus.visibility  = View.VISIBLE
                    set(R.id.tvSBLStatus, doc.getString("blStatus"))
                    wContainerType.visibility = View.VISIBLE
                    sContainerType.visibility = View.VISIBLE
                    set(R.id.tvSContainerType, doc.getString("containerType"))

                    val containerType = doc.getString("containerType")
                    if(containerType == "FCL"){
                        wContainerSize.visibility = View.VISIBLE
                        sContainerSize.visibility = View.VISIBLE
                        set(R.id.tvSContainerSize, doc.getString("containerSize"))
                    }
                    else {
                        wContainerSize.visibility = View.GONE
                        sContainerSize.visibility = View.GONE
                    }
                } else {
                    wBlStatus.visibility = View.GONE
                    sBlStatus.visibility = View.GONE
                    wContainerType.visibility = View.GONE
                    sContainerType.visibility = View.GONE
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error loading the data from firestore", Toast.LENGTH_SHORT).show()
            }
    }
}