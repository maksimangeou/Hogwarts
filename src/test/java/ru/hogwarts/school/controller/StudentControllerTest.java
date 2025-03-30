package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    private MockMvc mvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void saveStudentTest() throws Exception {
        // Подготовка данных
        Student student = new Student();
        student.setId(1L);
        student.setName("Гермиона Грейгджер");
        student.setAge(11);

        JSONObject studentJson = new JSONObject()
                .put("name", "Гермиона Грейгджер")
                .put("age", 11);

        // Мокирование
        when(studentService.createStudent(any(Student.class))).thenReturn(student);
        when(studentService.findStudent(1L)).thenReturn(student);

        // Тестирование POST
        mvc.perform(MockMvcRequestBuilders.post("/students")
                        .content(studentJson.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        // Тестирование GET
        mvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateStudentTest() throws Exception {
        // Подготовка данных
        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setName("Новое имя");
        updatedStudent.setAge(25);

        JSONObject updateJson = new JSONObject()
                .put("id", 1L)
                .put("name", "Новое имя")
                .put("age", 25);

        // Мокирование
        when(studentService.editStudent(any(Student.class))).thenReturn(updatedStudent);

        // Тестирование PUT
        mvc.perform(MockMvcRequestBuilders.put("/students")
                        .content(updateJson.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Новое имя"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void deleteStudentTest() throws Exception {
        // Мокирование
        doNothing().when(studentService).deleteStudent(anyLong());

        // Тестирование DELETE
        mvc.perform(MockMvcRequestBuilders.delete("/students/1"))
                .andExpect(status().isOk());

        // Проверка вызова метода
        verify(studentService, times(1)).deleteStudent(1L);
    }

}