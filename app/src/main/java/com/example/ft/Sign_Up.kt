package com.example.ft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class Sign_Up : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tvLogin = findViewById<TextView>(R.id.tvLogin)
        tvLogin.setOnClickListener {
            val intent = Intent(this, Client_Login::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val companyName = findViewById<TextInputEditText>(R.id.companyName)
        val email = findViewById<TextInputEditText>(R.id.email)
        val personName = findViewById<TextInputEditText>(R.id.personName)
        val contactNo = findViewById<TextInputEditText>(R.id.contactNo)
        val cnicNo = findViewById<TextInputEditText>(R.id.cnicNo)
        val password = findViewById<TextInputEditText>(R.id.password)
        val confirmPassword = findViewById<TextInputEditText>(R.id.confirmPassword)
        val companyAddress = findViewById<TextInputEditText>(R.id.companyAddress)
        val btnsignup = findViewById<Button>(R.id.btnsignup)
        btnsignup.setOnClickListener {
            val companyNameText = companyName.text.toString().trim()
            val emailText = email.text.toString().trim()
            val personNameText = personName.text.toString().trim()
            val contactNoText = contactNo.text.toString().trim()
            val cnicNoText = cnicNo.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val confirmPasswordText = confirmPassword.text.toString().trim()
            val companyAddressText = companyAddress.text.toString().trim()

            if (companyNameText.isEmpty() || emailText.isEmpty() || personNameText.isEmpty() || contactNoText.isEmpty() || cnicNoText.isEmpty()
                || companyAddressText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty())
            {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("Users").document(companyNameText).get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        Toast.makeText(this, "This company has  already registered. Please Login In to use the app",
                            Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        if (passwordText != confirmPasswordText)
                        {
                            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }
                        auth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
                                if (task.isSuccessful)
                                {
                                    val uid = auth.currentUser!!.uid
                                    val userData = hashMapOf(
                                        "companyName" to companyNameText,
                                        "email" to emailText,
                                        "personName" to personNameText,
                                        "contactNo" to contactNoText,
                                        "cnicNo" to cnicNoText,
                                        "companyAddress" to companyAddressText,
                                        "uid" to uid,
                                        "createdAt" to FieldValue.serverTimestamp()
                                    )
                                    db.collection("Users").document(companyNameText).set(userData).addOnSuccessListener {
                                            Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this,Client_Login::class.java)
                                            startActivity(intent)
                                            companyName.text?.clear()
                                            email.text?.clear()
                                            personName.text?.clear()
                                            contactNo.text?.clear()
                                            cnicNo.text?.clear()
                                            companyAddress.text?.clear()
                                            password.text?.clear()
                                            confirmPassword.text?.clear()
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
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Error checking company: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
