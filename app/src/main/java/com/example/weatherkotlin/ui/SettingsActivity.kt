package com.example.weatherkotlin.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherkotlin.R
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner


class SettingsActivity : AppCompatActivity() {
    private lateinit var spinner_tempC: MaterialSpinner
    private lateinit var spinner_Speed: MaterialSpinner
    private lateinit var btn_Back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        spinner_tempC = findViewById(R.id.spinner_tempC)
        spinner_Speed = findViewById(R.id.spinner_Speed)
        btn_Back = findViewById(R.id.btn_Back)
        spinner_tempC.setItems("°C", "°F")
        spinner_Speed.setItems("Km/h", "mi/h")
        spinner_tempC.setBackgroundResource(R.color.transparent);
        spinner_Speed.setBackgroundResource(R.color.transparent);
        spinner_tempC.setOnItemSelectedListener { view, position, id, item ->
            saveSetting("selectedTemp", item.toString())
            Snackbar.make(
                view,
                "Clicked $item",
                Snackbar.LENGTH_LONG
            ).show()
        }
        spinner_Speed.setOnItemSelectedListener { view, position, id, item ->
            saveSetting("selectedSpeed", item.toString())
            Snackbar.make(
                view,
                "Clicked $item",
                Snackbar.LENGTH_LONG
            ).show()
        }
        btn_Back.setOnClickListener {
            onBackPressed()
            finish()
        }



// Khôi phục giá trị đã lưu trong SharedPreferences
        var selectedTemp = getSetting("selectedTemp")
        var selectedSpeed = getSetting("selectedSpeed")
        if (selectedTemp.equals("")){
            selectedTemp="°F"
        }
        if (selectedSpeed.equals(""))
            selectedSpeed="mi/h"
        spinner_tempC.selectedIndex = spinner_tempC.getItems<String>().indexOf(selectedTemp)

        spinner_Speed.selectedIndex = spinner_Speed.getItems<String>().indexOf(selectedSpeed)
    }


    private fun saveSetting(key: String, value: String) {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    private fun getSetting(key: String): String {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "") ?: ""
    }


}