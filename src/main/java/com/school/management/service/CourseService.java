package com.school.management.service;

import com.school.management.repository.CourseRepository;
import com.school.management.repository.StudentRepository;
import com.school.management.repository.model.Course;
import com.school.management.repository.model.Student;
import com.school.management.repository.model.dto.CourseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public List<CourseDto> getCourses() {
        return courseRepository.findAll().stream()
                .map(course -> new CourseDto(course.getId(), course.getName()))
                .collect(Collectors.toList());
    }
    @Transactional
    public CourseDto getCourse(Long id) {
        return courseRepository.findById(id)
                .map(course -> new CourseDto(course.getId(), course.getName()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found."));
    }

    @Transactional
    public CourseDto updateCourse(CourseDto courseDto) {
        Course course = courseRepository.findById(courseDto.getId()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Course not found."));
        Boolean updated = false;
        if (courseDto.getName() != null && !courseDto.getName().isBlank() && !courseDto.getName().equals(course.getName())) {
            course.setName(courseDto.getName());
            updated = true;
        }
        return new CourseDto(course.getId(), course.getName());
    }

    @Transactional
    public List<CourseDto> createCourses(List<CourseDto> coursesDto) {
        if (coursesDto.size() > 50) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "A request can not contain more than 50 courses.");
        }

        List<Course> c = courseRepository.saveAll(coursesDto.stream()
                .filter(k -> k.getName() != null && !k.getName().isBlank())
                .map(courseDto -> new Course(courseDto.getName()))
                .collect(Collectors.toList()));

        return c.stream()
                .map(course -> new CourseDto(course.getId(),
                        course.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllCourses(Boolean confirmDeletion) {
        if (confirmDeletion) {
            courseRepository.deleteAll();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "To delete ALL courses and students-courses relationships, inform confirm-deletion=true as a query param.");
        }
    }

    @Transactional
    public void deleteCourse(Long id, Boolean confirmDeletion) {
        if (confirmDeletion) {
            Course course = courseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Course not found."));

            List<Student> students = new ArrayList<Student>(course.getStudents());
            for(int i = 0; i < students.size();i++) {
                students.get(i).getCourses().stream().filter(s -> s.getId() != id);
            }
            studentRepository.saveAll(students);
            courseRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "To delete the course and student-courses relationships, inform confirm-deletion=true as a query param.");
        }


    }

    public Set<Student> criarAlunos(List<Long> idsStudents){
        Set<Student> newStudents = new HashSet<Student>();
        for(int i = 0; i < idsStudents.size();i++){
            Optional<Student> optional = this.studentRepository.findById(idsStudents.get(i));
            if(optional.isPresent()){
                newStudents.add(optional.get());
            }
            else{
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Estudante(s) não encontrado(s).");
            }

        }
        return newStudents;

    }

   @Transactional
    public List<String> updateCourseStudents(Long idc, List<Long> ids) {
       Optional<Course> optionalCourse = this.courseRepository.findById(idc);
       List<String> lista = new ArrayList<String>();

       if (ids.size() > 50)
           throw new ResponseStatusException(
                   HttpStatus.FORBIDDEN,
                   "Número de alunos superior a  50.");

       if(optionalCourse.isPresent()){
           Course course = optionalCourse.get();
           Set<Student> newStudents = this.criarAlunos(ids);
           course.setStudents(newStudents);
           this.courseRepository.save(course);

           List<Student> newStudents2 = new ArrayList<Student>(newStudents);
           for(int i = 0; i < newStudents2.size();i++){
               int id = Math.toIntExact(newStudents2.get(i).getId());
               lista.add("StudentId: " + Integer.toString(id));
               lista.add("StudentName: " + newStudents2.get(i).getName());

               int id2 = Math.toIntExact(idc);
               lista.add("CourseId: " + Integer.toString(id2));
               lista.add("CourseName: " + course.getName());
               lista.add("----------------------------------------------");

           }
       }
       else {
           throw new ResponseStatusException(
                   HttpStatus.FORBIDDEN,
                   "Curso invalido.");
       }

       return lista;
   }
   @Transactional
    public List<Student> getStudentscourse(Long idc){
       Optional<Course> optional = this.courseRepository.findById(idc);

        if(!optional.isPresent()){
           throw new ResponseStatusException(
                   HttpStatus.NOT_FOUND,
                   "Curso não encontrado.");
       }

       Course course = optional.get();
       Set<Student> students;
       List<Student> resposta;
       students = course.getStudents();
       //Resetando a lista de cursos
       resposta = students.stream().map(s -> new Student(s.getId(),
                       s.getName(),
                       s.getAddress(),
                       s.getCreatedAt(),
                       s.getUpdatedAt()))
               .collect(Collectors.toList());


       return resposta;

   }

    @Transactional
    public List<String> getStudentsCourses(){
        List<String> list = new ArrayList<String>();

        List<Course> courses = new ArrayList<Course>();
        courses = courseRepository.findAll().stream().collect(Collectors.toList());
        for(int i = 0; i < courses.size();i++){
            for(int j = 0; j < courses.get(i).getStudents().size();j++){

                String nomeC = courses.get(i).getName();
                list.add("Course: " + nomeC);


                List<Student> students = new ArrayList<Student>(courses.get(i).getStudents());
                String studName = students.get(j).getName();
                list.add("Student: " + studName);
                list.add("------------------------------");
            }

        }
        return list;
    }
}


