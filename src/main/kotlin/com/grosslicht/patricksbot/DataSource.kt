package com.grosslicht.patricksbot

import com.grosslicht.patricksbot.models.Models
import io.requery.Persistable
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import org.mariadb.jdbc.MariaDbDataSource

/**
 * Created by patrickgrosslicht on 02/01/17.
 */
object DataSource {
    val dataSource: MariaDbDataSource = MariaDbDataSource()
    val data: KotlinEntityDataStore<Persistable>
        get() = KotlinEntityDataStore(KotlinConfiguration(dataSource = dataSource, model = Models.DEFAULT))
    init {
        val jdbcUrl = System.getenv("JDBC_URL")
        dataSource.setUrl(jdbcUrl)
    }
}
