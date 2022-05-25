package com.webapp.market.dao;

import com.webapp.market.dao.entities.RegisterAction;
import com.webapp.market.exceptions.UserDataFormatException;

import java.sql.Timestamp;
import java.util.List;

public interface RegisterActionDAO {

    boolean insertRegisterAction(RegisterAction registerAction);
    //List<RegisterAction> selectRegisterActionTO() throws UserDataFormatException;
    //List<RegisterAction> selectRegisterActionTO(RegisterAction registerAction, Timestamp begin, Timestamp end) throws UserDataFormatException;
}

