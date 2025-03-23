package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long lastId = 0;

    public void createStudent(Student student) {
        student.setId(++lastId);
        students.put(lastId, student);
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student updateStudent(Student student) {
        if (!students.containsKey(student.getId())) {
            return null;
        }
        students.put(student.getId(), student);
        return student;
    }

    public void deleteStudent(long id) {
        students.remove(id);
    }

    public Collection<Student> findStudentByAge(int age) {
        List<Student> result = new ArrayList<>();
        for (Student value : students.values()) {
            if (value.getAge() == age) {
                result.add(value);
            }
        }
        return result;
    }

}
