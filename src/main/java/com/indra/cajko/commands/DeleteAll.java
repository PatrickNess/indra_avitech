package com.indra.cajko.commands;

import com.indra.cajko.db.UserDbService;

public final class DeleteAll extends UserCommand {
    public DeleteAll(UserDbService dbService) {
        super(dbService);
    }

    @Override
    public void execute() {
        dbService.deleteAllUsers();
    }

    @Override
    public String toString() {
        return "DeleteAll";
    }
}
