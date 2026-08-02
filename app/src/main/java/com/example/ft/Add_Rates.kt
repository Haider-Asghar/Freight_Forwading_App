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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class Add_Rates : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_rates)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val toggleButtonAddRates =findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.toggleGroupTransportAddRates)
        val btnAirFormAddRates = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnAirAddRates)
        val btnSeaFormAddRates = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSeaAddRates)
        val airFormAddRates = findViewById<LinearLayout>(R.id.llAddRatesAir)
        val seaFormAddRates = findViewById<LinearLayout>(R.id.llAddRatesSea)
        toggleButtonAddRates.check(R.id.btnAirAddRates)
        airFormAddRates.visibility = View.VISIBLE
        seaFormAddRates.visibility = View.GONE
        btnAirFormAddRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnAirFormAddRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnSeaFormAddRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnSeaFormAddRates.setTextColor(ContextCompat.getColor(this,R.color.white))

        toggleButtonAddRates.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked){
                return@addOnButtonCheckedListener
            }
            when(checkedId){
                R.id.btnAirAddRates -> {
                    airFormAddRates.visibility = View.VISIBLE
                    seaFormAddRates.visibility = View.GONE
                    btnAirFormAddRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnAirFormAddRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnSeaFormAddRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnSeaFormAddRates.setTextColor(ContextCompat.getColor(this,R.color.white))
                }
                R.id.btnSeaAddRates -> {
                    airFormAddRates.visibility =View.GONE
                    seaFormAddRates.visibility = View.VISIBLE
                    btnSeaFormAddRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnSeaFormAddRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnAirFormAddRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnAirFormAddRates.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        }
        val selectWeightAirAddRates = findViewById<AutoCompleteTextView>(R.id.selectWeightAirAddRates)
        val weightList = listOf("< 45 Kg", ">= 45 & < 100 kg", ">=100 & <300 Kg", ">=300 & <500 Kg", ">=500 & <1000 Kg", ">=1000 Kg")
        selectWeightAirAddRates.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, weightList)
        )
        val cargoTypeAirAddRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeAirAddRates)
        val cargoTypeSeaAddRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaAddRates)
        val cargoType = listOf("DG Goods", "Non-DG Goods")
        cargoTypeAirAddRates.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cargoType)
        )
        cargoTypeSeaAddRates.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cargoType)
        )
        val containerTypeSeaAddRates = findViewById<AutoCompleteTextView>(R.id.containerTypeSeaAddRates)
        val containerType = listOf("FCL", "LCL")
        containerTypeSeaAddRates.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, containerType)
        )
        val layoutFclAddRates = findViewById<LinearLayout>(R.id.llAddRatesFclSea)
        val layoutLclAddRates = findViewById<LinearLayout>(R.id.llAddRatesLclSea)
        containerTypeSeaAddRates.setOnItemClickListener { _, _, position, _ ->
            if(position == 0){
                layoutFclAddRates.visibility = View.VISIBLE
                layoutLclAddRates.visibility = View.GONE
            }
            else{
                layoutLclAddRates.visibility = View.VISIBLE
                layoutFclAddRates.visibility = View.GONE
            }
        }
        val containerSizeSeaAddRates = findViewById<AutoCompleteTextView>(R.id.containerSizeSeaAddRates)
        val containerSize = listOf("20 ft", "40 ft", "40 High Cube")
        containerSizeSeaAddRates.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, containerSize)
        )

        db = FirebaseFirestore.getInstance()
        loadCountries("Air")
        loadCountries("Sea")
        val selectCountryAddRatesAir = findViewById<AutoCompleteTextView>(R.id.selectCountryAddRatesAir)
        selectCountryAddRatesAir.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            loadCities("Air", country)
        }
        val selectCountryAddRatesSea = findViewById<AutoCompleteTextView>(R.id.selectCountryAddRatesSea)
        selectCountryAddRatesSea.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            loadCities("Sea", country)
        }
        val btnAddRates = findViewById<Button>(R.id.btnAddRates)
        btnAddRates.setOnClickListener {
            if(airFormAddRates.visibility == View.VISIBLE){
                saveAirRates()
            }
            else{
                saveSeaRates()
            }
        }
    }
    private fun loadCountries(type: String){
        val countryAir = findViewById<AutoCompleteTextView>(R.id.selectCountryAddRatesAir)
        val countrySea = findViewById<AutoCompleteTextView>(R.id.selectCountryAddRatesSea)
        val list = ArrayList<String>()
        db.collection("Rates").document(type)
            .collection("Countries").get().addOnSuccessListener { result ->
                list.clear()
                for(doc in result) {
                    list.add(doc.id)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list)
                if(type == "Air"){
                    countryAir.setAdapter(adapter)
                }
                else{
                    countrySea.setAdapter(adapter)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching Countries from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadCities(type: String, country: String){
        val cityAir = findViewById<AutoCompleteTextView>(R.id.selectCityAddRatesAir)
        val citySea = findViewById<AutoCompleteTextView>(R.id.selectCityAddRatesSea)
        val list = ArrayList<String>()
        db.collection("Rates").document(type)
            .collection("Countries").document(country)
            .collection("Cities").get().addOnSuccessListener {
                list.clear()
                for(doc in it){
                    list.add(doc.id)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list)
                if(type == "Air"){
                    cityAir.setAdapter(adapter)
                }
                else{
                    citySea.setAdapter(adapter)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching Cities from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun saveAirRates(){
        val country = findViewById<AutoCompleteTextView>(R.id.selectCountryAddRatesAir).text.toString().trim()
        val city = findViewById<AutoCompleteTextView>(R.id.selectCityAddRatesAir).text.toString().trim()
        val cargo = findViewById<AutoCompleteTextView>(R.id.cargoTypeAirAddRates).text.toString().trim()
        val airline = findViewById<TextInputEditText>(R.id.addAirLineNameAddRates).text.toString().trim()
        val airlineName = airline.lowercase().replaceFirstChar { it.uppercase() }
        val weight = findViewById<AutoCompleteTextView>(R.id.selectWeightAirAddRates).text.toString().trim()
        val freight = findViewById<TextInputEditText>(R.id.addAirFreightAddRates).text.toString().trim()
        val exWork = findViewById<TextInputEditText>(R.id.addExWorkChargesAddRatesAir).text.toString().trim()
        val doCharges = findViewById<TextInputEditText>(R.id.addAirDoChargesAddRates).text.toString().trim()
        val transit = findViewById<TextInputEditText>(R.id.addAirTransitTimeAddRates).text.toString().trim()
        if(country.isEmpty() || city.isEmpty() || cargo.isEmpty() || airlineName.isEmpty() || weight.isEmpty() || freight.isEmpty() ||
            exWork.isEmpty() || doCharges.isEmpty() || transit.isEmpty()){
            Toast.makeText(this, "Please fill all above fields", Toast.LENGTH_SHORT).show()
            return
        }
        val baseRef = db.collection("Rates").document("Air")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("CargoTypes").document(cargo)
        baseRef.get().addOnSuccessListener { cargoDoc ->
            if(!cargoDoc.exists()){
                val cargoTypeMeta = hashMapOf(
                    "type" to cargo,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                val airlineRef = baseRef.collection("Airlines").document(airlineName)
                val airlineMeta = hashMapOf(
                    "name" to airlineName,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                baseRef.set(cargoTypeMeta)
                airlineRef.set(airlineMeta)

                val rateData = hashMapOf(
                    "airFreight" to freight,
                    "exWork" to exWork,
                    "doCharges" to doCharges,
                    "transitTime" to transit,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                baseRef.collection("Airlines").document(airlineName)
                    .collection("Rates").document(weight)
                    .set(rateData).addOnSuccessListener {
                        Toast.makeText(this, "Added successfully.", Toast.LENGTH_SHORT).show()
                        clearAirFields()
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            else{
                val airlineRef = baseRef.collection("Airlines").document(airlineName)
                airlineRef.get().addOnSuccessListener { airlineNameDoc ->
                    if(!airlineNameDoc.exists()){
                        val airlineMeta = hashMapOf(
                            "name" to airlineName,
                            "createdAt" to FieldValue.serverTimestamp()
                        )
                        airlineRef.set(airlineMeta)
                        val rateData = hashMapOf(
                            "airFreight" to freight,
                            "exWork" to exWork,
                            "doCharges" to doCharges,
                            "transitTime" to transit,
                            "createdAt" to FieldValue.serverTimestamp()
                        )
                        airlineRef.collection("Rates").document(weight)
                            .set(rateData).addOnSuccessListener {
                                Toast.makeText(this, "Added successfully.", Toast.LENGTH_SHORT).show()
                                clearAirFields()
                            }.addOnFailureListener { e ->
                                Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    else{
                        val weightRef = airlineRef.collection("Rates").document(weight)
                        weightRef.get().addOnSuccessListener { weightDoc ->
                            if(!weightDoc.exists()){
                                val rateData = hashMapOf(
                                    "airFreight" to freight,
                                    "exWork" to exWork,
                                    "doCharges" to doCharges,
                                    "transitTime" to transit,
                                    "createdAt" to FieldValue.serverTimestamp()
                                )
                                weightRef .set(rateData).addOnSuccessListener {
                                    Toast.makeText(this, "Added successfully.", Toast.LENGTH_SHORT).show()
                                    clearAirFields()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                Toast.makeText(this, "This rates have already exists. Please enter other weight rates or other " +
                                        "airline rates or other cargo type rates.", Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener {  e ->
                            Toast.makeText(this, "Error checking the existence of weight slap: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener {  e ->
                    Toast.makeText(this, "Error checking the existence of airline name: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {  e ->
            Toast.makeText(this, "Error checking the existence of cargo type: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun saveSeaRates(){
        val country = findViewById<AutoCompleteTextView>(R.id.selectCountryAddRatesSea).text.toString().trim()
        val city = findViewById<AutoCompleteTextView>(R.id.selectCityAddRatesSea).text.toString().trim()
        val containerType = findViewById<AutoCompleteTextView>(R.id.containerTypeSeaAddRates).text.toString().trim()
        val cargo = findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaAddRates).text.toString().trim()
        val containerSize = findViewById<AutoCompleteTextView>(R.id.containerSizeSeaAddRates).text.toString().trim()
        val freightFcl = findViewById<TextInputEditText>(R.id.addSeaFreightAddRatesFcl).text.toString().trim()
        val exWorkFcl = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaFclAddRates).text.toString().trim()
        val endorsementFcl = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaFclAddRates).text.toString().trim()
        val weightLcl = findViewById<TextInputEditText>(R.id.addWeightSeaLclAddRates).text.toString().trim()
        val freightLcl = findViewById<TextInputEditText>(R.id.addSeaFreightLclAddRates).text.toString().trim()
        val exWorkLcl = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaLclAddRates).text.toString().trim()
        val endorsementLcl = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaLclAddRates).text.toString().trim()
        val transit = findViewById<TextInputEditText>(R.id.addSeaTransitTimeAddRates).text.toString().trim()
        if(country.isEmpty() || city.isEmpty() || transit.isEmpty() || containerType.isEmpty() || cargo.isEmpty()){
            Toast.makeText(this, "Please fill all above fields.", Toast.LENGTH_SHORT).show()
            return
        }
        val baseRef = db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").document(containerType)
        baseRef.get().addOnSuccessListener { containerTypeDoc ->
            if(!containerTypeDoc.exists()){
                if(containerType == "FCL"){
                    if(containerSize.isEmpty() || freightFcl.isEmpty() || exWorkFcl.isEmpty() || endorsementFcl.isEmpty()){
                        Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
                    val containerTypeMeta = hashMapOf(
                        "containerType" to "FCL",
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val cargoTypeRef = baseRef.collection("CargoTypes").document(cargo)
                    val cargoTypeMeta = hashMapOf(
                        "cargoType" to cargo,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    baseRef.set(containerTypeMeta)
                    cargoTypeRef.set(cargoTypeMeta)
                    val data = hashMapOf(
                        "seaFreight" to freightFcl,
                        "exWork" to exWorkFcl,
                        "endorsement" to endorsementFcl,
                        "transitTime" to transit,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    baseRef.collection("CargoTypes").document(cargo)
                        .collection("ContainerSize").document(containerSize)
                        .set(data).addOnSuccessListener {
                            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                            clearSeaFields()
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                else{
                    if(weightLcl.isEmpty() || freightLcl.isEmpty() || exWorkLcl.isEmpty() || endorsementLcl.isEmpty()){
                        Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
                    val containerTypeMeta = hashMapOf(
                        "containerType" to "LCL",
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    val cargoTypeRef = baseRef.collection("CargoTypes").document(cargo)
                    val cargoTypeMeta = hashMapOf(
                        "cargoType" to cargo,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    baseRef.set(containerTypeMeta)
                    cargoTypeRef.set(cargoTypeMeta)
                    val data = hashMapOf(
                        "seaFreight" to freightLcl,
                        "exWork" to exWorkLcl,
                        "endorsement" to endorsementLcl,
                        "transitTime" to transit,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    baseRef.collection("CargoTypes").document(cargo)
                        .collection("Weights").document(weightLcl)
                        .set(data).addOnSuccessListener {
                            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                            clearSeaFields()
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            else{
                val cargoTypeRef = baseRef.collection("CargoTypes").document(cargo)
                cargoTypeRef.get().addOnSuccessListener { cargoDoc ->
                    if(!cargoDoc.exists()){
                        if(containerType == "FCL"){
                            if(containerSize.isEmpty() || freightFcl.isEmpty() || exWorkFcl.isEmpty() || endorsementFcl.isEmpty()){
                                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                                return@addOnSuccessListener
                            }
                            val cargoTypeMeta = hashMapOf(
                                "cargoType" to cargo,
                                "createdAt" to FieldValue.serverTimestamp()
                            )
                            cargoTypeRef.set(cargoTypeMeta)
                            val data = hashMapOf(
                                "seaFreight" to freightFcl,
                                "exWork" to exWorkFcl,
                                "endorsement" to endorsementFcl,
                                "transitTime" to transit,
                                "createdAt" to FieldValue.serverTimestamp()
                            )
                            cargoTypeRef.collection("ContainerSize").document(containerSize)
                                .set(data).addOnSuccessListener {
                                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                                    clearSeaFields()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        else{
                            if(weightLcl.isEmpty() || freightLcl.isEmpty() || exWorkLcl.isEmpty() || endorsementLcl.isEmpty()){
                                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                                return@addOnSuccessListener
                            }
                            val cargoTypeMeta = hashMapOf(
                                "cargoType" to cargo,
                                "createdAt" to FieldValue.serverTimestamp()
                            )
                            cargoTypeRef.set(cargoTypeMeta)
                            val data = hashMapOf(
                                "seaFreight" to freightLcl,
                                "exWork" to exWorkLcl,
                                "endorsement" to endorsementLcl,
                                "transitTime" to transit,
                                "createdAt" to FieldValue.serverTimestamp()
                            )
                            cargoTypeRef.collection("Weights").document(weightLcl)
                                .set(data).addOnSuccessListener {
                                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                                    clearSeaFields()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    else{
                        if(containerType == "FCL"){
                            if(containerSize.isEmpty() || freightFcl.isEmpty() || exWorkFcl.isEmpty() || endorsementFcl.isEmpty()){
                                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                                return@addOnSuccessListener
                            }
                            val containerSizeRef = cargoTypeRef.collection("ContainerSize").document(containerSize)
                            containerSizeRef.get().addOnSuccessListener { containerSizeDoc ->
                                if(!containerSizeDoc.exists()){
                                    val data = hashMapOf(
                                        "seaFreight" to freightFcl,
                                        "exWork" to exWorkFcl,
                                        "endorsement" to endorsementFcl,
                                        "transitTime" to transit,
                                        "createdAt" to FieldValue.serverTimestamp()
                                    )
                                    containerSizeRef.set(data).addOnSuccessListener {
                                            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                                            clearSeaFields()
                                        }.addOnFailureListener { e ->
                                            Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                else{
                                    Toast.makeText(this, "This rates have already exists. Please enter other container size or other " +
                                            "cargo type rates.", Toast.LENGTH_LONG).show()
                                }
                            }.addOnFailureListener {  e ->
                                Toast.makeText(this, "Error checking the existence of container size: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            if(weightLcl.isEmpty() || freightLcl.isEmpty() || exWorkLcl.isEmpty() || endorsementLcl.isEmpty()){
                                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                                return@addOnSuccessListener
                            }
                            val weightLclRef = cargoTypeRef.collection("Weights").document(weightLcl)
                            weightLclRef.get().addOnSuccessListener { weightLclDoc ->
                                if(!weightLclDoc.exists()){
                                    val data = hashMapOf(
                                        "seaFreight" to freightLcl,
                                        "exWork" to exWorkLcl,
                                        "endorsement" to endorsementLcl,
                                        "transitTime" to transit,
                                        "createdAt" to FieldValue.serverTimestamp()
                                    )
                                    weightLclRef.set(data).addOnSuccessListener {
                                            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                                            clearSeaFields()
                                        }.addOnFailureListener { e ->
                                            Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                else{
                                    Toast.makeText(this, "This rates have already exists. Please enter other weight rates or other " +
                                            "cargo type rates.", Toast.LENGTH_LONG).show()
                                }
                            }.addOnFailureListener {  e ->
                                Toast.makeText(this, "Error checking the existence of weights: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }.addOnFailureListener {  e ->
                    Toast.makeText(this, "Error checking the existence of cargo type: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {  e ->
            Toast.makeText(this, "Error checking the existence of container type: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun clearAirFields(){
        findViewById<AutoCompleteTextView>(R.id.selectCountryAddRatesAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityAddRatesAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeAirAddRates).setText("",false)
        findViewById<TextInputEditText>(R.id.addAirLineNameAddRates).setText("")
        findViewById<AutoCompleteTextView>(R.id.selectWeightAirAddRates).setText("",false)
        findViewById<TextInputEditText>(R.id.addAirFreightAddRates).setText("")
        findViewById<TextInputEditText>(R.id.addExWorkChargesAddRatesAir).setText("")
        findViewById<TextInputEditText>(R.id.addAirDoChargesAddRates).setText("")
        findViewById<TextInputEditText>(R.id.addAirTransitTimeAddRates).setText("")
    }
    private fun clearSeaFields(){
        findViewById<AutoCompleteTextView>(R.id.selectCountryAddRatesSea).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.selectCityAddRatesSea).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.containerTypeSeaAddRates).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaAddRates).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.containerSizeSeaAddRates).setText("", false)
        findViewById<TextInputEditText>(R.id.addSeaFreightAddRatesFcl).setText("")
        findViewById<TextInputEditText>(R.id.addExWorkChargesSeaFclAddRates).setText("")
        findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaFclAddRates).setText("")
        findViewById<TextInputEditText>(R.id.addWeightSeaLclAddRates).setText("")
        findViewById<TextInputEditText>(R.id.addSeaFreightLclAddRates).setText("")
        findViewById<TextInputEditText>(R.id.addExWorkChargesSeaLclAddRates).setText("")
        findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaLclAddRates).setText("")
        findViewById<TextInputEditText>(R.id.addSeaTransitTimeAddRates).setText("")
    }
}