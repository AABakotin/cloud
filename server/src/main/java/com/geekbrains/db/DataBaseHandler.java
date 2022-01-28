package com.geekbrains.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class DataBaseHandler extends Config {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseHandler.class);
    private static Connection dbConnection;
    private static DataBaseHandler instance;


    public static void dbConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        dbConnection = DriverManager.getConnection(CONNECTION);
        createDB();
        logger.info("SQL data base connected");

    }

    public static void closeConnection() {
        try {
            if (dbConnection != null) dbConnection.close();
            logger.info("Disconnected from db");
        } catch (SQLException e) {
            logger.debug(e.toString());
        }
    }

    private static void createDB() {
        try (Statement st = dbConnection.createStatement()) {
            st.execute(CREATE_DB);
//            st.execute(INIT_DB);
        } catch (SQLException e) {
            logger.error("Creating db error");
        }
    }

    public static void addNewUser(String login, String password, String location) {
        try (PreparedStatement ps = dbConnection.prepareStatement(ADD_NEW_USER)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, location);
            ps.executeUpdate();
            logger.info("Registration complete " + login);
            logger.info("SQL Update");
        } catch (SQLException e) {
            logger.warn("User not add in db " + login);
        }
    }

    public static String getLogin(String login) {
        try (PreparedStatement ps = dbConnection.prepareStatement(GET_LOGIN)) {
            ps.setString(1, login);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            logger.warn(e + "LOGIN -> " + login);
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
            logger.debug("Login NOT Found: " + login);
        }
        return null;
    }


    public static int ChangePass(String newPassword, String oldPassword, String login) {
        try (PreparedStatement ps = dbConnection.prepareStatement(CHANGE_PASSWORD)) {
            ps.setString(1, newPassword);
            ps.setString(2, oldPassword);
            ps.setString(3, login);
            if (ps.executeUpdate() > 0) {
                logger.info("SQL Update");
                return 1;
            }
        } catch (SQLException e) {
            logger.error("Change password fail, more info -> " + e);
        }
        return -1;
    }

    public static int deleteUser(String login, String password) {
        try (PreparedStatement ps = dbConnection.prepareStatement(DELETE_USER)) {
            ps.setString(1, login);
            ps.setString(2, password);
            if (ps.executeUpdate() > 0){
                logger.info("SQL Update");
                return 1;
            }
        } catch (SQLException e) {
            logger.error("Delete user fail, more info -> " + e);
        }
        return -1;
    }
}

