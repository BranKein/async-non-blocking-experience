package me.yeonhyuk.threadpoolqueue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/thread-queue-test")
@RequiredArgsConstructor
public class TestController {
    private final IDGeneratingService idGeneratingService;

    @PostMapping("/start")
    public String start() {
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();
        Long start = System.currentTimeMillis();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            log.info(String.format("Iteration %s Start", i));
            CompletableFuture<Void> c = this.idGeneratingService.generate10000Id(i, Duration.ofMillis(random.nextLong(100)), start);
            futures.add(c);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAccept(o -> {
                    Long end = System.currentTimeMillis();
                    System.out.printf("Controller Duration: %d ms%n", end - start);
                });

        return "return immediately";
    }
}
