package ru.hogwarts.school.controller;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class StudentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @LocalServerPort
    private int port;

    @Test
    void testGetStudent() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/1", String.class))
                .isNotNull();
    }

    @Test
    void testGetFindBetween_thanStudentFound() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/filter/between?min=1&max=5", String.class))
                .isNotNull();
    }

    @Test
    void testGetAge_thanStudentFound() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/filter?age=5", String.class))
                .isNotNull();
    }

    @Test
    void getStudent_whenGetFaculty() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/1/faculty", String.class))
                .isNotNull();
    }

    @Test
    void createStudent_ReturnsId() {
        Student student = new Student();
        student.setName("Harry Potter");
        student.setAge(20);

        ResponseEntity<Long> response = restTemplate.postForEntity(
                "/students",
                student,
                Long.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Student savedStudent = studentRepository.findById(response.getBody()).orElse(null);
        assertNotNull(savedStudent);
        assertEquals("Harry Potter", savedStudent.getName());
    }

    @Test
    void deleteStudent_RemovesFromDb() {
        Student student = studentRepository.save(new Student("Невил Долгопупс", 20));

        restTemplate.delete("/students/" + student.getId());

        assertFalse(studentRepository.existsById(student.getId()));
    }

    @Test
    void putFaculty_UpdatesExistingFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Когтевран");
        faculty.setColor("синий");
        Student originalStudent = new Student();
        originalStudent.setName("Палуна Лавгуд");
        originalStudent.setAge(11);
        originalStudent.setFaculty(faculty);
        Student savedStudent = studentRepository.save(originalStudent);

        Student updateStudent = new Student();
        updateStudent.setId(savedStudent.getId());
        updateStudent.setName("Чжоу Чанг");
        updateStudent.setAge(13);


        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:" + port + "/students",
                HttpMethod.PUT,
                new HttpEntity<>(updateStudent),
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Чжоу Чанг", response.getBody().getName());
        assertEquals(13, response.getBody().getAge());
    }

    @Test
    void getFacultyByStudentId_ReturnsFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        Student student = new Student("Harry Potter", 20);
        student.setFaculty(faculty);
        student = studentRepository.save(student);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/students/" + student.getId() + "/faculty",
                Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Gryffindor", response.getBody().getName());
    }
}
