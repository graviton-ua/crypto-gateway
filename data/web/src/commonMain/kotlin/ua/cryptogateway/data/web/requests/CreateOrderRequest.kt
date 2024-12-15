package ua.cryptogateway.data.web.requests

import kotlinx.serialization.Serializable
import ua.cryptogateway.data.models.Order


//{
//    "type": "Limit",
//    "orderSide": "Bid",
//    "pair": "BTC_USDT",
//    "price": "26440.46",
//    "quantity": "0.06",   //"quantity": 0.06,
//}

@Serializable
data class CreateOrderRequest(
    val type: Order.Type,
    val orderSide: Order.Side,
    val pair: String,
    val price: String? = null,
    val quantity: String,
)
