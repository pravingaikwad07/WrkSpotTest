package com.pravin.myapplication.data.repo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pravin.myapplication.data.model.Country
import com.pravin.myapplication.netowrk.ApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Type
import javax.inject.Inject

class MainRepoImpl @Inject constructor(
    @ApplicationContext val context: Context, private val apiService: ApiService
) : MainRepository {


    private fun <T> getList(jsonArray: String?, clazz: Class<T>?): List<T>? {
        val typeOfT: Type = TypeToken.getParameterized(MutableList::class.java, clazz).type
        return Gson().fromJson(jsonArray, typeOfT)
    }

    private fun Context.loadJSONFromAssets(fileName: String): String {
        return this.assets.open(fileName).bufferedReader().use { reader ->
            reader.readText()
        }
    }

    override suspend fun getCountriesList(): List<Country> {

        // to laod data from api
       // val dataApi = apiService.getCountries()

        val json = context.loadJSONFromAssets("countries.json")
        val dataFromJson = getList(json, Country::class.java) ?: emptyList()
        return dataFromJson
    }


}

