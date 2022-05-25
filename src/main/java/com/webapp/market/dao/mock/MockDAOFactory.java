package com.webapp.market.dao.mock;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.dao.RegisterActionDAO;
import com.webapp.market.dao.RoleDAO;
import com.webapp.market.dao.UserDAO;

public class MockDAOFactory extends DAOFactory {
    public static final String DRIVER = "";
    public static final String DB_URL = "";

    @Override
    public String getNameDB() {
        return "FakeDB";
    }

    @Override
    public UserDAO getUserDAO() {
        return null;
    }

    @Override
    public RoleDAO getRoleDAO() {
        return null;
    }

    @Override
    public RegisterActionDAO getRegisterActionDAO() {
        return null;
    }


}
