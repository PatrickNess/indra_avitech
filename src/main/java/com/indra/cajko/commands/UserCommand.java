package com.indra.cajko.commands;

import com.indra.cajko.db.UserDbService;

public abstract class UserCommand implements ExecutableCommand {
    protected final UserDbService dbService;

    protected UserCommand(UserDbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public abstract void execute();
}

