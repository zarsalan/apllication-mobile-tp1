package com.example.tp1_epicerie

import android.app.Application

class GroceryApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // On initialise le graph
        Graph.provide(this)
    }
}