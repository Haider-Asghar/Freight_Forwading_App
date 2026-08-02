package com.example.ft

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Add_Country : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_country)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val toggleButton = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupTransport)
        val btnAirForm = findViewById<MaterialButton>(R.id.btnAir)
        val btnSeaForm = findViewById<MaterialButton>(R.id.btnSea)
        val airForm = findViewById<LinearLayout>(R.id.linearLayoutAir)
        val seaForm = findViewById<LinearLayout>(R.id.linearLayoutSea)
        toggleButton.check(R.id.btnAir)
        airForm.visibility = View.VISIBLE
        seaForm.visibility = View.GONE
        btnAirForm.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnAirForm.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnSeaForm.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnSeaForm.setTextColor(ContextCompat.getColor(this, R.color.white))

        toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked) {
                return@addOnButtonCheckedListener
            }
            when(checkedId){
                R.id.btnAir -> {
                    airForm.visibility = View.VISIBLE
                    seaForm.visibility = View.GONE
                    btnAirForm.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnAirForm.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnSeaForm.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnSeaForm.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
                R.id.btnSea -> {
                    airForm.visibility = View.GONE
                    seaForm.visibility = View.VISIBLE
                    btnSeaForm.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnSeaForm.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnAirForm.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnAirForm.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        }
        val selectWeightAir = findViewById<AutoCompleteTextView>(R.id.selectWeightAir)
        val weightList = listOf("< 45 Kg", ">= 45 & < 100 kg", ">=100 & <300 Kg", ">=300 & <500 Kg", ">=500 & <1000 Kg", ">=1000 Kg")
        selectWeightAir.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, weightList)
        )
        val cargoTypeAir = findViewById<AutoCompleteTextView>(R.id.cargoTypeAir)
        val cargoTypeSea = findViewById<AutoCompleteTextView>(R.id.cargoTypeSea)
        val cargoType = listOf("DG Goods", "Non-DG Goods")
        cargoTypeAir.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cargoType)
        )
        cargoTypeSea.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cargoType)
        )
        val containerTypeSea = findViewById<AutoCompleteTextView>(R.id.containerTypeSea)
        val containerType = listOf("FCL", "LCL")
        containerTypeSea.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, containerType)
        )
        val layoutFcl = findViewById<LinearLayout>(R.id.linearLayoutFcl)
        val layoutLcl = findViewById<LinearLayout>(R.id.linearLayoutLcl)
        containerTypeSea.setOnItemClickListener { _, _, position, _ ->
            if(position == 0)
            {
                layoutFcl.visibility = View.VISIBLE
                layoutLcl.visibility = View.GONE
            }
            else{
                layoutLcl.visibility = View.VISIBLE
                layoutFcl.visibility = View.GONE
            }
        }
        val containerSizeSea = findViewById<AutoCompleteTextView>(R.id.containerSizeSea)
        val containerSize = listOf("20 ft", "40 ft", "40 High Cube")
        containerSizeSea.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, containerSize)
        )

        db = FirebaseFirestore.getInstance()

        val btnAddCountry = findViewById<Button>(R.id.btnAddCountry)
        btnAddCountry.setOnClickListener {
            if( airForm.visibility == View.VISIBLE ){
                addAirData()
            }
            else {
                addSeaData()
            }
        }
    }
    private fun addAirData(){
        val country = findViewById<TextInputEditText>(R.id.addCountryNameAir).text.toString().trim()
        val countryName = country.lowercase().replaceFirstChar { it.uppercase() }
        val city = findViewById<TextInputEditText>(R.id.addCityNameAir).text.toString().trim()
        val cityName = city.lowercase().replaceFirstChar { it.uppercase() }
        val cargoType = findViewById<AutoCompleteTextView>(R.id.cargoTypeAir).text.toString().trim()
        val airline = findViewById<TextInputEditText>(R.id.addAirLineName).text.toString().trim()
        val airlineName = airline.lowercase().replaceFirstChar { it.uppercase() }
        val weight = findViewById<AutoCompleteTextView>(R.id.selectWeightAir).text.toString().trim()
        val freight = findViewById<TextInputEditText>(R.id.addAirFreight).text.toString().trim()
        val exWork = findViewById<TextInputEditText>(R.id.addExWorkChargesAir).text.toString().trim()
        val doCharges = findViewById<TextInputEditText>(R.id.addAirDoCharges).text.toString().trim()
        val transit = findViewById<TextInputEditText>(R.id.addAirTransitTime).text.toString().trim()
        if(countryName.isEmpty() || cityName.isEmpty() || cargoType.isEmpty() || airlineName.isEmpty() || weight.isEmpty() || freight.isEmpty() ||
            exWork.isEmpty() || doCharges.isEmpty() || transit.isEmpty()){
            Toast.makeText(this, "Please fill all above fields", Toast.LENGTH_SHORT).show()
            return
        }
        val cityRef = db.collection("Rates").document("Air")
            .collection("Countries").document(countryName)
            .collection("Cities").document(cityName)

        cityRef.get().addOnSuccessListener {
            if(it.exists()){
                Toast.makeText(this, "Country and City already added. Please use Add Rates Activity", Toast.LENGTH_LONG).show()
            }
            else{
                val transportTypeRef = db.collection("Rates").document("Air")
                val transportTypeMeta = hashMapOf(
                    "transportationType" to "Air",
                    "createdAt" to FieldValue.serverTimestamp()
                )
                val countryRef = db.collection("Rates").document("Air")
                    .collection("Countries").document(countryName)
                val countryMeta = hashMapOf(
                    "name" to countryName,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                val cityMeta = hashMapOf(
                    "name" to cityName,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                val cargoTypeRef = cityRef.collection("CargoTypes").document(cargoType)
                val cargoTypeMeta = hashMapOf(
                    "type" to cargoType,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                val airlineRef = cargoTypeRef.collection("Airlines").document(airlineName)
                val airlineMeta = hashMapOf(
                    "name" to airlineName,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                transportTypeRef.get().addOnSuccessListener { doc ->
                    if (!doc.exists()) {
                        transportTypeRef.set(transportTypeMeta)
                    }
                }.addOnFailureListener {  e ->
                    Toast.makeText(this, "Error create the document of transport type: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                countryRef.get().addOnSuccessListener { doc ->
                    if (!doc.exists()) {
                        countryRef.set(countryMeta)
                    }
                }.addOnFailureListener {  e ->
                    Toast.makeText(this, "Error creating the document of country name: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                cityRef.set(cityMeta)
                cargoTypeRef.set(cargoTypeMeta)
                airlineRef.set(airlineMeta)

                val rateData = hashMapOf(
                    "airFreight" to freight,
                    "exWork" to exWork,
                    "doCharges" to doCharges,
                    "transitTime" to transit,
                    "createdAt" to FieldValue.serverTimestamp()
                )

                cityRef.collection("CargoTypes").document(cargoType)
                    .collection("Airlines").document(airlineName)
                    .collection("Rates").document(weight)
                    .set(rateData).addOnSuccessListener {
                        Toast.makeText(this, "Added successfully.", Toast.LENGTH_SHORT).show()
                        clearAirFields()
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {  e ->
                Toast.makeText(this, "Error checking the existence of country and city names: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun addSeaData(){
        val country = findViewById<TextInputEditText>(R.id.addCountryNameSea).text.toString().trim()
        val countryName = country.lowercase().replaceFirstChar { it.uppercase() }
        val city = findViewById<TextInputEditText>(R.id.addCityNameSea).text.toString().trim()
        val cityName = city.lowercase().replaceFirstChar { it.uppercase() }
        val transit = findViewById<TextInputEditText>(R.id.addSeaTransitTime).text.toString().trim()
        val containerType = findViewById<AutoCompleteTextView>(R.id.containerTypeSea).text.toString().trim()
        val cargoType = findViewById<AutoCompleteTextView>(R.id.cargoTypeSea).text.toString().trim()
        val containerSize = findViewById<AutoCompleteTextView>(R.id.containerSizeSea).text.toString().trim()
        val seaFreightFcl = findViewById<TextInputEditText>(R.id.addSeaFreightFcl).text.toString().trim()
        val exWorkFcl = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaFcl).text.toString().trim()
        val endorsementFcl = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaFcl).text.toString().trim()
        val weightLcl = findViewById<TextInputEditText>(R.id.addWeightSeaLcl).text.toString().trim()
        val seaFreightLcl = findViewById<TextInputEditText>(R.id.addSeaFreightLcl).text.toString().trim()
        val exWorkLcl = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaLcl).text.toString().trim()
        val endorsementLcl = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaLcl).text.toString().trim()
        if(countryName.isEmpty() || cityName.isEmpty() || transit.isEmpty() || containerType.isEmpty() || cargoType.isEmpty()){
            Toast.makeText(this, "Please fill all above fields", Toast.LENGTH_SHORT).show()
            return
        }
        val cityRef = db.collection("Rates").document("Sea")
            .collection("Countries").document(countryName)
            .collection("Cities").document(cityName)

        cityRef.get().addOnSuccessListener {
            if(it.exists()){
                Toast.makeText(this, "Country and City already added. Please use Add Rates Activity", Toast.LENGTH_LONG).show()
            }
            else{
                if(containerType.equals("FCL",true)){
                    if(containerSize.isEmpty() || seaFreightFcl.isEmpty() || exWorkFcl.isEmpty() || endorsementFcl.isEmpty()){
                        Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
                    val transportTypeRef = db.collection("Rates").document("Sea")
                    val transportTypeMeta = hashMapOf(
                        "transportationType" to "Sea",
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val countryRef = db.collection("Rates").document("Sea")
                        .collection("Countries").document(countryName)
                    val countryMeta = hashMapOf(
                        "name" to countryName,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val cityMeta = hashMapOf(
                        "name" to cityName,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val containerTypeRef = cityRef.collection("ContainerType").document(containerType)
                    val containerTypeMeta = hashMapOf(
                        "containerType" to containerType,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val cargoTypeRef = containerTypeRef.collection("CargoTypes").document(cargoType)
                    val cargoTypeMeta = hashMapOf(
                        "cargoType" to cargoType,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    transportTypeRef.get().addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            transportTypeRef.set(transportTypeMeta)
                        }
                    }.addOnFailureListener {  e ->
                        Toast.makeText(this, "Error create the document of transport type: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    countryRef.get().addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            countryRef.set(countryMeta)
                        }
                    }.addOnFailureListener {  e ->
                        Toast.makeText(this, "Error create the document of country name: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    cityRef.set(cityMeta)
                    containerTypeRef.set(containerTypeMeta)
                    cargoTypeRef.set(cargoTypeMeta)

                    val data = hashMapOf(
                        "seaFreight" to seaFreightFcl,
                        "exWork" to exWorkFcl,
                        "endorsement" to endorsementFcl,
                        "transitTime" to transit,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    cityRef.collection("ContainerType").document("FCL")
                        .collection("CargoTypes").document(cargoType)
                        .collection("ContainerSize").document(containerSize)
                        .set(data).addOnSuccessListener {
                            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                            clearSeaFields()
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                else{
                    if(weightLcl.isEmpty() || seaFreightLcl.isEmpty() || exWorkLcl.isEmpty() || endorsementLcl.isEmpty()){
                        Toast.makeText(this, "Please fill all required fields",Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
                    val transportTypeRef = db.collection("Rates").document("Sea")
                    val transportTypeMeta = hashMapOf(
                        "transportationType" to "Sea",
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val countryRef = db.collection("Rates").document("Sea")
                        .collection("Countries").document(countryName)
                    val countryMeta = hashMapOf(
                        "name" to countryName,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val cityMeta = hashMapOf(
                        "name" to cityName,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val containerTypeRef = cityRef.collection("ContainerType").document(containerType)
                    val containerTypeMeta = hashMapOf(
                        "containerType" to containerType,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val cargoTypeRef = containerTypeRef.collection("CargoTypes").document(cargoType)
                    val cargoTypeMeta = hashMapOf(
                        "cargoType" to cargoType,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    transportTypeRef.get().addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            transportTypeRef.set(transportTypeMeta)
                        }
                    }.addOnFailureListener {  e ->
                        Toast.makeText(this, "Error create the document of transport type: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    countryRef.get().addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            countryRef.set(countryMeta)
                        }
                    }.addOnFailureListener {  e ->
                        Toast.makeText(this, "Error create the document of country name: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    cityRef.set(cityMeta)
                    containerTypeRef.set(containerTypeMeta)
                    cargoTypeRef.set(cargoTypeMeta)

                    val data = hashMapOf(
                        "seaFreight" to seaFreightLcl,
                        "exWork" to exWorkLcl,
                        "endorsement" to endorsementLcl,
                        "transitTime" to transit,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    cityRef.collection("ContainerType").document("LCL")
                        .collection("CargoTypes").document(cargoType)
                        .collection("Weights").document(weightLcl)
                        .set(data).addOnSuccessListener {
                            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                            clearSeaFields()
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }.addOnFailureListener {  e ->
            Toast.makeText(this, "Error checking country and city names: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun clearAirFields(){
        findViewById<TextInputEditText>(R.id.addCountryNameAir).setText("")
        findViewById<TextInputEditText>(R.id.addCityNameAir).setText("")
        findViewById<AutoCompleteTextView>(R.id.cargoTypeAir).setText("",false)
        findViewById<TextInputEditText>(R.id.addAirLineName).setText("")
        findViewById<AutoCompleteTextView>(R.id.selectWeightAir).setText("",false)
        findViewById<TextInputEditText>(R.id.addAirFreight).setText("")
        findViewById<TextInputEditText>(R.id.addExWorkChargesAir).setText("")
        findViewById<TextInputEditText>(R.id.addAirDoCharges).setText("")
        findViewById<TextInputEditText>(R.id.addAirTransitTime).setText("")
    }
    private fun clearSeaFields() {
        findViewById<TextInputEditText>(R.id.addCountryNameSea).setText("")
        findViewById<TextInputEditText>(R.id.addCityNameSea).setText("")
        findViewById<TextInputEditText>(R.id.addSeaTransitTime).setText("")
        findViewById<TextInputEditText>(R.id.addSeaFreightFcl).setText("")
        findViewById<TextInputEditText>(R.id.addExWorkChargesSeaFcl).setText("")
        findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaFcl).setText("")
        findViewById<TextInputEditText>(R.id.addWeightSeaLcl).setText("")
        findViewById<TextInputEditText>(R.id.addSeaFreightLcl).setText("")
        findViewById<TextInputEditText>(R.id.addExWorkChargesSeaLcl).setText("")
        findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaLcl).setText("")
        findViewById<AutoCompleteTextView>(R.id.containerTypeSea).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeSea).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.containerSizeSea).setText("", false)
    }
}