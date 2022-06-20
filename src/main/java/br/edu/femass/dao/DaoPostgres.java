package br.edu.femass.dao;

import java.sql.*;

public class DaoPostgres {
    protected static String ADDRESS = "localhost";
    protected static String BD = "tech-shop-FeMASS";
    protected static String PORT = "5432";
    protected static String USER = "postgres";
    protected static String PASS = "postgres";

    protected Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://" + ADDRESS + ":" + PORT + "/" + BD;
        Connection con = DriverManager.getConnection(url, USER, PASS);
        return con;
    }

    protected PreparedStatement getPreparedStatment(String sql, Boolean insertion)  throws Exception {
        PreparedStatement ps = null;
            if(insertion) {
                return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                return getConnection().prepareStatement(sql);
            }
    }

}
