package com.webapp.market.dao;

import com.webapp.market.dao.entities.User;
import com.webapp.market.exceptions.UserDataFormatException;

import java.util.List;

public interface UserDAO {

    void insertUser(User user) throws UserDataFormatException;
    User readUserById(int id) throws UserDataFormatException;
    void updateUserById(User user) throws UserDataFormatException;
    void deleteUserById(int id) throws UserDataFormatException;

    List<User> selectUserTO(User user) throws UserDataFormatException;
    List<User> getAllUsers() throws UserDataFormatException;
    User findUser(User user) throws UserDataFormatException;
    boolean isIdExist(int id) throws UserDataFormatException;
    boolean isExist(String login) throws UserDataFormatException;
    boolean isExist(String login, String password) throws UserDataFormatException;
}
