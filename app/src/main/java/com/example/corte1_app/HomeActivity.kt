package com.example.corte1_app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.corte1_app.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnGoToDetails = findViewById<Button>(R.id.btnGoToDetails)
        val btnGoToSettings = findViewById<Button>(R.id.btnGoToSettings)

        btnGoToDetails.setOnClickListener {
            startActivity(Intent(this, DetailsActivity::class.java))
        }

        btnGoToSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
