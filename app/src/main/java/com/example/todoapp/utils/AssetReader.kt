package com.example.todoapp.utils

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class AssetReader(private val context: Context) {
    fun read(filename: String): JSONObject {
        val data = BufferedReader(InputStreamReader(context.assets.open(filename))).readText()
        return JSONObject(data)
    }
}