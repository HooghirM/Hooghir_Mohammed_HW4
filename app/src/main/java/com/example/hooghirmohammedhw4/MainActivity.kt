package com.example.hooghirmohammedhw4

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var API_KEY = "NpX8VLS2UGJeuzpUeAGf70SfzAJuSP8Z"
    private var BASE_URL = "https://app.ticketmaster.com/discovery/v2/"
    private val TAG = "MainActivity"

    private lateinit var editTextKeyword: EditText
    private lateinit var editTextCity: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userList = ArrayList<Event>()
        val adapter = TicketResponse(userList)

        editTextKeyword = findViewById(R.id.editTextKeyword)
        editTextCity = findViewById(R.id.editTextCity)
        searchButton = findViewById(R.id.searchButton)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.visibility = View.GONE

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ticketMasterAPI = retrofit.create(TicketMasterApi::class.java)

        editTextCity.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event?.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                searchButton.performClick()
                true
            } else {
                false
            }
        }

        searchButton.setOnClickListener {
            val keyword = editTextKeyword.text.toString().trim()
            val city = editTextCity.text.toString().trim()
            when {
                keyword.isEmpty() && city.isEmpty() -> showAlert(
                    "Search term and city missing",
                    "Search term and city cannot be empty. Please enter both."
                )

                keyword.isEmpty() -> showAlert(
                    "Search term missing",
                    "Search term cannot be empty. Please enter a search term."
                )

                city.isEmpty() -> showAlert(
                    "Location missing",
                    "City cannot be empty. Please enter a city."
                )

                else -> {
                    userList.clear()
                    ticketMasterAPI.searchEvents(API_KEY, keyword, city)
                        .enqueue(object : Callback<TicketData> {
                            override fun onResponse(
                                call: Call<TicketData>,
                                response: Response<TicketData>
                            ) {
                                if (response.isSuccessful && response.body() != null) {
                                    val events = response.body()!!.embedded.events
                                    userList.addAll(events)
                                    adapter.notifyDataSetChanged()
                                    recyclerView.visibility = View.VISIBLE
                                } else {
                                    showAlert(
                                        "No Results Found",
                                        "No events found for the provided search terms. Please try again."
                                    )
                                }
                            }

                            override fun onFailure(call: Call<TicketData>, t: Throwable) {
                                Log.e(TAG, "API call failed", t)
                                showAlert(
                                    "Error",
                                    "Failed to fetch events: ${t.localizedMessage}"
                                )
                            }
                        })
                }
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
}


//  “date,asc”
//        ticketMasterAPI.searchEvents(API_KEY, "Music", "Hartford").enqueue(object
//            : Callback<TicketData> {
//
//            override fun onResponse(call: Call<TicketData>, response: Response<TicketData>) {
//                Log.d(TAG, "onResponse: $response")
//
//                if (response.isSuccessful && response.body() != null) {
//                    val body = response.body()!!
//                    // Use the body
//                    Log.d(TAG, "onResponse: $body")
//
//                    // Clear the userList before adding new items to avoid showing stale data
//                    userList.clear()
//
//                    // Assuming '_embedded' and 'events' are correctly parsed and not null
//                    userList.addAll(body.embedded.events)
//
//                    // Notify the adapter that the data has changed
//                    adapter.notifyDataSetChanged()
//
//                    // Make sure to set the RecyclerView visibility to VISIBLE
////                    recyclerView.visibility = View.VISIBLE
//                } else {
//                    // Handle the case where the response is not successful or the body is null
//                    Log.w(TAG, "Response was not successful or was empty")
//                    // Optionally, show an alert dialog or a toast to inform the user
//                }
//
//
//                // Get access to the body with response.body().
//                val body = response.body()
//                if (body == null) {
//                    Log.w(TAG, "Valid response was not received")
//                    return
//                }
//                // Clear userList before adding new items from the API response to avoid showing state data
//                userList.clear()
//                // Add all items from the API response (parsed using Gson) to the user list
//                userList.addAll(body.embedded.events)
//                // Update the adapter with the new data
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onFailure(call: Call<TicketData>, t: Throwable) {
//                Log.d(TAG, "onFailure : $t")
//            }
//
//        })