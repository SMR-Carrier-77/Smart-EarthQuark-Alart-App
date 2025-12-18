package com.example.smartearthquarkalart.data.models

data class Earthquake_Data_Class(
    val id: Int,
    val event_id: String,
    val magnitude: Float,
    val place: String,
    val event_time: Long,
    val latitude: Double,
    val longitude: Double,
    val depth: String,
    val title: String,
    val tsunami: Int,
    val magType: String,
    val sig: Int
)
