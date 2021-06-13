package xyz.crossward.database

import org.springframework.jdbc.datasource.DriverManagerDataSource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.sql.Connection
import javax.sql.DataSource

@Configuration
class SQLiteConfig(
    var env: Environment,
) {

    @Bean
    fun dataSource(): DataSource? {
        val dataSource = DriverManagerDataSource()
        env.getProperty("driverClassName")?.let { dataSource.setDriverClassName(it) }
        dataSource.url = env.getProperty("url")
        dataSource.username = env.getProperty("username")
        dataSource.password = env.getProperty("password")
        return dataSource
    }
}