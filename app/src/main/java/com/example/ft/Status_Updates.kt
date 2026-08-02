package com.example.ft

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Status_Updates : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        setContentView(R.layout.activity_status_updates)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val toggleButtonStatusUpdate = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroupTransportStatusUpdate)
        val btnImportFormStatusUpdate = findViewById<MaterialButton>(R.id.btnImportStatusUpdate)
        val btnExportFormStatusUpdate = findViewById<MaterialButton>(R.id.btnExportStatusUpdate)
        toggleButtonStatusUpdate.check(R.id.btnImportStatusUpdate)
        btnImportFormStatusUpdate.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        btnImportFormStatusUpdate.setTextColor(ContextCompat.getColor(this, R.color.blue))
        btnExportFormStatusUpdate.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        btnExportFormStatusUpdate.setTextColor(ContextCompat.getColor(this, R.color.white))

        db = FirebaseFirestore.getInstance()
        loadRefNo("Import")

        val selectAction = findViewById<AutoCompleteTextView>(R.id.selectActionStatusUpdate)
        val actionList = listOf("Update Status", "View Status")

        val selectRefNo = findViewById<AutoCompleteTextView>(R.id.selectRefNoStatusUpdate)
        val statusUpdate = findViewById<LinearLayout>(R.id.llStatusUpdateEdit)
        val viewStatus = findViewById<ConstraintLayout>(R.id.clStatusUpdateView)
        selectRefNo.setOnItemClickListener { parent, _, position, _ ->
            val refNo = parent.getItemAtPosition(position).toString()
            selectAction.setText("",false)
            statusUpdate.visibility = View.GONE
            viewStatus.visibility = View.GONE
            selectAction.setAdapter(
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, actionList)
            )
            selectAction.setOnItemClickListener { _, _, position, _ ->
                if (position == 0) {
                    loadDataForUpdate("Import", refNo)
                    statusUpdate.visibility = View.VISIBLE
                    viewStatus.visibility = View.GONE

                } else {
                    loadDataForView("Import", refNo)
                    viewStatus.visibility = View.VISIBLE
                    statusUpdate.visibility = View.GONE
                }
            }
        }

        toggleButtonStatusUpdate.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if(!isChecked){
                return@addOnButtonCheckedListener
            }
            when(checkedId){

                R.id.btnImportStatusUpdate -> {
                    btnImportFormStatusUpdate.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnImportFormStatusUpdate.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnExportFormStatusUpdate.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnExportFormStatusUpdate.setTextColor(ContextCompat.getColor(this, R.color.white))

                    selectRefNo.setText("",false)
                    selectAction.setText("",false)
                    selectAction.setAdapter(null)
                    statusUpdate.visibility = View.GONE
                    viewStatus.visibility = View.GONE

                    loadRefNo("Import")
                    selectRefNo.setOnItemClickListener { parent, _, position, _ ->
                        val refNo = parent.getItemAtPosition(position).toString()
                        selectAction.setText("",false)
                        selectAction.setAdapter(null)
                        statusUpdate.visibility = View.GONE
                        viewStatus.visibility = View.GONE
                        selectAction.setAdapter(
                            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, actionList)
                        )

                        selectAction.setOnItemClickListener { _, _, position, _ ->
                            if (position == 0) {
                                loadDataForUpdate("Import", refNo)
                                statusUpdate.visibility = View.VISIBLE
                                viewStatus.visibility = View.GONE
                            } else {
                                loadDataForView("Import", refNo)
                                viewStatus.visibility = View.VISIBLE
                                statusUpdate.visibility = View.GONE
                            }
                        }
                    }
                }

                R.id.btnExportStatusUpdate -> {
                    btnExportFormStatusUpdate.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    btnExportFormStatusUpdate.setTextColor(ContextCompat.getColor(this, R.color.blue))
                    btnImportFormStatusUpdate.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    btnImportFormStatusUpdate.setTextColor(ContextCompat.getColor(this, R.color.white))

                    selectRefNo.setText("",false)
                    selectAction.setText("",false)
                    selectAction.setAdapter(null)
                    statusUpdate.visibility = View.GONE
                    viewStatus.visibility = View.GONE

                    loadRefNo("Export")
                    selectRefNo.setOnItemClickListener { parent, _, position, _ ->
                        val refNo = parent.getItemAtPosition(position).toString()
                        selectAction.setText("",false)
                        selectAction.setAdapter(null)
                        statusUpdate.visibility = View.GONE
                        viewStatus.visibility = View.GONE
                        selectAction.setAdapter(
                            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, actionList)
                        )

                        selectAction.setOnItemClickListener { _, _, position, _ ->
                            if (position == 0) {
                                loadDataForUpdate("Export", refNo)
                                statusUpdate.visibility = View.VISIBLE
                                viewStatus.visibility = View.GONE
                            } else {
                                loadDataForView("Export", refNo)
                                viewStatus.visibility = View.VISIBLE
                                statusUpdate.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }

        val blType = findViewById<AutoCompleteTextView>(R.id.selectBLStatusUpdate)
        val blOptions = listOf("Original", "Telex" , "Surrendered")
        blType.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, blOptions)
        )
    }
    private fun loadRefNo(type: String){
        val refNo = findViewById<AutoCompleteTextView>(R.id.selectRefNoStatusUpdate)
        val list = ArrayList<String>()
        db.collection("Jobs").document(type)
            .collection("Reference Number").get().addOnSuccessListener { result ->
                list.clear()
                for(doc in result) {
                    list.add(doc.id)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list)
                refNo.setAdapter(adapter)
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error Fetching the Reference Numbers from Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun loadDataForUpdate(type: String, referenceNo: String) {
        val consigneeLayout = findViewById<TextInputLayout>(R.id.consigneeNameStatusUpdateLayout)
        val consignee = findViewById<TextInputEditText>(R.id.consigneeNameStatusUpdate)
        val shipperLayout = findViewById<TextInputLayout>(R.id.shipperNameStatusUpdateLayout)
        val shipper = findViewById<TextInputEditText>(R.id.shipperNameStatusUpdate)
        val material = findViewById<TextInputEditText>(R.id.addMaterialStatusUpdate)
        val weight = findViewById<TextInputEditText>(R.id.addWeightStatusUpdate)
        val charWeight = findViewById<TextInputEditText>(R.id.addChargeableWeightStatusUpdate)
        val status = findViewById<TextInputEditText>(R.id.addStatusStatusUpdate)
        val lineLayout = findViewById<TextInputLayout>(R.id.addLineStatusUpdateLayout)
        val line = findViewById<TextInputEditText>(R.id.addLineStatusUpdate)
        val agent = findViewById<TextInputEditText>(R.id.addAgentStatusUpdate)
        val master = findViewById<TextInputEditText>(R.id.addMasterStatusUpdate)
        val house = findViewById<TextInputEditText>(R.id.addHouseStatusUpdate)
        val cutOff = findViewById<TextInputEditText>(R.id.addCuttOffStatusUpdate)
        val etd = findViewById<TextInputEditText>(R.id.addETDStatusUpdate)
        val eta = findViewById<TextInputEditText>(R.id.addETAStatusUpdate)
        val blLayout = findViewById<TextInputLayout>(R.id.selectBLStatusUpdateLayout)
        val bl = findViewById<AutoCompleteTextView>(R.id.selectBLStatusUpdate)
        val note = findViewById<TextInputEditText>(R.id.addNoteStatusUpdate)

        val ref = db.collection("Jobs").document(type)
            .collection("Reference Number").document(referenceNo)

        ref.get().addOnSuccessListener { doc ->
            val mode = doc.getString("mode") ?: ""
            consignee.setText(doc.getString("consignee") ?: "")
            shipper.setText(doc.getString("shipper") ?: "")
            material.setText(doc.getString("material") ?: "")
            weight.setText(doc.getString("weight") ?: "")
            charWeight.setText(doc.getString("chargeableWeight") ?: "")
            status.setText(doc.getString("currentStatus") ?: "")
            line.setText(doc.getString("line") ?: "")
            agent.setText(doc.getString("agent") ?: "")
            master.setText(doc.getString("masterAWB") ?: "")
            house.setText(doc.getString("houseAWB") ?: "")
            cutOff.setText(doc.getString("cuttOff") ?: "")

            cutOff.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                // DatePickerDialog instance
                val datePickerDialog = DatePickerDialog(this,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        // Date select hone par EditText mein set karein
                        val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        cutOff.setText(selectedDate)
                    }, year, month, day)

                datePickerDialog.show()
            }

            etd.setText(doc.getString("etd") ?: "")
            etd.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                // DatePickerDialog instance
                val datePickerDialog = DatePickerDialog(this,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        // Date select hone par EditText mein set karein
                        val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        etd.setText(selectedDate)
                    }, year, month, day)

                datePickerDialog.show()
            }

            eta.setText(doc.getString("eta") ?: "")
            eta.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                // DatePickerDialog instance
                val datePickerDialog = DatePickerDialog(this,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        // Date select hone par EditText mein set karein
                        val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        eta.setText(selectedDate)
                    }, year, month, day)

                datePickerDialog.show()
            }

            note.setText(doc.getString("note") ?: "")
            bl.setText(doc.getString("blStatus") ?: "", false)

            if (type == "Import") {
                consigneeLayout.isEnabled = false
                shipperLayout.isEnabled = true
            } else {
                consigneeLayout.isEnabled = true
                shipperLayout.isEnabled = false
            }

            if (mode == "Sea") {
                lineLayout.visibility = View.VISIBLE
                blLayout.visibility = View.VISIBLE
            } else {
                lineLayout.visibility = View.GONE
                blLayout.visibility = View.GONE
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error fetching the data from firestore", Toast.LENGTH_SHORT).show()
        }

        val btnStatusUpdate = findViewById<Button>(R.id.btnStatusUpdate)
        btnStatusUpdate.setOnClickListener {
            updateData(type, referenceNo)
        }
    }
    private fun loadDataForView(type: String, referenceNo: String) {
        val sDate = findViewById<TextView>(R.id.tvSDate)
        val sRefNo = findViewById<TextView>(R.id.tvSRefNo)
        val sConsignee = findViewById<TextView>(R.id.tvSConsignee)
        val sShipper = findViewById<TextView>(R.id.tvSShipper)
        val sOrigin = findViewById<TextView>(R.id.tvSorigin)
        val sCity = findViewById<TextView>(R.id.tvSCity)
        val sDestination = findViewById<TextView>(R.id.tvSDestination)
        val sMaterial = findViewById<TextView>(R.id.tvSMaterial)
        val sCargoType = findViewById<TextView>(R.id.tvSCargoType)
        val sWeight = findViewById<TextView>(R.id.tvSWeight)
        val sChargeableWeight = findViewById<TextView>(R.id.tvSChargeableWeight)
        val sStatus = findViewById<TextView>(R.id.tvSStatus)
        val sMode = findViewById<TextView>(R.id.tvSMode)
        val sLine = findViewById<TextView>(R.id.tvSLine)
        val wContainerType = findViewById<TextView>(R.id.tvWContainerType)
        val sContainerType = findViewById<TextView>(R.id.tvSContainerType)
        val wContainerSize = findViewById<TextView>(R.id.tvWContainerSize)
        val sContainerSize = findViewById<TextView>(R.id.tvSContainerSize)
        val sServiceTerm = findViewById<TextView>(R.id.tvSServiceTerm)
        val sAgent = findViewById<TextView>(R.id.tvSAgent)
        val sHouse = findViewById<TextView>(R.id.tvSMAWB)
        val sMaster = findViewById<TextView>(R.id.tvSHAWB)
        val sCutOff = findViewById<TextView>(R.id.tvSCuttOff)
        val sETD = findViewById<TextView>(R.id.tvSETD)
        val sETA = findViewById<TextView>(R.id.tvSETA)
        val wBlStatus = findViewById<TextView>(R.id.tvWBLStatus)
        val sBlStatus = findViewById<TextView>(R.id.tvSBLStatus)
        val sFreight = findViewById<TextView>(R.id.tvSFreight)
        val sExWork = findViewById<TextView>(R.id.tvSExWork)
        val sDo = findViewById<TextView>(R.id.tvSDO)
        val sTransitTime= findViewById<TextView>(R.id.tvSTransitTime)
        val sNote = findViewById<TextView>(R.id.tvSNote)
        val sLastUpdate = findViewById<TextView>(R.id.tvSLastUpdate)

        val ref = db.collection("Jobs").document(type)
            .collection("Reference Number").document(referenceNo)

        ref.get().addOnSuccessListener { doc ->

            val timestamp = doc.getTimestamp("date")
            val date = timestamp?.toDate()
            val formattedDate = if (date != null) {
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
            } else ""

            sDate.setText(formattedDate)
            sRefNo.setText(doc.getString("refNo") ?: "")
            sConsignee.setText(doc.getString("consignee") ?: "")
            sShipper.setText(doc.getString("shipper") ?: "")
            sMaterial.setText(doc.getString("material") ?: "")
            sWeight.setText(doc.getString("weight") ?: "")
            sChargeableWeight.setText(doc.getString("chargeableWeight") ?: "")
            sMode.setText(doc.getString("mode") ?: "")
            sLine.setText(doc.getString("line") ?: "")
            sAgent.setText(doc.getString("agent") ?: "")
            sMaster.setText(doc.getString("masterAWB") ?: "")
            sHouse.setText(doc.getString("houseAWB") ?: "")
            sCutOff.setText(doc.getString("cuttOff") ?: "")
            sETD.setText(doc.getString("etd") ?: "")
            sETA.setText(doc.getString("eta") ?: "-")
            sNote.setText(doc.getString("note") ?: "")

            val lastTime = doc.getTimestamp("lastUpdate")?.toDate()
            val lastUpdate = lastTime?.let {
                SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(it)
            } ?: ""

            sLastUpdate.setText(lastUpdate)
            sOrigin.setText(doc.getString("origin") ?: "")
            sCity.setText(doc.getString("city") ?: "")
            sDestination.setText(doc.getString("destination") ?: "")
            sCargoType.setText(doc.getString("cargoType") ?: "")
            sStatus.setText(doc.getString("currentStatus") ?: "")
            sServiceTerm.setText(doc.getString("serviceTerm") ?: "")
            sFreight.setText(doc.getString("freight") ?: "")
            sExWork.setText(doc.getString("exWork") ?: "")
            sDo.setText(doc.getString("doCharges") ?: "")
            sTransitTime.setText(doc.getString("transitTime") ?: "")

            val mode = doc.getString("mode")
            if(mode == "Sea"){
                wBlStatus.visibility = View.VISIBLE
                sBlStatus.visibility = View.VISIBLE
                sBlStatus.setText(doc.getString("blStatus") ?: "")
                wContainerType.visibility = View.VISIBLE
                sContainerType.visibility = View.VISIBLE
                sContainerType.setText(doc.getString("containerType") ?: "")

                val containerType = doc.getString("containerType")
                if(containerType == "FCL"){
                    wContainerSize.visibility = View.VISIBLE
                    sContainerSize.visibility = View.VISIBLE
                    sContainerSize.setText(doc.getString("containerSize") ?: "")
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
    private fun updateData(type: String, referenceNo: String) {
        val consignee = findViewById<TextInputEditText>(R.id.consigneeNameStatusUpdate)
        val shipper = findViewById<TextInputEditText>(R.id.shipperNameStatusUpdate)
        val material = findViewById<TextInputEditText>(R.id.addMaterialStatusUpdate)
        val weight = findViewById<TextInputEditText>(R.id.addWeightStatusUpdate)
        val charWeight = findViewById<TextInputEditText>(R.id.addChargeableWeightStatusUpdate)
        val status = findViewById<TextInputEditText>(R.id.addStatusStatusUpdate)
        val line = findViewById<TextInputEditText>(R.id.addLineStatusUpdate)
        val agent = findViewById<TextInputEditText>(R.id.addAgentStatusUpdate)
        val master = findViewById<TextInputEditText>(R.id.addMasterStatusUpdate)
        val house = findViewById<TextInputEditText>(R.id.addHouseStatusUpdate)
        val cutOff = findViewById<TextInputEditText>(R.id.addCuttOffStatusUpdate)
        val etd = findViewById<TextInputEditText>(R.id.addETDStatusUpdate)
        val eta = findViewById<TextInputEditText>(R.id.addETAStatusUpdate)
        val bl = findViewById<AutoCompleteTextView>(R.id.selectBLStatusUpdate)
        val note = findViewById<TextInputEditText>(R.id.addNoteStatusUpdate)

        val ref = db.collection("Jobs").document(type)
            .collection("Reference Number").document(referenceNo)

        val updates = hashMapOf<String, Any>()

        if(type  == "Import"){
            if (shipper.text.toString().isNotEmpty()) updates["shipper"] = shipper.text.toString()
        }
        else{
            if (consignee.text.toString().isNotEmpty()) updates["consignee"] = consignee.text.toString()
        }
        if (material.text.toString().isNotEmpty()) updates["material"] = material.text.toString()
        if (weight.text.toString().isNotEmpty()) updates["weight"] = weight.text.toString()
        if (charWeight.text.toString().isNotEmpty()) updates["chargeableWeight"] = charWeight.text.toString()
        if (status.text.toString().isNotEmpty()) updates["currentStatus"] = status.text.toString()
        if (line.text.toString().isNotEmpty()) updates["line"] = line.text.toString()
        if (agent.text.toString().isNotEmpty()) updates["agent"] = agent.text.toString()
        if (master.text.toString().isNotEmpty()) updates["masterAWB"] = master.text.toString()
        if (house.text.toString().isNotEmpty()) updates["houseAWB"] = house.text.toString()
        if (cutOff.text.toString().isNotEmpty()) updates["cuttOff"] = cutOff.text.toString()
        if (etd.text.toString().isNotEmpty()) updates["etd"] = etd.text.toString()
        if (eta.text.toString().isNotEmpty()) updates["eta"] = eta.text.toString()
        if (bl.text.toString().isNotEmpty()) updates["blStatus"] = bl.text.toString()
        if (note.text.toString().isNotEmpty()) updates["note"] = note.text.toString()
        updates["lastUpdate"] = FieldValue.serverTimestamp()

        ref.update(updates).addOnSuccessListener {
            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error update the data in firestore", Toast.LENGTH_SHORT).show()
        }

        val selectRefNo = findViewById<AutoCompleteTextView>(R.id.selectRefNoStatusUpdate)
        val selectAction = findViewById<AutoCompleteTextView>(R.id.selectActionStatusUpdate)
        val statusUpdate = findViewById<LinearLayout>(R.id.llStatusUpdateEdit)
        val viewStatus = findViewById<ConstraintLayout>(R.id.clStatusUpdateView)
        selectRefNo.setText("",false)
        selectAction.setText("",false)
        selectAction.setAdapter(null)
        statusUpdate.visibility = View.GONE
        viewStatus.visibility = View.GONE
    }
}