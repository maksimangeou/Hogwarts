package ru.hogwarts.school.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repositories.AvatarRepository;

import java.util.List;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public List<Avatar> getAllAvatarCollection(Integer pageNumber, Integer pageSize) {
        if (pageNumber == 0) {
            throw new RuntimeException();
        } else {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
            return avatarRepository.findAll(pageRequest).getContent();
        }
    }
}
