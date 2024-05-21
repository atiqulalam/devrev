package com.example.devrev.models

data class ApiResponse(val page:Int,
                       val results:ArrayList<Movie>? = ArrayList(),
                       val total_pages:Int = 0,
                       val total_results:Int = 0)