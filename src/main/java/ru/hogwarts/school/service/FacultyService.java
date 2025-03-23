package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.Map;
@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    public Faculty createStudent(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(lastId, faculty);
        return faculty;
    }

    public Faculty findStudent(long id) {
        return faculties.get(id);
    }

    public Faculty updateStudent(Faculty faculty) {
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteStudent(long id) {
        return faculties.remove(id);
    }

}
