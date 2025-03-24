package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
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
        return studentService.editStudent(student);
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("filter")
    public Collection<Student> getStudentByAge(@RequestParam(required = false) int age) {
        return studentService.findStudentByAge(age);
    }

    @GetMapping("filter/between")
    public Collection<Student> getStudentByAgeBetween(@RequestParam() int min,
                                                      @RequestParam() int max) {
        return studentService.findStudentByAfeBetweenFromTo(min, max);
    }

    @GetMapping("{id}/faculty")
    public Faculty getFacultyByStudentId(@PathVariable long id) {
        return studentService.findStudent(id).getFaculty();
    }


}
