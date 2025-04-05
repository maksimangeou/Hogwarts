package ru.hogwarts.school.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Info;


@Service
@Profile("portSecondType")
public class InfoServiceSecondType implements InfoService {

    public Info getPort() {
        logger.info("Запуск метода getPort в portSecondType");
        Info info = new Info();
        info.setPort(4040);
        logger.debug("portSecondType = {}", info);
        return info;
    }
}
