package com.example.medicationreminderapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicationreminderapp.data.Medication
import com.example.medicationreminderapp.data.MedsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalTime

class MedsViewModel(
    private val repository: MedsRepository = Graph.medsRepository
) : ViewModel() {

    var medNameState by mutableStateOf("")
    var medDescriptionState by mutableStateOf("")
    var medQuantityState by mutableStateOf(1f)
    val medTimeState = mutableStateOf(LocalTime.of(0,0).toString())

    fun onMedNameChanged(newString: String){
        medNameState = newString
    }
    fun onMedDescriptionChanged(newString: String){
        medDescriptionState = newString
    }
    fun onMedQuantityChanged(newQuantity: Float){
        medQuantityState = newQuantity
    }

    lateinit var getAllMeds: Flow<List<Medication>>

    init {
        viewModelScope.launch {
            getAllMeds = repository.getMeds()
        }
    }

        fun addMed(medication: Medication) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.addMeds(medication)
            }
        }

        fun updateMed(medication: Medication) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateMeds(medication)
            }
        }

        fun getMedById(id: Long): Flow<Medication> {
            return repository.getMedsByid(id)
        }

        fun deleteMed(medication: Medication) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteMeds(medication)
            }
        }

}