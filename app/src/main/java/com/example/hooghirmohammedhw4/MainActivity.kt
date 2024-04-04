package com.example.hooghirmohammedhw4

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName

class MainActivity : AppCompatActivity() {

    // Global variables for editText keyword & editText city & search button
    private lateinit var editTextKeyword: EditText
    private lateinit var editTextCity: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize editText's & button
        editTextKeyword = findViewById(R.id.editTextKeyword)
        editTextCity = findViewById(R.id.editTextCity)
        searchButton = findViewById(R.id.searchButton)

        // Set action listener for hiding keyboard on pressing 'Enter'
        editTextCity.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event?.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER) {
                // Hide the keyboard
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                // Perform the search when 'Enter' is pressed
                searchButton.performClick()
                true // Consume the event
            } else {
                false // Let the system handle the event
            }
        }

        // Search Button On Click Listener
        searchButton.setOnClickListener {
            val keyword = editTextKeyword.text.toString().trim()
            val city = editTextCity.text.toString().trim()
            when {
                keyword.isEmpty() -> showAlert(
                    "Search term missing",
                    "Search term cannot be empty. Please enter a search term."
                )

                city.isEmpty() -> showAlert(
                    "Location missing",
                    "City cannot be empty. Please enter a city."
                )

                else -> performSearch(keyword, city)
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OKAY") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun performSearch(keyword: String, city: String) {

    }
}