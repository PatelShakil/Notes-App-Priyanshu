package com.techsavvy.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.techsavvy.notesapp.helpers.Note
import com.techsavvy.notesapp.helpers.NotesPreferences
import com.techsavvy.notesapp.ui.NotesApp
import com.techsavvy.notesapp.ui.theme.NotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notesPreferences = NotesPreferences(this)
        if(!notesPreferences.isAnyFixedNoteExists()){
            notesPreferences.saveNote(Note(1,"Shopping List","1",System.currentTimeMillis()))
            val notesList = mutableListOf<Note>()
            for (i in 1..12) {
                notesList.add(Note(i.toLong(), "Dummy Notes No. $i", "Description of Dummy Note $i",System.currentTimeMillis()))
            }
            notesPreferences.saveFixedNotes(notesList)
        }
        setContent {
            NotesAppTheme {
                NotesApp()
            }
        }
    }
}
