package com.example.ft

import android.content.Intent
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
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class Client_Rates_Query : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_rates_query)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,  0, systemBars.right, systemBars.bottom)
            insets
        }
        val toggleButtonClientRates = findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.toggleGroupTransportClientRatesQuery)
        val btnAirFormClientRates = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnAirClientRatesQuery)
        val btnSeaFormClientRates = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSeaClientRatesQuery)
        val airFormClientRates = findViewById<LinearLayout>(R.id.llClientRatesAir)
        val seaFormClientRates = findViewById<LinearLayout>(R.id.llClientRatesSea)
        toggleButtonClientRates.check(R.id.btnAirClientRatesQuery)
        airFormClientRates.visibility = View.VISIBLE
        seaFormClientRates.visibility = View.GONE
        btnAirFormClientRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnAirFormClientRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnSeaFormClientRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnSeaFormClientRates.setTextColor(ContextCompat.getColor(this, R.color.white))

        toggleButtonClientRates.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked){
                return@addOnButtonCheckedListener
            }
            when(checkedId){
                R.id.btnAirClientRatesQuery -> {
                    airFormClientRates.visibility = View.VISIBLE
                    seaFormClientRates.visibility = View.GONE
                    btnAirFormClientRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnAirFormClientRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnSeaFormClientRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnSeaFormClientRates.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
                R.id.btnSeaClientRatesQuery -> {
                    airFormClientRates.visibility = View.GONE
                    seaFormClientRates.visibility = View.VISIBLE
                    btnSeaFormClientRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnSeaFormClientRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnAirFormClientRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnAirFormClientRates.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        }
        val selectTypeClientRatesAir = findViewById<AutoCompleteTextView>(R.id.selectTypeClientRatesAir)
        val serviceTypes = listOf("Import", "Export")
        selectTypeClientRatesAir.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, serviceTypes)
        )
        val selectTypeClientRatesSea = findViewById<AutoCompleteTextView>(R.id.selectTypeClientRatesSea)
        selectTypeClientRatesSea.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, serviceTypes)
        )
        val serviceTermAirClientRates = findViewById<AutoCompleteTextView>(R.id.serviceTermAirClientRates)
        val serviceTerm = listOf("FOB", "ExWork")
        serviceTermAirClientRates.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, serviceTerm)
        )
        val serviceTermSeaClientRates = findViewById<AutoCompleteTextView>(R.id.serviceTermSeaClientRates)
        serviceTermSeaClientRates.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, serviceTerm)
        )
        db = FirebaseFirestore.getInstance()
        loadCountries("Air")
        loadCountries("Sea")
        val selectCountryClientRatesAir = findViewById<AutoCompleteTextView>(R.id.selectCountryClientRatesAir)
        val selectCityClientRatesAir = findViewById<AutoCompleteTextView>(R.id.selectCityClientRatesAir)
        val cargoTypeAirClientRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeAirClientRates)
        val addWeightAirClientRates = findViewById<TextInputEditText>(R.id.addWeightAirClientRates)
        selectCountryClientRatesAir.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            selectCityClientRatesAir.setText("", false)
            cargoTypeAirClientRates.setText("", false)
            addWeightAirClientRates.setText("")
            loadCities("Air", country)
        }
        selectCityClientRatesAir.setOnItemClickListener { parent, _, position, _ ->
            val city = parent.getItemAtPosition(position).toString()
            val country =selectCountryClientRatesAir.text.toString()
            cargoTypeAirClientRates.setText("", false)
            addWeightAirClientRates.setText("")
            loadCargoTypesAir(country, city)
        }
        val selectCountryClientRatesSea = findViewById<AutoCompleteTextView>(R.id.selectCountryClientRatesSea)
        val selectCityClientRatesSea = findViewById<AutoCompleteTextView>(R.id.selectCityClientRatesSea)
        val containerTypeSeaClientRates = findViewById<AutoCompleteTextView>(R.id.containerTypeSeaClientRates)
        val cargoTypeSeaClientRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaClientRates)
        val containerSizeSeaClientRates = findViewById<AutoCompleteTextView>(R.id.containerSizeSeaClientRates)
        val addWeightSeaLclClientRates = findViewById<TextInputEditText>(R.id.addWeightSeaLclClientRates)
        selectCountryClientRatesSea.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            selectCityClientRatesSea.setText("", false)
            containerTypeSeaClientRates.setText("", false)
            cargoTypeSeaClientRates.setText("", false)
            containerSizeSeaClientRates.setText("", false)
            addWeightSeaLclClientRates.setText("")
            loadCities("Sea", country)
        }
        selectCityClientRatesSea.setOnItemClickListener { parent, _, position, _ ->
            val city = parent.getItemAtPosition(position).toString()
            val country = selectCountryClientRatesSea.text.toString()
            containerTypeSeaClientRates.setText("", false)
            cargoTypeSeaClientRates.setText("", false)
            containerSizeSeaClientRates.setText("", false)
            addWeightSeaLclClientRates.setText("")
            loadContainerType(country, city)
        }
        containerTypeSeaClientRates.setOnItemClickListener { parent, _, position, _ ->
            val containerType = parent.getItemAtPosition(position).toString()
            val city = selectCityClientRatesSea.text.toString()
            val country = selectCountryClientRatesSea.text.toString()
            cargoTypeSeaClientRates.setText("", false)
            containerSizeSeaClientRates.setText("", false)
            addWeightSeaLclClientRates.setText("")
            loadCargoTypeSea(country, city, containerType)
            val containerSize = findViewById<TextInputLayout>(R.id.containerSizeSeaClientRatesLayout)
            val weightLcl = findViewById<TextInputLayout>(R.id.addWeightSeaLclClientRatesLayout)
            val containertype =containerTypeSeaClientRates.text.toString().trim()
            if(containertype == "FCL"){
                containerSize.visibility = View.VISIBLE
                weightLcl.visibility = View.GONE
            }
            else{
                weightLcl.visibility = View.VISIBLE
                containerSize.visibility = View.GONE
            }
        }
        cargoTypeSeaClientRates.setOnItemClickListener { parent, _, position, _ ->
            val cargoType = parent.getItemAtPosition(position).toString()
            val containerType = containerTypeSeaClientRates.text.toString()
            val city = selectCityClientRatesSea.text.toString()
            val country = selectCountryClientRatesSea.text.toString()
            val containertype =containerTypeSeaClientRates.text.toString().trim()
            if(containertype == "FCL"){
                containerSizeSeaClientRates.setText("", false)
                loadContainerSize(country, city, containerType, cargoType)
            }
            else{
                addWeightSeaLclClientRates.setText("")
            }
        }
        val btnClientRatesQuery = findViewById<Button>(R.id.btnClientRatesQuery)
        btnClientRatesQuery.setOnClickListener {
            if(airFormClientRates.visibility == View.VISIBLE){
                val selectType = selectTypeClientRatesAir.text.toString()
                val country = selectCountryClientRatesAir.text.toString()
                val city = selectCityClientRatesAir.text.toString()
                val cargoType = cargoTypeAirClientRates.text.toString()
                val weightStr = addWeightAirClientRates.text.toString()
                val serviceTerm = serviceTermAirClientRates.text.toString()
                if(selectType.isEmpty() || country.isEmpty() || city.isEmpty() || cargoType.isEmpty() || weightStr.isEmpty() || serviceTerm.isEmpty()){
                    Toast.makeText(this, "Please fill all above fields.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val weight = weightStr.toDouble()
                val slab = getWeightSlab(weight)
                fetchAirRates(selectType, country, city, cargoType, weightStr, slab, serviceTerm)
            }
            else{
                val selectType = selectTypeClientRatesSea.text.toString()
                val country = selectCountryClientRatesSea.text.toString()
                val city = selectCityClientRatesSea.text.toString()
                val containerType = containerTypeSeaClientRates.text.toString().trim()
                val cargoType = cargoTypeSeaClientRates.text.toString()
                val serviceTerm = serviceTermSeaClientRates.text.toString()
                val containerSize = containerSizeSeaClientRates.text.toString()
                val weightLcl = addWeightSeaLclClientRates.text.toString()
                if(containerType == "FCL"){
                    if(selectType.isEmpty() || country.isEmpty() || city.isEmpty() || containerType.isEmpty() || cargoType.isEmpty() ||
                        serviceTerm.isEmpty() || containerSize.isEmpty()){
                        Toast.makeText(this, "Please fill all above fields.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    fetchSeaFCLRates(selectType, country, city, containerType, cargoType, containerSize, serviceTerm)
                }
                else{
                    if(selectType.isEmpty() || country.isEmpty() || city.isEmpty() || containerType.isEmpty() || cargoType.isEmpty() ||
                        serviceTerm.isEmpty() || weightLcl.isEmpty()){
                        Toast.makeText(this, "Please fill all above fields.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val weight = weightLcl.toDouble()
                    val slab = getSeaWeightSlab(weight)
                    fetchSeaLCLRates(selectType, country, city, containerType, cargoType, weightLcl, slab, serviceTerm)
                }
            }
        }
    }
    private fun loadCountries(type: String){
        val countryAir = findViewById<AutoCompleteTextView>(R.id.selectCountryClientRatesAir)
        val countrySea = findViewById<AutoCompleteTextView>(R.id.selectCountryClientRatesSea)
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
        val cityAir = findViewById<AutoCompleteTextView>(R.id.selectCityClientRatesAir)
        val citySea = findViewById<AutoCompleteTextView>(R.id.selectCityClientRatesSea)
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
    private fun loadCargoTypesAir(country: String, city: String) {
        val cargoTypes = findViewById<AutoCompleteTextView>(R.id.cargoTypeAirClientRates)
        db.collection("Rates").document("Air")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("CargoTypes").get().addOnSuccessListener {
                val list = ArrayList<String>()
                for(doc in it) {
                    list.add(doc.id)
                }
                cargoTypes.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching Cargo Types from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadContainerType(country: String, city: String) {
        val containerType = findViewById<AutoCompleteTextView>(R.id.containerTypeSeaClientRates)
        db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").get().addOnSuccessListener {
                val list = ArrayList<String>()
                for(doc in it) {
                    list.add(doc.id)
                }
                containerType.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching the Container Types from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadCargoTypeSea(country: String, city: String, containerType: String) {
        val cargoType = findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaClientRates)
        db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").document(containerType)
            .collection("CargoTypes").get().addOnSuccessListener {
                val list = ArrayList<String>()
                for(doc in it) {
                    list.add(doc.id)
                }
                cargoType.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching Cargo Type from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadContainerSize(country: String, city: String, containerType: String, cargoType: String) {
        val containerSize = findViewById<AutoCompleteTextView>(R.id.containerSizeSeaClientRates)
        db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").document(containerType)
            .collection("CargoTypes").document(cargoType)
            .collection("ContainerSize").get().addOnSuccessListener {
                val list = ArrayList<String>()
                for(doc in it) {
                    list.add(doc.id)
                }
                containerSize.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching Container Size from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun getWeightSlab(weight: Double): String {
        return when {
            weight < 45 -> "< 45 Kg"
            weight < 100 -> ">= 45 & < 100 kg"
            weight < 300 -> ">=100 & <300 Kg"
            weight < 500 -> ">=300 & <500 Kg"
            weight < 1000 -> ">=500 & <1000 Kg"
            else -> ">=1000 Kg"
        }
    }
    private fun fetchAirRates(selectType: String, country: String, city: String, cargoType: String, weight: String, slab: String, serviceTerm: String) {
        val list = ArrayList<AirRatesModel>()
        db.collection("Rates").document("Air")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("CargoTypes").document(cargoType)
            .collection("Airlines").get().addOnSuccessListener { airlines ->

                for (airlineDoc in airlines) {
                    val airlineName = airlineDoc.id
                    db.collection("Rates").document("Air")
                        .collection("Countries").document(country)
                        .collection("Cities").document(city)
                        .collection("CargoTypes").document(cargoType)
                        .collection("Airlines").document(airlineName)
                        .collection("Rates").document(slab).get().addOnSuccessListener { doc ->
                            if (doc.exists()) {
                                val freight = doc.getString("airFreight") ?: ""
                                val exWork = doc.getString("exWork") ?: ""
                                val doCharges = doc.getString("doCharges") ?: ""
                                val transit = doc.getString("transitTime") ?: ""
                                list.add(AirRatesModel(airlineName, freight, exWork, doCharges, transit))
                                if (list.size == airlines.size()) {
                                    // 👉 move to next activity
                                    val intent = Intent(this, Client_Select_AirRates::class.java)
                                    intent.putExtra("transportType", "Air")
                                    intent.putExtra("selectType", selectType)
                                    intent.putExtra("country", country)
                                    intent.putExtra("city", city)
                                    intent.putExtra("cargoType", cargoType)
                                    intent.putExtra("weight", weight)
                                    intent.putExtra("serviceTerm", serviceTerm)
                                    intent.putParcelableArrayListExtra("ratesList", list)
                                    startActivity(intent)
                                    clearAirFields()
                                }
                            }
                        }
                }
            }
    }
    private fun getSeaWeightSlab(weight: Double): String {
        return when {
            weight <= 1 -> "1 CBM"
            weight <= 2 -> "2 CBM"
            else -> "2 CBM"
        }
    }
    private fun fetchSeaFCLRates(selectType: String, country: String, city: String, containerType: String, cargoType: String,
                                 containerSize: String, serviceTerm: String) {
        val list = ArrayList<AirRatesModel>()
        db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").document(containerType)
            .collection("CargoTypes").document(cargoType)
            .collection("ContainerSize").document(containerSize).get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val freight = doc.getString("seaFreight") ?: ""
                    val exWork = doc.getString("exWork") ?: ""
                    val doCharges = doc.getString("endorsement") ?: ""
                    val transit = doc.getString("transitTime") ?: ""

                    list.add(AirRatesModel(containerSize, freight, exWork, doCharges, transit))
                    moveToSeaResultActivity(list, selectType, country, city, containerType, cargoType, containerSize, serviceTerm)
                }
            }
    }
    private fun fetchSeaLCLRates(selectType: String, country: String, city: String, containerType: String, cargoType: String, weight: String,
                                 slab: String, serviceTerm: String) {
        val list = ArrayList<AirRatesModel>()
        db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").document(containerType)
            .collection("CargoTypes").document(cargoType)
            .collection("Weights").document(slab).get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val freight = doc.getString("seaFreight") ?: ""
                    val exWork = doc.getString("exWork") ?: ""
                    val doCharges = doc.getString("endorsement") ?: ""
                    val transit = doc.getString("transitTime") ?: ""

                    list.add(AirRatesModel(slab, freight, exWork, doCharges, transit))
                    moveToSeaResultActivity(list, selectType, country, city, containerType, cargoType, weight, serviceTerm)
                }
            }
    }
    private fun moveToSeaResultActivity(list: ArrayList<AirRatesModel>, selectType: String, country: String, city: String, containerType: String,
                                        cargoType: String, weight: String, serviceTerm: String) {
        val intent = Intent(this, Client_Select_AirRates::class.java)
        intent.putExtra("transportType", "Sea")
        intent.putExtra("selectType", selectType)
        intent.putExtra("country", country)
        intent.putExtra("city", city)
        intent.putExtra("containerType", containerType)
        intent.putExtra("cargoType", cargoType)
        intent.putExtra("weight", weight)
        intent.putExtra("serviceTerm", serviceTerm)
        intent.putParcelableArrayListExtra("ratesList", list)
        startActivity(intent)
        clearSeaFields()
    }
    private fun clearAirFields() {
        findViewById<AutoCompleteTextView>(R.id.selectTypeClientRatesAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCountryClientRatesAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityClientRatesAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeAirClientRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.serviceTermAirClientRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityClientRatesAir).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeAirClientRates).setAdapter(null)
        findViewById<TextInputEditText>(R.id.addWeightAirClientRates).setText("")
    }
    private fun clearSeaFields() {
        findViewById<AutoCompleteTextView>(R.id.selectTypeClientRatesSea).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.selectCountryClientRatesSea).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.selectCityClientRatesSea).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.containerTypeSeaClientRates).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaClientRates).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.containerSizeSeaClientRates).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.serviceTermSeaClientRates).setText("", false)
        findViewById<TextInputEditText>(R.id.addWeightSeaLclClientRates).setText("")
        findViewById<AutoCompleteTextView>(R.id.selectCityClientRatesSea).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.containerTypeSeaClientRates).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaClientRates).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.containerSizeSeaClientRates).setAdapter(null)
    }
}