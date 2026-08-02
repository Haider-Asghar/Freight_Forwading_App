package com.example.ft

import android.os.Bundle
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

class Add_Admin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val adminName = findViewById<TextInputEditText>(R.id.adminName)
        val adminEmail = findViewById<TextInputEditText>(R.id.adminEmail)
        val adminDesignation = findViewById<TextInputEditText>(R.id.adminDesignation)
        val adminContactNo = findViewById<TextInputEditText>(R.id.adminContactNo)
        val adminCnicNo = findViewById<TextInputEditText>(R.id.adminCnicNo)
        val adminPassword = findViewById<TextInputEditText>(R.id.adminPassword)
        val adminConfimPassword = findViewById<TextInputEditText>(R.id.adminconfirmPassword)
        val btnAddAdmin = findViewById<Button>(R.id.btnAddAdmin)
        btnAddAdmin.setOnClickListener{
            val adminNameText = adminName.text.toString().trim()
            val adminEmailText = adminEmail.text.toString().trim()
            val adminDesignationText = adminDesignation.text.toString().trim()
            val adminContactNoText = adminContactNo.text.toString().trim()
            val adminCnicNoText = adminCnicNo.text.toString().trim()
            val adminPasswordText = adminPassword.text.toString().trim()
            val adminConfirmPasswordText = adminConfimPassword.text.toString().trim()
            if(adminNameText.isEmpty() || adminEmailText.isEmpty() || adminDesignationText.isEmpty() || adminContactNoText.isEmpty()
                || adminCnicNoText.isEmpty() || adminPasswordText.isEmpty() || adminConfirmPasswordText.isEmpty()){
                Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(adminPasswordText != adminConfirmPasswordText)
            {
                Toast.makeText(this,"Passwords do not match.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            db.collection("Admins").whereEqualTo("adminCnicNo", adminCnicNoText).get().addOnSuccessListener { query ->
                if(!query.isEmpty){
                    Toast.makeText(this,"This admin has already added. Please add another admin.",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    auth.createUserWithEmailAndPassword(adminEmailText, adminPasswordText).addOnCompleteListener{ task ->
                        if(task.isSuccessful)
                        {
                            val uid = auth.currentUser!!.uid
                            val adminData = hashMapOf(
                                "adminName" to adminNameText,
                                "adminEmail" to adminEmailText,
                                "adminDesignation" to adminDesignationText,
                                "adminContactNo" to adminContactNoText,
                                "adminCnicNo" to adminCnicNoText,
                                "uid" to uid,
                                "createdAt" to FieldValue.serverTimestamp()
                            )
                            db.collection("Admins").document(uid).set(adminData).addOnSuccessListener {
                                Toast.makeText(this,"Admin added successfully!",Toast.LENGTH_SHORT).show()
                                adminName.text?.clear()
                                adminEmail.text?.clear()
                                adminDesignation.text?.clear()
                                adminContactNo.text?.clear()
                                adminCnicNo.text?.clear()
                                adminPassword.text?.clear()
                                adminConfimPassword.text?.clear()
                            }.addOnFailureListener { e ->
                                auth.currentUser?.delete()
                                Toast.makeText(this, "Error saving Firestore data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else
                        {
                            Toast.makeText(this, "Auth failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error checking admin: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}