package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student createStudent(Student student) {
        LOGGER.info("Вызван метод createStudent");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        LOGGER.info("Вызван метод findStudent({})", id);
        return studentRepository.findById(id);
    }

    public Student editStudent(Student student) {
        LOGGER.info("Вызван метод editStudent({})", student);
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        LOGGER.info("Вызван метод deleteStudent({})", id);
        studentRepository.deleteById(id);
    }

    public List<Student> getAllStudent() {
        LOGGER.info("Вызван метод getAllStudent()");
        return studentRepository.findAll();
    }

    public List<Student> findStudentByAge(int age) {
        LOGGER.info("Вызван метод findStudentByAge({})", age);
        return studentRepository.findByAge(age);
    }

    public List<Student> findStudentByAfeBetweenFromTo(int from, int to) {
        LOGGER.info("Вызван метод findStudentByAfeBetweenFromTo({}, {})", from, to);
        return studentRepository.findByAgeBetween(from, to);
    }

    @Transactional
    public Avatar findAvatar(long studentId) {
        LOGGER.info("Вызван метод findAvatar({})", studentId);
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        LOGGER.info("Вызван метод uploadAvatar({}, {})", studentId, file);

        Student student = findStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    public Integer getStudentCount() {
        LOGGER.info("Вызван метод getStudentCount()");

        return studentRepository.getStudentCount();
    }

    public Double getAverageStudentAge() {
        LOGGER.info("Вызван метод getAverageStudentAge()");

        return studentRepository.getAverageStudentAge();
    }

    public List<Student> getLastFiveStudent() {
        LOGGER.info("Вызван метод getLastFiveStudent()");

        return studentRepository.findLastFiveStudent();
    }

    public List<String> getAllStudentNameToUpperCaseBySort() {
        return studentRepository.findAll().stream()
                .map(i -> i.getName().toUpperCase())
                .sorted()
                .toList();
    }

    public double getAvgStudentAge() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

}
