package com.pravin.myapplication.data

import com.pravin.myapplication.data.model.Country

data class CountryListState(
    val isLoading: Boolean = false,
    val data: List<Country> = emptyList(),
    val error: String = ""
)
