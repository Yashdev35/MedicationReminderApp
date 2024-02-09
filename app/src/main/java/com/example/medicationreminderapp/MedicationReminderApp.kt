package com.example.medicationreminderapp

import android.app.Application
import com.google.firebase.FirebaseApp

class MedicationReminderApp : Application(){
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        FirebaseApp.initializeApp(this)
    }


}