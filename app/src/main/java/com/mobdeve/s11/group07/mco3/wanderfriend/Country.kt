package com.mobdeve.s11.group07.mco3.wanderfriend

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("name")
    val name: Name,
    @SerializedName("flags")
    val flags: Flags
)

data class Name(
    @SerializedName("common")
    val common: String
)

data class Flags(
    @SerializedName("png")
    val png: String
)
