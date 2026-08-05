package com.classpoll.teacher.presentation.poll.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.classpoll.teacher.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPollScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditPollViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pollLoaded by viewModel.pollLoaded.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState) {
        if (uiState is Resource.Success) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Poll") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (pollLoaded) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                OutlinedTextField(
                    value = viewModel.question,
                    onValueChange = { viewModel.onQuestionChange(it) },
                    label = { Text("Question") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Options", style = MaterialTheme.typography.titleSmall)
                    Row {
                        IconButton(onClick = { viewModel.removeOption(viewModel.options.lastIndex) },
                            enabled = viewModel.options.size > 2) {
                            Icon(Icons.Default.Remove, contentDescription = "Remove option")
                        }
                        IconButton(onClick = { viewModel.addOption() },
                            enabled = viewModel.options.size < 6) {
                            Icon(Icons.Default.Add, contentDescription = "Add option")
                        }
                    }
                }

                viewModel.options.forEachIndexed { index, option ->
                    OutlinedTextField(
                        value = option,
                        onValueChange = { viewModel.updateOption(index, it) },
                        label = { Text("Option ${('A' + index)}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
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
                    onClick = { viewModel.updatePoll() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = viewModel.question.isNotBlank() && uiState !is Resource.Loading
                ) {
                    if (uiState is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Update Poll")
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
