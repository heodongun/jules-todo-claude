package com.board.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * 데이터베이스 연결 및 설정을 관리하는 싱글턴 객체입니다.
 */
object DatabaseFactory {
    /**
     * 데이터베이스를 초기화합니다.
     * HikariCP 커넥션 풀을 설정하고, 데이터베이스에 연결한 후,
     * 필요한 테이블 스키마를 생성합니다.
     */
    fun init() {
        val config = HikariConfig().apply {
            driverClassName = "com.mysql.cj.jdbc.Driver"
            jdbcUrl = System.getenv("DATABASE_URL") ?: "jdbc:mysql://localhost:3306/boarddb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
            username = System.getenv("DATABASE_USER") ?: "board_user"
            password = System.getenv("DATABASE_PASSWORD") ?: "board_password"
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(config)
        val database = Database.connect(dataSource)

        transaction(database) {
            SchemaUtils.create(Posts)
        }
    }

    /**
     * 데이터베이스 쿼리를 비동기적으로 실행하는 헬퍼 함수입니다.
     * 새로운 트랜잭션을 IO 디스패처에서 실행하여 블로킹 I/O 작업을 처리합니다.
     *
     * @param T 쿼리 결과의 타입
     * @param block 실행할 데이터베이스 쿼리 람다
     * @return 쿼리 실행 결과
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
