package com.example.yandexnotes.di

import android.content.Context
import android.provider.Settings

object DeviceIdProvider {
    private const val PREFS_KEY = "device_prefs_key"
    private const val DEVICE_ID_KEY = "device_id_key"

    fun getDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        return prefs.getString(DEVICE_ID_KEY, null) ?: run {
            val newId = generateDeviceId()
            prefs.edit().putString(DEVICE_ID_KEY, newId).apply()
            newId
        }
    }

    private fun generateDeviceId(): String {
        return java.util.UUID.randomUUID().toString()
    }
}