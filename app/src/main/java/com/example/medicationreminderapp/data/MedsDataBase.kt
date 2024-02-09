package com.example.medicationreminderapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Medication::class],
    version = 1,
    exportSchema = false
    )
abstract class MedsDataBase : RoomDatabase() {
    abstract fun medsDao(): MedsDao
}