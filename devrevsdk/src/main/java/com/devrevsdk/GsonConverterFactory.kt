package com.devrevsdk

import com.google.gson.Gson

class GsonConverterFactory(private val gson: Gson) : ConverterFactory {
    override fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }

    override fun <T> toJson(obj: T): String {
        return gson.toJson(obj)
    }
}
