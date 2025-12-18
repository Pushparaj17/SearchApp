package com.coding.cvs.searchapp.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.coding.cvs.searchapp.domain.model.Character


/**
 * Detail UI for a single character.
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    uiState: CharacterDetailUiState,
    onBack: () -> Unit,
) {
    val title = uiState.character?.name ?: "Character"
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
        ) {
            when {
                uiState.isLoading -> {
                    Text(
                        text = "Loading…",
                        modifier = Modifier.padding(16.dp),
                    )
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                uiState.character != null -> {
                    CharacterDetailContent(character = uiState.character)
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailContent(character: Character) {
    AsyncImage(
        model = character.imageUrl,
        contentDescription = character.name,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentScale = ContentScale.Crop,
    )

    Text(
        text = character.name,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )

    DetailRow(label = "Species", value = character.species)
    DetailRow(label = "Status", value = character.status)
    DetailRow(label = "Origin", value = character.originName)
    if (character.type != null) {
        DetailRow(label = "Type", value = character.type)
    }
    DetailRow(label = "Created", value = character.created)
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

