package ua.hospes.cryptogateway.ui.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import ua.hospes.cryptogateway.ui.common.theme.AppTheme

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SwitchColors = SwitchDefaults.colors(checkedThumbColor = AppTheme.colors.primary),
) = Switch(checked, onCheckedChange, modifier, thumbContent, enabled, colors, interactionSource)

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    colors: SwitchColors = SwitchDefaults.colors(checkedThumbColor = AppTheme.colors.primary),
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier
            .clip(shape = AppTheme.shapes.small)
            .let { m -> onCheckedChange?.let { m.clickable(enabled = enabled) { onCheckedChange(!checked) } } ?: m }
            .padding(contentPadding),
    ) {
        AppSwitch(
            checked = checked,
            onCheckedChange = null,
            thumbContent = thumbContent,
            enabled = enabled,
            colors = colors,
        )

        content()
    }
}


@Preview
@Composable
private fun Preview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) checked: Boolean,
) {
    AppTheme {
        AppSwitch(
            checked = checked,
            onCheckedChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewWithContent(
    @PreviewParameter(BooleanPreviewParameterProvider::class) checked: Boolean,
) {
    AppTheme {
        AppSwitch(
            checked = checked,
            onCheckedChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { Text(text = "Example with content") }
    }
}