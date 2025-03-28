package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class FacultyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @LocalServerPort
    private int port;


    @Test
    void testGetFaculty() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculties/1", String.class))
                .isNotNull();
    }

    @Test
    void testGetFind_thanFacultyFound() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculties/find?name=Грифиндор", String.class))
                .isNotNull();
    }

    @Test
    void testGetColor_thanFacultyFound() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculties/filter?color=Грифиндор", String.class))
                .isNotNull();
    }

    @Test
    void getListStudent_whenGetFaculty() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculties/1/students", String.class))
                .isNotNull();
    }

    @Test
    void postFaculty_CreatesNewFaculty() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Слизерин");
        newFaculty.setColor("зеленый");

        ResponseEntity<Long> response = restTemplate.postForEntity("http://localhost:" + port + "/faculties", newFaculty, Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Faculty created = facultyRepository.findById(response.getBody()).orElse(null);
        assertNotNull(created);
        assertEquals("Слизерин", created.getName());
        assertEquals("зеленый", created.getColor());
    }

    @Test
    void putFaculty_UpdatesExistingFaculty() {
        Faculty originalFaculty = new Faculty();
        originalFaculty.setName("Когтевран");
        originalFaculty.setColor("синий");
        Faculty savedFaculty = facultyRepository.save(originalFaculty);
        Faculty updateData = new Faculty();
        updateData.setId(savedFaculty.getId());
        updateData.setName("Изменения");
        updateData.setColor("серебряный");

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculties",
                HttpMethod.PUT,
                new HttpEntity<>(updateData),
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Изменения", response.getBody().getName());
        assertEquals("серебряный", response.getBody().getColor());
    }

    @Test
    void getDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Test Color");
        Faculty savedFaculty = facultyRepository.save(faculty);

        long id = savedFaculty.getId();

        restTemplate.delete("http://localhost:" + port + "/faculties/" + id, id);

        Faculty result = facultyRepository.findById(id);
        assertNull(result);
    }

}