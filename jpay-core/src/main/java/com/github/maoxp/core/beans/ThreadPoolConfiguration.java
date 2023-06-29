package com.github.maoxp.core.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * ThreadPoolConfiguration
 *
 * @author mxp
 * @date 2023/6/7年06月07日 14:31
 * @since JDK 1.8
 */

@Configuration
public class ThreadPoolConfiguration {
    /**
     * 核心线程池大小，默认的核心线程的1，向线程池提交一个任务时，如果线程池已经创建的线程数小于核心线程数，
     * 即使此时存在空闲线程，也会通过创建一个新线程来执行新任务，知道创建的线程等于核心线程数时，如果有空闲线程，则使用空闲线程。
     */
    private static final int CORE_POOL_SIZE = 50;

    /**
     * 最大可创建的线程数
     * 默认的最大线程数是Integer.MAX_VALUE 即2<sup>31</sup>-1
     */
    private static final int MAX_POOL_SIZE = 50;

    /**
     * 缓冲队列数，默认的缓冲队列数是Integer.MAX_VALUE 即2<sup>31</sup>-1，用于保存执行任务的阻塞队列。
     */
    private static final int QUEUE_CAPACITY = 1000;

    /**
     * 线程池维护线程所允许的空闲时间,默认的线程空闲时间为60秒。
     * 当线程中的线程数大于核心线程数时，线程的空闲时间如果超过线程的存活时间，则此线程会被销毁，直到线程池中的线程数小于等于核心线程数时。
     */
    private static final int KEEP_ALIVE_SECONDS = 60;

    /**
     * 异步线程池前缀名
     */
    private static final String ASYNC_THREAD_NAME_PREFIX = "Task_AsyncThreadPool-";


    /**
     * 定时线程池前缀名
     */
    private static final String SCHEDULED_THREAD_NAME_PREFIX = "Task_SchedulerThreadPool-";


    /**
     * 执行周期性或定时任务
     * <blockquote>
     * Example code:
     * <pre>
     *     {@code ScheduledFuture<?> future = scheduler.schedule(new Runnable(xx), new CronTrigger(cron))}
     * </pre>
     * </blockquote>
     */
    @Bean("taskScheduler")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(CORE_POOL_SIZE);
        scheduler.setThreadNamePrefix(SCHEDULED_THREAD_NAME_PREFIX);
        return scheduler;
    }

    /**
     * 定义一个主要用于异步任务的执行的线程池
     * <blockquote>
     * Example code:
     * <pre>
     *      {@code @Async("taskExecutor") //可以加在类上或方法上}
     *      {@code taskExecutor.execute();}
     * </pre>
     * </blockquote>
     *
     * @return
     */
    @Bean("taskExecutor")
    @SuppressWarnings("all")
    public ThreadPoolTaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        taskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        taskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        taskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        taskExecutor.setThreadNamePrefix(ASYNC_THREAD_NAME_PREFIX);
        /**
         * 销毁机制,默认是false
         * <p>
         * allowCoreThreadTimeOut为true则线程池数量最后销毁到0个。
         * <p>
         * allowCoreThreadTimeOut为false销毁机制：超过核心线程数时，而且（超过最大值或者timeout过），就会销毁。
         */
        boolean allowCoreThreadTimeOut = false;
        taskExecutor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程池初始化
        taskExecutor.initialize();
        return taskExecutor;
    }

}
