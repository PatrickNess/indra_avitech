package com.indra.cajko;

import com.indra.cajko.commands.ExecutableCommand;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CommandConsumerTest {

    @Test
    void testCommandIsExecuted() throws InterruptedException {
        // Arrange
        ExecutableCommand command = mock(ExecutableCommand.class);
        BlockingQueue<ExecutableCommand> queue = new ArrayBlockingQueue<>(1);
        queue.put(command);

        CompletableFuture<Void> producerDone = new CompletableFuture<>();
        CommandConsumer<ExecutableCommand> consumer = new CommandConsumer<>(queue, producerDone);

        // Act
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        // Mark producer as done after a short delay
        Thread.sleep(100);
        producerDone.complete(null);
        consumerThread.join();

        // Assert
        verify(command, times(1)).execute();
    }

    @Test
    void testInterruptedExceptionHandling() throws InterruptedException {
        // Arrange
        BlockingQueue<ExecutableCommand> queue = new ArrayBlockingQueue<>(1);
        CompletableFuture<Void> producerDone = new CompletableFuture<>();
        CommandConsumer<ExecutableCommand> consumer = new CommandConsumer<>(queue, producerDone);

        Thread consumerThread = new Thread(consumer);

        // Act
        consumerThread.start();
        Thread.sleep(100); // Let the consumer start and block on poll
        consumerThread.interrupt();
        consumerThread.join();

        // Assert
        // No exception should be thrown, and the thread should be terminated
        assertFalse(consumerThread.isAlive());
    }

    @Test
    void testConsumerWaitsForProducerWhenQueueIsEmpty() throws Exception {
        // Arrange
        BlockingQueue<ExecutableCommand> queue = new ArrayBlockingQueue<>(1);
        CompletableFuture<Void> producerDone = new CompletableFuture<>();
        CommandConsumer<ExecutableCommand> consumer = new CommandConsumer<>(queue, producerDone);
        ExecutableCommand command = mock(ExecutableCommand.class);

        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        // Act: Add command after a delay
        Thread.sleep(300);
        queue.put(command);
        Thread.sleep(100); // Give consumer time to process

        // Producer not done yet, so consumer should still be running
        assertFalse(producerDone.isDone());
        assertTrue(consumerThread.isAlive());

        // Now finish producer and wait for consumer to exit
        producerDone.complete(null);
        consumerThread.join();

        // Assert
        verify(command, times(1)).execute();
    }
}
