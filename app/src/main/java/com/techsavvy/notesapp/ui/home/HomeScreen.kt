package com.techsavvy.notesapp.ui.home

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.techsavvy.notesapp.helpers.Note
import com.techsavvy.notesapp.helpers.NotesPreferences
import com.techsavvy.notesapp.helpers.vibrateStrong
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    val notesPreferences = NotesPreferences(context)
    var notes by remember { mutableStateOf(listOf<Note>()) }
    var fixedNotesList by remember { mutableStateOf(listOf<Note>()) }
    var lastClickTime by remember { mutableStateOf(0L) }
    var selectedId by remember{ mutableStateOf(notesPreferences.getSelectedId()) }


    LaunchedEffect(true) {
        notes = notesPreferences.getNoteList()
        fixedNotesList = NotesPreferences(context).getAllFixedNotes()
    }

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
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Notes",
                    modifier = Modifier.clickable {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastClickTime < 300) {
                            navController.navigate("settings")
                        }
                        lastClickTime = currentTime
                    },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp
                )
            }
        )
    },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_note_screen") },
                containerColor = Color(0xFFEC0421),
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    Icons.Default.Add, contentDescription = "Add Note",
                    tint = Color.White
                )
            }
        }
    ) {

        val view = LocalView.current
        var isDelete by remember { mutableStateOf(false) }
        var noteId by remember { mutableStateOf(0L) }
        fun updateSelectedNoteId(id : Long){
            view.vibrateStrong()

            if(notesPreferences.getNote(1L) != null){
                notesPreferences.deleteNote(1L)
                notes = notesPreferences.getNoteList()
            }
            notesPreferences.saveSelectedId(id)
            selectedId = notesPreferences.getSelectedId()
        }
        Box() {

            if(fixedNotesList.isNotEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize().padding(it)
                ){
                    repeat(3){x->
                        Row(
                            modifier = Modifier.weight(if(x == 0) .3f else 1f).fillMaxWidth()
                        ){
                            repeat(3){y->
                                Box(modifier = Modifier.weight(1f).fillMaxSize()
                                    .combinedClickable(indication = null, interactionSource = remember{ MutableInteractionSource() }, onLongClick = {
                                        //On Long Click
                                        when{
                                            x==1 && y==0 -> {
                                                updateSelectedNoteId(7L)
                                            }
                                            x==1 && y==1 -> {
                                                updateSelectedNoteId(8L)
                                            }
                                            x==1 && y==2->{
                                                updateSelectedNoteId(9L)
                                            }
                                            x==2 && y==0 -> {
                                                updateSelectedNoteId(10L)
                                            }
                                            x==2 && y==1 -> {
//                                                navController.navigate("add_note_screen?id=${5}/isFixed=true")
                                                updateSelectedNoteId(11L)
                                            }
                                            x==2 && y==2 -> {
                                                updateSelectedNoteId(12L)
                                            }
                                        }
                                    }){
                                        //On Click
                                        when{
                                            x==1 && y==0 -> {
                                                updateSelectedNoteId(1L)
                                            }
                                            x==1 && y==1 -> {
                                                updateSelectedNoteId(2L)
                                            }
                                            x==1 && y==2->{
                                                updateSelectedNoteId(3L)
                                            }
                                            x==2 && y==0 -> {
                                                updateSelectedNoteId(4L)
                                            }
                                            x==2 && y==1 -> {
//                                                navController.navigate("add_note_screen?id=${5}/isFixed=true")
                                                updateSelectedNoteId(5L)
                                            }
                                            x==2 && y==2 -> {
                                                updateSelectedNoteId(6L)
                                            }
                                        }
                                    })
                            }
                        }
                    }
                }
            }



            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.padding(it)
            ) {

                if(selectedId != 0L){
                    val note = notesPreferences.getFixedNote(notesPreferences.getSelectedId())
                    if(note != null) {
                        item {
                            NoteItemGrid(note.copy(content = ""), {
                                navController.navigate("add_note_screen?id=${note.id}/isFixed=true")
                            }) {

                            }
                        }
                    }
                }

                itemsIndexed(notes) { index, note ->
                    NoteItemGrid(note, {
                        navController.navigate("add_note_screen?id=${note.id}/isFixed=false")
                    }) {
                        isDelete = true
                        noteId = note.id
                    }
                }
            }

        }


        if(isDelete){
            AlertDialog(onDismissRequest = {
                isDelete = false
                noteId = 0L
            }, confirmButton = {
                Button(
                    onClick ={
                        notesPreferences.deleteNote(noteId)
                        notes = notesPreferences.getNoteList()
                        isDelete = false
                        noteId = 0L
                    }
                ){
                    Text("Delete")

                }

            }, dismissButton = { Button(
                onClick ={

                    isDelete = false
                    noteId  = 0L
                }
            ){
                Text("Cancel")

            }}, title = {
                Text(text = "Delete Note")
            }, text = {
                Text(text = "Are you sure you want to delete this note?")
            })
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItemGrid(note: Note, onClick: () -> Unit, onLongClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 15.dp),
        contentAlignment = Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(onLongClick = onLongClick, onClick = onClick),
            colors = CardDefaults.cardColors(Color(0xFFF5F5F5)),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = SimpleDateFormat(
                        "MMM dd yyyy ",
                        Locale.getDefault()
                    ).format(Date(note.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold

                )
                Text(
                    text = note.title, style = MaterialTheme.typography.bodyLarge, maxLines = 2,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = note.content.take(100), style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(5.dp))

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

    }
}
