package com.techsavvy.notesapp.ui.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import com.techsavvy.notesapp.MainActivity
import com.techsavvy.notesapp.R
import com.techsavvy.notesapp.helpers.Note
import com.techsavvy.notesapp.helpers.NotesPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@Composable
fun AddNoteScreen(
    navController: NavController,
    initialId: Long? = null,
    reminderTime: Int,
    isFixed : Boolean
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val noteId = initialId ?: System.currentTimeMillis()
    val notesPreferences = remember { NotesPreferences(context) }
    var saveStatus by remember { mutableStateOf<Result<Boolean>?>(null) } // Track save status
    var lastNotificationTime by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(true) {
        if(initialId != null){
            val note = if(isFixed) notesPreferences.getFixedNote(initialId) else notesPreferences.getNote(initialId)
            title = note?.title ?: ""
            content = note?.content ?: ""
        }
    }


    LaunchedEffect(Unit) {
        createNotificationChannel(context)
    }

    // Notification logic based on time interval
    LaunchedEffect(Unit) {
        while (true) {
            delay(reminderTime * 1000L) // Wait for the reminder time interval
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastNotificationTime >= reminderTime * 1000L) {
                if (title.isNotEmpty() || content.isNotEmpty()) {
                    lastNotificationTime = currentTime
                    showHighPriorityNotification(context, title, content)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                }
                IconButton(onClick = {
                    if (title.isEmpty() || content.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Title and content cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val newNote = Note(
                            id = noteId,
                            title = title,
                            content = content,
                            timestamp = System.currentTimeMillis()
                        )
                        val result = if(isFixed) notesPreferences.saveFixedNote(newNote) else notesPreferences.saveNote(newNote)
                        saveStatus = result // Update save status

                        if (result.isSuccess) {
                            Toast.makeText(context, "Note saved", Toast.LENGTH_SHORT).show()
                            lastNotificationTime = System.currentTimeMillis()
                        } else {
                            Toast.makeText(context, "Error saving note", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Icon(painterResource(id = R.drawable.ic_save), contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

        TextField(
            value = title,
            onValueChange = { title = it},
            placeholder = {
                Text(
                    "Title",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 5.dp),
        )

        TextField(
            value = content,
            onValueChange = { content = it },
            placeholder = {
                Text(
                    "Content",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(0.dp)
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Notes Notifications"
        val descriptionText = "Notifications for unsaved notes"
        val importance = NotificationManager.IMPORTANCE_LOW // High importance
        val channel = NotificationChannel("notes_channel", name, importance).apply {
            description = descriptionText
            enableLights(true)
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            )
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun showHighPriorityNotification(context: Context, title: String, content: String) {
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val notification = NotificationCompat.Builder(context, "notes_channel")
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setContentIntent(pendingIntent) // Set the intent that will fire when the user taps the notification
        .setAutoCancel(true) // Automatically removes the notification when the user taps it
//        .setFullScreenIntent(pendingIntent, true) // Attempt to make it full screen
        .build()

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(context).notify(1, notification)
    }else{
        NotificationManagerCompat.from(context).notify(1, notification)
    }
}
