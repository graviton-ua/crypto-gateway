package ua.cryptogateway.data.web.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


//{
//    "id": "b0fcb54c-2278-4f16-a300-02765faad8b0",     // ID  of your newly created order
//    "type": "Limit",                                  // Type of an order
//    "quantity": "0.06",                               // Original order quantity
//    "executedQuantity": "0",                          // Traded quantity in stock (>0 if traded)
//    "pair": "BTC_USDT",                               // Traded pair
//    "price": "26440.46",                              // Price of the trade
//    "status": "Open",                                 // The status of the order
//    "createdAt": "2023-07-11T08:01:30.550Z",          // Date-time of order creation, UTC
//    "updatedAt": "2023-07-11T08:01:30.550Z"           // Date-time of the last update of the order, UTC
//}
@Serializable
data class KunaCreateOrder(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("quantity") val quantity: Double,
    @SerialName("executedQuantity") val executedQuantity: Double,
    @SerialName("pair") val pair: String,
    @SerialName("price") val price: Double,
    @SerialName("status") val status: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String,
)