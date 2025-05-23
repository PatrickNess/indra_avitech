package com.indra.cajko;

import com.indra.cajko.commands.Add;
import com.indra.cajko.commands.DeleteAll;
import com.indra.cajko.commands.PrintAll;
import com.indra.cajko.commands.UserCommand;
import com.indra.cajko.db.UserDbService;
import com.indra.cajko.db.UserDbServiceImpl;
import com.indra.cajko.dto.User;
import com.indra.cajko.logging.LoggingUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class Main implements LoggingUtils {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        UserDbService userDbService = UserDbServiceImpl.INSTANCE;
        BlockingQueue<UserCommand> queue = new LinkedBlockingQueue<>(3);
        // flag which indicates that consumer is done with its job
        var donePromise = new CompletableFuture<Void>();

        final var commandProducer = new CommandProducer<>(queue, getUserCommands(userDbService), donePromise);
        final var commandConsumer = new CommandConsumer<>(queue, donePromise);

        try {
            new Thread(commandProducer).start();
            new Thread(commandConsumer).start();
        } catch (Exception e) {
            logger.error("Not handled exception, exiting application.", e);
            System.exit(1);
        }
    }

    private static ArrayList<UserCommand> getUserCommands(UserDbService userDbService) {
        return new ArrayList<>(List.of(
                new Add(userDbService, new User(1, "a1", "Robert")),
                new Add(userDbService, new User(2, "a2", "Martin")),
                new PrintAll(userDbService),
                new DeleteAll(userDbService),
                new PrintAll(userDbService)
        ));
    }
}