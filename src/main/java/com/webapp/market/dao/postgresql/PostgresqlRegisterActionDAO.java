package com.webapp.market.dao.postgresql;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.dao.RegisterActionDAO;
import com.webapp.market.dao.entities.RegisterAction;
import com.webapp.market.dao.entities.User;
import com.webapp.market.exceptions.UserDataFormatException;

import java.sql.*;

public class PostgresqlRegisterActionDAO implements RegisterActionDAO {

    private Connection connection;

    public PostgresqlRegisterActionDAO() {
        connection = PostgresqlDAOFactory.createConnection();
    }

    @Override
    public boolean insertRegisterAction(RegisterAction registerAction) {
        String query = "INSERT INTO register_actions (user_id, date_time, action) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            User user = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO().findUser(registerAction.getUser());
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setTimestamp(2, new Timestamp(registerAction.getDate().getTime()));
            preparedStatement.setString(3, registerAction.getAction());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | UserDataFormatException e) {
            e.printStackTrace();
        }
         return false;
    }

    /*@Override
    public List<RegisterAction> selectRegisterActionTO() throws UserDataFormatException {
        List<RegisterAction> registerActions = new ArrayList<>();
        String query = "SELECT * FROM register_actions LEFT JOIN users ON users_id=register_actions_id";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = DAOFactory
                        .getDAOFactory(DAOFactory.POSTGRESQL)
                        .getUserDAO()
                        .findUser(new User(resultSet.getInt("user_id")));
                registerActions.add(new RegisterAction(
                        resultSet.getInt("register_actions_id"),
                        user,
                        resultSet.getTimestamp("date_time"),
                        resultSet.getString("action")
                ));
            }
            return registerActions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*@Override
    public List<RegisterAction> selectRegisterActionTO(RegisterAction registerAction, Timestamp begin, Timestamp end) throws UserDataFormatException {
        List<RegisterAction> registerActions = new ArrayList<>();
        String query =
                "SELECT register_actions.*, login \n" +
                "FROM register_actions \n" +
                "LEFT JOIN users \n" +
                "ON users_id=user_id\n" +
                "WHERE " + prepareCondition(registerAction) + " date_time>=? AND date_time<=?";
                //"WHERE user_id=4 AND action='connect' AND date_time>='2022-04-25 00:00:00' AND date_time<='2022-04-25 23:59:59.999'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setTimestamp(1, begin);
            preparedStatement.setTimestamp(2, end);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = DAOFactory
                        .getDAOFactory(DAOFactory.POSTGRESQL)
                        .getUserDAO()
                        .findUser(new User(resultSet.getInt("user_id")));
                registerActions.add(new RegisterAction(
                        resultSet.getInt("register_actions_id"),
                        user,
                        resultSet.getTimestamp("date_time"),
                        resultSet.getString("action")
                ));
            }
            return registerActions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*private String prepareCondition(RegisterAction registerAction) throws UserDataFormatException {
        String result = "";
        if (registerAction.getUser() != null && registerAction.getUser().getLogin() != null) {
            User user = DAOFactory
                    .getDAOFactory(DAOFactory.POSTGRESQL)
                    .getUserDAO()
                    .findUser(registerAction.getUser());
            result = String.format("user_id=%d AND", user.getId());
        }
        if (registerAction.getAction() != null) {
            result = result + String.format(" action='%s' AND", registerAction.getAction());
        }
        return "user_id=4 AND action='connect' AND";
    }*/
}

