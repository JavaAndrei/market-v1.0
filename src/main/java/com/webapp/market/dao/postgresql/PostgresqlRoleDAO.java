package com.webapp.market.dao.postgresql;

import com.webapp.market.dao.RoleDAO;
import com.webapp.market.dao.entities.Role;
import com.webapp.market.exceptions.UserDataFormatException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresqlRoleDAO implements RoleDAO {

    private Connection connection;

    public PostgresqlRoleDAO() {
        connection = PostgresqlDAOFactory.createConnection();
    }

    /*@Override
    public boolean insertRole(Role role) throws UserDataFormatException {
        String query = "INSERT INTO roles(role) VALUES(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, role.getName());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new UserDataFormatException("Error: Can't create role. " + e.getMessage());
        }
    }*/

    @Override
    public Role findRole(Role role) throws UserDataFormatException {
        String query = "SELECT * FROM roles" + getWhereFields(role);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new Role(resultSet.getInt("roles_id"), resultSet.getString("role"));
            }
        } catch (SQLException e) {
            throw new UserDataFormatException("Error: Can't find role. " + e.getMessage());
        }
        return null;
    }

    /*@Override
    public boolean updateRole(Role role) {
        System.out.println("\"public boolean updateRole(Role role)\" hasn't implemented yet");
        return false;
    }*/

    /*@Override
    public boolean deleteRole(Role role) throws UserDataFormatException {
        String query = "DELETE FROM roles WHERE roles_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, role.getId());
            if (preparedStatement.executeUpdate() == 0)
                throw new UserDataFormatException("Error: Role wasn't deleted. UNKNOWN");
        } catch (SQLException e) {
            throw new UserDataFormatException("Error: Can't delete role. " + e.getMessage());
        }
        return false;
    }*/

    @Override
    public List<Role> selectRoleTO(Role role) throws UserDataFormatException {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT * FROM roles" + getWhereFields(role);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                roles.add(new Role(resultSet.getInt("roles_id"), resultSet.getString("role")));
            }
            return roles;
        } catch (SQLException e) {
            throw new UserDataFormatException("Error: Can't select role. " + e.getMessage());
        }
    }

    private String getWhereFields(Role role) {
        StringBuilder sb = new StringBuilder();
        if (role.getId() != 0)
            sb.append(String.format("roles_id=%d ", role.getId()));
        if (role.getName() != null)
            sb.append(String.format("role='%s' ", role.getName()));
        if (sb.length() > 0)
            return " WHERE " + sb.toString().trim().replaceAll(" ", " AND ");
        return "";
    }
}
