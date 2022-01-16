package com.geekbrains.netty.db;


import lombok.extern.slf4j.Slf4j;

import java.sql.*;


@Slf4j
public class DataBaseHandler extends Config {

    private static Connection dbConnection;
    private DataBaseHandler instance;

    public DataBaseHandler() {
        try {
            dbConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        createDB();
    }


    public static void dbConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        dbConnection = DriverManager.getConnection(CONNECTION);
        log.debug("DB connected");
    }

    public static void closeConnection() {
        try {
            if (dbConnection != null) dbConnection.close();
            log.debug("Disconnected from db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public DataBaseHandler getInstance() {
        if (instance != null) return instance;
        instance = new DataBaseHandler();
        return instance;
    }


    private void createDB() {
            try (Statement st = dbConnection.createStatement()) {
                st.execute(CREATE_DB);
//            st.execute(INIT_DB);
            } catch (SQLException e) {
                log.debug(e.toString());
            }

        }



    public static void addNewUser(String login, String password, String location) {
        try (PreparedStatement ps = dbConnection.prepareStatement(ADD_NEW_USER)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, location);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String getLogin(String login) {
        try (PreparedStatement ps = dbConnection.prepareStatement(GET_LOGIN)) {
            ps.setString(1, login);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString(1);
                return name;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getLocation(String login, String password) {
        try (PreparedStatement ps = dbConnection.prepareStatement(GET_LOCATION)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.debug("User not found");
        }
        return null;
    }


    public static void ChangePass(String oldPassword, String newPassword) {
        try (PreparedStatement ps = dbConnection.prepareStatement(CHANGE_PASSWORD)) {
            ps.setString(1, newPassword);
            ps.setString(1, oldPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String login, String password) {
        try (PreparedStatement ps = dbConnection.prepareStatement(DELETE_USER)) {
            ps.setString(1, login);
            ps.setString(2, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

