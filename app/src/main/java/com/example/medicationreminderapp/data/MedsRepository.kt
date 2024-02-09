package com.example.medicationreminderapp.data

import kotlinx.coroutines.flow.Flow

class MedsRepository(
    private val medsDao: MedsDao
) {
    suspend fun addMeds(medicationEntity: Medication) {
        medsDao.addMeds(medicationEntity)
    }

    fun getMeds(): Flow<List<Medication>> {
        return medsDao.getAllMeds()
    }

    fun getMedsByid(id: Long): Flow<Medication> {
        return medsDao.getMeds(id)
    }

    suspend fun updateMeds(medicationEntity: Medication) {
        medsDao.updateMeds(medicationEntity)
    }

    suspend fun deleteMeds(medicationEntity: Medication) {
        medsDao.deleteMeds(medicationEntity)
    }
}