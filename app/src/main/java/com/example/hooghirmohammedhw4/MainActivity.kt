package com.example.hooghirmohammedhw4

import android.bluetooth.BluetoothHidDevice
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.auth.User
import com.google.gson.annotations.SerializedName
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var API_KEY = "NpX8VLS2UGJeuzpUeAGf70SfzAJuSP8Z"
    private var BASE_URL = "https://app.ticketmaster.com/discovery/v2/"

    // Global variables for editText keyword & editText city & search button
    private lateinit var editTextKeyword: EditText
    private lateinit var editTextCity: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Define an array to store a list of users -- this will be the list storing information
        // coming from the API
        val userList = ArrayList<Event>()

        // specify a viewAdapter for the dataset
        val adapter = UsersAdapter(userList)

        // Initialize editText's & button
        editTextKeyword = findViewById(R.id.editTextKeyword)
        editTextCity = findViewById(R.id.editTextCity)
        searchButton = findViewById(R.id.searchButton)

        // Creating a Retrofit instance with specified base URL and Gson converter factory
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // Set the base URL for the REST API
            .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter factory for JSON serialization/deserialization
            .build() // Build the Retrofit instance

        val ticketMasterAPI = retrofit.create(TicketMasterApi::class.java)

        //  “date,asc”
        ticketMasterAPI.searchEvents(API_KEY, "music","hartford",).enqueue(object
        : Callback<Event> {

            override fun onResponse(call: Call<Event>, response: Response<EventResponse>) {
                Log.d(TAG, "onResponse: $response")

                // Get access to the body with response.body().
                val body = response.body()
                if (body == null) {
                    Log.w(TAG, "Valid response was not received")
                    return
                }



                // Add all items from the API response (parsed using Gson) to the user list
                userList.addAll(body.results)
                // Update the adapter with the new data
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Log.d(TAG, "onFailure : $t")
            }

        })

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