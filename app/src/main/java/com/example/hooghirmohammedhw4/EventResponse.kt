package com.example.hooghirmohammedhw4

import android.app.usage.UsageEvents

//data class EventResponse (val events: List<UsageEvents.Event>)


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide                     // Error on bumptech
import com.google.firebase.firestore.auth.User


class UsersAdapter(private val users: ArrayList<Event>) : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder (itemView) {
        // This class will represent a single row in our recyclerView list
        // This class also allows caching views and reuse them
        // Each MyViewHolder object keeps a reference to 3 view items in our row_item.xml file
        val ticketImage = itemView.findViewById<ImageView>(R.id.imageViewTicket)
        val ticketName = itemView.findViewById<TextView>(R.id.textViewTicket)
        val ticketLocation= itemView.findViewById<TextView>(R.id.textViewLocation)
        val ticketAddress = itemView.findViewById<TextView>(R.id.textViewAddress)
        val ticketDate = itemView.findViewById<TextView>(R.id.textViewDate)
        val priceRange = itemView.findViewById<TextView>(R.id.textViewPrice)
        val seeTicket = itemView.findViewById<Button>(R.id.buttonBuyTicket)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate a layout from our XML (row_item.XML) and return the holder
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val currentItem = users[position]
        holder.ticketImage.imageAlpha = currentItem.ticketImage
        holder.ticketName.text = "${currentItem.ticketName.title} "
        holder.ticketLocation.text = currentItem.ticketLocation
        holder.ticketAddress.text = currentItem.ticketAddress
        holder.ticketDate.text = currentItem.ticketDate
        holder.priceRange.text = currentItem.priceRange
        holder.seeTicket.text = currentItem.seeTicket   // "See Tickets"???
        holder.seeTicket.setOnClickListener {
            /* When button is clicked, opens the ticket page link
            obtained from the API in the device's web browser */
        }


        // Get the context for glide
        val context = holder.itemView.context

        // Load the image from the url using Glide library
        Glide.with(context)     // DO I need a class for GLIDE???
            .load(currentItem.imageUrl.medium)
//            .placeholder(R.drawable.baseline_account_circle_24) // In case the image is not loaded show this placeholder image
//            .circleCrop() // optional - Circle image with rounded corners
            .into(holder.ticketImage)

    }

    override fun getItemCount(): Int {
        // Return the size of your dataset (invoked by the layout manager)
        return users.size
    }

}