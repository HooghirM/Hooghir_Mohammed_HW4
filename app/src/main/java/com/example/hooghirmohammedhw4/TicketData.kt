package com.example.hooghirmohammedhw4

data class TicketData(
    val _embedded: EmbeddedData
)

data class EmbeddedData(
    val events: List<Event>
)

class Event (
    val name: String,
    val images: List<Image>,
    //val start: Start,
    //val venues: List<Venue>,
    //val priceRanges: List<PriceRange>
)

class PriceRange (val min: Float, val max: Float)

class Venue (val name: String, val city: String, val state: String, val address: String, val url: String)

class Start (val dateTime: String)

class Image (val url: String)