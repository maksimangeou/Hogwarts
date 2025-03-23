package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.*;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    public void createFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(lastId, faculty);
    }

    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            return null;
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public void deleteFaculty(long id) {
        faculties.remove(id);
    }

    public Collection<Faculty> findFacultiesByColor(String color) {
        List<Faculty> result = new ArrayList<>();
        for (Faculty value : faculties.values()) {
            if (value.getColor().equalsIgnoreCase(color)) {
                result.add(value);
            }
        }
        return result;
    }

}
