package com.pravin.myapplication.data

import com.pravin.myapplication.NwResponseState
import com.pravin.myapplication.data.model.Country
import com.pravin.myapplication.data.repo.MainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountryListUseCase @Inject constructor(
    private val repository: MainRepository
) {
    operator fun invoke(): Flow<NwResponseState<List<Country>>> = flow {
        try {
            emit(NwResponseState.Loading())
            val images = repository.getCountriesList()
            // delay to showcase the Loading using states. (mocking delay as if we are getting response from server/api call
            delay(2000)
            emit(NwResponseState.Loaded(images))

        } catch (e: Exception) {
            emit(NwResponseState.Error(e))
        }
    }
}