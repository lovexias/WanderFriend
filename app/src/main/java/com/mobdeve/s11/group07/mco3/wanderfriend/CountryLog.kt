package com.mobdeve.s11.group07.mco3.wanderfriend

// Log model includes countryId to associate logs with specific countries
data class CountryLog(
    val logId: Long,  // Primary Key
    val countryId: Long,  // Foreign Key referencing Country
    val date: String,
    val caption: String,
    val photoUri: String
)
