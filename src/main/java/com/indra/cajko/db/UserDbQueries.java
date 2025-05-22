package com.indra.cajko.db;

final class UserDbQueries {
    private UserDbQueries() {}

    final static String CREATE_USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS SUSERS (
                USER_ID BIGINT PRIMARY KEY,
                USER_GUID VARCHAR(255) NOT NULL UNIQUE,
                USER_NAME VARCHAR(255)
            )
            """;

    final static String INSERT_USER = "INSERT INTO SUSERS (USER_ID, USER_GUID, USER_NAME) VALUES (?, ?, ?)";

    final static String SELECT_ALL_USERS = "SELECT USER_ID, USER_GUID, USER_NAME FROM SUSERS";

    final static String DELETE_ALL_USERS = "DELETE FROM SUSERS";
}
