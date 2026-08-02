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
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class Remove_Rates : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remove_rates)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val toggleButtonRemoveRates =findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.toggleGroupTransportRemoveRates)
        val btnAirFormRemoveRates = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnAirRemoveRates)
        val btnSeaFormRemoveRates = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSeaRemoveRates)
        val airFormRemoveRates = findViewById<LinearLayout>(R.id.llRemoveRatesAir)
        val seaFormRemoveRates = findViewById<LinearLayout>(R.id.llRemoveRatesSea)
        toggleButtonRemoveRates.check(R.id.btnAirRemoveRates)
        airFormRemoveRates.visibility = View.VISIBLE
        seaFormRemoveRates.visibility = View.GONE
        btnAirFormRemoveRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnAirFormRemoveRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnSeaFormRemoveRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnSeaFormRemoveRates.setTextColor(ContextCompat.getColor(this, R.color.white))
        
        toggleButtonRemoveRates.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked){
                return@addOnButtonCheckedListener
            }
            when(checkedId){
                R.id.btnAirRemoveRates -> {
                    airFormRemoveRates.visibility = View.VISIBLE
                    seaFormRemoveRates.visibility = View.GONE
                    btnAirFormRemoveRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnAirFormRemoveRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnSeaFormRemoveRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnSeaFormRemoveRates.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
                R.id.btnSeaRemoveRates -> {
                    airFormRemoveRates.visibility = View.GONE
                    seaFormRemoveRates.visibility = View.VISIBLE
                    btnSeaFormRemoveRates.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnSeaFormRemoveRates.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnAirFormRemoveRates.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnAirFormRemoveRates.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        }
        db = FirebaseFirestore.getInstance()
        loadCountries("Air")
        loadCountries("Sea")
        val selectCountryRemoveRatesAir = findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveRatesAir)
        val selectCityRemoveRatesAir = findViewById<AutoCompleteTextView>(R.id.selectCityRemoveRatesAir)
        val cargoTypeAirRemoveRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeAirRemoveRates)
        val selectAirlineNameRemoveRates = findViewById<AutoCompleteTextView>(R.id.selectAirLineNameRemoveRates)
        selectCountryRemoveRatesAir.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            selectCityRemoveRatesAir.setText("", false)
            cargoTypeAirRemoveRates.setText("", false)
            selectAirlineNameRemoveRates.setText("", false)
            loadCities("Air", country)
        }
        val selectCountryRemoveRatesSea = findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveRatesSea)
        val containerTypeSeaRemoveRates = findViewById<AutoCompleteTextView>(R.id.containerTypeSeaRemoveRates)
        val selectCityRemoveRatesSea = findViewById<AutoCompleteTextView>(R.id.selectCityRemoveRatesSea)
        val cargoTypeSeaRemoveRates = findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaRemoveRates)
        selectCountryRemoveRatesSea.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            selectCityRemoveRatesSea.setText("", false)
            containerTypeSeaRemoveRates.setText("", false)
            cargoTypeSeaRemoveRates.setText("", false)
            loadCities("Sea", country)
        }
        selectCityRemoveRatesAir.setOnItemClickListener { parent, _, position, _ ->
            val city = parent.getItemAtPosition(position).toString()
            val country = selectCountryRemoveRatesAir.text.toString()
            cargoTypeAirRemoveRates.setText("", false)
            selectAirlineNameRemoveRates.setText("", false)
            loadCargoTypesAir(country, city)
        }
        cargoTypeAirRemoveRates.setOnItemClickListener { parent, _, position, _ ->
            val cargoType = parent.getItemAtPosition(position).toString()
            val city = selectCityRemoveRatesAir.text.toString()
            val country = selectCountryRemoveRatesAir.text.toString()
            selectAirlineNameRemoveRates.setText("", false)
            loadAirlines(country, city, cargoType)
        }
        selectCityRemoveRatesSea.setOnItemClickListener { parent, _, position, _ ->
            val city = parent.getItemAtPosition(position).toString()
            val country = selectCountryRemoveRatesSea.text.toString()
            containerTypeSeaRemoveRates.setText("", false)
            cargoTypeSeaRemoveRates.setText("", false)
            loadContainerType(country, city)
        }
        containerTypeSeaRemoveRates.setOnItemClickListener { parent, _, position, _ ->
            val containerType = parent.getItemAtPosition(position).toString()
            val city = selectCityRemoveRatesSea.text.toString()
            val country = selectCountryRemoveRatesSea.text.toString()
            cargoTypeSeaRemoveRates.setText("", false)
            loadCargoTypeSea(country, city, containerType)
        }
        val btnRemoveRates = findViewById<Button>(R.id.btnRemoveRates)

        btnRemoveRates.setOnClickListener {

            btnRemoveRates.isEnabled = false
            btnRemoveRates.alpha = 0.5f

            if (airFormRemoveRates.visibility == View.VISIBLE) {

                val country = selectCountryRemoveRatesAir.text.toString()
                val city = selectCityRemoveRatesAir.text.toString()
                val cargo = cargoTypeAirRemoveRates.text.toString()
                val airline = selectAirlineNameRemoveRates.text.toString()

                if (country.isEmpty() || city.isEmpty() || cargo.isEmpty() || airline.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    resetButton(btnRemoveRates)
                    return@setOnClickListener
                }

                deleteAirFlow(country, city, cargo, airline, btnRemoveRates)

            } else {

                val country = selectCountryRemoveRatesSea.text.toString()
                val city = selectCityRemoveRatesSea.text.toString()
                val container = containerTypeSeaRemoveRates.text.toString()
                val cargo = cargoTypeSeaRemoveRates.text.toString()

                if (country.isEmpty() || city.isEmpty() || container.isEmpty() || cargo.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    resetButton(btnRemoveRates)
                    return@setOnClickListener
                }

                deleteSeaFlow(country, city, container, cargo, btnRemoveRates)
            }
        }
    }
    private fun loadCountries(type: String){
        val countryAir = findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveRatesAir)
        val countrySea = findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveRatesSea)
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
        val cityAir = findViewById<AutoCompleteTextView>(R.id.selectCityRemoveRatesAir)
        val citySea = findViewById<AutoCompleteTextView>(R.id.selectCityRemoveRatesSea)
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
        val cargoTypes = findViewById<AutoCompleteTextView>(R.id.cargoTypeAirRemoveRates)
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
        val airlineName = findViewById<AutoCompleteTextView>(R.id.selectAirLineNameRemoveRates)
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
    private fun loadContainerType(country: String, city: String) {
        val containerType = findViewById<AutoCompleteTextView>(R.id.containerTypeSeaRemoveRates)
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
        val cargoType = findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaRemoveRates)
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
    private fun resetButton(btn: Button) {
        btn.isEnabled = true
        btn.alpha = 1f
    }
    private fun deleteAirFlow(country: String, city: String, cargo: String, airline: String, btn: Button) {

        val airlineRef = db.collection("Rates").document("Air")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("CargoTypes").document(cargo)
            .collection("Airlines").document(airline)

        // STEP 1: Delete Airline sub collections manually (Weights etc.)
        airlineRef.collection("Rates").get().addOnSuccessListener { ratesDocs ->
            val tasks = mutableListOf<Task<Void>>()

            for (doc in ratesDocs) {
                val task = airlineRef.collection("Rates").document(doc.id).delete()
                tasks.add(task)
            }

            Tasks.whenAll(tasks).addOnSuccessListener {

            // STEP 2: Delete Airline document
                airlineRef.delete().addOnSuccessListener {

                    val airlinesCol = airlineRef.parent!!

                    airlinesCol.get().addOnSuccessListener { remainingAirlines ->

                        if (!remainingAirlines.isEmpty) {
                            done("Airline Deleted Successfully", btn)
                            return@addOnSuccessListener
                        }

                        // STEP 3: delete Airlines collection parent (CargoTypes doc)
                        val cargoRef = airlinesCol.parent!!

                        cargoRef.delete().addOnSuccessListener {

                            val cargoCol = cargoRef.parent!!

                            cargoCol.get().addOnSuccessListener { remainingCargo ->

                                if (!remainingCargo.isEmpty) {
                                    done("Airline and Cargo Type Deleted Successfully", btn)
                                    return@addOnSuccessListener
                                }

                                val cityRef = cargoCol.parent!!
                                cityRef.delete().addOnSuccessListener {

                                    val cityCol = cityRef.parent!!
                                    cityCol.get().addOnSuccessListener {

                                        if (!it.isEmpty) {
                                            done("Airline, Cargo Type and City Deleted Successfully", btn)
                                            return@addOnSuccessListener
                                        }

                                        val countryRef = cityCol.parent!!
                                        countryRef.delete().addOnSuccessListener {

                                            val countryCol = countryRef.parent!!
                                            countryCol.get().addOnSuccessListener {

                                                if (!it.isEmpty) {
                                                    done("Airline, Cargo Type, City and Country Deleted Successfully", btn)
                                                    return@addOnSuccessListener
                                                }

                                                val airDoc = countryCol.parent!!
                                                airDoc.delete().addOnSuccessListener {

                                                    done("Air section fully deleted", btn)

                                                }.addOnFailureListener { e ->
                                                    error("Failed to delete air section", e, btn)
                                                }
                                            }.addOnFailureListener { e ->
                                                error("Failed to fetch countries", e, btn)
                                            }
                                        }.addOnFailureListener { e ->
                                            error("Failed to delete country", e, btn)
                                        }
                                    }.addOnFailureListener { e ->
                                        error("Failed to fetch cities", e, btn)
                                    }
                                }.addOnFailureListener { e ->
                                    error("Failed to delete city", e, btn)
                                }
                            }.addOnFailureListener { e ->
                                error("Failed to fetch cargo types", e, btn)
                            }
                        }.addOnFailureListener { e ->
                            error("Failed to delete cargo type", e, btn)
                        }
                    }.addOnFailureListener { e ->
                        error("Failed to fetch airlines", e, btn)
                    }
                }.addOnFailureListener { e ->
                    error("Failed to delete airline", e, btn)
                }
            }.addOnFailureListener { e ->
                error("Failed to delete weight docs", e, btn)
            }
        } .addOnFailureListener { e ->
            error("Failed to fetch weights", e, btn)
        }
    }
    private fun deleteSeaFlow(country: String, city: String, container: String, cargo: String, btn: Button) {

        val cargoRef = db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)
            .collection("ContainerType").document(container)
            .collection("CargoTypes").document(cargo)

        val subCollections = listOf("ContainerSize", "Weights")

        val fetchTasks = mutableListOf<Task<*>>()

        for (sub in subCollections) {
            fetchTasks.add(cargoRef.collection(sub).get())
        }
        Tasks.whenAllSuccess<QuerySnapshot>(fetchTasks).addOnSuccessListener { results ->

            val deleteTasks = mutableListOf<Task<Void>>()

            for (i in results.indices) {
                val sub = subCollections[i]
                val docs = results[i]

                for (doc in docs) {
                    deleteTasks.add(
                        cargoRef.collection(sub).document(doc.id).delete())
                }
            }

            Tasks.whenAll(deleteTasks).addOnSuccessListener {
                // 🔥 STEP 2: Delete CargoType document
                cargoRef.delete().addOnSuccessListener {

                    val cargoCol = cargoRef.parent!!

                    cargoCol.get().addOnSuccessListener { remainingCargo ->

                        if (!remainingCargo.isEmpty) {
                            done("Cargo type deleted successfully", btn)
                            return@addOnSuccessListener
                        }

                        val containerRef = cargoCol.parent!!
                        containerRef.delete().addOnSuccessListener {
                            val containerCol = containerRef.parent!!
                            containerCol.get().addOnSuccessListener { remainingContainers ->

                                if (!remainingContainers.isEmpty) {
                                    done("Cargo type and container type deleted successfully", btn)
                                    return@addOnSuccessListener
                                }

                                val cityRef = containerCol.parent!!
                                cityRef.delete().addOnSuccessListener {

                                    val cityCol = cityRef.parent!!
                                    cityCol.get().addOnSuccessListener { remainingCities ->
                                        if (!remainingCities.isEmpty) {
                                            done(
                                                "Cargo type, container type and city deleted successfully",
                                                btn
                                            )
                                            return@addOnSuccessListener
                                        }

                                        val countryRef = cityCol.parent!!
                                        countryRef.delete().addOnSuccessListener {
                                            val countryCol = countryRef.parent!!
                                            countryCol.get()
                                                .addOnSuccessListener { remainingCountries ->

                                                    if (!remainingCountries.isEmpty) {
                                                        done(
                                                            "Cargo type, container type, city and country deleted successfully",
                                                            btn
                                                        )
                                                        return@addOnSuccessListener
                                                    }

                                                    val seaDoc = countryCol.parent!!
                                                    seaDoc.delete().addOnSuccessListener {

                                                        done("Sea section fully deleted", btn)

                                                    }.addOnFailureListener { e ->
                                                        error(
                                                            "Failed to delete sea section",
                                                            e,
                                                            btn
                                                        )
                                                    }
                                                }.addOnFailureListener { e ->
                                                error("Failed to fetch countries", e, btn)
                                            }
                                        }.addOnFailureListener { e ->
                                            error("Failed to delete country", e, btn)
                                        }
                                    }.addOnFailureListener { e ->
                                        error("Failed to fetch cities", e, btn)
                                    }
                                }.addOnFailureListener { e ->
                                    error("Failed to delete city", e, btn)
                                }
                            }.addOnFailureListener { e ->
                                error("Failed to fetch container types", e, btn)
                            }
                        }.addOnFailureListener { e ->
                            error("Failed to delete container types", e, btn)
                        }
                    }.addOnFailureListener { e ->
                        error("Failed to fetch cargo types", e, btn)
                    }
                }.addOnFailureListener { e ->
                    error("Failed to delete cargo types", e, btn)
                }
            }.addOnFailureListener { e ->
                error("Failed to delete sub collection docs", e, btn)
            }
        }.addOnFailureListener { e ->
            error("Failed to fetch sub collections", e, btn)
        }
    }
    private fun error(msg: String, e: Exception, btn: Button) {
        Toast.makeText(this, "$msg: ${e.message}", Toast.LENGTH_LONG).show()
        resetButton(btn)
    }
    private fun done(msg: String, btn: Button) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

        // clear all fields
        findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveRatesAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityRemoveRatesAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeAirRemoveRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectAirLineNameRemoveRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityRemoveRatesAir).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeAirRemoveRates).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.selectAirLineNameRemoveRates).setAdapter(null)

        findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveRatesSea).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityRemoveRatesSea).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.containerTypeSeaRemoveRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaRemoveRates).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.selectCityRemoveRatesSea).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.containerTypeSeaRemoveRates).setAdapter(null)
        findViewById<AutoCompleteTextView>(R.id.cargoTypeSeaRemoveRates).setAdapter(null)
        loadCountries("Air")
        loadCountries("Sea")
        resetButton(btn)
    }
}