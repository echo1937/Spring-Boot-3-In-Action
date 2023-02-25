package com.example.threads;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

class ThreadPoolTaskExecutorUnitTest {

    void startThreads(ThreadPoolTaskExecutor taskExecutor, CountDownLatch countDownLatch, int numThreads) {
        for (int i = 0; i < numThreads; i++) {
            taskExecutor.execute(() -> {
                try {
                    Thread.sleep(100L * ThreadLocalRandom.current().nextLong(1, 10));
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    @Test
    void whenUsingDefaults_thenSingleThread() {
        // defines a corePoolSize of one thread, an unbounded maxPoolSize, and an unbounded queueCapacity
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.afterPropertiesSet();

        // https://www.cnblogs.com/Andya/p/12925634.html
        CountDownLatch countDownLatch = new CountDownLatch(10);
        this.startThreads(taskExecutor, countDownLatch, 10);

        // 运行至此时, 所有的task都已经提交
        while (countDownLatch.getCount() > 0) {
            Assertions.assertEquals(1, taskExecutor.getPoolSize());
        }
    }

    @Test
    void whenCorePoolSizeFive_thenFiveThreads() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.afterPropertiesSet();

        CountDownLatch countDownLatch = new CountDownLatch(10);
        this.startThreads(taskExecutor, countDownLatch, 10);

        while (countDownLatch.getCount() > 0) {
            Assertions.assertEquals(5, taskExecutor.getPoolSize());
        }
    }

    @Test
    void whenCorePoolSizeFiveAndMaxPoolSizeTen_thenFiveThreads() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.afterPropertiesSet();

        CountDownLatch countDownLatch = new CountDownLatch(10);
        this.startThreads(taskExecutor, countDownLatch, 10);

        while (countDownLatch.getCount() > 0) {
            Assertions.assertEquals(5, taskExecutor.getPoolSize());
        }
    }

    @Test
    void whenCorePoolSizeFiveAndMaxPoolSizeTenAndQueueCapacityZero_thenTenThreads() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.afterPropertiesSet();

        CountDownLatch countDownLatch = new CountDownLatch(10);
        this.startThreads(taskExecutor, countDownLatch, 10);

        while (countDownLatch.getCount() > 0) {
            Assertions.assertEquals(10, taskExecutor.getPoolSize());
        }
    }

    @Test
    void whenCorePoolSizeFiveAndMaxPoolSizeTenAndQueueCapacityTen_thenTenThreads() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.afterPropertiesSet();

        CountDownLatch countDownLatch = new CountDownLatch(20);
        this.startThreads(taskExecutor, countDownLatch, 20);

        while (countDownLatch.getCount() > 0) {
            Assertions.assertEquals(10, taskExecutor.getPoolSize());
        }
    }
}
