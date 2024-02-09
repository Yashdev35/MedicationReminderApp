package com.example.medicationreminderapp

import android.content.Context
import androidx.room.Room
import com.example.medicationreminderapp.data.MedsDataBase
import com.example.medicationreminderapp.data.MedsRepository

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