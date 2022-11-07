package com.school.management.repository.model.dto;

import com.school.management.repository.model.Course;

import javax.persistence.ManyToMany;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

public class StudentDto {

	private Long id;

	private String name;

	private String address;

	private Timestamp createdAt;

	private Timestamp updatedAt;


	Set<Course> courses;

	public StudentDto(Long id, String name, String address, Timestamp createdAt, Timestamp updatedAt, Set<Course> courses) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.courses = courses;
	}

	public StudentDto(){}
	public StudentDto(String name, String address) {
		Timestamp ts = Timestamp.from(Instant.now());
		this.id = 0L;
		this.name = name;
		this.address = address;
		this.createdAt = ts;
		this.updatedAt = ts;
	}

	public StudentDto(Long id, String name, String address, Timestamp createdAt, Timestamp updatedAt) {
		this(name, address);
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}
