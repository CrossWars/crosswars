package xyz.crossward.database

import org.hibernate.dialect.Dialect
import org.hibernate.dialect.identity.IdentityColumnSupport
import java.sql.Types

/**
 * Custom SQLite Dialect
 * This is needed because spring jpa doesn't have support for SQLite
 */
class SQLiteDialect : Dialect() {

    init {
        registerColumnType(Types.BIT, "integer")
        registerColumnType(Types.TINYINT, "tinyint")
        registerColumnType(Types.SMALLINT, "smallint")
        registerColumnType(Types.INTEGER, "integer")
        registerColumnType(Types.VARCHAR, "char")
        registerColumnType(Types.DATE, "date")
    }

    override fun getIdentityColumnSupport(): IdentityColumnSupport {
        return SQLiteIdentityColumnSupport()
    }

    override fun hasAlterTable(): Boolean {
        return false
    }

    override fun dropConstraints(): Boolean {
        return false
    }

    override fun getDropForeignKeyString(): String? {
        return ""
    }

    override fun getAddForeignKeyConstraintString(
        cn: String?,
        fk: Array<String?>?, t: String?, pk: Array<String?>?, rpk: Boolean
    ): String {
        return ""
    }

    override fun getAddPrimaryKeyConstraintString(constraintName: String?): String {
        return ""
    }
}