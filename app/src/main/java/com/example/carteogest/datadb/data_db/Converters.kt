package com.example.carteogest.datadb.data_db  // Ajuste para o seu pacote

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun listToJson(value: List<String>?): String? {
        return gson.toJson(value)  // Retorna null se value for null, ou JSON como "[]" se vazio
    }

    @TypeConverter
    fun jsonToList(value: String?): List<String> {
        if (value == null || value.isEmpty()) {
            return emptyList()  // Trata null ou vazio como lista vazia
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}