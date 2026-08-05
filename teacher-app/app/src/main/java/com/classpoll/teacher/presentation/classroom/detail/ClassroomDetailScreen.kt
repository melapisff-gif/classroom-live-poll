package com.classpoll.teacher.presentation.classroom.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.classpoll.teacher.domain.model.ClassroomDetail
import com.classpoll.teacher.domain.model.PollSummary
import com.classpoll.teacher.domain.model.Student
import com.classpoll.teacher.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomDetailScreen(
    classroomId: String,
    onCreatePoll: () -> Unit,
    onLivePoll: (String) -> Unit,
    onEditPoll: (String) -> Unit,
    onEditClassroom: () -> Unit,
    onLeaderboard: () -> Unit,
    onAnalytics: () -> Unit,
    onJoinCode: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ClassroomDetailViewModel = hiltViewModel()
) {
    val classroom by viewModel.classroom.collectAsState()
    val polls by viewModel.polls.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDeletePollDialog by remember { mutableStateOf(false) }
    var pollToDelete by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(classroomId) {
        viewModel.loadClassroom(classroomId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Classroom") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onJoinCode) {
                        Icon(Icons.Default.QrCode, contentDescription = "Join Code")
                    }
                    IconButton(onClick = onLeaderboard) {
                        Icon(Icons.Default.Leaderboard, contentDescription = "Leaderboard")
                    }
                    IconButton(onClick = onAnalytics) {
                        Icon(Icons.Default.BarChart, contentDescription = "Analytics")
                    }
                    IconButton(onClick = onEditClassroom) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePoll) {
                Icon(Icons.Default.Add, contentDescription = "Create Poll")
            }
        }
    ) { padding ->
        when (classroom) {
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val data = (classroom as Resource.Success).data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = data.name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        if (data.description != null) {
                            Text(
                                text = data.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Students (${data.students.size})",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(data.students) { student ->
                        StudentItem(student = student, onRemove = {
                            viewModel.removeStudent(classroomId, student.id)
                        })
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Polls",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    when (polls) {
                        is Resource.Success -> {
                            items((polls as Resource.Success).data) { poll ->
                                PollItem(
                                    poll = poll,
                                    onStart = { viewModel.startPoll(poll.id) },
                                    onView = { onLivePoll(poll.id) },
                                    onEdit = { onEditPoll(poll.id) },
                                    onDelete = {
                                        pollToDelete = poll.id
                                        showDeletePollDialog = true
                                    }
                                )
                            }
                        }
                        else -> {}
                    }
                }
            }
            is Resource.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (classroom as Resource.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Classroom") },
            text = { Text("Are you sure you want to delete this classroom?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteClassroom(classroomId)
                    showDeleteDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeletePollDialog && pollToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeletePollDialog = false },
            title = { Text("Delete Poll") },
            text = { Text("Are you sure you want to delete this poll?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletePoll(pollToDelete!!)
                    showDeletePollDialog = false
                    pollToDelete = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeletePollDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StudentItem(student: Student, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = student.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = student.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.RemoveCircle, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun PollItem(poll: PollSummary, onStart: () -> Unit, onView: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = poll.question, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "${poll.responseCount} responses • ${poll.status}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row {
                if (poll.status == "DRAFT") {
                    IconButton(onClick = onStart) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                    }
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
                IconButton(onClick = onView) {
                    Icon(Icons.Default.Visibility, contentDescription = "View")
                }
            }
        }
    }
}
