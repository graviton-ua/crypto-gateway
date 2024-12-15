package ua.cryptogateway.domain.models

import ua.cryptogateway.data.db.models.BotConfigEntity
import ua.cryptogateway.data.models.Order

data class BotConfigModel(
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

internal fun BotConfigEntity.toModel() = BotConfigModel(
    baseAsset = baseAsset,
    quoteAsset = quoteAsset,
    side = side,
    fond = fond,
    startPrice = startPrice,
    priceStep = priceStep,
    biasPrice = biasPrice,
    minSize = minSize,
    orderSize = orderSize,
    sizeStep = sizeStep,
    orderAmount = orderAmount,
    priceForce = priceForce,
    market = market,
    basePrec = basePrec,
    quotePrec = quotePrec,
    active = active
)