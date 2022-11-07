package com.school.management.rest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.school.management.repository.model.Student;
import com.school.management.repository.model.dto.CourseDto;
import com.school.management.repository.model.dto.StudentDto;
import com.school.management.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDto> getCourses() {
       /* return null;*/
       return courseService.getCourses();

    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDto getCourse(@PathVariable Long id) {
        return courseService.getCourse(id);
    }


    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDto updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        courseDto.setId(id);
        return courseService.updateCourse(courseDto);
    }


    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CourseDto> createCourses(@RequestBody List<CourseDto> courseDtoList) {
        return courseService.createCourses(courseDtoList);


    }

    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourses(@RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        courseService.deleteAllCourses(confirmDeletion.orElse(false));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id, @RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        courseService.deleteCourse(id, confirmDeletion.orElse(false));
    }




    @PutMapping(value = "/{id}/students")
    @ResponseStatus(HttpStatus.OK)
    public List<String> updateCourse(@PathVariable Long id,@RequestBody List<Long> ids) {
         return courseService.updateCourseStudents(id,ids);
    }

    @GetMapping(value = "/{id}/students")
    @ResponseStatus(HttpStatus.OK)
    public List<Student> getStudentsCourse(@PathVariable Long id) {
        return courseService.getStudentscourse(id);
    }

    @GetMapping(value = "/students")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getStudentsCourses() {
        return courseService.getStudentsCourses();
    }

}
