package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Info;

@Service
public class InfoService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(InfoService.class);


    @Value("${server.port}")
    private int port;

    public Info getPort() {
        LOGGER.info("Запуск метода getPort в portFirstType");
        Info info = new Info();
        info.setPort(port);
        LOGGER.debug("portFirstType = {}", info);
        return info;
    }
}
