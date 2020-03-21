package fr.legrand.runalytics.data.utils

import com.google.gson.Gson

inline fun <reified T> Gson.fromJson(json: String): T = this.fromJson(json, T::class.java)