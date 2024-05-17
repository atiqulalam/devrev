package com.devrevsdk

interface ConverterFactory {
    fun <T> fromJson(json: String, clazz: Class<T>): T
    fun <T> toJson(obj: T): String
}
