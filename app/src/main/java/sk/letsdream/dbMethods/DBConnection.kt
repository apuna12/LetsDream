package sk.letsdream.dbMethods

import android.database.SQLException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class DBConnection {
    internal var conn: Connection? = null
    internal var username = "HbOgj5NDZ2" // provide the username
    internal var password = "JfZxfWyCeW" // provide the corresponding password

    fun executeMySQLQuery() {
        var stmt: Statement? = null
        var resultset: ResultSet? = null
        try {
            stmt = conn!!.createStatement()
            resultset = stmt!!.executeQuery("SHOW DATABASES;")
            if (stmt.execute("SHOW DATABASES;")) {
                resultset = stmt.resultSet
            }
            while (resultset!!.next()) {
                println(resultset.getString("Database"))
            }
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } finally {
            // release resources
            if (resultset != null) {
                try {
                    resultset.close()
                } catch (sqlEx: SQLException) {
                }
                resultset = null
            }
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
                stmt = null
            }
            if (conn != null) {
                try {
                    conn!!.close()
                } catch (sqlEx: SQLException) {
                }
                conn = null
            }
        }
    }
    /**
     * This method makes a connection to MySQL Server
     * In this example, MySQL Server is running in the local host (so 127.0.0.1)
     * at the standard port 3306
     */
    fun getConnection() {
        val connectionProps = Properties()
        connectionProps.put("user", username)
        connectionProps.put("password", password)
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection(
                "jdbc:" + "mysql" + "://" +
                        "remotemysql.com" +
                        ":" + "3306" + "/" +
                        "",
                connectionProps)
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }
}