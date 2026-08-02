package com.example.ft

import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FeedBack_Review : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feed_back_review)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        var ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        var feedback = findViewById<TextInputEditText>(R.id.Feedback)
        var btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {

            val ratingValue = ratingBar.rating
            val feedbackText = feedback.text.toString().trim()

            // Disable button
            btnSubmit.isEnabled = false

            // ✅ Validation
            if (ratingValue < 1) {
                Toast.makeText(this, "Please select at least 1 star", Toast.LENGTH_SHORT).show()
                btnSubmit.isEnabled = true
                return@setOnClickListener
            }

            if (feedbackText.isEmpty()) {
                Toast.makeText(this, "Please enter feedback", Toast.LENGTH_SHORT).show()
                btnSubmit.isEnabled = true
                return@setOnClickListener
            }

            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()

            val user = auth.currentUser

            if (user == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                btnSubmit.isEnabled = true
                return@setOnClickListener
            }

            val email = user.email!!

            // ✅ Step 1: Find company name
            db.collection("Users").whereEqualTo("email", email).get()
                .addOnSuccessListener { result ->

                    if (result.isEmpty) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                        btnSubmit.isEnabled = true
                        return@addOnSuccessListener
                    }

                    val companyName = result.documents[0].id

                    // ✅ Step 2: Save feedback in subcollection
                    val data = hashMapOf(
                        "rating" to ratingValue,
                        "feedback" to feedbackText,
                        "time" to FieldValue.serverTimestamp()
                    )

                    db.collection("Users").document(companyName)
                        .collection("Rating").add(data).addOnSuccessListener {

                            Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show()

                            // ✅ Reset fields
                            ratingBar.rating = 0f
                            feedback.setText("")

                            btnSubmit.isEnabled = true

                        }.addOnFailureListener {
                            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                            btnSubmit.isEnabled = true
                        }
                } .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    btnSubmit.isEnabled = true
                }
        }
    }
}