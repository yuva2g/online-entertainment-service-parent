package com.myhobbies.online.entertainmentservice.config;

import com.myhobbies.online.entertainmentservice.config.albumsqueryservice.AlbumsQueryServiceProperties;
import com.myhobbies.online.entertainmentservice.config.booksqueryservice.BooksQueryServiceProperties;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

@Configuration
@EnableConfigurationProperties({AlbumsQueryServiceProperties.class, BooksQueryServiceProperties.class, AsyncProperties.class})
public class AppConfiguration {


    @Bean(name = "asyncExecutor", destroyMethod = "shutdown")
    public ExecutorService asyncExecutor(MeterRegistry meterRegistry, AsyncProperties asyncProperties) {
        ThreadPoolTaskExecutor executor = getThreadPoolTaskExecutor(asyncProperties);
        executor.initialize();
        ExecutorService executorService = ExecutorServiceMetrics.monitor(meterRegistry,
                executor.getThreadPoolExecutor(),
                "entertainment_executor");
        return executorService;
//        return ContextExecutorService.wrap(
//                executorService, ContextSnapshotFactory.builder().build()::captureAll);
    }

    private static ThreadPoolTaskExecutor getThreadPoolTaskExecutor(AsyncProperties asyncProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("tcs-%d").build();
//        executor.setThreadFactory(threadFactory);
        executor.setCorePoolSize(asyncProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncProperties.getQueueCapacity());
        executor.setThreadNamePrefix(asyncProperties.getAsyncThreadNamePrefix());
        return executor;
    }

}
