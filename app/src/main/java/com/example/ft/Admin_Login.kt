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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class Admin_Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvClientLogin = findViewById<TextView>(R.id.tvClientLogin)
        tvClientLogin.setOnClickListener{
            val intent = Intent(this,Client_Login::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailAdmin = findViewById<TextInputEditText>(R.id.emailAdmin)
        val passwordAdmin = findViewById<TextInputEditText>(R.id.passwordAdmin)
        val pbAdminLogin = findViewById<ProgressBar>(R.id.pbAdminLogin)
        val btnAdminLogin = findViewById<Button>(R.id.btnAdminLogin)
        btnAdminLogin.setOnClickListener{
            val emailAdminText = emailAdmin.text.toString().trim()
            val passwordAdminText = passwordAdmin.text.toString().trim()
            if(emailAdminText.isEmpty() || passwordAdminText.isEmpty())
            {
                Toast.makeText(this, "Please fill all above fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showLoading(true,pbAdminLogin,btnAdminLogin)
            auth.signInWithEmailAndPassword(emailAdminText, passwordAdminText).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    val uid = auth.currentUser!!.uid
                    db.collection("Admins").document(uid).get().addOnSuccessListener { document ->
                        showLoading(false,pbAdminLogin,btnAdminLogin)
                        if(document.exists())
                        {
                            val adminName =document.getString("adminName")
                            val intent = Intent(this,Admin_Dashboard::class.java)
                            intent.putExtra("adminName",adminName)
                            startActivity(intent)
                            emailAdmin.text?.clear()
                            passwordAdmin.text?.clear()
                        }
                        else
                        {
                            auth.signOut()
                            Toast.makeText(this,"Admin record not found in firestore",Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        showLoading(false,pbAdminLogin,btnAdminLogin)
                        Toast.makeText(this, "Error fetching data from Firestore", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    showLoading(false,pbAdminLogin,btnAdminLogin)
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean,pbAdminLogin: ProgressBar,btnAdminLogin: Button)
    {
        if(isLoading)
        {
            pbAdminLogin.visibility = View.VISIBLE
            btnAdminLogin.isEnabled = false
            btnAdminLogin.alpha = 0.6f
        }
        else
        {
            pbAdminLogin.visibility = View.GONE
            btnAdminLogin.isEnabled = true
            btnAdminLogin.alpha = 1.0f
        }
    }
}
