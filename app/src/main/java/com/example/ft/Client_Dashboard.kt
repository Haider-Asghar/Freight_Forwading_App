package com.example.ft

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Client_Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_dashboard)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvCompanyName = findViewById<TextView>(R.id.tvCompanyName)
        val companyName = intent.getStringExtra("COMPANY_NAME")
        tvCompanyName.text = companyName

        val cvOthers = findViewById<CardView>(R.id.cvOthers)
        cvOthers.setOnClickListener{
            val dialog = OtherIconsDialogFragment.newInstance(companyName ?: "")
            dialog.show(supportFragmentManager, "OtherIconsDialog")
        }

        val cvRates = findViewById<CardView>(R.id.cvRates)
        cvRates.setOnClickListener{
            val intent = Intent(this, Client_Rates_Query::class.java)
            startActivity(intent)
        }

        val cvStatusUpdates = findViewById<CardView>(R.id.cvStatusUpdates)
        cvStatusUpdates.setOnClickListener{
            val intent = Intent(this, View_Statuses::class.java)
            startActivity(intent)
        }

        val clientChats = findViewById<ImageView>(R.id.chatBar)
        clientChats.setOnClickListener{
            val intent = Intent(this, Client_Chats::class.java)
            intent.putExtra("COMPANY_NAME", companyName)
            startActivity(intent)
        }

        val chatBot = findViewById<ImageView>(R.id.chatBot)
        chatBot.setOnClickListener{
            val intent = Intent(this, Chat_Bot::class.java)
            startActivity(intent)
        }

        val docScanner = findViewById<ImageView>(R.id.docScanner)
        docScanner.setOnClickListener{
            val intent = Intent(this, Scanner_Activity::class.java)
            intent.putExtra("role", "client")
            intent.putExtra("companyName", companyName)
            startActivity(intent)
        }

        val clientProfile = findViewById<ImageView>(R.id.profile)
        clientProfile.setOnClickListener{
            val intent = Intent(this, Client_Profile::class.java)
            startActivity(intent)
        }

        val clientInvoices = findViewById<CardView>(R.id.cvInvoices)
        clientInvoices.setOnClickListener{
            val intent = Intent(this, Client_Invoices::class.java)
            startActivity(intent)
        }

        val cvDeliveryOrder = findViewById<CardView>(R.id.cvDeliveryOrder)
        cvDeliveryOrder.setOnClickListener{
            val intent = Intent(this, Client_Do::class.java)
            startActivity(intent)
        }

        val cvTracking  = findViewById<CardView>(R.id.cvTracking)
        cvTracking.setOnClickListener{
            val intent = Intent(this, Tracking_Activity::class.java)
            startActivity(intent)
        }
    }
}