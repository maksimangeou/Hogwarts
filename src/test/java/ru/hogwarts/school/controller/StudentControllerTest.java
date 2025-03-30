package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        Student student = new Student();
        student.setId(1L);
        student.setName("Гермиона Грейгджер");
        student.setAge(11);

        JSONObject studentJson = new JSONObject()
                .put("name", "Гермиона Грейгджер")
                .put("age", 11);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);
        when(studentService.findStudent(1L)).thenReturn(student);

        mvc.perform(MockMvcRequestBuilders.post("/students")
                        .content(studentJson.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        mvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateStudentTest() throws Exception {
        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setName("Новое имя");
        updatedStudent.setAge(25);

        JSONObject updateJson = new JSONObject()
                .put("id", 1L)
                .put("name", "Новое имя")
                .put("age", 25);

        when(studentService.editStudent(any(Student.class))).thenReturn(updatedStudent);

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
        doNothing().when(studentService).deleteStudent(anyLong());

        mvc.perform(MockMvcRequestBuilders.delete("/students/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    void getStudentsByAgeTest() throws Exception {
        Student student = new Student();
        student.setAge(20);
        List<Student> expected = Collections.singletonList(student);

        when(studentService.findStudentByAge(20)).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get("/students/filter?age=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(20));
    }

    @Test
    void getStudentsByAgeBetweenTest() throws Exception {
        Student student = new Student();
        student.setAge(25);
        List<Student> expected = Collections.singletonList(student);

        when(studentService.findStudentByAfeBetweenFromTo(20, 30)).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get("/students/filter/between?min=20&max=30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(25));
    }

    @Test
    void getFacultyByStudentIdTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        Student student = new Student();
        student.setFaculty(faculty);

        when(studentService.findStudent(1L)).thenReturn(student);

        mvc.perform(MockMvcRequestBuilders.get("/students/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }

    @Test
    void uploadAvatarTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "avatar",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mvc.perform(MockMvcRequestBuilders.multipart("/students/1/avatar")
                        .file(file))
                .andExpect(status().isOk());

        verify(studentService).uploadAvatar(eq(1L), any());
    }

    @Test
    void uploadAvatarSizeExceededTest() throws Exception {
        byte[] bigFile = new byte[1024 * 301];
        MockMultipartFile file = new MockMultipartFile(
                "avatar",
                "big.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                bigFile
        );

        mvc.perform(MockMvcRequestBuilders.multipart("/students/1/avatar")
                        .file(file))
                .andExpect(status().isBadRequest());
    }


    @Test
    void downloadAvatarPreviewTest() throws Exception {
        Avatar avatar = new Avatar();
        avatar.setMediaType("image/png");
        avatar.setData("test".getBytes());

        when(studentService.findAvatar(1L)).thenReturn(avatar);

        mvc.perform(MockMvcRequestBuilders.get("/students/1/avatar/preview"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "image/png"))
                .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, 4))
                .andExpect(content().bytes("test".getBytes()));
    }

    @Test
    void downloadAvatarStreamTest() throws Exception {
        Avatar avatar = new Avatar();
        avatar.setMediaType("image/jpeg");
        avatar.setFilePath("test.jpg");
        avatar.setFileSize(1024L);

        Path testFile = Files.createTempFile("test", ".jpg");
        Files.write(testFile, "test content".getBytes());
        avatar.setFilePath(testFile.toString());

        when(studentService.findAvatar(1L)).thenReturn(avatar);

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get("/students/1/avatar"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentType()).isEqualTo("image/jpeg");
        assertThat(response.getContentAsByteArray()).isEqualTo(Files.readAllBytes(testFile));
    }

}