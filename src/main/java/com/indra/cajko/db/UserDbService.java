package com.indra.cajko.db;

import  com.indra.cajko.dto.User;

import java.util.List;

public interface UserDbService {
    void insertUser(User user);
    List<User> getAllUsers();
    void deleteAllUsers();
}
