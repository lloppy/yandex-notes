package com.example.yandexnotes

import android.app.Application
import com.example.yandexnotes.di.AppContainer
import com.example.yandexnotes.di.AppDataContainer

class YandexNotesApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}