package com.example.medicationreminderapp

import android.app.Application
import com.google.firebase.FirebaseApp
//this class is used to initialize the app and firebase, this class has global scope
class MedicationReminderApp : Application(){
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        FirebaseApp.initializeApp(this)
        scheduleDailyAlarm(applicationContext,13,30,"Had lunch? now its medicine time")
        scheduleDailyAlarm(applicationContext,20,30,"Time to take your night medicine")
        scheduleDailyAlarm(applicationContext,8,30,"Good Morning! Time to take your morning medicine")
    }


}