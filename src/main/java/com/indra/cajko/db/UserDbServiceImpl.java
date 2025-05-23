package com.indra.cajko.db;
import  com.indra.cajko.dto.User;
import com.indra.cajko.logging.LoggingUtils;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.indra.cajko.db.UserDbQueries.*;

public enum UserDbServiceImpl implements LoggingUtils, UserDbService {
    INSTANCE;

    private final Logger logger = logger();
    private final Connection dbConnection;

    UserDbServiceImpl() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            logger.debug("Database connection established");
            initDbTable();
        } catch (SQLException e) {
            // we want the application to fail in case connection to db is not successful or User table not created
            throw new RuntimeException(e);
        }
    }

    private void initDbTable() throws SQLException {
        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(CREATE_USERS_TABLE);
            logger.debug("Users table created");
        }
    }

    @Override
    public void insertUser(User user) {
        try (PreparedStatement pstmt = dbConnection.prepareStatement(INSERT_USER)) {
            pstmt.setLong(1, user.id());
            pstmt.setString(2, user.guid());
            pstmt.setString(3, user.name());
            pstmt.executeUpdate();
            logger.info("Inserted user: {}", user);
        } catch (SQLException e) {
            logger.error("Error inserting user", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement pstmt = dbConnection.prepareStatement(SELECT_ALL_USERS);
             var rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("USER_ID"),
                        rs.getString("USER_GUID"),
                        rs.getString("USER_NAME")
                ));
            }
        } catch (SQLException e) {
            logger.error("Error fetching users", e);
        }
        return users;
    }

    @Override
    public void deleteAllUsers() {
        try (Statement stmt = dbConnection.createStatement()) {
            int deleted = stmt.executeUpdate(DELETE_ALL_USERS);
            logger.info("Deleted {} users from SUSERS table", deleted);
        } catch (SQLException e) {
            logger.error("Error deleting all users", e);
        }
    }
}