package com.example.medicationreminderapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "medication_table")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0 ,
    @ColumnInfo(name = "medication_name")
    val name : String = "" ,
    @ColumnInfo(name = "medication_description")
    val description : String = "" ,
    @ColumnInfo(name = "medication_quantity")
    val quantity : Float = 0f,
    @ColumnInfo(name = "medication_time")
    val time : String = "0:0"
)

object DummyMeds{
    val medicationList = listOf(
        Medication(id = 1, name = "Medication1", description = "Description1", quantity = 1.0f, time = LocalTime.of(8, 0).toString()),
        Medication(id = 2, name = "Medication2", description = "Description2", quantity = 2.0f, time = LocalTime.of(10, 0).toString()),
        Medication(id = 3, name = "Medication3", description = "Description3", quantity = 3.0f, time = LocalTime.of(12, 0).toString())
        // Add more Medication objects as needed
    )
}