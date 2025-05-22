package com.indra.cajko;

import com.indra.cajko.logging.LoggingUtils;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class CommandProducer<T> implements Runnable, LoggingUtils {
    private final Logger logger = logger();
    private final BlockingQueue<T> dataQueue;
    private final List<T> commandsToProduce;
    private final CompletableFuture<Void> jobDone;

    public CommandProducer(BlockingQueue<T> dataQueue, List<T> commandsToProduce, CompletableFuture<Void> jobDone) {
        this.dataQueue = dataQueue;
        this.commandsToProduce = commandsToProduce;
        this.jobDone = jobDone;
    }

    @Override
    public void run() {
        logger.debug("Starting command producer");
        commandsToProduce.forEach(command -> {
            try {
                logger.debug("Putting command: {}", command);
                dataQueue.put(command);
                logger.debug("Command successfully put: {}", command);
            } catch (InterruptedException e) {
                logger.warn("Interrupted while putting command {}", command, e);
                Thread.currentThread().interrupt();
            }
        });
        jobDone.complete(null);
    }
}