package fr.saveyourdreams.app.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.saveyourdreams.app.BuildConfig;

public class Database {

    private static Database instance = new Database();

    private Connection connection;
    private final String host = BuildConfig.DB_HOST;

    private final String database = BuildConfig.DB_DATABASE;
    private final String port = BuildConfig.DB_PORT;
    private final String user = BuildConfig.DB_USER;
    private final String pass = BuildConfig.DB_PASSWORD;
    private String url = "jdbc:postgresql://%s:%s/%s";

    private Database() {
        this.url = String.format(this.url, this.host, this.port, this.database);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(url, user, pass);
    }

    public static Database getInstance() {
        return instance;
    }

    public boolean isConnected() {
        try {
            return !this.connection.isClosed();
        } catch (Exception e) {
            // Dans tous les cas, peut importe l'erreur si y'en a une c'est que c'est pas bon
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
