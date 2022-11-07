package com.school.management.rest;

import com.school.management.repository.model.Course;
import com.school.management.repository.model.Student;
import com.school.management.repository.model.dto.StudentDto;
import com.school.management.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
//	public List<StudentDto> getStudents(@RequestParam(name = "without-courses") Optional<Boolean> withoutCourses) {
    public List<StudentDto> getStudents() {
        return studentService.getStudents();
    }


    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDto getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }


    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDto updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) {
        studentDto.setId(id);
        return studentService.updateStudent(studentDto);
    }


    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public List<StudentDto> createStudents(@RequestBody List<StudentDto> studentDtoList) {
        return studentService.createStudents(studentDtoList);
    }

    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudents(@RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        studentService.deleteAllStudents(confirmDeletion.orElse(false));
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id, @RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        studentService.deleteStudent(id, confirmDeletion.orElse(false));
    }



    @PutMapping(value = "/{id}/courses")
    @ResponseStatus(HttpStatus.OK)
    public List<String> updateStudentCourses(@PathVariable Long id, @RequestBody List<Long> idc) {
        return studentService.updateStudentCourses(id,idc);
    }

    @GetMapping(value = "/{id}/courses")
    @ResponseStatus(HttpStatus.OK)
    public List<Course> getCoursesStudent(@PathVariable Long id) {
        return studentService.getCoursestudent(id);
    }

    @GetMapping(value = "/courses")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getCoursesStudent() {
        return studentService.getCoursesStudents();
    }

}
