package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;


@RestController
@RequestMapping("faculties")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public Faculty getFaculty(@PathVariable long id) {
        return facultyService.findFaculty(id);
    }

    @PostMapping
    public long postFaculty(@RequestBody Faculty faculty) {
        facultyService.createFaculty(faculty);
        return faculty.getId();
    }

    @PutMapping
    public Faculty putFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }

    @DeleteMapping("{id}")
    public void deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping("filter")
    public List<Faculty> getFacultiesByColor(@RequestParam(required = false) String color) {
        return facultyService.findFacultiesByColor(color);
    }

    @GetMapping("find")
    public List<Faculty> getFacultyByNameIgnoreCaseOrColorIgnoreCase(@RequestParam(required = false) String name,
                                                                           @RequestParam(required = false) String color) {
        return facultyService.findFacultyByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    @GetMapping("{id}/students")
    public List<Student> findAllStudentByFacultyId(@PathVariable long id) {
        return facultyService.findFaculty(id).getStudents();
    }


}
