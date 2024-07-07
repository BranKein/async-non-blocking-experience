package me.yeonhyuk.threadpoolqueue;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {

    @Qualifier("dBInsertExecutor")
    @Bean("dBInsertExecutor")
    public TaskExecutor DBInsertExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        // 기본 스레드 수 (CorePoolSize 만큼 스레드 생성)
        threadPoolTaskExecutor.setCorePoolSize(4);

        // 전체 스레드 수.
        threadPoolTaskExecutor.setMaxPoolSize(4);

        // CorePoolSize가 꽉 차면 Queue에 저장되어 대기.
        // Queue가 꽉 차게 되면 max 사이즈만큼 스레드 호출하여 처리.
//        threadPoolTaskExecutor.setQueueCapacity(100);

        // 기본 스레드 수를 유지하기 위한 설정.
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(false);

        // Max 스레드 수와 Queue가 꽉 차면 RejectedExecutionException 발생.
        // CallerRunsPolicy 설정하여 예외와 누락없이 최대한 처리하도록 설정.
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 서버가 종료되기 전 Queue에 남은 모든 작업이 수행되도록 설정 (모든 작업 수행 후 종료)
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(false);

        // Thread 이름 지정
        threadPoolTaskExecutor.setThreadNamePrefix("DB-INSERT-");

        return threadPoolTaskExecutor;
    }

    @Qualifier("iDGeneratorExecutor")
    @Bean("iDGeneratorExecutor")
    public TaskExecutor IDGeneratorExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        // 기본 스레드 수 (CorePoolSize 만큼 스레드 생성)
        threadPoolTaskExecutor.setCorePoolSize(4);

        // 전체 스레드 수.
        threadPoolTaskExecutor.setMaxPoolSize(4);

        // CorePoolSize가 꽉 차면 Queue에 저장되어 대기.
        // Queue가 꽉 차게 되면 max 사이즈만큼 스레드 호출하여 처리.
        threadPoolTaskExecutor.setQueueCapacity(100);

        // 기본 스레드 수를 유지하기 위한 설정.
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(false);

        // Max 스레드 수와 Queue가 꽉 차면 RejectedExecutionException 발생.
        // CallerRunsPolicy 설정하여 예외와 누락없이 최대한 처리하도록 설정.
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 서버가 종료되기 전 Queue에 남은 모든 작업이 수행되도록 설정 (모든 작업 수행 후 종료)
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(false);

        // Thread 이름 지정
        threadPoolTaskExecutor.setThreadNamePrefix("ID-GENERATE-");

        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {}
        };
    }
}
