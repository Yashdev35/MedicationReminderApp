package com.example.medicationreminderapp.screen.uielements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medicationreminderapp.R
import com.example.medicationreminderapp.data.DummyMeds
import com.example.medicationreminderapp.data.Medication
import com.example.medicationreminderapp.scheduleDailyAlarm
import java.time.LocalTime

//this file defines small composables like custom buttons or app bar
@Composable
fun AppBar(
    title: String,
    onBackNavClicked: () -> Unit = {}
) {
    val navigationIcon : (@Composable () -> Unit)? =
        if(!title.contains("Your Medications")) {
            {
                IconButton(onClick = {
                    onBackNavClicked()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back button on top bar",
                        tint = colorResource(id = R.color.white)
                    )
                }
            }
        }else{
            null
        }
    TopAppBar(
        title = {
        Text(title , color = colorResource(id = R.color.white),
            modifier = Modifier.padding(start = 4.dp).heightIn(max = 24.dp))
    },
        elevation = 3.dp,
        backgroundColor = Color(0xFF3F51B5),
        navigationIcon = navigationIcon
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationItem(
    medicine : Medication,
    onClick:() -> Unit = {}
){
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clickable {
                onClick()
            },
        elevation = 10.dp,
        backgroundColor = Color.White  ){
        Row(
            modifier = Modifier
                .fillMaxWidth().wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp, top = 6.dp)
                .clickable {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
          Image(
              painter = painterResource(id = R.drawable.pillicon),
              contentDescription = "Medicine Image",
              modifier = Modifier
                  .padding(16.dp)
                  .wrapContentHeight().size(50.dp),
          )
            Column(modifier = Modifier.padding(16.dp).wrapContentSize(),
            ) {
                Text(text = "Med name :\n${medicine.name}", color = Color.Black)
                Text(text = "Quantity: \n${medicine.quantity.toString()}", color = Color.Black)
            }
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth(),
            ) {
                Text(text = medicine.description, color = Color.Black, maxLines = 2)
                Text(text = "timing :\n ${medicine.time}" ,color = Color.Black)

            }
        }

    }
    scheduleDailyAlarm(context, LocalTime.parse(medicine.time).hour, LocalTime.parse(medicine.time).minute, medicine.name)
}

@Preview
@Composable
fun AppBarPreview() {
    MedicationItem(medicine = DummyMeds.medicationList[1]){
    }
}