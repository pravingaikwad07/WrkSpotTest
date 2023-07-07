package com.pravin.myapplication

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pravin.myapplication.data.CountryListState
import com.pravin.myapplication.domain.CountryListUseCase
import com.pravin.myapplication.domain.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val countryUse: CountryListUseCase) : ViewModel() {

    /*EST - -05:00
HST - -10:00
MST - -07:00
ACT - Australia/Darwin
AET - Australia/Sydney
AGT - America/Argentina/Buenos_Aires
ART - Africa/Cairo
AST - America/Anchorage
BET - America/Sao_Paulo
BST - Asia/Dhaka
CAT - Africa/Harare
CNT - America/St_Johns
CST - America/Chicago
CTT - Asia/Shanghai
EAT - Africa/Addis_Ababa
ECT - Europe/Paris
IET - America/Indiana/Indianapolis
IST - Asia/Kolkata
JST - Asia/Tokyo
*/
    private val timeZones = listOf("IST", "EST", "HST", "BST", "ACT")
    var currentTimeZoneIndex = 0

    val changingTime = MutableLiveData<String>()
    val filterData = MutableLiveData<List<Country>>()
    private var countriesList: List<Country> = ArrayList()

    private val _countries = MutableStateFlow(CountryListState())
    var countries: StateFlow<CountryListState> = _countries

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime() = viewModelScope.launch {

        while (isActive) {
            if (currentTimeZoneIndex >= timeZones.size - 1) {
                currentTimeZoneIndex = 0
            } else {
                currentTimeZoneIndex += 1
            }

            val tz = timeZones[currentTimeZoneIndex]
            val timeZone = TimeZone.getTimeZone(tz)
            val c = Calendar.getInstance()
            c.timeZone = timeZone

            val articlePublishedZonedTime = ZonedDateTime.ofInstant(c.toInstant(), ZoneId.of(tz))

            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss Z")
            val formattedString: String = articlePublishedZonedTime.format(formatter)
            changingTime.value = tz + "\n" + formattedString

            Log.d(
                "TimeZone",
                "getCurrentTime: ${timeZone.id} || ${c.timeZone.id} || $tz >> " + c.time.toString()
            )

            delay(2000)
        }

    }


    fun fetchCountries() = viewModelScope.launch {
        countryUse().collect {
            when (it) {
                is NwResponseState.Error -> {
                    _countries.value =
                        CountryListState(error = it.exception.message ?: "Something went wrong")
                }

                is NwResponseState.Loaded -> {
                    countriesList = it.data
                    _countries.value = CountryListState(data = it.data)
                }

                is NwResponseState.Loading -> {
                    _countries.value = CountryListState(isLoading = true)

                }
            }
        }


    }

    fun applyFilter(type: Int) {

        val list = ArrayList<Country>()
        if (type != -1) {
            countriesList.forEach {
                if (!it.population.isNullOrEmpty()) {
                    if (type == 1) {
                        if (it.population.toInt() < 1000000) {
                            list.add(it)
                        }
                    } else if (type == 5) {
                        if (it.population.toInt() < 5000000) {
                            list.add(it)
                        }
                    } else if (type == 10) {
                        if (it.population.toInt() < 10000000) {
                            list.add(it)
                        }
                    }
                }
            }

            Log.d("Filter-","type $type & Size is: "+list.size)
            filterData.value = list
        } else {
            filterData.value = countriesList
        }


    }

}