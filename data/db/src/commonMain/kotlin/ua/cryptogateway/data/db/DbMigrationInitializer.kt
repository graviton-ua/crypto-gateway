package ua.cryptogateway.data.db

import MigrationUtils
import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ua.cryptogateway.appinitializers.AppSuspendedInitializer
import ua.cryptogateway.data.db.schema.BalanceSchema
import ua.cryptogateway.data.db.schema.OrderSchema
import ua.cryptogateway.data.db.schema.TickerSchema
import ua.cryptogateway.data.db.schema.TradeBookSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class DbMigrationInitializer(
    dispatchers: AppCoroutineDispatchers,
    private val database: Database,
) : AppSuspendedInitializer {
    private val dispatcher = dispatchers.io

    override suspend fun initialize() {
        newSuspendedTransaction(context = dispatcher, db = database) {

            SchemaUtils.createMissingTablesAndColumns(
                OrderSchema, TickerSchema, BalanceSchema, TradeBookSchema,
            )

            MigrationUtils.statementsRequiredForDatabaseMigration(
                TickerSchema, BalanceSchema, TradeBookSchema
            ).forEach { exec(it) }
        }
    }
}