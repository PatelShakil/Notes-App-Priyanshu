package com.techsavvy.notesapp.helpers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotesPreferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)

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
}
