package com.example.bin

data class Bin(
    val id: Long,
    val bin: String,
    val numberLength: Int = 0,
    val scheme: String = "",
    val type: String = "",
    val brand: String = "",
    val country: Country = Country(),
    val bank: Bank = Bank(),
)

data class Country(
    val numeric: String = "",
    val alpha2: String = "",
    val name: String = "",
    val currency: String = "",
    val latitude: Int = 0,
    val longitude: Int = 0,
)

data class Bank(
    val name: String = "",
    val url: String = "",
    val phone: String = "",
    val city: String = "",
)