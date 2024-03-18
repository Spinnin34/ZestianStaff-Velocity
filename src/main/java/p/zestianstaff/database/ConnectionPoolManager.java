package p.zestianstaff.database;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import p.zestianstaff.ZestianStaff;
import p.zestianstaff.manager.ConfigManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPoolManager {

    private final ZestianStaff plugin;
    private HikariDataSource dataSource;
    private DatabaseCredentials dbCredentials;

    public ConnectionPoolManager(ZestianStaff plugin) {
        this.plugin = plugin;
        init();
        setupPool();
    }

    private void init() {
        dbCredentials = ConfigManager.getDBHostname();
    }

    private void setupPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:mysql://" +
                        dbCredentials.hostname() +
                        ":" +
                        dbCredentials.port() +
                        "/" +
                        dbCredentials.database()
        );
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(dbCredentials.username());
        config.setPassword(dbCredentials.password());
        config.setIdleTimeout(870000000);
        config.setMaxLifetime(870000000);
        config.setConnectionTimeout(870000000);
        config.setMinimumIdle(20);
        config.setRegisterMbeans(true);
        config.setMaximumPoolSize(10);
        config.setConnectionTestQuery("SELECT 1");
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }

}
