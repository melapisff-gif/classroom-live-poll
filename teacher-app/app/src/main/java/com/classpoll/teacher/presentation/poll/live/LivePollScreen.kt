package com.classpoll.teacher.presentation.poll.live

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.classpoll.teacher.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivePollScreen(
    classroomId: String,
    pollId: String,
    onNavigateBack: () -> Unit,
    viewModel: LivePollViewModel = hiltViewModel()
) {
    val poll by viewModel.poll.collectAsState()
    val responseCount by viewModel.responseCount.collectAsState()
    val remainingTime by viewModel.remainingTime.collectAsState()
    var showCorrectAnswerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(pollId) {
        viewModel.loadPoll(pollId, classroomId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Poll") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.pausePoll(pollId) }) {
                        Icon(Icons.Default.Pause, contentDescription = "Pause")
                    }
                    IconButton(onClick = { viewModel.resumePoll(pollId) }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Resume")
                    }
                    IconButton(onClick = { viewModel.stopPoll(pollId) }) {
                        Icon(Icons.Default.Stop, contentDescription = "Stop")
                    }
                }
            )
        }
    ) { padding ->
        when (poll) {
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val data = (poll as Resource.Success).data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Timer
                    Text(
                        text = "${remainingTime}s",
                        fontSize = 48.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Question
                    Text(
                        text = data.question,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Options
                    data.options.forEach { option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${('A' + option.index)}. ${option.content}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${responseCount[option.index] ?: 0}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Response count
                    val totalResponses = responseCount.values.sum()
                    Text(
                        text = "Responses: $totalResponses",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons
                    Button(
                        onClick = { showCorrectAnswerDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Set Correct Answer")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.publishResults(pollId) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Publish Results")
                    }
                }
            }
            is Resource.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (poll as Resource.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showCorrectAnswerDialog) {
        AlertDialog(
            onDismissRequest = { showCorrectAnswerDialog = false },
            title = { Text("Select Correct Answer") },
            text = {
                Column {
                    (poll as? Resource.Success)?.data?.options?.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = viewModel.selectedCorrectIndex == option.index,
                                onClick = { viewModel.selectedCorrectIndex = option.index }
                            )
                            Text(
                                text = "${('A' + option.index)}. ${option.content}",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setCorrectAnswer(pollId)
                    showCorrectAnswerDialog = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCorrectAnswerDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
