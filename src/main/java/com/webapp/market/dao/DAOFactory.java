package com.webapp.market.dao;

import com.webapp.market.dao.mock.MockDAOFactory;
import com.webapp.market.dao.postgresql.PostgresqlDAOFactory;

public abstract class DAOFactory {

    public static final int MOCK = 1;
    public static final int POSTGRESQL = 2;


    public abstract String getNameDB();

    public abstract UserDAO getUserDAO();
    public abstract RoleDAO getRoleDAO();
    public abstract RegisterActionDAO getRegisterActionDAO();

    public static DAOFactory getDAOFactory(int factory) {
        switch (factory) {
            case MOCK:
                return new MockDAOFactory();
            case POSTGRESQL:
                return new PostgresqlDAOFactory();
            default:
                return null;
        }
    }
}