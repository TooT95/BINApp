package com.example.bin

import android.app.Application
import android.content.Context
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BinRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    suspend fun getBinList(): List<Bin> {
        return suspendCoroutine { continuation ->
            val result = mutableListOf<Bin>()
            dbHelper.getAllBin().forEach {
                result.add(getBinFromJson(it[DatabaseHelper.TableBin.id] as Long,
                    it[DatabaseHelper.TableBin.binId] as String,
                    it[DatabaseHelper.TableBin.responseString] as String))
            }
            continuation.resume(result)
        }
    }

    suspend fun getBinData(bin: String): Bin {
        return suspendCoroutine { continuation ->
            Networking.api.bin(bin).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string() ?: ""
                        val id = dbHelper.addBin(bin, responseBody)
                        continuation.resume(getBinFromJson(id, bin, responseBody))
                    } else {
                        continuation.resume(Bin(id = 0, bin = bin))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private fun getBinFromJson(id: Long, bin: String, jsonResponse: String): Bin {
        val jsonObject = JSONObject(jsonResponse)
        val scheme = getIfNullString(jsonObject, KEY_SCHEME)
        val type = getIfNullString(jsonObject, KEY_TYPE)
        val brand = getIfNullString(jsonObject, KEY_BRAND)
        val country = getCountryFromJson(jsonObject)
        val bank = getBankFromJson(jsonObject)
        val numberJson = jsonObject.getJSONObject(KEY_NUMBER)
        val length = getIfNullInt(numberJson, KEY_NUMBER_LENGTH)
        return Bin(id = id, bin = bin,
            numberLength = length,
            scheme = scheme,
            type = type,
            brand = brand,
            country = country,
            bank = bank)
    }

    private fun getIfNullString(jsonObject: JSONObject, key: String): String {
        return if (jsonObject.isNull(key)) {
            ""
        } else {
            jsonObject.getString(key)
        }
    }

    private fun getIfNullInt(jsonObject: JSONObject, key: String): Int {
        return if (jsonObject.isNull(key)) {
            0
        } else {
            jsonObject.getInt(key)
        }
    }

    private fun getCountryFromJson(jsonObject: JSONObject): Country {
        val countryJson = jsonObject.getJSONObject(KEY_COUNTRY)
        val numeric = getIfNullString(countryJson, KEY_NUMERIC)
        val alpha2 = getIfNullString(countryJson, KEY_ALPHA2)
        val name = getIfNullString(countryJson, KEY_NAME)
        val currency = getIfNullString(countryJson, KEY_CURRENCY)
        val latitude = getIfNullInt(countryJson, KEY_LANG)
        val longitude = getIfNullInt(countryJson, KEY_LONG)
        return Country(numeric, alpha2, name, currency, latitude, longitude)
    }

    private fun getBankFromJson(jsonObject: JSONObject): Bank {
        if (jsonObject.isNull(KEY_BANK)) {
            return Bank()
        }
        val bankJson = jsonObject.getJSONObject(KEY_BANK)
        val name = getIfNullString(bankJson, KEY_NAME)
        val url = getIfNullString(bankJson, KEY_URL)
        val phone = getIfNullString(bankJson, KEY_PHONE)
        val city = getIfNullString(bankJson, KEY_CITY)
        return Bank(name, url, phone, city)
    }

    companion object {
        private const val KEY_NUMBER = "number"
        private const val KEY_NUMBER_LENGTH = "length"
        private const val KEY_SCHEME = "scheme"
        private const val KEY_TYPE = "type"
        private const val KEY_BRAND = "brand"
        private const val KEY_COUNTRY = "country"
        private const val KEY_BANK = "bank"
        private const val KEY_NUMERIC = "numeric"
        private const val KEY_ALPHA2 = "alpha2"
        private const val KEY_NAME = "name"
        private const val KEY_CURRENCY = "currency"
        private const val KEY_LANG = "latitude"
        private const val KEY_LONG = "longitude"
        private const val KEY_URL = "url"
        private const val KEY_PHONE = "phone"
        private const val KEY_CITY = "city"
    }
}