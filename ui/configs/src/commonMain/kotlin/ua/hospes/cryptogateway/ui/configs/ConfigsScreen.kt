package ua.hospes.cryptogateway.ui.configs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.inject.injectViewModel
import ua.hospes.cryptogateway.ui.common.navigation.OpenResultRecipient
import ua.hospes.cryptogateway.ui.configs.views.ConfigGroup

@Serializable
data object ConfigsScreen

@Composable
internal fun ConfigsScreen(
    diComponent: ConfigsComponent,
    resultConfigEdit: OpenResultRecipient<Unit>,
) {
    ConfigsScreen(
        viewModel = injectViewModel { diComponent.configsViewModel() },
        resultConfigEdit = resultConfigEdit,
    )
}

@Composable
private fun ConfigsScreen(
    viewModel: ConfigsViewModel,
    resultConfigEdit: OpenResultRecipient<Unit>,
) {
    resultConfigEdit.onNavResult { viewModel.refreshList() }

    val state by viewModel.state.collectAsStateWithLifecycle()
    ConfigsScreen(
        state = state,
        onClick = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigsScreen(
    state: ConfigsViewState,
    onClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crypto Bot configs") },
                modifier = Modifier.fillMaxWidth(),
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddings ->
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
                .padding(paddings)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            state.groups.forEach {
                ConfigGroup(
                    baseAsset = it.key,
                    items = it.value,
                    onEdit = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ConfigsScreen(
            state = ConfigsViewState.Init,
            onClick = {},
        )
    }
}