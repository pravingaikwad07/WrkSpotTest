package com.pravin.myapplication


sealed class NwResponseState<out R> {
    data class Loading<T>(val data: T? = null) : NwResponseState<T>()
    data class Loaded<out T>(val data: T) : NwResponseState<T>()
    data class Error(val exception: Exception) : NwResponseState<Nothing>()
}


