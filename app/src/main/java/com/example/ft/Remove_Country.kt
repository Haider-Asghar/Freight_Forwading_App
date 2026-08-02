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

class Remove_Country : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remove_country)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val toggleButtonRemoveCountry =
            findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.toggleGroupTransportRemoveCountry)
        val btnAirFormRemoveCountry =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.btnAirRemoveCountry)
        val btnSeaFormRemoveCountry =
            findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSeaRemoveCountry)
        val airFormRemoveCountry = findViewById<LinearLayout>(R.id.llRemoveCountryAir)
        val seaFormRemoveCountry = findViewById<LinearLayout>(R.id.llRemoveCountrySea)
        toggleButtonRemoveCountry.check(R.id.btnAirRemoveCountry)
        airFormRemoveCountry.visibility = View.VISIBLE
        seaFormRemoveCountry.visibility = View.GONE
        btnAirFormRemoveCountry.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnAirFormRemoveCountry.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnSeaFormRemoveCountry.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnSeaFormRemoveCountry.setTextColor(ContextCompat.getColor(this, R.color.white))

        toggleButtonRemoveCountry.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) {
                return@addOnButtonCheckedListener
            }
            when (checkedId) {
                R.id.btnAirRemoveCountry -> {
                    airFormRemoveCountry.visibility = View.VISIBLE
                    seaFormRemoveCountry.visibility = View.GONE
                    btnAirFormRemoveCountry.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnAirFormRemoveCountry.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnSeaFormRemoveCountry.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnSeaFormRemoveCountry.setTextColor(ContextCompat.getColor(this, R.color.white))
                }

                R.id.btnSeaRemoveCountry -> {
                    airFormRemoveCountry.visibility = View.GONE
                    seaFormRemoveCountry.visibility = View.VISIBLE
                    btnSeaFormRemoveCountry.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnSeaFormRemoveCountry.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnAirFormRemoveCountry.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnAirFormRemoveCountry.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        }
        db = FirebaseFirestore.getInstance()
        loadCountries("Air")
        loadCountries("Sea")
        val btnRemoveCountry = findViewById<Button>(R.id.btnRemoveCountry)
        btnRemoveCountry.setOnClickListener {

            btnRemoveCountry.isEnabled = false
            btnRemoveCountry.alpha = 0.5f

            val type = if (airFormRemoveCountry.visibility == View.VISIBLE) {
                "Air"
            } else {
                "Sea"
            }
            val countryName = if (type == "Air") {
                findViewById<AutoCompleteTextView>(R.id.removeCountryAir).text.toString()
            } else {
                findViewById<AutoCompleteTextView>(R.id.removeCountrySea).text.toString()
            }
            if (countryName.isEmpty()) {
                Toast.makeText(this, "Please Select Country.", Toast.LENGTH_SHORT).show()
                resetButton(btnRemoveCountry)
                return@setOnClickListener
            }
            if (type == "Air") {
                deleteAirCountry(countryName, btnRemoveCountry)
            } else {
                deleteSeaCountry(countryName, btnRemoveCountry)
            }
        }
    }
    private fun loadCountries(type: String) {
        val countryAir = findViewById<AutoCompleteTextView>(R.id.removeCountryAir)
        val countrySea = findViewById<AutoCompleteTextView>(R.id.removeCountrySea)
        val list = ArrayList<String>()
        db.collection("Rates").document(type).collection("Countries").get().addOnSuccessListener { result ->
                list.clear()
                for (doc in result) {
                    list.add(doc.id)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list)
                if (type == "Air") {
                    countryAir.setAdapter(adapter)
                } else {
                    countrySea.setAdapter(adapter)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching Countries from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun resetButton(btn: Button) {
        btn.isEnabled = true
        btn.alpha = 1f
    }
    private fun deleteAirCountry(country: String, btn: Button) {

        val countryRef = db.collection("Rates").document("Air")
            .collection("Countries").document(country)

        countryRef.collection("Cities").get().addOnSuccessListener { cityDocs ->

            val tasks = mutableListOf<Task<Void>>()

            for (city in cityDocs) {

                val cityRef = countryRef.collection("Cities").document(city.id)

                val task = cityRef.collection("CargoTypes").get().continueWithTask { cargoTask ->

                    if (!cargoTask.isSuccessful) {
                        throw cargoTask.exception ?: Exception("CargoTypes fetch failed")
                    }

                    val cargoDocs = cargoTask.result
                    val innerTasks = mutableListOf<Task<Void>>()

                    for (cargo in cargoDocs) {

                        val cargoRef = cityRef.collection("CargoTypes").document(cargo.id)

                        val cargoInnerTask = cargoRef.collection("Airlines").get().continueWithTask { airlineTask ->

                            if (!airlineTask.isSuccessful) {
                                throw airlineTask.exception ?: Exception("Airlines fetch failed")
                            }

                            val airlineDocs = airlineTask.result
                            val airlineTasks = mutableListOf<Task<Void>>()

                            for (airline in airlineDocs) {

                                val airlineRef = cargoRef.collection("Airlines").document(airline.id)

                                val rateTask = airlineRef.collection("Rates").get().continueWithTask { rateTask ->

                                    if (!rateTask.isSuccessful) {
                                        throw rateTask.exception ?: Exception("Rates fetch failed")
                                    }

                                    val rateDocs = rateTask.result
                                    val deleteTasks = mutableListOf<Task<Void>>()

                                    for (rate in rateDocs) {
                                        deleteTasks.add(airlineRef.collection("Rates").document(rate.id).delete())
                                    }

                                    deleteTasks.add(airlineRef.delete())

                                    return@continueWithTask Tasks.whenAll(deleteTasks)
                                }

                                airlineTasks.add(rateTask)
                            }

                            return@continueWithTask Tasks.whenAll(airlineTasks)
                                .continueWithTask {
                                    cargoRef.delete()
                                }
                        }

                        innerTasks.add(cargoInnerTask)
                    }

                    return@continueWithTask Tasks.whenAll(innerTasks)
                        .continueWithTask {
                            cityRef.delete()
                        }
                }

                tasks.add(task)
            }

            Tasks.whenAll(tasks).addOnSuccessListener {

                // Delete Country
                countryRef.delete().addOnSuccessListener {
                    checkAndDeleteCountriesCollection("Air", btn)
                }.addOnFailureListener {
                    error("Country delete failed", it, btn)
                }

            }.addOnFailureListener {
                error("Air country delete failed", it, btn)
            }

        }.addOnFailureListener {
            error("Cities fetch failed", it, btn)
        }
    }
    private fun deleteSeaCountry(country: String, btn: Button) {

        val countryRef = db.collection("Rates").document("Sea")
            .collection("Countries").document(country)

        countryRef.collection("Cities").get().addOnSuccessListener { cityDocs ->

            val tasks = mutableListOf<Task<Void>>()

            for (city in cityDocs) {

                val cityRef = countryRef.collection("Cities").document(city.id)

                val task = cityRef.collection("ContainerType").get().continueWithTask { containerTask ->

                    if (!containerTask.isSuccessful) {
                        throw containerTask.exception ?: Exception("ContainerType fetch failed")
                    }

                    val containerDocs = containerTask.result
                    val innerTasks = mutableListOf<Task<Void>>()

                    for (container in containerDocs) {

                        val containerRef = cityRef.collection("ContainerType").document(container.id)

                        val containerInnerTask = containerRef.collection("CargoTypes").get().continueWithTask { cargoTask ->

                            if (!cargoTask.isSuccessful) {
                                throw cargoTask.exception ?: Exception("CargoTypes fetch failed")
                            }

                            val cargoDocs = cargoTask.result
                            val cargoTasks = mutableListOf<Task<Void>>()

                            for (cargo in cargoDocs) {

                                val cargoRef = containerRef.collection("CargoTypes").document(cargo.id)

                                val subTasks = mutableListOf<Task<Void>>()

                                val collections = listOf("ContainerSize", "Weights")

                                for (sub in collections) {

                                    val subTask = cargoRef.collection(sub).get().continueWithTask { docsTask ->

                                        if (!docsTask.isSuccessful) {
                                            throw docsTask.exception ?: Exception("$sub fetch failed")
                                        }

                                        val docs = docsTask.result
                                        val deleteTasks = mutableListOf<Task<Void>>()

                                        for (doc in docs) {
                                            deleteTasks.add(cargoRef.collection(sub).document(doc.id).delete())
                                        }

                                        return@continueWithTask Tasks.whenAll(deleteTasks)
                                    }

                                    subTasks.add(subTask)
                                }

                                val deleteCargoTask = Tasks.whenAll(subTasks)
                                    .continueWithTask {
                                        cargoRef.delete()
                                    }

                                cargoTasks.add(deleteCargoTask)
                            }

                            return@continueWithTask Tasks.whenAll(cargoTasks)
                                .continueWithTask {
                                    containerRef.delete()
                                }
                        }

                        innerTasks.add(containerInnerTask)
                    }

                    return@continueWithTask Tasks.whenAll(innerTasks)
                        .continueWithTask {
                            cityRef.delete()
                        }
                }

                tasks.add(task)
            }

            Tasks.whenAll(tasks).addOnSuccessListener {

                countryRef.delete().addOnSuccessListener {
                    checkAndDeleteCountriesCollection("Sea", btn)
                }.addOnFailureListener {
                    error("Country delete failed", it, btn)
                }

            }.addOnFailureListener {
                error("Sea country delete failed", it, btn)
            }

        }.addOnFailureListener {
            error("Cities fetch failed", it, btn)
        }
    }
    private fun error(msg: String, e: Exception, btn: Button) {
        Toast.makeText(this, "$msg: ${e.message}", Toast.LENGTH_LONG).show()
        resetButton(btn)
    }
    private fun checkAndDeleteCountriesCollection(transportType: String, btn: Button) {

        val countriesRef = db.collection("Rates").document(transportType)
            .collection("Countries")

        countriesRef.get().addOnSuccessListener { countries ->

            if (!countries.isEmpty) {
                done("Country deleted successfully", btn)
                return@addOnSuccessListener
            }

            db.collection("Rates").document(transportType).delete()
                .addOnSuccessListener {
                    done("$transportType section fully deleted", btn)
                }.addOnFailureListener {
                    error("$transportType document delete failed", it, btn)
                }

        }.addOnFailureListener {
            error("Countries check failed", it, btn)
        }
    }
    private fun done(msg: String, btn: Button) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

        findViewById<AutoCompleteTextView>(R.id.removeCountryAir).setText("", false)
        findViewById<AutoCompleteTextView>(R.id.removeCountrySea).setText("", false)

        loadCountries("Air")
        loadCountries("Sea")
        resetButton(btn)
    }
}