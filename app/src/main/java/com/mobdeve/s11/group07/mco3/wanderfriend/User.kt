package com.mobdeve.s11.group07.mco3.wanderfriend

data class User(
    val id: Long = 0,
    val name: String,
    val age: Int,
    val country: String,
    val traveledCountries: List<Country>
)
