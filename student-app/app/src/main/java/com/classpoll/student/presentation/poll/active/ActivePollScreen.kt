package com.classpoll.student.presentation.poll.active

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.classpoll.student.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivePollScreen(
    pollId: String,
    onNavigateToResult: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ActivePollViewModel = hiltViewModel()
) {
    val poll by viewModel.poll.collectAsState()
    val remainingTime by viewModel.remainingTime.collectAsState()
    val submitState by viewModel.submitState.collectAsState()

    LaunchedEffect(pollId) {
        viewModel.loadPoll(pollId)
    }

    LaunchedEffect(submitState) {
        if (submitState is Resource.Success) {
            onNavigateToResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Live Poll") })
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
                    Text(
                        text = "${remainingTime}s",
                        fontSize = 48.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = data.question,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    data.options.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = viewModel.selectedOption == option.index,
                                onClick = { viewModel.selectedOption = option.index }
                            )
                            Text(
                                text = "${('A' + option.index)}. ${option.content}",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }

                    if (submitState is Resource.Error) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = (submitState as Resource.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.submitAnswer(pollId) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.selectedOption != null && submitState !is Resource.Loading
                    ) {
                        if (submitState is Resource.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Submit Answer")
                        }
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
}
