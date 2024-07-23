package com.mobdeve.s11.group07.mco3.wanderfriend

import retrofit2.Call
import retrofit2.http.GET

interface RestCountriesApi {
    @GET("v3.1/all?fields=name,flags")
    fun getAllCountries(): Call<List<Country>>
}
