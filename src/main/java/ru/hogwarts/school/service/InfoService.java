package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Info;

import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Service
public class InfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfoService.class);


    @Value("${server.port}")
    private int port;

    public Info getPort() {
        LOGGER.info("Запуск метода getPort в portFirstType");
        Info info = new Info();
        info.setPort(port);
        LOGGER.debug("portFirstType = {}", info);
        return info;
    }


    public long getSumProgression() {
        return LongStream.iterate(1, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .sum();
    }

    public long getSumProgressionV2() {
        int a1 = 1;
        long n = 1_000_000;
        int an = 1_000_000;
        return n * (a1 + an) / 2;
    }
}
