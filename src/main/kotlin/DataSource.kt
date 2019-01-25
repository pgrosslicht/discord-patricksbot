package com.grosslicht.patricksbot

import com.grosslicht.patricksbot.models.Models
import io.requery.Persistable
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import org.mariadb.jdbc.MariaDbPoolDataSource


object DataSource {
    private val dataSource: MariaDbPoolDataSource = MariaDbPoolDataSource()
    val data: KotlinEntityDataStore<Persistable>
        get() = KotlinEntityDataStore(KotlinConfiguration(dataSource = dataSource, model = Models.DEFAULT))

    init {
        val jdbcUrl = System.getenv("JDBC_URL")
        dataSource.setUrl(jdbcUrl)
    }
}
