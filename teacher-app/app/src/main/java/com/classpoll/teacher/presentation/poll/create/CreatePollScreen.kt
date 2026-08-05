package com.classpoll.teacher.presentation.poll.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.classpoll.teacher.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePollScreen(
    classroomId: String,
    onNavigateBack: () -> Unit,
    viewModel: CreatePollViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState) {
        if (uiState is Resource.Success) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Poll") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = viewModel.question,
                onValueChange = { viewModel.question = it },
                label = { Text("Question") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Poll Type", style = MaterialTheme.typography.titleSmall)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val types = listOf("SINGLE_CHOICE", "MULTIPLE_CHOICE", "TRUE_FALSE", "YES_NO")
                types.forEach { type ->
                    FilterChip(
                        selected = viewModel.pollType == type,
                        onClick = { viewModel.pollType = type },
                        label = { Text(type.replace("_", " "), style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Number of Options", style = MaterialTheme.typography.titleSmall)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { if (viewModel.numberOfOptions > 2) viewModel.numberOfOptions-- }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                Text(
                    text = viewModel.numberOfOptions.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(onClick = { if (viewModel.numberOfOptions < 6) viewModel.numberOfOptions++ }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Options", style = MaterialTheme.typography.titleSmall)

            repeat(viewModel.numberOfOptions) { index ->
                OutlinedTextField(
                    value = viewModel.options.getOrElse(index) { "" },
                    onValueChange = { viewModel.updateOption(index, it) },
                    label = { Text("Option ${('A' + index)}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Timer", style = MaterialTheme.typography.titleSmall)

            val timers = listOf(30, 45, 60, 90, 120, 150)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                timers.forEach { timer ->
                    FilterChip(
                        selected = viewModel.timer == timer,
                        onClick = { viewModel.timer = timer },
                        label = { Text("${timer}s") }
                    )
                }
            }

            if (uiState is Resource.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (uiState as Resource.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.createPoll(classroomId) },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.question.isNotBlank() && uiState !is Resource.Loading
            ) {
                if (uiState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create Poll")
                }
            }
        }
    }
}
