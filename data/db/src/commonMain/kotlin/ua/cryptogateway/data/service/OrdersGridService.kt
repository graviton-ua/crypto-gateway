package ua.cryptogateway.data.service

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ua.cryptogateway.data.models.db.OrdersGridEntity
import ua.cryptogateway.data.schema.OrdersGridSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class OrdersGridService(
    dispatchers: AppCoroutineDispatchers,
    private val database: Database,
) {
    private val dispatcher = dispatchers.io

    init {
        // We don't need to create table as we connect to database with already created schema
        //transaction(database) {
        //    SchemaUtils.create(OrdersGridSchema)
        //}
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(context = dispatcher, db = database) { block() }

    suspend fun readAll(): List<OrdersGridEntity> {
        return dbQuery {
            OrdersGridSchema.selectAll()
                //.where { Users.id eq id }
                .map { OrdersGridEntity(it[OrdersGridSchema.pairname], it[OrdersGridSchema.side]) }
        }
    }

}