package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hogwarts.school.model.Info;

public interface InfoService {

    Info getPort();

    Logger logger = LoggerFactory.getLogger(InfoService.class);
}
