package com.example.yandexnotes.di

import android.content.Context
import android.provider.Settings

object DeviceIdProvider {
    private const val PREFS_KEY = "device_prefs"
    private const val DEVICE_ID_KEY = "device_id"

    fun getDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        return prefs.getString(DEVICE_ID_KEY, null) ?: run {
            val newId = generateDeviceId(context)
            prefs.edit().putString(DEVICE_ID_KEY, newId).apply()
            newId
        }
    }

    private fun generateDeviceId(context: Context): String {
        // Пробуем получить Android ID
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        // Если Android ID доступен и не является дефолтным
        if (!androidId.isNullOrEmpty() && androidId != "9774d56d682e549c") {
            return androidId
        }

        // Генерируем случайный UUID as fallback
        return java.util.UUID.randomUUID().toString()
    }
}