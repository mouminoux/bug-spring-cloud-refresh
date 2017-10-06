package com.example.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ContextRefresher contextRefresher;

    @Test
    public void logDebugDisabled() {
        tryToLogDebug();
    }

    @Test
    public void logDebugDisabledWhileRefreshingContext() {
        Thread thread = new Thread(() -> {
            IntStream.range(1, 1000).forEach(i -> {
                contextRefresher.refresh();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            });
        });
        thread.start();

        tryToLogDebug();
    }

    private void tryToLogDebug() {
        IntStream.range(1, 1000).forEach(i -> {
            if (logger.isDebugEnabled()) {
                throw new RuntimeException("This log level should not be enabled!");
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        });
    }
}
