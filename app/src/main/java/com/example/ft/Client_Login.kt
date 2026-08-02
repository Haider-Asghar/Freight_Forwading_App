package com.example.ft

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Client_Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signup = findViewById<TextView>(R.id.tvSignUp)
        signup.setOnClickListener {
            val intent = Intent(this, Sign_Up::class.java)
            startActivity(intent)
        }

        val tvAdminLogin = findViewById<TextView>(R.id.tvAdminLogin)
        tvAdminLogin.setOnClickListener{
            val intent = Intent(this,Admin_Login::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val email = findViewById<TextInputEditText>(R.id.email)
        val password = findViewById<TextInputEditText>(R.id.password)
        val progressBar = findViewById<ProgressBar>(R.id.pbClientLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please fill all above fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showLoading(true,progressBar,btnLogin)
            auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    getCompanyNameFromFirestore(emailText,email,password,progressBar,btnLogin)
                }
                else
                {
                    showLoading(false,progressBar,btnLogin)
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCompanyNameFromFirestore(emailText: String, email: TextInputEditText, password: TextInputEditText, progressBar: ProgressBar,
        btnLogin: Button)
    {
        firestore.collection("Users").get().addOnSuccessListener { result ->
            var companyNameFound: String? = null

            //  Loop through all documents to find email match
            for (document in result) {
                val storedEmail = document.getString("email")
                if (storedEmail == emailText) {
                    companyNameFound = document.id //  Document ID is the company name (primary key)
                    break
                }
            }
            showLoading(false,progressBar,btnLogin)
            if (companyNameFound != null) {
                //  Company found — go to Dashboard
                val intent = Intent(this, Client_Dashboard::class.java)
                intent.putExtra("COMPANY_NAME", companyNameFound)
                startActivity(intent)
                email.text?.clear()
                password.text?.clear()
            }
            else
            {
                auth.signOut()
                Toast.makeText(this, "Company not found in Firestore", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            showLoading(false,progressBar,btnLogin)
            Toast.makeText(this, "Error fetching data from Firestore", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showLoading(isLoading: Boolean,progressBar: ProgressBar,btnLogin: Button)
    {
        if(isLoading)
        {
            progressBar.visibility = View.VISIBLE
            btnLogin.isEnabled = false
            btnLogin.alpha = 0.6f
        }
        else
        {
            progressBar.visibility = View.GONE
            btnLogin.isEnabled = true
            btnLogin.alpha = 1.0f
        }
    }
}