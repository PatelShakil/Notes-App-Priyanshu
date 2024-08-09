package com.techsavvy.notesapp.ui.home

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techsavvy.notesapp.helpers.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, notes: List<Note>) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                (context as Activity).requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addNote") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) {
        LazyColumn (
            modifier = Modifier.padding(it)
        ){
            itemsIndexed(notes) { index, note ->
                NoteItem(note = note, onClick = {
                    navController.navigate(
                        "addNote?noteId=$index&title=${note.title}&content=${note.content}"
                    )
                })
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick)
    ) {
        Text(text = note.title, style = MaterialTheme.typography.titleLarge)
        Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(note.timestamp)),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
