package citibank.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpotStoreImplTest {

    @Test
    void name() {
        final var spotStore = new SpotStoreImpl();

        final var dateTime = LocalDateTime.of(2020, 10, 10, 10, 10, 10);
        final var executorService = Executors.newFixedThreadPool(100);

        IntStream.range(0, 100000)
                .mapToObj(i -> CompletableFuture.runAsync(
                        () -> {
                            spotStore.add("RUBUSD", 14.12 + i, dateTime.plusHours(i));
                            spotStore.add("RUBEUR", 10.3 + i, dateTime.plusHours(i));
                            spotStore.add("ABCEUR", 108.3 + i, dateTime.plusHours(i));
                        }, executorService)
                )
                .forEach(CompletableFuture::join);

        final var rubusd = spotStore.get("RUBUSD", dateTime.plusSeconds(10L));

        assertEquals(14.12, rubusd);
    }
}