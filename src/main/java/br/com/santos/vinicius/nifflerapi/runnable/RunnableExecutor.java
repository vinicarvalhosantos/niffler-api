package br.com.santos.vinicius.nifflerapi.runnable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class RunnableExecutor {

    @Value("${threads.to.be.used}")
    private int numberOfThreads;

    public void execute(Runnable worker) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        executor.execute(worker);
        executor.shutdown();
        executor.awaitTermination(10000L, TimeUnit.MILLISECONDS);

    }

}
