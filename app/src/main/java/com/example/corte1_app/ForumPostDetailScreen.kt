// ForumPostDetailScreen.kt
package com.example.corte1_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corte1_app.models.ForumPost
import com.example.corte1_app.models.Reply
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumPostDetailScreen(postId: String) {
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser?.email ?: "Anon"

    var post by remember { mutableStateOf<ForumPost?>(null) }
    var replies by remember { mutableStateOf(listOf<Reply>()) }
    var replyText by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(postId) {
        firestore.collection("forum").document(postId).addSnapshotListener { doc, _ ->
            doc?.let {
                post = it.toObject(ForumPost::class.java)?.copy(id = it.id)
            }
        }
        firestore.collection("forum").document(postId).collection("replies")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    replies = it.documents.mapNotNull { d ->
                        d.toObject(Reply::class.java)?.copy(id = d.id)
                    }.sortedByDescending { it.timestamp }
                }
            }
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar discusión") },
            text = { Text("¿Estás seguro de que quieres eliminar esta discusión? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        firestore.collection("forum").document(postId).delete()
                        showDeleteDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Discusión eliminada")
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header del post
            post?.let { currentPost ->
                PostHeader(
                    post = currentPost,
                    currentUser = currentUser,
                    onCloseDiscussion = {
                        firestore.collection("forum").document(postId).update("isClosed", true)
                        scope.launch {
                            snackbarHostState.showSnackbar("Discusión cerrada")
                        }
                    },
                    onDeleteDiscussion = { showDeleteDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sección de respuestas
            RepliesSection(
                replies = replies,
                currentUser = currentUser,
                postId = postId,
                firestore = firestore,
                snackbarHostState = snackbarHostState,
                scope = scope,
                modifier = Modifier.weight(1f)
            )

            // Campo para nueva respuesta
            post?.let { currentPost ->
                if (!currentPost.isClosed) {
                    ReplyInputSection(
                        replyText = replyText,
                        onReplyTextChange = { replyText = it },
                        onSendReply = {
                            if (replyText.isNotBlank()) {
                                val reply = Reply(text = replyText.trim(), author = currentUser)
                                firestore.collection("forum").document(postId)
                                    .collection("replies").add(reply)
                                replyText = ""
                                scope.launch {
                                    snackbarHostState.showSnackbar("Respuesta enviada")
                                }
                            }
                        }
                    )
                } else {
                    ClosedDiscussionIndicator()
                }
            }
        }
    }
}

@Composable
private fun PostHeader(
    post: ForumPost,
    currentUser: String,
    onCloseDiscussion: () -> Unit,
    onDeleteDiscussion: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (post.isClosed) {
                    Surface(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Cerrado",
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.onError
                            )
                            Text(
                                "Cerrado",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onError,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Autor y controles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Por: ${post.author}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Controles del autor
                if (currentUser == post.author && !post.isClosed) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilledTonalButton(
                            onClick = onCloseDiscussion,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                "Cerrar",
                                fontSize = 12.sp
                            )
                        }

                        OutlinedButton(
                            onClick = onDeleteDiscussion,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                "Eliminar",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RepliesSection(
    replies: List<Reply>,
    currentUser: String,
    postId: String,
    firestore: FirebaseFirestore,
    snackbarHostState: SnackbarHostState,
    scope: kotlinx.coroutines.CoroutineScope,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header de respuestas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Respuestas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "${replies.size}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Lista de respuestas
        if (replies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                EmptyRepliesIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(replies) { reply ->
                    ReplyItem(
                        reply = reply,
                        currentUser = currentUser,
                        // Reemplaza la función onLike en RepliesSection con esta versión:
                        onLike = {
                            if (!reply.likedBy.contains(currentUser)) {
                                // ✅ AGREGAR LIKE
                                val updatedLikes = reply.likes + 1
                                val updatedLikedBy = reply.likedBy + currentUser
                                firestore.collection("forum").document(postId)
                                    .collection("replies").document(reply.id)
                                    .update(mapOf("likes" to updatedLikes, "likedBy" to updatedLikedBy))
                                scope.launch {
                                    snackbarHostState.showSnackbar("Gracias por tu reacción 👍")
                                }
                            } else {
                                // ✅ QUITAR LIKE
                                val updatedLikes = (reply.likes - 1).coerceAtLeast(0) // Evita números negativos
                                val updatedLikedBy = reply.likedBy - currentUser
                                firestore.collection("forum").document(postId)
                                    .collection("replies").document(reply.id)
                                    .update(mapOf("likes" to updatedLikes, "likedBy" to updatedLikedBy))
                                scope.launch {
                                    snackbarHostState.showSnackbar("Like removido")
                                }
                            }
                        },
                        onDelete = {
                            firestore.collection("forum").document(postId)
                                .collection("replies").document(reply.id).delete()
                            scope.launch {
                                snackbarHostState.showSnackbar("Respuesta eliminada")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReplyItem(
    reply: Reply,
    currentUser: String,
    onLike: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = reply.text,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Por: ${reply.author}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón de like
                    FilledTonalIconButton(
                        onClick = onLike,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = if (reply.likedBy.contains(currentUser)) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            if (reply.likedBy.contains(currentUser)) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                            contentDescription = "Like",
                            modifier = Modifier.size(18.dp),
                            tint = if (reply.likedBy.contains(currentUser)) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }

                    // Contador de likes
                    Text(
                        text = reply.likes.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.widthIn(min = 16.dp)
                    )

                    // Botón de eliminar (solo para el autor)
                    if (reply.author == currentUser) {
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar respuesta",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        // Spacer para mantener el layout consistente
                        Spacer(modifier = Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ReplyInputSection(
    replyText: String,
    onReplyTextChange: (String) -> Unit,
    onSendReply: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = replyText,
                onValueChange = onReplyTextChange,
                label = { Text("Escribe tu respuesta...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onSendReply,
                    enabled = replyText.isNotBlank()
                ) {
                    Text("Enviar respuesta")
                }
            }
        }
    }
}

@Composable
private fun ClosedDiscussionIndicator() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = "Cerrado",
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Esta discusión está cerrada",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun EmptyRepliesIndicator() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aún no hay respuestas",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "¡Sé el primero en responder!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}