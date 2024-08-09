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
import com.techsavvy.notesapp.ui.home.AddNoteScreen
import com.techsavvy.notesapp.ui.home.HomeScreen
@Composable
fun NotesApp() {
    val navController = rememberNavController()
    val notesPreferences = NotesPreferences(LocalContext.current)
    var notes by remember { mutableStateOf(notesPreferences.getNoteList()) }

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, notes)
        }
        composable(
            route = "addNote?noteId={noteId}&title={title}&content={content}",
            arguments = listOf(
                navArgument("noteId") { defaultValue = -1 },
                navArgument("title") { defaultValue = "" },
                navArgument("content") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""

            AddNoteScreen(
                navController,
                onSave = { newNote ->
                    if (noteId >= 0) {
                        notes = notes.mapIndexed { index, note ->
                            if (index == noteId) newNote else note
                        }
                    } else {
                        notes = notes + newNote
                    }
                    notesPreferences.saveNoteList(notes)
                },
                initialTitle = title,
                initialContent = content
            )
        }
    }
}
