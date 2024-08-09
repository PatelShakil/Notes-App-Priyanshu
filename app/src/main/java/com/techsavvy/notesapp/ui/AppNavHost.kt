package com.techsavvy.notesapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techsavvy.notesapp.helpers.NotesPreferences
import com.techsavvy.notesapp.helpers.getReminderTimeFromPrefs
import com.techsavvy.notesapp.helpers.saveReminderTimeToPrefs
import com.techsavvy.notesapp.ui.home.AddNoteScreen
import com.techsavvy.notesapp.ui.home.HomeScreen
import com.techsavvy.notesapp.ui.home.SettingsScreen

@Composable
fun NotesApp() {
    val navController = rememberNavController()
    val notesPreferences = NotesPreferences(LocalContext.current)
    var notes by remember { mutableStateOf(notesPreferences.getNoteList()) }
    val context = LocalContext.current
    var reminderTime by remember { mutableStateOf(getReminderTimeFromPrefs(context)) }

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("add_note_screen?id={id}&title={title}&content={content}",
            arguments = listOf(
                navArgument("id") { defaultValue = "" },
                navArgument("title") { defaultValue = "" },
                navArgument("content") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""

                AddNoteScreen(
                    navController,
                    initialId = id,
                    initialTitle = title,
                    initialContent = content,
                    reminderTime = reminderTime
                )
        }

        composable("settings") {
            SettingsScreen(
                navController,
                onSaveReminderTime = { newReminderTime ->
                    reminderTime = newReminderTime
                    saveReminderTimeToPrefs(context, newReminderTime)
                },
                currentReminderTime = reminderTime
            )
        }
    }
}
