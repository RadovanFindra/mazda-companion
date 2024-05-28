package com.example.mazdacompanionapp

import android.app.Application
import com.example.mazdacompanionapp.data.AppContainer
import com.example.mazdacompanionapp.data.AppDataContainer

class CompanionAplication : Application() {
    var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}