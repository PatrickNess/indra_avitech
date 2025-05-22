package com.indra.cajko;

import com.indra.cajko.commands.ExecutableCommand;
import com.indra.cajko.logging.LoggingUtils;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CommandConsumer<T extends ExecutableCommand> implements Runnable, LoggingUtils {
    private final Logger logger = logger();
    private final BlockingQueue<T> commandQueue;
    private final CompletableFuture<Void> producerDone;

    public CommandConsumer(BlockingQueue<T> commandQueue, CompletableFuture<Void> producerDone) {
        this.commandQueue = commandQueue;
        this.producerDone = producerDone;
    }

    @Override
    public void run() {
        logger.debug("CommandConsumer started");
        try {
            while (!commandQueue.isEmpty() || !producerDone.isDone()) {
                var command = commandQueue.poll(2, TimeUnit.SECONDS);
                logger.debug("Command received: {}", String.valueOf(command));
                Optional.ofNullable(command).ifPresent(T::execute);
            }
            logger.debug("CommandConsumer finished: producer done and queue empty.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            logger.debug("CommandConsumer interrupted, exiting.");
        }
    }
}
