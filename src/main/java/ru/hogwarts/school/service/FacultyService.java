package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        LOGGER.info("Вызван метод createFaculty({})", faculty);

        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        LOGGER.info("Вызван метод findFaculty({})", id);

        return facultyRepository.findById(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        LOGGER.info("Вызван метод updateFaculty({})", faculty);

        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        LOGGER.info("Вызван метод deleteFaculty({})", id);

        facultyRepository.deleteById(id);
    }

    public List<Faculty> findFacultiesByColor(String color) {
        LOGGER.info("Вызван метод findFacultiesByColor({})", color);

        return facultyRepository.findByColor(color);
    }


    public List<Faculty> findFacultyByNameIgnoreCaseOrColorIgnoreCase(String name, String color) {
        LOGGER.info("Вызван метод findFacultyByNameIgnoreCaseOrColorIgnoreCase({}, {})", name, color);

        return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

}
