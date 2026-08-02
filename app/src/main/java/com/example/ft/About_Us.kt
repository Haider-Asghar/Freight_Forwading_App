package com.example.ft

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class About_Us : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about_us)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var aboutText = findViewById<TextView>(R.id.aboutText)

        val aboutUs = """ 
            Dynamic Resume Manager is an innovative Android application designed to simplify and enhance the job application process for modern job seekers. By leveraging user data such as education, work experience, and skills, the app intelligently predicts suitable job roles and generates customized resumes optimized for those roles.
            
            The application automatically adjusts resume content to match the requirements of specific job listings, helping users create highly targeted and professional resumes. With integration to multiple job platforms, users can apply to jobs directly through the app, eliminating repetitive form-filling and document uploads.
            
            This smart and user-friendly system increases the chances of passing recruiter attention. By automating resume creation and streamlining job applications, Dynamic Resume Manager empowers users to achieve their career goals efficiently and effectively.
            """.trimIndent()

        aboutText.text = aboutUs
    }
}