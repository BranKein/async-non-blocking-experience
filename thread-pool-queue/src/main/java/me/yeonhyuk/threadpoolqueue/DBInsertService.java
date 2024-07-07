package me.yeonhyuk.threadpoolqueue;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class DBInsertService {

    private final GeneratedResultRepository generatedResultRepository;

    @Async("dBInsertExecutor")
    @Transactional
    public CompletableFuture<Void> insert() {
        GeneratedResult generatedResult = IDGeneratingService.asyncQueue.asyncPoll();
//        log.info(String.format("[%d] Insert into DB", generatedResult.getId()));
        if (Thread.currentThread().getName().startsWith("ID-GENERATE")) {
//            Thread t = Thread.currentThread();
            log.info(String.format("[%d] Insert into DB", generatedResult.getId()));
        }
        this.generatedResultRepository.save(generatedResult);
        return CompletableFuture.completedFuture(null);
    }
}
