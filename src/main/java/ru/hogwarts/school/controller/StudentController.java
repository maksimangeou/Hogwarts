package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public Student getStudent(@PathVariable long id) {
        return studentService.findStudent(id);
    }

    @PostMapping
    public long postStudent(@RequestBody Student student) {
        studentService.createStudent(student);
        return student.getId();
    }

    @PutMapping
    public Student putStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("filter")
    public ResponseEntity<Collection<Student>> getStudentByAge(@RequestParam (required = false) int age) {
        if (age > 0) {
            return ResponseEntity.ok((studentService.findStudentByAge(age)));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }


}
