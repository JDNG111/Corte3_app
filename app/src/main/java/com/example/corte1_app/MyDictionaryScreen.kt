package com.example.corte1_app.ui

import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*

data class ConceptItem(
    val id: String,
    val concept: String,
    val meaning: String,
    val createdAt: Date?
)

@Composable
fun MyDictionaryScreen(navController: NavController) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    var concepts by remember { mutableStateOf(listOf<ConceptItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var newConcept by remember { mutableStateOf("") }
    var newMeaning by remember { mutableStateOf("") }
    var listener: ListenerRegistration? by remember { mutableStateOf(null) }

    DisposableEffect(currentUser) {
        if (currentUser != null) {
            listener = firestore.collection("users")
                .document(currentUser.uid)
                .collection("dictionary")
                .addSnapshotListener { snapshot, _ ->
                    val entries = snapshot?.documents?.mapNotNull {
                        val concept = it.getString("concept") ?: return@mapNotNull null
                        val meaning = it.getString("meaning") ?: return@mapNotNull null
                        val createdAt = it.getTimestamp("createdAt")?.toDate()
                        ConceptItem(id = it.id, concept = concept, meaning = meaning, createdAt = createdAt)
                    }?.sortedBy { it.concept.lowercase(Locale.getDefault()) } ?: emptyList()
                    concepts = entries
                }
        }
        onDispose { listener?.remove() }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "My Dictionary", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(concepts, key = { it.id }) { conceptItem ->
                var editConcept by remember { mutableStateOf(conceptItem.concept) }
                var editMeaning by remember { mutableStateOf(conceptItem.meaning) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        OutlinedTextField(
                            value = editConcept,
                            onValueChange = { editConcept = it },
                            label = { Text("Concept") },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(lineHeight = 20.sp)
                        )
                        OutlinedTextField(
                            value = editMeaning,
                            onValueChange = { editMeaning = it },
                            label = { Text("Meaning") },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(lineHeight = 20.sp)
                        )
                        conceptItem.createdAt?.let {
                            Text(
                                text = "Added on: ${SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(it)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                firestore.collection("users")
                                    .document(currentUser!!.uid)
                                    .collection("dictionary")
                                    .document(conceptItem.id)
                                    .update("concept", editConcept, "meaning", editMeaning)
                            }) {
                                Icon(Icons.Default.Check, contentDescription = "Update")
                            }
                            IconButton(onClick = {
                                firestore.collection("users")
                                    .document(currentUser!!.uid)
                                    .collection("dictionary")
                                    .document(conceptItem.id)
                                    .delete()
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Concept")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val docId = UUID.randomUUID().toString()
                    firestore.collection("users")
                        .document(currentUser!!.uid)
                        .collection("dictionary")
                        .document(docId)
                        .set(
                            mapOf(
                                "concept" to newConcept,
                                "meaning" to newMeaning,
                                "createdAt" to Date()
                            ), SetOptions.merge()
                        ).addOnSuccessListener {
                            Toast.makeText(context, "Concept added", Toast.LENGTH_SHORT).show()
                        }
                    newConcept = ""
                    newMeaning = ""
                    showDialog = false
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("New Concept") },
            text = {
                Column {
                    OutlinedTextField(value = newConcept, onValueChange = { newConcept = it }, label = { Text("Concept") })
                    OutlinedTextField(value = newMeaning, onValueChange = { newMeaning = it }, label = { Text("Meaning") })
                }
            }
        )
    }
}
