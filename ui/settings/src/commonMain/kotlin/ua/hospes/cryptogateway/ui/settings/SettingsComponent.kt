package ua.hospes.cryptogateway.ui.settings

import me.tatarka.inject.annotations.Component

@Component
interface SettingsComponent {
    val settingsViewModel: () -> SettingsViewModel
}