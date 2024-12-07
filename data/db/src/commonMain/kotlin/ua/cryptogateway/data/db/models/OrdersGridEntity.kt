package ua.cryptogateway.data.db.models

data class OrdersGridEntity(
    val pairname: String?,
    val side: String?,
    val remvolume: Double?,
    val volume: Double?,
    val pricebid: Double?,
    val price: Double?,
    val priceask: Double?,
    val change: Float?,
    val gecko: Double?,
    val offset: Float?,
    val smashort: Double?,
    val smalong: Double?,
    val atr: Int?,
    val id: String?,
    val status: String?,
    val ordtype: String?,
    val deleteord: Boolean?,
    val timemarket: String?,
    val lost: Boolean?
)