package ua.cryptogateway.data.db.columns

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.EnumerationColumnType
import org.jetbrains.exposed.sql.vendors.currentDialect
import ua.cryptogateway.data.models.Order
import kotlin.reflect.KClass

internal class OrderSideColumnType : ColumnType<Order.Side>() {
    private val klass: KClass<Order.Side> = Order.Side::class

    override fun sqlType(): String = currentDialect.dataTypeProvider.varcharType(8)
    //private val enumConstants by lazy { klass.java.enumConstants!! }

    @Suppress("UNCHECKED_CAST")
    override fun valueFromDB(value: Any): Order.Side = when (value) {
        is String -> Order.Side.valueOf(value)
        is Enum<*> -> value as Order.Side
        else -> error("$value of ${value::class.qualifiedName} is not valid for enum ${klass.simpleName}")
    }

    override fun notNullValueToDB(value: Order.Side): String = value.name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as EnumerationColumnType<*>

        return klass == other.klass
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + klass.hashCode()
        return result
    }
}