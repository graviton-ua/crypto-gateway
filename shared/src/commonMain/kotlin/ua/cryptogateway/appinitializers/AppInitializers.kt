package ua.cryptogateway.appinitializers

import me.tatarka.inject.annotations.Inject

@Inject
class AppInitializers(
    private val initializers: Lazy<Set<AppInitializer>>,
) : AppInitializer {
    override fun initialize() {
        for (initializer in initializers.value) {
            initializer.initialize()
        }
    }
}
