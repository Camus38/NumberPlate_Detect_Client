package com.example.numberplate_detect_client

import android.app.Application
import android.content.Context

class getContext : Application() {

    init {
        instance = this
    }
    companion object{
        lateinit var instance :getContext
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }
}