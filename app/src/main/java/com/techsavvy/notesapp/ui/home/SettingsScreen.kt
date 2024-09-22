package com.techsavvy.notesapp.ui.home

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techsavvy.notesapp.R
import com.techsavvy.notesapp.helpers.Note
import com.techsavvy.notesapp.helpers.NotesPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    onSaveReminderTime: (Int) -> Unit,
    currentReminderTime: Int
) {
    var reminderTime by remember { mutableStateOf(currentReminderTime.toString()) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val sharedPreferences = NotesPreferences(context)
                    var isDarkMode by remember{ mutableStateOf(sharedPreferences.getIsDarkMode()) }

                    Row(modifier= Modifier.fillMaxWidth()
                        .padding(end = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("Settings", fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically,) {
                            Text("Light",style = MaterialTheme.typography.titleSmall)
                            Switch(isDarkMode, {
                                sharedPreferences.saveIsDarkMode(!isDarkMode)
                                isDarkMode = sharedPreferences.getIsDarkMode()
                                (context as Activity).recreate()
//                                val packageManager: PackageManager = context.packageManager
//                                val intent: Intent = packageManager.getLaunchIntentForPackage(context.packageName)!!
//                                val componentName: ComponentName = intent.component!!
//                                val restartIntent: Intent = Intent.makeRestartActivityTask(componentName)
//                                context.startActivity(restartIntent)
//                                Runtime.getRuntime().exit(0)
                            },
                                modifier= Modifier.padding(horizontal = 10.dp))
                            Text("Dark",style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(
                text = "Notification Interval ${currentReminderTime}s",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

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
            var notesList by remember {
                mutableStateOf(mutableListOf<Note>())
            }
            LaunchedEffect(key1 = true) {
                val notesPreferences = NotesPreferences(context)
                notesList = notesPreferences.getAllFixedNotes().toMutableList()
            }
            if (notesList.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                ) {
                    itemsIndexed(notesList) { index, note ->
                        NoteItemGrid(note, {
                            navController.navigate("add_note_screen?id=${note.id}/isFixed=true")
                        }) {
                        }
                    }
                }
            }


        }
    }
}
