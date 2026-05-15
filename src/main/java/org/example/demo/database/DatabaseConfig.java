package org.example.demo.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (in == null) {
                throw new RuntimeException("Arquivo database.properties não encontrado!");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configurações: " + e.getMessage(), e);
        }
    }

    public static String getUrl()            { return props.getProperty("db.url"); }
    public static String getUsuario()        { return props.getProperty("db.usuario"); }
    public static String getSenha()          { return props.getProperty("db.senha"); }
    public static int getMaxConnections()    { return Integer.parseInt(props.getProperty("db.maxConnections", "10")); }
    public static int getMinIdle()           { return Integer.parseInt(props.getProperty("db.minIdle", "2")); }
    public static long getConnectionTimeout(){ return Long.parseLong(props.getProperty("db.connectionTimeout", "30000")); }
    public static long getIdleTimeout()      { return Long.parseLong(props.getProperty("db.idleTimeout", "600000")); }
}
