package com.grosslicht.patricksbot

import com.grosslicht.patricksbot.models.Models
import com.mysql.cj.jdbc.MysqlDataSource
import io.requery.Persistable
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore

/**
 * Created by patrickgrosslicht on 02/01/17.
 */
object DataSource {
    val dataSource: MysqlDataSource = MysqlDataSource()
    val data: KotlinEntityDataStore<Persistable>
        get() = KotlinEntityDataStore(KotlinConfiguration(dataSource = dataSource, model = Models.DEFAULT))
    init {
        val jdbcUrl = System.getenv("JDBC_URL")
        dataSource.setURL(jdbcUrl)
    }
}