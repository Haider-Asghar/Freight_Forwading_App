package com.example.ft

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Client_Select_AirRates : AppCompatActivity() {

    var selectedRate: AirRatesModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_select_air_rates)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val transportType = intent.getStringExtra("transportType")
        val selectType = intent.getStringExtra("selectType")
        val country = intent.getStringExtra("country")
        val city = intent.getStringExtra("city")
        val containerType = intent.getStringExtra("containerType")
        val cargo = intent.getStringExtra("cargoType")
        val weight = intent.getStringExtra("weight")
        val serviceTerm = intent.getStringExtra("serviceTerm")
        val list = intent.getParcelableArrayListExtra<AirRatesModel>("ratesList") ?: arrayListOf()

        val tvTransportSelectClientRates = findViewById<TextView>(R.id.tvTransportSelectClientRates)
        val tvTypeSelectClientRates = findViewById<TextView>(R.id.tvTypeSelectClientRates)
        val tvCountrySelectClientRates = findViewById<TextView>(R.id.tvCountrySelectClientRates)
        val tvCitySelectClientRates = findViewById<TextView>(R.id.tvCitySelectClientRates)
        val tvCargoTypeSelectClientRates = findViewById<TextView>(R.id.tvCargoTypeSelectClientRates)
        val tvWeightSelectClientRates = findViewById<TextView>(R.id.tvWeightSelectClientRates)
        val tvServiceTermSelectClientRates =
            findViewById<TextView>(R.id.tvServiceTermSelectClientRates)
        val tvContainerTypeSelectClientRates =
            findViewById<TextView>(R.id.tvContainerTypeSelectClientRates)
        tvTransportSelectClientRates.text = "$transportType Rates"
        tvTypeSelectClientRates.text = "$selectType"
        tvCountrySelectClientRates.text = "$country"
        tvCitySelectClientRates.text = "$city"
        tvCargoTypeSelectClientRates.text = "$cargo"
        tvServiceTermSelectClientRates.text = "$serviceTerm"
        if (transportType == "Sea") {
            tvContainerTypeSelectClientRates.text = "$containerType"
            if (containerType == "FCL") {
                tvWeightSelectClientRates.text = "$weight"
            } else {
                tvWeightSelectClientRates.text = "$weight CBM"
            }
        } else {
            tvContainerTypeSelectClientRates.visibility = View.GONE
            tvWeightSelectClientRates.text = "$weight Kg"
        }

        val btnSelectClientRates = findViewById<Button>(R.id.btnSelectClientRates)
        val rv = findViewById<RecyclerView>(R.id.rvRates)
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.adapter = AirRatesAdapter(list, serviceTerm!!) { selectedItem ->
            btnSelectClientRates.isEnabled = true
            btnSelectClientRates.alpha = 1f

            selectedRate = selectedItem
        }
        btnSelectClientRates.setOnClickListener {
            btnSelectClientRates.isEnabled = false
            val db = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            if (user == null || selectedRate == null) {
                btnSelectClientRates.isEnabled = true
                return@setOnClickListener
            }
            val email = user.email!!
            db.collection("Users").whereEqualTo("email", email).get().addOnSuccessListener { userResult ->
                if (userResult.isEmpty) {
                    btnSelectClientRates.isEnabled = true
                    return@addOnSuccessListener
                }
                val companyName = userResult.documents[0].id
                val jobsRef = db.collection("Jobs")
                val docName = selectType!!
                val prefix = if (selectType == "Import") {
                    "IMP"
                } else {
                    "EXP"
                }
                jobsRef.document(docName).collection("Reference Number").get().addOnSuccessListener { refDocs ->
                    var nextNumber = 101
                    if (!refDocs.isEmpty) {
                        val numbers = refDocs.documents.mapNotNull {
                            it.id.split("-").getOrNull(1)?.toIntOrNull()
                        }
                        if (numbers.isNotEmpty()) {
                            nextNumber = numbers.maxOrNull()!! + 1
                        }
                    }
                    val refNumber = "$prefix-$nextNumber"
                    val data = HashMap<String, Any>()

                    if(prefix == "IMP"){
                        data["consignee"] = companyName
                        data["shipper"] = ""
                        data["destination"] = "Pakistan"
                        data["origin"] = country!!
                    } else{
                        data["consignee"] = ""
                        data["shipper"] = companyName
                        data["destination"] = country!!
                        data["origin"] = "Pakistan"
                    }
                    data["refNo"] = "$prefix-$nextNumber"
                    data["chatName"] = "$refNumber/$companyName"
                    data["date"] = FieldValue.serverTimestamp()
                    data["city"] = city!!
                    data["material"] = ""
                    data["cargoType"] = cargo!!
                    data["currentStatus"] = "Booking Confirmed"
                    data["serviceTerm"] = serviceTerm!!
                    data["mode"] = transportType!!
                    data["agent"] = ""
                    data["houseAWB"] = ""
                    data["masterAWB"] = ""
                    data["cuttOff"] = ""
                    data["etd"] = ""
                    data["eta"] = ""
                    data["note"] = ""
                    data["lastUpdate"] = FieldValue.serverTimestamp()
                    // Sea case
                    if (transportType == "Sea") {
                        data["containerType"] = containerType!!
                        data["line"] = ""
                        data["blStatus"]  = ""
                        if(containerType == "FCL"){
                            data["containerSize"] = weight!!
                            data["weight"] = ""
                            data["chargeableWeight"] = ""
                        }
                        else{
                            data["weight"] = weight!! + " CBM"
                            data["chargeableWeight"] = selectedRate!!.airline
                        }
                    }
                    else{
                        data["weight"] = weight!! + " KG"
                        data["chargeableWeight"] = ""
                        data["line"] = selectedRate!!.airline
                    }

                    // Rate data
                    data["freight"] = selectedRate!!.freight
                    if(serviceTerm == "ExWork"){
                        data["exWork"] = selectedRate!!.exWork
                    }
                    data["doCharges"] = selectedRate!!.doCharges
                    data["transitTime"] = selectedRate!!.transitTime
                    data["lastMessage"] = ""
                    data["lastTime"] = FieldValue.serverTimestamp()
                    data["unreadCountClient"] = 0
                    data["unreadCountAdmin"] = 0

                    //Step 4: Create Job Structure
                    val docRef = jobsRef.document(docName)
                    docRef.get().addOnSuccessListener { doc ->
                        val task = if(!doc.exists()){
                            docRef.set(mapOf(
                                "selectType" to selectType!!,
                                "createdAt" to FieldValue.serverTimestamp()))
                        } else{
                            null
                        }
                        val proceed = task ?: com.google.android.gms.tasks.Tasks.forResult(null)
                        proceed.addOnSuccessListener {
                            //  Ab sub-collection create karo
                            docRef.collection("Reference Number").document(refNumber).set(data).addOnSuccessListener {
                                AlertDialog.Builder(this)
                                    .setTitle("Job Created Successfully")
                                    .setMessage("Your Shipment Reference Number is:\n$refNumber")
                                    .setCancelable(false)
                                    .setPositiveButton("OK") { _, _ ->
                                        //  Move to Dashboard
                                        val intent = Intent(this, Client_Dashboard::class.java)
                                        intent.putExtra("COMPANY_NAME", companyName)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    }.show()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Error creating the job in firestore", Toast.LENGTH_SHORT).show()
                                btnSelectClientRates.isEnabled = true
                            }
                        }.addOnFailureListener {
                            btnSelectClientRates.isEnabled = true
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error fetching the data from firestore", Toast.LENGTH_SHORT).show()
                        btnSelectClientRates.isEnabled = true
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Error fetching the reference number from firestore", Toast.LENGTH_SHORT).show()
                    btnSelectClientRates.isEnabled = true
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error matching the email in firestore", Toast.LENGTH_SHORT).show()
                btnSelectClientRates.isEnabled = true
            }
        }
    }
}