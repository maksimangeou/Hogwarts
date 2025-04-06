package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repositories.AvatarRepository;

import java.util.List;

@Service
public class AvatarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvatarService.class);


    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public List<Avatar> getAllAvatarCollection(Integer pageNumber, Integer pageSize) {
        LOGGER.info("Вызван метод getAllAvatarCollection({}, {})", pageNumber, pageSize);

        if (pageNumber == 0) {
            LOGGER.error("Поле pageNumber принимает значение отрицательного числа");
            throw new RuntimeException();
        } else {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
            List<Avatar> avatarList = avatarRepository.findAll(pageRequest).getContent();
            LOGGER.debug("Список аватарок {} для страницы {} и размера {}", avatarList, pageNumber, pageSize);
            return avatarList;
        }
    }
}
