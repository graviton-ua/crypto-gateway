package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.BotConfigsDao
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class SaveBotConfig(
    dispatchers: AppCoroutineDispatchers,
    val dao: BotConfigsDao,
) : ResultInteractor<SaveBotConfig.Params, Result<Unit>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Result<Unit> = withContext(dispatcher) {
        dao.save(
            id = params.id,
            baseAsset = params.baseAsset,
            quoteAsset = params.quoteAsset,
            side = params.side,
            fond = params.fond,
            startPrice = params.startPrice,
            priceStep = params.priceStep,
            biasPrice = params.biasPrice,
            minSize = params.minSize,
            orderSize = params.orderSize,
            sizeStep = params.sizeStep,
            orderAmount = params.orderAmount,
            priceForce = params.priceForce,
            market = params.market,
            basePrec = params.basePrec,
            quotePrec = params.quotePrec,
            active = params.active,
        )
            .onSuccess { Log.info(tag = TAG) { "Config saved: $params" } }
            .onFailure { Log.error(tag = TAG, throwable = it) { "Config wasn't saved" } }
            .map { }
    }


    suspend operator fun invoke(
        id: Int?,
        baseAsset: String, quoteAsset: String, side: Order.Side,
        fond: Double, startPrice: Double, priceStep: Double,
        biasPrice: Double, minSize: Double, orderSize: Int,
        sizeStep: Double, orderAmount: Int, priceForce: Boolean,
        market: Boolean, basePrec: Int, quotePrec: Int, active: Boolean,
    ) = executeSync(
        Params(
            id,
            baseAsset, quoteAsset, side, fond,
            startPrice, priceStep, biasPrice,
            minSize, orderSize, sizeStep, orderAmount,
            priceForce, market, basePrec, quotePrec, active,
        )
    )

    data class Params(
        val id: Int?,
        val baseAsset: String,
        val quoteAsset: String,
        val side: Order.Side,
        val fond: Double,
        val startPrice: Double,
        val priceStep: Double,
        val biasPrice: Double,
        val minSize: Double,
        val orderSize: Int,
        val sizeStep: Double,
        val orderAmount: Int,
        val priceForce: Boolean,
        val market: Boolean,
        val basePrec: Int,
        val quotePrec: Int,
        val active: Boolean,
    )


    companion object {
        private const val TAG = "SaveBotConfig"
    }
}