package com.webapp.market.dao.postgresql;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.dao.RegisterActionDAO;
import com.webapp.market.dao.RoleDAO;
import com.webapp.market.dao.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresqlDAOFactory extends DAOFactory {

    public static final String DRIVER = "org.postgresql.Driver";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/market_db";
    public static final String USER = "postgres";
    public static final String PASSWORD = "123";

    public static Connection createConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getNameDB() {
        return DB_URL;
    }

    @Override
    public UserDAO getUserDAO() {

        return new PostgresqlUserDAO();
    }

    @Override
    public RoleDAO getRoleDAO() {

        return new PostgresqlRoleDAO();
    }

    @Override
    public RegisterActionDAO getRegisterActionDAO() {

        return new PostgresqlRegisterActionDAO();
    }
}
