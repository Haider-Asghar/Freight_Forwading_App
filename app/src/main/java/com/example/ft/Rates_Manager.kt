package com.example.ft

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Rates_Manager : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rates_manager)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgIvBack = findViewById<ImageView>(R.id.img_ivBack)
        imgIvBack.setOnClickListener{
            finish()
        }

        val rlAddCountry = findViewById<RelativeLayout>(R.id.rl_addCountry)
        rlAddCountry.setOnClickListener{
            val intent = Intent(this, Add_Country::class.java)
            startActivity(intent)
        }
        val rlAddRates = findViewById<RelativeLayout>(R.id.rl_addRates)
        rlAddRates.setOnClickListener{
            val intent = Intent(this, Add_Rates::class.java)
            startActivity(intent)
        }
        val rlUpdateRates = findViewById<RelativeLayout>(R.id.rl_updateRates)
        rlUpdateRates.setOnClickListener{
            val intent = Intent(this, Update_Rates::class.java)
            startActivity(intent)
        }
        val rlRemoveCountry = findViewById<RelativeLayout>(R.id.rl_removeCountry)
        rlRemoveCountry.setOnClickListener{
            val intent = Intent(this, Remove_Country::class.java)
            startActivity(intent)
        }
        val rlRemoveCity = findViewById<RelativeLayout>(R.id.rl_removeCity)
        rlRemoveCity.setOnClickListener{
            val intent = Intent(this, Remove_City::class.java)
            startActivity(intent)
        }
        val rlRemoveRates = findViewById<RelativeLayout>(R.id.rl_removeRates)
        rlRemoveRates.setOnClickListener{
            val intent = Intent(this, Remove_Rates::class.java)
            startActivity(intent)
        }
    }
}