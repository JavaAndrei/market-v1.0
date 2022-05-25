package com.webapp.market.dao.postgresql;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.dao.UserDAO;
import com.webapp.market.dao.entities.Role;
import com.webapp.market.dao.entities.User;
import com.webapp.market.exceptions.UserDataFormatException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresqlUserDAO implements UserDAO {

    private Connection connection;

    public PostgresqlUserDAO() {
        connection = PostgresqlDAOFactory.createConnection();
    }

    @Override
    public void insertUser(User user) throws UserDataFormatException {
        if (isExist(user.getLogin()))
            throw new UserDataFormatException("Error: Can't create user. That Login '" + user.getLogin() + "' already exists");
        if (checkLogin(user.getLogin()) && checkPassword(user.getPassword())) {
            String query = "INSERT INTO users(login, password, role_id) VALUES(?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, user.getPassword());
                Role role = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getRoleDAO().findRole(user.getRole());
                preparedStatement.setInt(3, role.getId());
                if (preparedStatement.executeUpdate() == 0)
                    throw new UserDataFormatException("Error: User wasn't created. UNKNOWN");
            } catch (SQLException e) {
                throw new UserDataFormatException("Error: Can't create user. " + e.getMessage());
            }
        }
    }

    @Override
    public User readUserById(int id) throws UserDataFormatException {
        if (!isIdExist(id))
            throw new UserDataFormatException("Error: Can't read user. That \"id\" doesn't exist.");
        return findUser(new User(id));
    }

    @Override
    public void updateUserById(User user) throws UserDataFormatException {
        if (!isIdExist(user.getId()))
            throw new UserDataFormatException("Error: Can't update user. That \"id\" doesn't exist.");
        String login = readUserById(user.getId()).getLogin();
        if (isExist(user.getLogin()) && !login.equals(user.getLogin()))
            throw new UserDataFormatException("Error: Can't update user. That Login '" + login + "' already exists");
        if (checkLogin(user.getLogin()) && checkPassword(user.getPassword())) {
            Role role = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getRoleDAO().findRole(user.getRole());
            String query = "UPDATE users SET login=?, password=?, role_id=? WHERE users_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setInt(3, role.getId());
                preparedStatement.setInt(4, user.getId());
                if (preparedStatement.executeUpdate() == 0)
                    throw new UserDataFormatException("Error: User wasn't updated. UNKNOWN");
            } catch (SQLException e) {
                throw new UserDataFormatException("Error: Can't update user. " + e.getMessage());
            }
        }
    }

    public void deleteUserById(int id) throws UserDataFormatException {
        String query = "DELETE FROM users WHERE users_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() == 0)
                throw new UserDataFormatException("Error: User wasn't deleted. UNKNOWN");
        } catch (SQLException e) {
            throw new UserDataFormatException("Error: Can't delete user. " + e.getMessage());
        }
    }

    public List<User> selectUserTO(User user) throws UserDataFormatException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users LEFT JOIN roles ON roles.roles_id=role_id" + getWhereFields(user);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("users_id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        new Role(resultSet.getString("role"))
                ));
            }
            return users;
        } catch (SQLException e) {
            throw new UserDataFormatException("Error: Can't select user. " + e.getMessage());
        }
    }

    private String getWhereFields(User user) {
        StringBuilder sb = new StringBuilder();
        if (user.getId() != 0)
            sb.append(String.format("users_id=%d ", user.getId()));
        if (user.getLogin() != null)
            sb.append(String.format("login='%s' ", user.getLogin()));
        if (user.getPassword() != null)
            sb.append(String.format("password='%s' ", user.getPassword()));
        if (user.getRole() != null)
            sb.append(String.format("role='%s' ", user.getRole()));
        String query;
        if (sb.length() > 0)
            return " WHERE " + sb.toString().trim().replaceAll(" ", " AND ");
        return "";
    }

    @Override
    public User findUser(User user) throws UserDataFormatException {
        String query = "SELECT * FROM users LEFT JOIN roles ON roles.roles_id=role_id" + getWhereFields(user);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("users_id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        new Role(resultSet.getString("role"))
                );
            }
        } catch (SQLException e) {
            throw new UserDataFormatException("Error: Can't find user. " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() throws UserDataFormatException {
        return selectUserTO(new User());
    }

    @Override
    public boolean isIdExist(int id) throws UserDataFormatException {
        if (id == 0)
            return false;
        return findUser(new User(id)) != null;
    }

    @Override
    public boolean isExist(String login) throws UserDataFormatException {
        return findUser(new User(login)) != null;
    }

    @Override
    public boolean isExist(String login, String password) throws UserDataFormatException {
        return findUser(new User(login, password)) != null;
    }

    private boolean checkLogin(String login) throws UserDataFormatException {
        if (login == null)
            throw new UserDataFormatException("Error: Login can't be null");
        if (login.length() < 2)
            throw new UserDataFormatException("Error: Login '" + login + "' is less than 2 symbols.");
        if (!login.matches("\\w{1,}"))
            throw new UserDataFormatException("Error: Login '" + login + "' must start with a letter and may contain symbols \"'A-Z'; 'a-z'; '0-9'; '_'\".");
        return true;
    }

    private boolean checkPassword(String password) throws UserDataFormatException {
        if (password == null)
            throw new UserDataFormatException("Error: Password can't be null");
        if (password.length() < 4)
            throw new UserDataFormatException("Error: Password '" + password + "' is less than 4 symbols.");
        if (!password.matches("\\w{1,}"))
            throw new UserDataFormatException("Error: Password '" + password + "' may contain symbols \"'A-Z'; 'a-z'; '0-9'; '_'\".");
        return true;
    }
}