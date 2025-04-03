package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    StudentService studentService;


    @Test
    void getFacultyByIdTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Грифиндор");
        faculty.setColor("Красный");

        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mvc.perform(MockMvcRequestBuilders.get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Грифиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));
    }

    @Test
    void createFacultyTest() throws Exception {
        Faculty newFaculty = new Faculty();
        newFaculty.setId(1L);
        newFaculty.setName("Когтевран");
        newFaculty.setColor("Синий");

        JSONObject facultyJson = new JSONObject()
                .put("name", "Когтевран")
                .put("color", "Синий");

        // Исправлен вызов метода сервиса
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(newFaculty);

        mvc.perform(MockMvcRequestBuilders.post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facultyJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateFacultyTest() throws Exception {
        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(1L);
        updatedFaculty.setName("Slytherin");
        updatedFaculty.setColor("Green");

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(updatedFaculty);

        mvc.perform(MockMvcRequestBuilders.put("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Slytherin\", \"color\": \"Green\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("Green"));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        doNothing().when(facultyService).deleteFaculty(1L);

        mvc.perform(delete("/faculties/1"))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).deleteFaculty(1L);
    }

    @Test
    void getFacultiesByColorTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setColor("Blue");

        when(facultyService.findFacultiesByColor("Blue")).thenReturn(Collections.singletonList(faculty));

        mvc.perform(MockMvcRequestBuilders.get("/faculties/filter").param("color", "Blue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("Blue"));
    }

    @Test
    void searchFacultiesTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Griffindor");
        faculty.setColor("Red");

        when(facultyService.findFacultyByNameIgnoreCaseOrColorIgnoreCase("griff", "red"))
                .thenReturn(Collections.singletonList(faculty));

        mvc.perform(MockMvcRequestBuilders.get("/faculties/find")
                        .param("name", "griff")
                        .param("color", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Griffindor"))
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    @Test
    void getStudentsByFacultyTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");

        Faculty faculty = new Faculty();
        faculty.setStudents(Collections.singletonList(student));

        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mvc.perform(MockMvcRequestBuilders.get("/faculties/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"));
    }

    @Test
    void getFacultyByNotExistId() throws Exception {
        when(facultyService.findFaculty(999L)).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/faculties/999"))
                .andExpect(status().isNotFound());
    }

}
