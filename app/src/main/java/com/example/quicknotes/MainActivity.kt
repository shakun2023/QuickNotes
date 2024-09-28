package com.example.quicknotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Data class to represent a Note
data class Note(
    var title: String,
    var content: String
)

// Main Activity to handle different screens
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickNotesApp()
        }
    }
}

@Composable
fun QuickNotesApp() {
    var currentScreen by remember { mutableStateOf("home") }
    var notes by remember { mutableStateOf(mutableListOf<Note>()) }
    var selectedNoteIndex by remember { mutableStateOf(-1) }

    when (currentScreen) {
        "home" -> HomeScreen(
            notes = notes,
            onAddNote = { currentScreen = "create" },
            onSelectNote = { index ->
                selectedNoteIndex = index
                currentScreen = "edit"
            }
        )
        "create" -> CreateNoteScreen(
            onSaveNote = { newNote ->
                notes.add(newNote)
                currentScreen = "home"
            }
        )
        "edit" -> EditNoteScreen(
            note = notes[selectedNoteIndex],
            onSaveNote = {
                notes[selectedNoteIndex] = it
                currentScreen = "home"
            }
        )
    }
}

// Home Screen: Display list of notes using LazyColumn
@Composable
fun HomeScreen(notes: List<Note>, onAddNote: () -> Unit, onSelectNote: (Int) -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Text("+")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                NoteItem(note = note, onClick = { onSelectNote(notes.indexOf(note)) })
            }
        }
    }
}

// UI for each individual note displayed in a Card
@Composable
fun NoteItem(note: Note, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content.take(100) + "...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Screen for creating a new note
@Composable
fun CreateNoteScreen(onSaveNote: (Note) -> Unit) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxHeight(0.6f)
        )
        Button(
            onClick = {
                if (title.text.isNotEmpty() && content.text.isNotEmpty()) {
                    onSaveNote(Note(title.text, content.text))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

// Screen for viewing and editing an existing note
@Composable
fun EditNoteScreen(note: Note, onSaveNote: (Note) -> Unit) {
    var title by remember { mutableStateOf(TextFieldValue(note.title)) }
    var content by remember { mutableStateOf(TextFieldValue(note.content)) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxHeight(0.6f)
        )
        Button(
            onClick = {
                if (title.text.isNotEmpty() && content.text.isNotEmpty()) {
                    onSaveNote(Note(title.text, content.text))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuickNotesApp()
}
