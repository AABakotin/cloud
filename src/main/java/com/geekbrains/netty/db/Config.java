package com.geekbrains.netty.db;

public class Config {

    protected static final String DRIVER = "org.sqlite.JDBC";

    protected static final String CONNECTION = "jdbc:sqlite:db/clients.db";

    protected static final String GET_LOCATION = "select location from clients where login = ? and password = ?;";

    protected static final String ADD_NEW_USER = "insert into clients (login, password, location) values (?, ?, ?);";

    protected static final String DELETE_USER = "";

    protected static final String GET_LOGIN = "select login from clients where login = ?";

    protected static final String CHANGE_PASSWORD = "update clients set password = ? where login = ? and password = ?";

    protected static final String CREATE_DB = "create table if not exists clients (id integer primary key autoincrement," +
            " login text unique not null, password text not null, location text not null);";

    protected static final String INIT_DB = "insert into clients (login, password, location) " +
            "values ('log1', 'pass1', 'storage/log1'), ('log2', 'pass2', 'storage/log2'), ('log3', 'pass3', 'storage/log3');";

}
