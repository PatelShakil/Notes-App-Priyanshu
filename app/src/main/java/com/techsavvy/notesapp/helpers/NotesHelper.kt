package com.techsavvy.notesapp.helpers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class NotesPreferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
    private val fixedSharedPreferences = context.getSharedPreferences("fixed_notes_prefs", Context.MODE_PRIVATE)

    fun getAllFixedNotes(): List<Note> {
        val json = fixedSharedPreferences.getString("fixed_notes", null) ?: return emptyList()
        val type = object : TypeToken<List<Note>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun saveFixedNotes(noteList: List<Note>) {
        val editor = fixedSharedPreferences.edit()
        val json = Gson().toJson(noteList)
        editor.putString("fixed_notes", json)
        editor.apply()
    }
    fun saveFixedNote(note: Note) : Result<Boolean>{
        return try {
            val fixedNotes = getAllFixedNotes().toMutableList()
            val index = fixedNotes.indexOfFirst { it.id == note.id }
            if (index >= 0) {
                // Note exists, update it
                fixedNotes[index] = note
            } else {
                // Note does not exist, add it
                fixedNotes.add(note)
            }
            saveFixedNotes(fixedNotes)
            Result.success(true) // Return success
        } catch (e: Exception) {
            // Handle any exceptions
            Result.failure(e) // Return failure
        }
    }

    fun addFixedNote(note: Note) {
        val fixedNotes = getAllFixedNotes().toMutableList()
        fixedNotes.add(note)
        saveFixedNotes(fixedNotes)
        }

    fun removeFixedNote(noteId: Long) {
        val fixedNotes = getAllFixedNotes().toMutableList()
        fixedNotes.removeAll { it.id == noteId }
        saveFixedNotes(fixedNotes)
    }
    fun getFixedNote(noteId : Long): Note? {
        val notes= getAllFixedNotes()
        return notes.find { it.id == noteId }
    }

    fun isAnyFixedNoteExists(): Boolean {
        val fixedNotes = getAllFixedNotes()
        return fixedNotes.isNotEmpty()
    }



    fun saveNoteList(noteList: List<Note>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(noteList)
        editor.putString("notes", json)
        editor.apply()
    }

    fun getNoteList(): List<Note> {
        val json = sharedPreferences.getString("notes", null) ?: return emptyList()
        val type = object : TypeToken<List<Note>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun getNote(noteId : Long): Note? {
        val notes= getNoteList()
        return notes.find { it.id == noteId }
    }

    fun deleteNote(noteId: Long): Result<Boolean> {
        return try {
            val notes = getNoteList().toMutableList()
            val index = notes.indexOfFirst { it.id == noteId }
            if (index >= 0) {
                notes.removeAt(index)
                saveNoteList(notes)
                Result.success(true) // Return success
            } else {
                Result.failure(Exception("Note not found")) // Return failure
            }
        }
        catch (e: Exception) {
            // Handle any exceptions
            Result.failure(e) // Return failure
        }
    }


    fun saveNote(note: Note): Result<Boolean> {
        return try {
            val notes = getNoteList().toMutableList()
            val index = notes.indexOfFirst { it.id == note.id }

            if (index >= 0) {
                // Note exists, update it
                notes[index] = note
            } else {
                // Note does not exist, add it
                notes.add(note)
            }

            saveNoteList(notes)
            Result.success(true) // Return success
        } catch (e: Exception) {
            // Handle any exceptions
            Result.failure(e) // Return failure
        }
    }
}


fun getReminderTimeFromPrefs(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("reminder_time", 15) // Default to 15 seconds
}

fun saveReminderTimeToPrefs(context: Context, reminderTime: Int) {
    val sharedPreferences = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putInt("reminder_time", reminderTime)
        apply()
    }
}
