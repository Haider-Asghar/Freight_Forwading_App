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

class Remove_City : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remove_city)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val toggleButtonRemoveCity =findViewById<com.google.android.material.button.MaterialButtonToggleGroup>(R.id.toggleGroupTransportRemoveCity)
        val btnAirFormRemoveCity = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnAirRemoveCity)
        val btnSeaFormRemoveCity = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSeaRemoveCity)
        val airFormRemoveCity = findViewById<LinearLayout>(R.id.llRemoveCityAir)
        val seaFormRemoveCity =findViewById<LinearLayout>(R.id.llRemoveCitySea)
        toggleButtonRemoveCity.check(R.id.btnAirRemoveCity)
        airFormRemoveCity.visibility =View.VISIBLE
        seaFormRemoveCity.visibility = View.GONE
        btnAirFormRemoveCity.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnAirFormRemoveCity.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnSeaFormRemoveCity.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnSeaFormRemoveCity.setTextColor(ContextCompat.getColor(this, R.color.white))

        toggleButtonRemoveCity.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked){
                return@addOnButtonCheckedListener
            }
            when(checkedId){
                R.id.btnAirRemoveCity -> {
                    airFormRemoveCity.visibility =View.VISIBLE
                    seaFormRemoveCity.visibility = View.GONE
                    btnAirFormRemoveCity.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnAirFormRemoveCity.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnSeaFormRemoveCity.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnSeaFormRemoveCity.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
                R.id.btnSeaRemoveCity -> {
                    airFormRemoveCity.visibility = View.GONE
                    seaFormRemoveCity.visibility = View.VISIBLE
                    btnSeaFormRemoveCity.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnSeaFormRemoveCity.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnAirFormRemoveCity.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnAirFormRemoveCity.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        }
        db = FirebaseFirestore.getInstance()
        loadCountries("Air")
        loadCountries("Sea")
        val selectCountryRemoveCityAir = findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveCityAir)
        val removeCityAir = findViewById<AutoCompleteTextView>(R.id.removeCityAir)
        selectCountryRemoveCityAir.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            removeCityAir.setText("", false)
            loadCities("Air", country)
        }
        val selectCountryRemoveCitySea = findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveCitySea)
        val removeCitySea = findViewById<AutoCompleteTextView>(R.id.removeCitySea)
        selectCountryRemoveCitySea.setOnItemClickListener { parent, _, position, _ ->
            val country = parent.getItemAtPosition(position).toString()
            removeCitySea.setText("", false)
            loadCities("Sea", country)
        }
        val btnRemoveCity = findViewById<Button>(R.id.btnRemoveCity)

        btnRemoveCity.setOnClickListener {

            btnRemoveCity.isEnabled = false
            btnRemoveCity.alpha = 0.5f

            if (airFormRemoveCity.visibility == View.VISIBLE) {

                val country = selectCountryRemoveCityAir.text.toString()
                val city = removeCityAir.text.toString()

                if (country.isEmpty() || city.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    resetButton(btnRemoveCity)
                    return@setOnClickListener
                }

                deleteAirCity(country, city, btnRemoveCity)

            } else {

                val country = selectCountryRemoveCitySea.text.toString()
                val city = removeCitySea.text.toString()

                if (country.isEmpty() || city.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    resetButton(btnRemoveCity)
                    return@setOnClickListener
                }

                deleteSeaCity(country, city, btnRemoveCity)
            }
        }
    }
    private fun loadCountries(type: String){
        val countryAir = findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveCityAir)
        val countrySea = findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveCitySea)
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
        val cityAir = findViewById<AutoCompleteTextView>(R.id.removeCityAir)
        val citySea = findViewById<AutoCompleteTextView>(R.id.removeCitySea)
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
    private fun resetButton(btn: Button) {
        btn.isEnabled = true
        btn.alpha = 1f
    }
    private fun deleteAirCity(country: String, city: String, btn: Button) {

        val cityRef = db.collection("Rates").document("Air")
            .collection("Countries").document(country)
            .collection("Cities").document(city)

        cityRef.collection("CargoTypes").get().addOnSuccessListener { cargoDocs ->

                val tasks = mutableListOf<Task<Void>>()

                for (cargo in cargoDocs) {

                    val cargoRef = cityRef.collection("CargoTypes").document(cargo.id)

                    val cargoTask = cargoRef.collection("Airlines").get().continueWithTask { airlineTask ->

                            if (!airlineTask.isSuccessful) {
                                throw airlineTask.exception ?: Exception("Airlines fetch failed")
                            }

                            val airlineDocs = airlineTask.result
                            val innerTasks = mutableListOf<Task<Void>>()

                            for (airline in airlineDocs) {

                                val airlineRef = cargoRef.collection("Airlines").document(airline.id)

                                val rateTask = airlineRef.collection("Rates").get().continueWithTask { rateDocsTask ->

                                        if (!rateDocsTask.isSuccessful) {
                                            throw rateDocsTask.exception ?: Exception("Rates fetch failed")
                                        }

                                        val rateDocs = rateDocsTask.result
                                        val deleteTasks = mutableListOf<Task<Void>>()

                                        for (rate in rateDocs) {
                                            deleteTasks.add(airlineRef.collection("Rates").document(rate.id).delete())
                                        }

                                        deleteTasks.add(airlineRef.delete())

                                        return@continueWithTask Tasks.whenAll(deleteTasks)
                                    }

                                innerTasks.add(rateTask)
                            }

                        val deleteCargoTask = Tasks.whenAll(innerTasks)
                            .continueWithTask {
                                cargoRef.delete()
                            }
                        return@continueWithTask deleteCargoTask
                        }

                    tasks.add(cargoTask)
                }

                Tasks.whenAll(tasks).addOnSuccessListener {

                        cityRef.delete().addOnSuccessListener {

                            checkAndDeleteCountry(country,"Air", btn)

                        }.addOnFailureListener {
                                error("City delete failed", it, btn)
                        }
                }.addOnFailureListener {
                        error("Air city delete failed", it, btn)
                }

        }.addOnFailureListener {
                error("CargoTypes fetch failed", it, btn)
        }
    }
    private fun deleteSeaCity(country: String, city: String, btn: Button) {

        val cityRef = db.collection("Rates").document("Sea")
            .collection("Countries").document(country)
            .collection("Cities").document(city)

        cityRef.collection("ContainerType").get().addOnSuccessListener { containerDocs ->

                val tasks = mutableListOf<Task<Void>>()

                for (container in containerDocs) {

                    val containerRef = cityRef.collection("ContainerType").document(container.id)

                    val containerTask = containerRef.collection("CargoTypes").get().continueWithTask { cargoTask ->

                            if (!cargoTask.isSuccessful) {
                                throw cargoTask.exception ?: Exception("CargoTypes fetch failed")
                            }

                            val cargoDocs = cargoTask.result
                            val innerTasks = mutableListOf<Task<Void>>()

                            for (cargo in cargoDocs) {

                                val cargoRef = containerRef.collection("CargoTypes").document(cargo.id)

                                val subTasks = mutableListOf<Task<Void>>()

                                val collections = listOf("ContainerSize", "Weights")

                                for (sub in collections) {
                                    val task = cargoRef.collection(sub).get().continueWithTask { docsTask ->

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

                                    subTasks.add(task)
                                }

                                val deleteCargoTask = Tasks.whenAll(subTasks)
                                    .continueWithTask {
                                        cargoRef.delete()
                                    }

                                innerTasks.add(deleteCargoTask)
                            }

                        val deleteContainerTask = Tasks.whenAll(innerTasks)
                            .continueWithTask {
                                containerRef.delete()
                            }

                        return@continueWithTask deleteContainerTask
                        }

                    tasks.add(containerTask)
                }

                Tasks.whenAll(tasks).addOnSuccessListener {

                        cityRef.delete().addOnSuccessListener {
                            checkAndDeleteCountry(country,"Sea", btn)
                            }.addOnFailureListener {
                                error("City delete failed", it, btn)
                            }
                    }.addOnFailureListener {
                        error("Sea delete failed", it, btn)
                    }

            }.addOnFailureListener {
                error("ContainerType fetch failed", it, btn)
            }
    }
    private fun error(msg: String, e: Exception, btn: Button) {
        Toast.makeText(this, "$msg: ${e.message}", Toast.LENGTH_LONG).show()
        resetButton(btn)
    }
    private fun checkAndDeleteCountry(country: String, transportType: String, btn: Button) {

        val countryRef = db.collection("Rates").document(transportType)
            .collection("Countries").document(country)

        countryRef.collection("Cities").get().addOnSuccessListener { cities ->

            if (!cities.isEmpty) {
                done("City deleted successfully", btn)
                return@addOnSuccessListener
            }

            // Delete Country
            countryRef.delete().addOnSuccessListener {

                checkAndDeleteCountriesCollection(transportType, btn)

            }.addOnFailureListener {
                error("Country deleted failed", it, btn)
            }

        }.addOnFailureListener {
            error("Cities check failed", it, btn)
        }
    }
    private fun checkAndDeleteCountriesCollection(transportType: String, btn: Button) {

        val countriesRef = db.collection("Rates").document(transportType)
            .collection("Countries")

        countriesRef.get().addOnSuccessListener { countries ->

            if (!countries.isEmpty) {
                done("Country deleted successfully", btn)
                return@addOnSuccessListener
            }

            // Delete Air document
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

        // clear all fields
        findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveCityAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.removeCityAir).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.removeCityAir).setAdapter(null)

        findViewById<AutoCompleteTextView>(R.id.selectCountryRemoveCitySea).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.removeCitySea).setText("",false)
        findViewById<AutoCompleteTextView>(R.id.removeCitySea).setAdapter(null)

        loadCountries("Air")
        loadCountries("Sea")
        resetButton(btn)
    }
}