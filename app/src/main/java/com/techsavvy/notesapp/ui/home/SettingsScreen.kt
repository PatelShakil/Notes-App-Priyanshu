package com.techsavvy.notesapp.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techsavvy.notesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, onSaveReminderTime: (Int) -> Unit, currentReminderTime: Int) {
    var reminderTime by remember { mutableStateOf(currentReminderTime.toString()) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = "Notification Interval ${currentReminderTime}s",
                modifier = Modifier.padding(horizontal = 16.dp))

            OutlinedTextField(
                value = reminderTime,
                onValueChange = { reminderTime = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        onSaveReminderTime(reminderTime.toIntOrNull() ?: currentReminderTime)
                        navController.popBackStack()
                        Toast.makeText(context, "Reminder time saved", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_save),
                            contentDescription = null
                        )
                    }
                },
                singleLine = true
            )
        }
    }
}
