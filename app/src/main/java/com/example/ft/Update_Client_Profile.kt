package com.example.ft

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class Update_Client_Profile : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var companyName: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var personName: TextInputEditText
    private lateinit var contactNo: TextInputEditText
    private lateinit var cnicNo: TextInputEditText
    private lateinit var address: TextInputEditText
    private lateinit var btnUpdate: Button

    private var documentId: String = ""
    private var oldContactNo: String = ""
    private var isDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_client_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        companyName = findViewById(R.id.companyNameCP)
        email = findViewById(R.id.emailCP)
        personName = findViewById(R.id.personNameCP)
        contactNo = findViewById(R.id.contactNoCP)
        cnicNo = findViewById(R.id.cnicNoCP)
        address = findViewById(R.id.companyAddressCP)
        btnUpdate = findViewById(R.id.btnUpdateClientProfile)

        btnUpdate.isEnabled = false
        btnUpdate.alpha = 0.5f

        loadUserData()
        setupEditListeners()
    }
    private fun loadUserData() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userEmail = currentUser.email

            db.collection("Users").whereEqualTo("email", userEmail).get().addOnSuccessListener { result ->

                if (!result.isEmpty) {

                    val document = result.documents[0]
                        documentId = document.id

                        companyName.setText(document.getString("companyName"))
                        email.setText(document.getString("email"))
                        personName.setText(document.getString("personName"))

                        val updatedContact = document.getString("updatedContactNo")
                        if (!updatedContact.isNullOrEmpty()) {
                            contactNo.setText(updatedContact)
                            oldContactNo = updatedContact
                        } else {
                            val originalContact = document.getString("contactNo") ?: ""
                            contactNo.setText(originalContact)
                            oldContactNo = originalContact
                        }

                        cnicNo.setText(document.getString("cnicNo"))
                        address.setText(document.getString("companyAddress"))

                        // ❌ Disable fields
                        companyName.isEnabled = false
                        email.isEnabled = false
                    }
                   isDataLoaded = true
                } .addOnFailureListener {
                Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupEditListeners() {

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (!isDataLoaded) return

                btnUpdate.isEnabled = true
                btnUpdate.alpha = 1f
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }


        personName.addTextChangedListener(textWatcher)
        contactNo.addTextChangedListener(textWatcher)
        cnicNo.addTextChangedListener(textWatcher)
        address.addTextChangedListener(textWatcher)


        btnUpdate.setOnClickListener {
            validateAndUpdate()
        }
    }
    private fun validateAndUpdate() {

        val newPersonName = personName.text.toString().trim()
        val newContact = contactNo.text.toString().trim()
        val newCnic = cnicNo.text.toString().trim()
        val newAddress = address.text.toString().trim()

        // ❌ Validation
        if (newPersonName.isEmpty() || newContact.isEmpty() || newCnic.isEmpty() || newAddress.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        updateData(newPersonName, newContact, newCnic, newAddress)
    }

    private fun updateData( newPersonName: String, newContact: String, newCnic: String, newAddress: String) {

        btnUpdate.isEnabled = false
        btnUpdate.alpha = 0.5f

        val updates = hashMapOf<String, Any>(
            "personName" to newPersonName,
            "cnicNo" to newCnic,
            "companyAddress" to newAddress,
            "lastUpdated" to FieldValue.serverTimestamp())
        if (newContact != oldContactNo) {
            updates["updatedContactNo"] = newContact
        }

        db.collection("Users").document(documentId).update(updates).addOnSuccessListener {

                Toast.makeText(this, "Detail Updated Successfully", Toast.LENGTH_SHORT).show()
                personName.text?.clear()
                contactNo.text?.clear()
                cnicNo.text?.clear()
                address.text?.clear()

                finish() // remove from stack
            }.addOnFailureListener {
                btnUpdate.isEnabled = true
                btnUpdate.alpha = 1f
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
    }
}