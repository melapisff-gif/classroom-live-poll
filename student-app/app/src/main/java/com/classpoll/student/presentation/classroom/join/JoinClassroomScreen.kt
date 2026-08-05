package com.classpoll.student.presentation.classroom.join

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.classpoll.student.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinClassroomScreen(
    onNavigateBack: () -> Unit,
    viewModel: JoinClassroomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is Resource.Success) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Join Classroom") },
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
        ) {
            OutlinedTextField(
                value = viewModel.joinCode,
                onValueChange = { viewModel.joinCode = it },
                label = { Text("Enter Join Code") },
                modifier = Modifier.fillMaxWidth()
            )

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
                onClick = { viewModel.joinClassroom() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.joinCode.isNotBlank() && uiState !is Resource.Loading
            ) {
                if (uiState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Join Classroom")
                }
            }
        }
    }
}
