package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Info;

@Service
@Profile("portFirstType")
public class InfoServiceFirstType implements InfoService {

    @Value("${server.port}")
    private int port;

    public Info getPort() {
        logger.info("Запуск метода getPort в portFirstType");
        Info info = new Info();
        info.setPort(port);
        logger.debug("portFirstType = {}", info);
        return info;
    }
}
