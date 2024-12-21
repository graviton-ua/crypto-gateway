package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.logs.Log
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.db.models.OrderEntity
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.models.KunaActiveOrder
import ua.cryptogateway.domain.DataPuller
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ApplicationScope
@Inject
class ActiveOrdersPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val api: KunaApi,
    private val dao: OrderDao,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private val delay = MutableStateFlow<Duration>(10.seconds)
    private var job: Job? = null
    private val data = MutableStateFlow<List<KunaActiveOrder>?>(null)

    init {
        //start()
        scope.updateActiveTable()
    }

    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug { "DataPuller job started" }
            DataPuller().pull(delay.value) { api.getActiveOrders() }
                .mapNotNull { it.getOrNull() }
                .catch { Log.error(throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.value = it
                }
        }.also {
            it.invokeOnCompletion {
                Log.debug { "DataPuller job completed (exception: ${it?.message})" }; job = null
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }


    private fun CoroutineScope.updateActiveTable() = launch(dispatcher) {
        Log.debug { "updateActiveTable() job started" }

        data.filterNotNull()
            .map { it.map(KunaActiveOrder::toEntity) }
            .collectLatest { list ->
                Result.runCatching { dao.saveActive(list) }
//                    .onSuccess { Log.info { "ActiveTable updated" } }
                    .onFailure { Log.error(throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug { "updateActiveTable() job completed (exception: ${it?.message})" } } }
}

private fun KunaActiveOrder.toEntity(): OrderEntity = OrderEntity(
    id, type, quantity, executedQuantity,
    cumulativeQuoteQty, cost, side, pair, price, status, createdAt, updatedAt
)