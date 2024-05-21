package com.example.devrev.utils
import com.example.devrev.models.Genre
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {

    fun convertDate(inputDate: String): String {
        if (inputDate.isEmpty())
            return ""
        // Define the input date format
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Parse the input date string
        val date: Date = inputFormat.parse(inputDate)!!

        // Define the output date format
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        // Format the parsed date to the desired format
        return outputFormat.format(date)
    }
    fun commaSeparatedString(list: ArrayList<Genre>?):String{
        val builder = StringBuilder()
        if (list.isNullOrEmpty())
            return builder.toString()
        for (i in 0..<list.size){
            builder.append(list[i].name)
            if (i< list.size-2){
                builder.append(",")
            }
        }
        return builder.toString()
    }

}