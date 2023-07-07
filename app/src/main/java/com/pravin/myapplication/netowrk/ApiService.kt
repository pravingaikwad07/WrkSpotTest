package com.pravin.myapplication.netowrk

import com.pravin.myapplication.domain.model.Country
import retrofit2.http.GET

interface ApiService {

    @GET("/api/countries")
    fun getCountries(): List<Country>
}