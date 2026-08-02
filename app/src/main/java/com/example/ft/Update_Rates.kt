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
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class Update_Rates : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_rates)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val toggleButtonUpdateRates = findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.toggleGroupTransportUpdateRates)
        val btnAirFormUpdateRates = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnAirUpdateRates)
        val btnSeaFormUpdateRates = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSeaUpdateRates)
        val airFormUpdateRates = findViewById<LinearLayout>(R.id.llUpdateRatesAir)
        val seaFormUpdateRates = findViewById<LinearLayout>(R.id.llUpdateRatesSea)
        toggleButtonUpdateRates.check(R.id.btnAirUpdateRates)
        airFormUpdateRates.visibility = View.VISIBLE
        seaFormUpdateRates.visibility = View.GONE
        btnAirFormUpdateRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnAirFormUpdateRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnSeaFormUpdateRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnSeaFormUpdateRates.setTextColor(ContextCompat.getColor(this, R.color.white))

        toggleButtonUpdateRates.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked){
                return@addOnButtonCheckedListener
            }
            when(checkedId){
                R.id.btnAirUpdateRates -> {
                    airFormUpdateRates.visibility = View.VISIBLE
                    seaFormUpdateRates.visibility = View.GONE
                    btnAirFormUpdateRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnAirFormUpdateRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnSeaFormUpdateRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnSeaFormUpdateRates.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
                R.id.btnSeaUpdateRates -> {
                    airFormUpdateRates.visibility = View.GONE
                    seaFormUpdateRates.visibility = View.VISIBLE
                    btnSeaFormUpdateRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnSeaFormUpdateRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnAirFormUpdateRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnAirFormUpdateRates.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        }
        val btnUpdateRates = findViewById<Button>(R.id.btnUpdateRates)
        btnUpdateRates.isEnabled = false
        btnUpdateRates.alpha = 0.5f

        db = FirebaseFirestore.getInstance()
        loadCountries("Air")
        loadCountries("Sea")
        val selectCountryUpdateRatesAir = findViewById<AutoCompleteTextView>(R.id.selectCountryUpdateRatesAir)
        val selectCityUpdateRatesAir = findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesAir)
        val cargoTypeAirUpdateRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeAirUpdateRates)
        val selectAirlineNameUpdateRates = findViewById<AutoCompleteTextView>(R.id.selectAirLineNameUpdateRates)
        val selectWeightAirUpdateRates = findViewById<AutoCompleteTextView>(R.id.selectWeightAirUpdateRates)
        val addAirFreightUpdateRates = findViewById<TextInputEditText>(R.id.addAirFreightUpdateRates)
        val addExWorkChargesUpdateRatesAir = findViewById<TextInputEditText>(R.id.addExWorkChargesUpdateRatesAir)
        val addAirDoChargesUpdateRatesAir = findViewById<TextInputEditText>(R.id.addAirDoChargesUpdateRates)
        val addAirTransitTimeUpdateRates = findViewById<TextInputEditText>(R.id.addAirTransitTimeUpdateRates)
        selectCountryUpdateRatesAir.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            selectCityUpdateRatesAir.setText("", false)
            cargoTypeAirUpdateRates.setText("", false)
            selectAirlineNameUpdateRates.setText("", false)
            selectWeightAirUpdateRates.setText("", false)
            addAirFreightUpdateRates.setText("")
            addExWorkChargesUpdateRatesAir.setText("")
            addAirDoChargesUpdateRatesAir.setText("")
            addAirTransitTimeUpdateRates.setText("")
            loadCities("Air", country)
        }
        val selectCountryUpdateRatesSea = findViewById<AutoCompleteTextView>(R.id.selectCountryUpdateRatesSea)
        val containerTypeSeaUpdateRates = findViewById<AutoCompleteTextView>(R.id.containerTypeSeaUpdateRates)
        val selectCityUpdateRatesSea = findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesSea)
        val cargoTypeSeaUpdateRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaUpdateRates)
        val containerSizeSeaUpdateRates = findViewById<AutoCompleteTextView>(R.id.containerSizeSeaUpdateRates)
        val weightSeaLClUpdateRates = findViewById<AutoCompleteTextView>(R.id.weightSeaLclUpdateRates)
        val addSeaFreightUpdateRatesFcl = findViewById<TextInputEditText>(R.id.addSeaFreightUpdateRatesFcl)
        val addExWorkChargesSeaFclUpdateRates = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaFclUpdateRates)
        val addEndorsementChargesSeaFclUpdateRates = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaFclUpdateRates)
        val addSeaTransitTimeFclUpdateRates = findViewById<TextInputEditText>(R.id.addSeaTransitTimeFclUpdateRates)
        val addSeaFreightLclUpdateRates = findViewById<TextInputEditText>(R.id.addSeaFreightLclUpdateRates)
        val addExWorkChargesSeaLclUpdateRates = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaLclUpdateRates)
        val addEndorsementChargesSeaLclUpdateRates = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaLclUpdateRates)
        val addSeaTransitTimeLclUpdateRates = findViewById<TextInputEditText>(R.id.addSeaTransitTimeLclUpdateRates)
        selectCountryUpdateRatesSea.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            selectCityUpdateRatesSea.setText("", false)
            containerTypeSeaUpdateRates.setText("", false)
            cargoTypeSeaUpdateRates.setText("", false)
            containerSizeSeaUpdateRates.setText("", false)
            weightSeaLClUpdateRates.setText("", false)
            addSeaFreightUpdateRatesFcl.setText("")
            addExWorkChargesSeaFclUpdateRates.setText("")
            addEndorsementChargesSeaFclUpdateRates.setText("")
            addSeaTransitTimeFclUpdateRates.setText("")
            addSeaFreightLclUpdateRates.setText("")
            addExWorkChargesSeaLclUpdateRates.setText("")
            addEndorsementChargesSeaLclUpdateRates.setText("")
            addSeaTransitTimeLclUpdateRates.setText("")
            loadCities("Sea", country)
        }
        selectCityUpdateRatesAir.setOnItemClickListener { parent, _, position, _ ->
            val city = parent.getItemAtPosition(position).toString()
            val country = selectCountryUpdateRatesAir.text.toString()
            cargoTypeAirUpdateRates.setText("", false)
            selectAirlineNameUpdateRates.setText("", false)
            selectWeightAirUpdateRates.setText("", false)
            addAirFreightUpdateRates.setText("")
            addExWorkChargesUpdateRatesAir.setText("")
            addAirDoChargesUpdateRatesAir.setText("")
            addAirTransitTimeUpdateRates.setText("")
            loadCargoTypesAir(country, city)
        }
        cargoTypeAirUpdateRates.setOnItemClickListener { parent, _, position, _ ->
            val cargoType = parent.getItemAtPosition(position).toString()
            val city = selectCityUpdateRatesAir.text.toString()
            val country = selectCountryUpdateRatesAir.text.toString()
            selectAirlineNameUpdateRates.setText("", false)
            selectWeightAirUpdateRates.setText("", false)
            addAirFreightUpdateRates.setText("")
            addExWorkChargesUpdateRatesAir.setText("")
            addAirDoChargesUpdateRatesAir.setText("")
            addAirTransitTimeUpdateRates.setText("")
            loadAirlines(country, city, cargoType)
        }
        selectAirlineNameUpdateRates.setOnItemClickListener { parent, _, position, _ ->
            val airline = parent.getItemAtPosition(position).toString()
            val cargoType = cargoTypeAirUpdateRates.text.toString()
            val city = selectCityUpdateRatesAir.text.toString()
            val country = selectCountryUpdateRatesAir.text.toString()
            selectWeightAirUpdateRates.setText("", false)
            addAirFreightUpdateRates.setText("")
            addExWorkChargesUpdateRatesAir.setText("")
            addAirDoChargesUpdateRatesAir.setText("")
            addAirTransitTimeUpdateRates.setText("")
            loadWeightsAir(country, city, cargoType, airline)
        }
        selectWeightAirUpdateRates.setOnItemClickListener { parent, _, position, _ ->
            val weight = parent.getItemAtPosition(position).toString()
            val airline = selectAirlineNameUpdateRates.text.toString()
            val cargoType = cargoTypeAirUpdateRates.text.toString()
            val city = selectCityUpdateRatesAir.text.toString()
            val country = selectCountryUpdateRatesAir.text.toString()
            loadAirRates(country, city, cargoType, airline, weight)
        }
        addAirFreightUpdateRates.addTextChangedListener {
            enableButton()
        }
        addExWorkChargesUpdateRatesAir.addTextChangedListener {
            enableButton()
        }
        addAirDoChargesUpdateRatesAir.addTextChangedListener {
            enableButton()
        }
        addAirTransitTimeUpdateRates.addTextChangedListener {
            enableButton()
        }
        selectCityUpdateRatesSea.setOnItemClickListener { parent, _, position, _ ->
            val city = parent.getItemAtPosition(position).toString()
            val country = selectCountryUpdateRatesSea.text.toString()
            containerTypeSeaUpdateRates.setText("", false)
            cargoTypeSeaUpdateRates.setText("", false)
            containerSizeSeaUpdateRates.setText("", false)
            weightSeaLClUpdateRates.setText("", false)
            addSeaFreightUpdateRatesFcl.setText("")
            addExWorkChargesSeaFclUpdateRates.setText("")
            addEndorsementChargesSeaFclUpdateRates.setText("")
            addSeaTransitTimeFclUpdateRates.setText("")
            addSeaFreightLclUpdateRates.setText("")
            addExWorkChargesSeaLclUpdateRates.setText("")
            addEndorsementChargesSeaLclUpdateRates.setText("")
            addSeaTransitTimeLclUpdateRates.setText("")
            loadContainerType(country, city)
        }
        containerTypeSeaUpdateRates.setOnItemClickListener { parent, _, position, _ ->
            val containerType = parent.getItemAtPosition(position).toString()
            val city = selectCityUpdateRatesSea.text.toString()
            val country = selectCountryUpdateRatesSea.text.toString()
            cargoTypeSeaUpdateRates.setText("", false)
            containerSizeSeaUpdateRates.setText("", false)
            weightSeaLClUpdateRates.setText("", false)
            addSeaFreightUpdateRatesFcl.setText("")
            addExWorkChargesSeaFclUpdateRates.setText("")
            addEndorsementChargesSeaFclUpdateRates.setText("")
            addSeaTransitTimeFclUpdateRates.setText("")
            addSeaFreightLclUpdateRates.setText("")
            addExWorkChargesSeaLclUpdateRates.setText("")
            addEndorsementChargesSeaLclUpdateRates.setText("")
            addSeaTransitTimeLclUpdateRates.setText("")
            loadCargoTypeSea(country, city, containerType)
            val layoutFcl = findViewById<LinearLayout>(R.id.llUpdateRatesFclSea)
            val layoutLcl = findViewById<LinearLayout>(R.id.llUpdateRatesLclSea)
            val containertype = containerTypeSeaUpdateRates.text.toString().trim()
            if(containertype == "FCL"){
                layoutFcl.visibility = View.VISIBLE
                layoutLcl.visibility = View.GONE
            }
            else{
                layoutLcl.visibility = View.VISIBLE
                layoutFcl.visibility = View.GONE
            }
        }
        cargoTypeSeaUpdateRates.setOnItemClickListener { parent, _, position, _ ->
            val cargoType = parent.getItemAtPosition(position).toString()
            val containerType = containerTypeSeaUpdateRates.text.toString()
            val city = selectCityUpdateRatesSea.text.toString()
            val country = selectCountryUpdateRatesSea.text.toString()
            val containertype = containerTypeSeaUpdateRates.text.toString().trim()
            if(containertype == "FCL"){
                containerSizeSeaUpdateRates.setText("", false)
                addSeaFreightUpdateRatesFcl.setText("")
                addExWorkChargesSeaFclUpdateRates.setText("")
                addEndorsementChargesSeaFclUpdateRates.setText("")
                addSeaTransitTimeFclUpdateRates.setText("")
                loadContainerSize(country, city, containerType, cargoType)
            }
            else{
                weightSeaLClUpdateRates.setText("", false)
                addSeaFreightLclUpdateRates.setText("")
                addExWorkChargesSeaLclUpdateRates.setText("")
                addEndorsementChargesSeaLclUpdateRates.setText("")
                addSeaTransitTimeLclUpdateRates.setText("")
                loadWeightSea(country, city, containerType, cargoType)
            }
        }
        containerSizeSeaUpdateRates.setOnItemClickListener { parent, _, position, _ ->
            val containerSize = parent.getItemAtPosition(position).toString()
            val cargoType = cargoTypeSeaUpdateRates.text.toString()
            val containerType = containerTypeSeaUpdateRates.text.toString()
            val city = selectCityUpdateRatesSea.text.toString()
            val country = selectCountryUpdateRatesSea.text.toString()
            loadSeaRatesFcl(country, city, containerType, cargoType, containerSize)
        }
        weightSeaLClUpdateRates.setOnItemClickListener { parent, _, position, _ ->
            val weightSea = parent.getItemAtPosition(position).toString()
            val cargoType = cargoTypeSeaUpdateRates.text.toString()
            val containerType = containerTypeSeaUpdateRates.text.toString()
            val city = selectCityUpdateRatesSea.text.toString()
            val country = selectCountryUpdateRatesSea.text.toString()
            loadSeaRatesLcl(country, city, containerType, cargoType, weightSea)
        }
        addSeaFreightUpdateRatesFcl.addTextChangedListener {
            enableButton()
        }
        addExWorkChargesSeaFclUpdateRates.addTextChangedListener {
            enableButton()
        }
        addEndorsementChargesSeaFclUpdateRates.addTextChangedListener {
            enableButton()
        }
        addSeaTransitTimeFclUpdateRates.addTextChangedListener {
            enableButton()
        }
        addSeaFreightLclUpdateRates.addTextChangedListener {
            enableButton()
        }
        addExWorkChargesSeaLclUpdateRates.addTextChangedListener {
            enableButton()
        }
        addEndorsementChargesSeaLclUpdateRates.addTextChangedListener {
            enableButton()
        }
        addSeaTransitTimeLclUpdateRates.addTextChangedListener {
            enableButton()
        }
        btnUpdateRates.setOnClickListener {
            if(airFormUpdateRates.visibility == View.VISIBLE){
                updateAirRates()
            }
            else{
                updateSeaRates()
            }
        }
    }
    fun enableButton() {
        val btnUpdateRates = findViewById<Button>(R.id.btnUpdateRates)
        btnUpdateRates.isEnabled = true
        btnUpdateRates.alpha = 1f
    }
    private fun loadCountries(type: String){
        val countryAir = findViewById<AutoCompleteTextView>(R.id.selectCountryUpdateRatesAir)
        val countrySea = findViewById<AutoCompleteTextView>(R.id.selectCountryUpdateRatesSea)
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
        val cityAir = findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesAir)
        val citySea = findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesSea)
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
        val cargoTypes = findViewById<AutoCompleteTextView>(R.id.cargoTypeAirUpdateRates)
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
    private fun loadAirlines(country: String, city: String, cargo: String) {
        val airlineName = findViewById<AutoCompleteTextView>(R.id.selectAirLineNameUpdateRates)
        db.collection("Rates").document("Air")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("CargoTypes").document(cargo)
            .collection("Airlines").get().addOnSuccessListener {
                val list = ArrayList<String>()
                for(doc in it) {
                    list.add(doc.id)
                }
                airlineName.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching Airline Names from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadWeightsAir(country: String, city: String, cargo: String, airline: String) {
        val weightAir = findViewById<AutoCompleteTextView>(R.id.selectWeightAirUpdateRates)
        db.collection("Rates").document("Air")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("CargoTypes").document(cargo)
            .collection("Airlines").document(airline)
            .collection("Rates").get().addOnSuccessListener {
                val list = ArrayList<String>()
                for(doc in it) {
                    list.add(doc.id)
                }
                weightAir.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching Weight Slap from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadAirRates(country: String, city: String, cargo: String, airline: String, weight: String) {
        db.collection("Rates").document("Air")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("CargoTypes").document(cargo)
            .collection("Airlines").document(airline)
            .collection("Rates").document(weight).get().addOnSuccessListener { doc ->
                val freight = findViewById<TextInputEditText>(R.id.addAirFreightUpdateRates)
                freight.setText(doc.getString("airFreight"))
                val exWork = findViewById<TextInputEditText>(R.id.addExWorkChargesUpdateRatesAir)
                exWork.setText(doc.getString("exWork"))
                val doCharges = findViewById<TextInputEditText>(R.id.addAirDoChargesUpdateRates)
                doCharges.setText(doc.getString("doCharges"))
                val transitTime = findViewById<TextInputEditText>(R.id.addAirTransitTimeUpdateRates)
                transitTime.setText(doc.getString("transitTime"))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching the Rates from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadContainerType(country: String, city: String) {
        val containerType = findViewById<AutoCompleteTextView>(R.id.containerTypeSeaUpdateRates)
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
        val cargoType = findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaUpdateRates)
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
        val containerSize = findViewById<AutoCompleteTextView>(R.id.containerSizeSeaUpdateRates)
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
    private fun loadWeightSea(country: String, city: String, containerType: String, cargoType: String) {
        val weightSea = findViewById<AutoCompleteTextView>(R.id.weightSeaLclUpdateRates)
        db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").document(containerType)
            .collection("CargoTypes").document(cargoType)
            .collection("Weights").get().addOnSuccessListener {
                val list = ArrayList<String>()
                for(doc in it) {
                    list.add(doc.id)
                }
                weightSea.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching Weight Slap from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadSeaRatesFcl(country: String, city: String, containerType: String, cargoType: String, containerSize: String) {
        db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").document(containerType)
            .collection("CargoTypes").document(cargoType)
            .collection("ContainerSize").document(containerSize).get().addOnSuccessListener { doc ->
                val freight = findViewById<TextInputEditText>(R.id.addSeaFreightUpdateRatesFcl)
                freight.setText(doc.getString("seaFreight"))
                val exWork = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaFclUpdateRates)
                exWork.setText(doc.getString("exWork"))
                val endorsement = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaFclUpdateRates)
                endorsement.setText(doc.getString("endorsement"))
                val transitTime = findViewById<TextInputEditText>(R.id.addSeaTransitTimeFclUpdateRates)
                transitTime.setText(doc.getString("transitTime"))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching the Rates from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadSeaRatesLcl(country: String, city: String, containerType: String, cargoType: String, weightSea: String) {
        db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").document(containerType)
            .collection("CargoTypes").document(cargoType)
            .collection("Weights").document(weightSea).get().addOnSuccessListener { doc ->
                val freight = findViewById<TextInputEditText>(R.id.addSeaFreightLclUpdateRates)
                freight.setText(doc.getString("seaFreight"))
                val exWork = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaLclUpdateRates)
                exWork.setText(doc.getString("exWork"))
                val endorsement = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaLclUpdateRates)
                endorsement.setText(doc.getString("endorsement"))
                val transitTime = findViewById<TextInputEditText>(R.id.addSeaTransitTimeLclUpdateRates)
                transitTime.setText(doc.getString("transitTime"))
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching the Rates from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun updateAirRates(){
        val freight = findViewById<TextInputEditText>(R.id.addAirFreightUpdateRates)
        val exWork = findViewById<TextInputEditText>(R.id.addExWorkChargesUpdateRatesAir)
        val doCharges = findViewById<TextInputEditText>(R.id.addAirDoChargesUpdateRates)
        val transit = findViewById<TextInputEditText>(R.id.addAirTransitTimeUpdateRates)
        val selectAirlineNameUpdateRates = findViewById<AutoCompleteTextView>(R.id.selectAirLineNameUpdateRates)
        val selectCityUpdateRatesAir = findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesAir)
        val cargoTypeAirUpdateRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeAirUpdateRates)
        val selectWeightAirUpdateRates = findViewById<AutoCompleteTextView>(R.id.selectWeightAirUpdateRates)
        val selectCountryUpdateRatesAir = findViewById<AutoCompleteTextView>(R.id.selectCountryUpdateRatesAir)
        if(freight.text.isNullOrEmpty() || exWork.text.isNullOrEmpty() || doCharges.text.isNullOrEmpty() || transit.text.isNullOrEmpty()){
            Toast.makeText(this, "Please fill all above fields", Toast.LENGTH_SHORT).show()
            return
        }
        val data = hashMapOf(
            "airFreight" to freight.text.toString().trim(),
            "exWork" to exWork.text.toString().trim(),
            "doCharges" to doCharges.text.toString().trim(),
            "transitTime" to transit.text.toString().trim(),
            "lastUpdate" to FieldValue.serverTimestamp()
        )
        val airline = selectAirlineNameUpdateRates.text.toString()
        val cargoType = cargoTypeAirUpdateRates.text.toString()
        val city = selectCityUpdateRatesAir.text.toString()
        val country = selectCountryUpdateRatesAir.text.toString()
        val weight = selectWeightAirUpdateRates.text.toString()
        db.collection("Rates").document("Air")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("CargoTypes").document(cargoType)
            .collection("Airlines").document(airline)
            .collection("Rates").document(weight).update(data as Map<String, Any>).addOnSuccessListener {
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                clearAirFields()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Update Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun updateSeaRates(){
        val freightFcl = findViewById<TextInputEditText>(R.id.addSeaFreightUpdateRatesFcl)
        val exWorkFcl = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaFclUpdateRates)
        val endorsementFcl = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaFclUpdateRates)
        val transitTimeFcl = findViewById<TextInputEditText>(R.id.addSeaTransitTimeFclUpdateRates)
        val freightLcl = findViewById<TextInputEditText>(R.id.addSeaFreightLclUpdateRates)
        val exWorkLcl = findViewById<TextInputEditText>(R.id.addExWorkChargesSeaLclUpdateRates)
        val endorsementLcl = findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaLclUpdateRates)
        val transitTimeLcl = findViewById<TextInputEditText>(R.id.addSeaTransitTimeLclUpdateRates)
        val selectCountryUpdateRatesSea = findViewById<AutoCompleteTextView>(R.id.selectCountryUpdateRatesSea)
        val selectCityUpdateRatesSea = findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesSea)
        val containerTypeSeaUpdateRates = findViewById<AutoCompleteTextView>(R.id.containerTypeSeaUpdateRates)
        val containerType = containerTypeSeaUpdateRates.text.toString().trim()
        val cargoTypeSeaUpdateRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaUpdateRates)
        val containerSizeSeaUpdateRates = findViewById<AutoCompleteTextView>(R.id.containerSizeSeaUpdateRates)
        val weightSeaLClUpdateRates = findViewById<AutoCompleteTextView>(R.id.weightSeaLclUpdateRates)
        if(containerType == "FCL"){
            if(freightFcl.text.isNullOrEmpty() || exWorkFcl.text.isNullOrEmpty() || endorsementFcl.text.isNullOrEmpty() ||
                transitTimeFcl.text.isNullOrEmpty()){
                Toast.makeText(this, "Please fill all above fields", Toast.LENGTH_SHORT).show()
                return
            }
            val data = hashMapOf(
                "seaFreight" to freightFcl.text.toString().trim(),
                "exWork" to exWorkFcl.text.toString().trim(),
                "endorsement" to endorsementFcl.text.toString().trim(),
                "transitTime" to transitTimeFcl.text.toString().trim(),
                "lastUpdate" to FieldValue.serverTimestamp()
            )
            val country = selectCountryUpdateRatesSea.text.toString()
            val city = selectCityUpdateRatesSea.text.toString()
            val cargoType = cargoTypeSeaUpdateRates.text.toString()
            val containerSize = containerSizeSeaUpdateRates.text.toString()
            db.collection("Rates").document("Sea")
                .collection("Countries").document(country)
                .collection("Cities").document(city)
                .collection("ContainerType").document(containerType)
                .collection("CargoTypes").document(cargoType)
                .collection("ContainerSize").document(containerSize).update(data as Map<String, Any>).addOnSuccessListener {
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    clearSeaFields()
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Error Update Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        else{
            if(freightLcl.text.isNullOrEmpty() || exWorkLcl.text.isNullOrEmpty() || endorsementLcl.text.isNullOrEmpty() ||
                transitTimeLcl.text.isNullOrEmpty()){
                Toast.makeText(this, "Please fill all above fields", Toast.LENGTH_SHORT).show()
                return
            }
            val data = hashMapOf(
                "seaFreight" to freightLcl.text.toString().trim(),
                "exWork" to exWorkLcl.text.toString().trim(),
                "endorsement" to endorsementLcl.text.toString().trim(),
                "transitTime" to transitTimeLcl.text.toString().trim(),
                "lastUpdate" to FieldValue.serverTimestamp()
            )
            val country = selectCountryUpdateRatesSea.text.toString()
            val city = selectCityUpdateRatesSea.text.toString()
            val cargoType = cargoTypeSeaUpdateRates.text.toString()
            val weight = weightSeaLClUpdateRates.text.toString()
            db.collection("Rates").document("Sea")
                .collection("Countries").document(country)
                .collection("Cities").document(city)
                .collection("ContainerType").document(containerType)
                .collection("CargoTypes").document(cargoType)
                .collection("Weights").document(weight).update(data as Map<String, Any>).addOnSuccessListener {
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    clearSeaFields()
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Error Update Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun clearAirFields(){
        findViewById<AutoCompleteTextView>(R.id.selectCountryUpdateRatesAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeAirUpdateRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectAirLineNameUpdateRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectWeightAirUpdateRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesAir).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeAirUpdateRates).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.selectAirLineNameUpdateRates).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.selectWeightAirUpdateRates).setAdapter(null)
        findViewById<TextInputEditText>(R.id.addAirFreightUpdateRates).setText("")
        findViewById<TextInputEditText>(R.id.addExWorkChargesUpdateRatesAir).setText("")
        findViewById<TextInputEditText>(R.id.addAirDoChargesUpdateRates).setText("")
        findViewById<TextInputEditText>(R.id.addAirTransitTimeUpdateRates).setText("")
        val btnUpdateRates = findViewById<Button>(R.id.btnUpdateRates)
        btnUpdateRates.isEnabled = false
        btnUpdateRates.alpha = 0.5f
    }
    private fun clearSeaFields(){
        findViewById<AutoCompleteTextView>(R.id.selectCountryUpdateRatesSea).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesSea).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.containerTypeSeaUpdateRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaUpdateRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.containerSizeSeaUpdateRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.weightSeaLclUpdateRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityUpdateRatesSea).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.containerTypeSeaUpdateRates).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaUpdateRates).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.containerSizeSeaUpdateRates).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.weightSeaLclUpdateRates).setAdapter(null)
        findViewById<TextInputEditText>(R.id.addSeaFreightUpdateRatesFcl).setText("")
        findViewById<TextInputEditText>(R.id.addExWorkChargesSeaFclUpdateRates).setText("")
        findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaFclUpdateRates).setText("")
        findViewById<TextInputEditText>(R.id.addSeaTransitTimeFclUpdateRates).setText("")
        findViewById<TextInputEditText>(R.id.addSeaFreightLclUpdateRates).setText("")
        findViewById<TextInputEditText>(R.id.addExWorkChargesSeaLclUpdateRates).setText("")
        findViewById<TextInputEditText>(R.id.addEndorsementChargesSeaLclUpdateRates).setText("")
        findViewById<TextInputEditText>(R.id.addSeaTransitTimeLclUpdateRates).setText("")
        val btnUpdateRates = findViewById<Button>(R.id.btnUpdateRates)
        btnUpdateRates.isEnabled = false
        btnUpdateRates.alpha = 0.5f
    }
}