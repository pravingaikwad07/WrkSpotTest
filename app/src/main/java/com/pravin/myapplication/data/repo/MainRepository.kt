package com.pravin.myapplication.data.repo

import com.pravin.myapplication.NwResponseState
import com.pravin.myapplication.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getCountriesList(): List<Country>

}