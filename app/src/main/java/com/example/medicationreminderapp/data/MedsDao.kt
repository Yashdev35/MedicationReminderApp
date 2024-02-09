package com.example.medicationreminderapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
abstract class MedsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addMeds(medicationEntity: Medication)

    @Query("Select * from medication_table")
    abstract fun getAllMeds(): Flow<List<Medication>>

    @Update
    abstract suspend fun updateMeds(medicationEntity: Medication)

    @Delete
    abstract suspend fun deleteMeds(medicationEntity: Medication)

    @Query("Select * from medication_table where id = :id")
    abstract fun getMeds(id : Long): Flow<Medication>

}