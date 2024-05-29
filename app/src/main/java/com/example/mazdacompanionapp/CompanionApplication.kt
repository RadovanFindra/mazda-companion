package com.example.mazdacompanionapp

import android.app.Application
import com.example.mazdacompanionapp.data.AppDataContainer

class CompanionApplication : Application() {
    lateinit var container: AppDataContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}