package com.example.medicationreminderapp.screen

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicationreminderapp.MedsViewModel
import com.example.medicationreminderapp.R
import com.example.medicationreminderapp.Screens
import com.example.medicationreminderapp.data.DummyMeds
import com.example.medicationreminderapp.data.Medication
import com.example.medicationreminderapp.scheduleDailyAlarm
import com.example.medicationreminderapp.screen.uielements.AppBar
import com.example.medicationreminderapp.screen.uielements.MedicationItem
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: MedsViewModel,
    navController: NavController
){
    val scope = rememberCoroutineScope()
    var day = LocalDate.now().dayOfWeek
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppBar(title = "Your Medications for ${day.toString().lowercase()} ") {}
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                   navController.navigate(Screens.AddScreen.route + "/0L")
                },
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                backgroundColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new medication",
                    tint = colorResource(id = R.color.white)
                )
            }
        },
    ) {
        val medsList = viewModel.getAllMeds.collectAsState(initial = listOf())
   LazyColumn(
       modifier = Modifier
           .fillMaxSize()
           .padding(it)){

        items(medsList.value, key = {medication -> medication.hashCode()}){
            medication ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if((it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart)){
                         scope.launch {
                             delay(100)
                             viewModel.deleteMed(medication)
                         }
                    }
                    true
                }
            )

            SwipeToDismiss(
                state = dismissState,
                background = {
                    val color by animateColorAsState(
                        if(dismissState.dismissDirection
                            == DismissDirection.EndToStart || dismissState.dismissDirection == DismissDirection.StartToEnd) Color.Red else Color.Transparent
                        ,label = ""
                    )
                    val alignment = Alignment.CenterEnd
                    Box(
                        Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp),
                        contentAlignment = alignment
                    ){
                        Icon(Icons.Default.Delete,
                            contentDescription = "Delete Icon",
                            tint = Color.White)
                    }

                },
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                dismissThresholds = {FractionalThreshold(0.75f)},
                dismissContent = {
                    MedicationItem(medicine = medication){
                        val id = medication.id
                        navController.navigate(Screens.AddScreen.route + "/$id")
                    }
                }
            )

         }
       }
    }
}

@Composable
fun AddEditDetailView(
    id: Long,
    viewModel: MedsViewModel,
    navController: NavController
) {
    val snackMessage = remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    if(id != 0L) {
        val medication = viewModel.getMedById(id).collectAsState(initial = Medication(id = 0L, name = "", description = "", quantity = 0.0f, time = ""))
        viewModel.medNameState = medication.value.name
        viewModel.medDescriptionState = medication.value.description
        viewModel.medQuantityState = medication.value.quantity
        viewModel.medTimeState.value = medication.value.time
    }else{
        viewModel.medNameState = " "
        viewModel.medDescriptionState = " "
        viewModel.medQuantityState = 0f
        viewModel.medTimeState.value = LocalTime.of(0,1).format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    Scaffold(
        topBar = {
            AppBar(title =
            if (id == 0L) stringResource(id = R.string.add_medication)
            else stringResource(id = R.string.update_medication),
                onBackNavClicked = {
                    navController.navigateUp()
                })
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Spacer(modifier = Modifier.padding(10.dp))
            CustomTextField(
                label = "Title",
                value = viewModel.medNameState,
                onValueChanged = {
                    viewModel.onMedNameChanged(it)
                }
            )
            Spacer(modifier = Modifier.padding(10.dp))
            CustomTextField(
                label = "Description",
                value = viewModel.medDescriptionState,
                onValueChanged = {
                    viewModel.onMedDescriptionChanged(it)
                }
            )
            Spacer(modifier = Modifier.padding(10.dp))
            CustomTextFieldForDose(label = "Dose", value = viewModel.medQuantityState,
                onValueChanged = {
                    viewModel.onMedQuantityChanged(it.toFloat())
                }
            )
            Spacer(modifier = Modifier.padding(10.dp))
            CustomTextFieldForTime(label = "Time", value = viewModel.medTimeState)
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = {
                if (viewModel.medNameState.isNotEmpty() && viewModel.medDescriptionState.isNotEmpty()){
                          if(id == 0L){
                              //add meds
                             viewModel.addMed(
                                 Medication(
                                     name = viewModel.medNameState.trim(),
                                     description = viewModel.medDescriptionState.trim(),
                                     quantity = viewModel.medQuantityState,
                                     time = viewModel.medTimeState.value
                                 )
                             )
                          }else {
                              //update meds
                                viewModel.updateMed(
                                    Medication(
                                        id = id,
                                        name = viewModel.medNameState.trim(),
                                        description = viewModel.medDescriptionState.trim(),
                                        quantity = viewModel.medQuantityState,
                                        time = viewModel.medTimeState.value
                                    )
                                )

                          }
                } else {
                       snackMessage.value = "Please enter name and description"
                }
                scope.launch{
                    Toast.makeText(context, "Medicine added to list", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }

            }) {
                Text(
                    text = if (id == 0L) stringResource(id = R.string.add_medication)
                    else stringResource(id = R.string.update_medication),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = MaterialTheme.typography.h6.fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label , color = Color.Black) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Red,
            unfocusedBorderColor = Color.Black,
            focusedLabelColor = Color.Red,
            unfocusedLabelColor = Color.Black
        )
    )
}

@Composable
fun CustomTextFieldForDose(
    label: String,
    value: Float,
    onValueChanged: (String) -> Unit
){
    OutlinedTextField(
        value = value.toString(),
        onValueChange = onValueChanged,
        label = { Text(text = label , color = Color.Black) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Red,
            unfocusedBorderColor = Color.Black,
            focusedLabelColor = Color.Red,
            unfocusedLabelColor = Color.Black
        )
    )
}

@Composable
fun CustomTextFieldForTime(
    label: String,
    value: MutableState<String>
){
    var stringToTime = remember {
        if(value.value.isNotEmpty()){
            LocalTime.parse(value.value, DateTimeFormatter.ofPattern("HH:mm"))
        }else{
            LocalTime.now()
        }
    }
    val context = LocalContext.current

    val timeDialogState = rememberMaterialDialogState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        Text(text = "Selected Time: ${value.value}")
        Button(onClick = {
            timeDialogState.show()
        }) {
            Text(text = "Select Time")

        }
    }
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton("OK") {
                Toast.makeText(context, "Time selected", Toast.LENGTH_SHORT).show()
            }
            negativeButton(text ="Cancel")
        }
    ) {
       timepicker(
           initialTime = LocalTime.now(),
           title = "pick time for medication",
           timeRange = LocalTime.of(0,0)..LocalTime.of(23,59)
       ){
           stringToTime = it
           if(stringToTime.minute < 10){
               value.value = "${stringToTime.hour}:0${stringToTime.minute}"
              }else{
                value.value = "${stringToTime.hour}:${stringToTime.minute}"
           }
       }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CustomTextField(
        label = "Title",
        value = "Title",
        onValueChanged = {

        }
    )
}