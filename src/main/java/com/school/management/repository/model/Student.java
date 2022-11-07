package com.school.management.repository.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	@ManyToMany(mappedBy = "students")
	Set<Course> courses;
	public Student() {
	}

	public Student(Long id) {
		this.id = id;
	}

	public Student(String name, String address, Timestamp createdAt, Timestamp updatedAt) {
		this.name = name;
		this.address = address;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Student(Long id, String name, String address, Timestamp createdAt, Timestamp updatedAt) {
		this(name, address, createdAt, updatedAt);
		this.id = id;
	}

	public Student(Long id, String name, String address, Timestamp createdAt, Timestamp updatedAt, Set<Course> courses) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.courses = courses;
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
