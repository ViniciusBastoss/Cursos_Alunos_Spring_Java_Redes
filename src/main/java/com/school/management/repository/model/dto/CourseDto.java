package com.school.management.repository.model.dto;

import com.school.management.repository.model.Student;

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class CourseDto {
    private Long id;

    private String name;


    Set<Student> students;

    public CourseDto(){

    }

    public CourseDto(Long id, String name, Set<Student> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public CourseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
