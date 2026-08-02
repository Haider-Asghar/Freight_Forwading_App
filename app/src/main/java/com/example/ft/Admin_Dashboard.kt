package com.example.ft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Admin_Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvAdminName = findViewById<TextView>(R.id.tvAdminName)
        val adminName = intent.getStringExtra("adminName")
        tvAdminName.text = adminName

        val cvOthersAdmin = findViewById<CardView>(R.id.cvOthersAdmin)
        cvOthersAdmin.setOnClickListener{
            val dialog = OtherIconsAdminDialogFragment()
            dialog.show(supportFragmentManager,"OtherIconsAdminDialog")
        }

        val profileAdmin = findViewById<ImageView>(R.id.profileAdmin)
        profileAdmin.setOnClickListener{
            val intent = Intent(this, AdminProfile::class.java)
            startActivity(intent)
        }

        val cvRatesManagement = findViewById<CardView>(R.id.cvRatesManagement)
        cvRatesManagement.setOnClickListener{
            val intent = Intent(this, Rates_Manager::class.java)
            startActivity(intent)
        }

        val cvStatusUpdateAdmin = findViewById<CardView>(R.id.cvStatusUpdatesAdmin)
        cvStatusUpdateAdmin.setOnClickListener{
            val intent = Intent(this, Status_Updates::class.java)
            startActivity(intent)
        }

        val chatBarAdmin = findViewById<ImageView>(R.id.chatBarAdmin)
        chatBarAdmin.setOnClickListener{
            val intent = Intent(this, Admin_Chats::class.java)
            startActivity(intent)
        }

        val docScannerAdmin = findViewById<ImageView>(R.id.docScannerAdmin)
        docScannerAdmin.setOnClickListener{
            val intent = Intent(this, Scanner_Activity::class.java)
            startActivity(intent)
        }

        val cvInvoicesAdmin = findViewById<CardView>(R.id.cvInvoicesAdmin)
        cvInvoicesAdmin.setOnClickListener{
            val intent = Intent(this, Admin_Invoices::class.java)
            startActivity(intent)
        }

        val cvDeliveryOrderAdmin = findViewById<CardView>(R.id.cvDeliveryOrderAdmin)
        cvDeliveryOrderAdmin.setOnClickListener{
            val intent = Intent(this, Admin_Do::class.java)
            startActivity(intent)
        }

        val cvTrackingAdmin = findViewById<CardView>(R.id.cvTrackingAdmin)
        cvTrackingAdmin.setOnClickListener{
            val intent = Intent(this, Tracking_Activity::class.java)
            startActivity(intent)
        }
    }
}