package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import ua.cryptogateway.data.db.models.KunaListEntity
import ua.cryptogateway.data.db.schema.KunaListSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class KunaListDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    /**
     * Retrieves all entries from the KunaListSchema and maps them to a list of KunaListEntity objects.
     *
     * This function performs a database query to select all records from the KunaListSchema table.
     * Each record is transformed into a KunaListEntity instance, and the function returns a list
     * of these entities.
     *
     * @return A list of KunaListEntity objects representing all records in the KunaListSchema.
     * @throws SQLException If an error occurs during the database query.
     */
    suspend fun getAll() = dbQuery {
        KunaListSchema.selectAll()
            .map { row ->
                KunaListEntity(
                    basecoin = row[KunaListSchema.basecoin],
                    currency = row[KunaListSchema.currency],
                    tikername = row[KunaListSchema.tikername],
                    active = row[KunaListSchema.active],
                )
            }
    }

    /**
     * Retrieves a list of `KunaListEntity` objects that are marked as active from the database.
     *
     * This function performs a database query that selects all entries from the KunaListSchema
     * where the active flag is set to true. It returns a list of `KunaListEntity` instances
     * created from the results of the query.
     *
     * @return A list of `KunaListEntity` objects with the active status set to true.
     * @throws SQLException If there is an error while executing the database query.
     */
    suspend fun getActive() = dbQuery {
        KunaListSchema.selectAll()
            .where { KunaListSchema.active eq true }
            .map { row ->
                KunaListEntity(
                    basecoin = row[KunaListSchema.basecoin],
                    currency = row[KunaListSchema.currency],
                    tikername = row[KunaListSchema.tikername].trim(),
                    active = row[KunaListSchema.active],
                )
            }
    }

    /**
     * Retrieves a list of active ticker names from the KunaListSchema.
     *
     * This function executes a database query to select all ticker names from the KunaListSchema
     * where the 'active' flag is set to true. It returns a list of these ticker names.
     *
     * @return A list of String representing the active ticker names.
     */
    suspend fun getActiveTickers() = dbQuery {
        KunaListSchema.selectAll()
            .where { KunaListSchema.active eq true }
            .map { row -> row[KunaListSchema.tikername].trim() }
    }
}