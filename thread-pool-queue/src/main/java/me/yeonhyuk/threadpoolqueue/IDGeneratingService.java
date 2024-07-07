package me.yeonhyuk.threadpoolqueue;

import com.github.BranKein.mp_id_generator.snowflake.SnowFlakeGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FutureUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class IDGeneratingService {

    public static AsyncQueue asyncQueue = new AsyncQueue();

    private final DBInsertService dbInsertService;

    private SnowFlakeGenerator idGenerator;

    @PostConstruct
    public void init() {
        this.idGenerator = SnowFlakeGenerator.getGenerator(100L);
    }

    @Async("iDGeneratorExecutor")
    public CompletableFuture<Void> generate10000Id(int number, Duration delay, Long start) {
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            int id = number * 10000 + i;

            if (Thread.currentThread().getName().startsWith("DB-INSERT")) {
                Thread t = Thread.currentThread();
                log.info(String.format("ID generated"));
            }


            GeneratedResult g = GeneratedResult.builder()
                    .id(id)
                    .delay(delay.toMillis())
                    .newId(this.idGenerator.generateId())
                    .createDate(LocalDateTime.now())
                    .build();

            IDGeneratingService.asyncQueue.asyncAdd(g);

            CompletableFuture<Void> c = dbInsertService.insert();
            futures.add(c);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAccept(o -> {
                    Long end = System.currentTimeMillis();
                    System.out.printf("IDGS Duration: %d ms%n", end - start);
                });

        return CompletableFuture.completedFuture(null);
    }

}
