package com.example.medicationreminderapp

import android.content.Context
import androidx.room.Room
import com.example.medicationreminderapp.data.MedsDataBase
import com.example.medicationreminderapp.data.MedsRepository
//graph object is used to provide the database and repository to the app, it necessary to have a global scope and garph
//also maintains the MVVM architecture
object Graph {
    lateinit var database : MedsDataBase

    val medsRepository by lazy {
        MedsRepository(database.medsDao())
    }

    fun provide(context : Context){
        database = Room.databaseBuilder(
            context,
            MedsDataBase::class.java,
            "medicines.db"
        ).build()
    }

}