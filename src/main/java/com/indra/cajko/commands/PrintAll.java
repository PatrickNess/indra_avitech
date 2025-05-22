package com.indra.cajko.commands;

import com.indra.cajko.db.UserDbService;

public final class PrintAll extends UserCommand {
    public PrintAll(UserDbService dbService) {
        super(dbService);
    }

    @Override
    public void execute() {
        dbService.getAllUsers().forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "PrintAll";
    }
}
