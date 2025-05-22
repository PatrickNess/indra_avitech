package com.indra.cajko.commands;

import com.indra.cajko.db.UserDbService;
import com.indra.cajko.dto.User;

public final class Add extends UserCommand {
    private final User user;

    public Add(UserDbService userDbService, User user) {
        super(userDbService);
        this.user = user;
    }

    @Override
    public void execute() {
        dbService.insertUser(user);
    }

    @Override
    public String toString() {
        return "Add{user=" + user + "}";
    }
}
