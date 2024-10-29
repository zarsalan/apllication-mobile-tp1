package com.example.tp1_epicerie

import android.app.Application

// On crée une classe GroceryApp qui hérite de Application et qui va nous permettre d'initialiser le graph
class GroceryApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // On initialise le graph
        Graph.provide(this)
    }
}