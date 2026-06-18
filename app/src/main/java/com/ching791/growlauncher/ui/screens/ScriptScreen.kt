package com.ching791.growlauncher.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ching791.growlauncher.ui.components.AppTopBar
import com.ching791.growlauncher.viewmodel.ScriptViewModel

@Composable
fun ScriptScreen(viewModel: ScriptViewModel, onBack: () -> Unit) {
    val scripts by viewModel.scripts.collectAsStateWithLifecycle()
    var newScript by remember { mutableStateOf("") }

    Scaffold(topBar = { AppTopBar(title = "My Scripts", onBack = onBack) }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = newScript,
                    onValueChange = { newScript = it },
                    label = { Text("Script Name") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    viewModel.addScript(newScript)
                    newScript = ""
                }) { Text("Add") }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(scripts, key = { it.id }) { script ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(script.name)
                            Text(if (script.enabled) "Enabled" else "Disabled")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = script.enabled,
                                onCheckedChange = { viewModel.toggleScript(script.id) }
                            )
                            Button(onClick = { viewModel.deleteScript(script.id) }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
