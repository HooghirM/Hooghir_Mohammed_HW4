package com.example.hooghirmohammedhw4

//data class EventResponse (val events: List<UsageEvents.Event>)


import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class TicketResponse(private val users: ArrayList<Event>) : RecyclerView.Adapter<TicketResponse.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // This class will represent a single row in our recyclerView list
        // This class also allows caching views and reuse them
        // Each MyViewHolder object keeps a reference to 3 view items in our row_item.xml file
        val ticketImage = itemView.findViewById<ImageView>(R.id.imageViewTicket)
        val ticketName = itemView.findViewById<TextView>(R.id.textViewTicket)
        val ticketLocation = itemView.findViewById<TextView>(R.id.textViewLocation)
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

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = users[position] // Assuming `users` is now a list of `Event` objects
        val highestQualityImage = currentItem.images.maxByOrNull { it.imageUrl.length } // Simplified for illustration
        Glide.with(holder.itemView.context) // Assuming using Glide for image loading
            .load(highestQualityImage?.imageUrl)
            .into(holder.ticketImage)

        holder.ticketName.text = currentItem.eventName
        val venue = currentItem.venuesEmbedded.venues.firstOrNull() // Assuming taking the first venue
        holder.ticketLocation.text = "${venue?.city?.cityName}, ${venue?.state?.stateName}"
        holder.ticketAddress.text = venue?.address?.line1
        holder.ticketDate.text = "${currentItem.dates.start.localDate} at ${currentItem.dates.start.localTime}"

        holder.priceRange.text = currentItem.priceRanges?.let {
            if (it.isNotEmpty()) "Price Range: $${it[0].min} - $${it[0].max}" else "Price Not Available"
        } ?: "Price Not Available"

        holder.seeTicket.text = "See Tickets"
        holder.seeTicket.setOnClickListener {
            // Assuming context is available
            val context = holder.itemView.context
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(venue?.ticketLink))
            context.startActivity(intent)
        }
    }



//    // Get the context for glide
//        val context = holder.itemView.context

        // Load the image from the url using Glide library
        //Glide.with(context)     // DO I need a class for GLIDE???
            //.load(currentItem.images)
//            .placeholder(R.drawable.baseline_account_circle_24) // In case the image is not loaded show this placeholder image
//            .circleCrop() // optional - Circle image with rounded corners
            //.into(holder.ticketImage)

    }