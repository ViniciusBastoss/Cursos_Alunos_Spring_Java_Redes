package com.school.management.service;


import com.school.management.repository.CourseRepository;
import com.school.management.repository.model.Course;
import com.school.management.repository.model.Student;
import com.school.management.repository.model.dto.StudentDto;
import com.school.management.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;


	public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
	}


	public void removeStud(Long id){
		List<Course> todsCurses = this.courseRepository.findAll();
		List<Student> ListStudent ;
		Set<Student> ListStuSet;

		//Removendo o aluno de todos os cursos
		for(int i = 0; i < todsCurses.size();i++) {
			ListStudent = new ArrayList<Student>(todsCurses.get(i).getStudents());
			ListStuSet = ListStudent.stream().filter(s -> s.getId() != id).collect(Collectors.toSet());
			todsCurses.get(i).setStudents(ListStuSet);
			courseRepository.save(todsCurses.get(i));
		}
	}
	public List<StudentDto> getStudents() {
		return studentRepository.findAll().stream()
			.map(student -> new StudentDto(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	public StudentDto getStudent(Long id) {
		return studentRepository.findById(id)
			.map(student -> new StudentDto(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt()))
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Student not found."));
	}

	@Transactional
	public StudentDto updateStudent(StudentDto studentDto) {
		Student student = studentRepository.findById(studentDto.getId()).orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "Student not found."));

		Boolean updated = false;
		if (studentDto.getName() != null && !studentDto.getName().isBlank() && !studentDto.getName().equals(student.getName())) {
			student.setName(studentDto.getName());
			updated = true;
		}
		if (studentDto.getAddress() != null && !studentDto.getAddress().isBlank() && !studentDto.getAddress().equals(student.getAddress())) {
			student.setAddress(studentDto.getAddress());
			updated = true;
		}

		if (updated) {
			student.setUpdatedAt(Timestamp.from(Instant.now()));
			student = studentRepository.save(student);
		}

		return new StudentDto(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt());
	}

	public List<StudentDto> createStudents(List<StudentDto> studentsDto) {
		if (studentsDto.size() > 50) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "A request can not contain more than 50 students.");
		}

		Timestamp ts = Timestamp.from(Instant.now());
		List<Student> l = studentRepository.saveAll(studentsDto.stream()
			.filter(s -> s.getName() != null && !s.getName().isBlank() && s.getAddress() != null && !s.getAddress().isBlank())
			.map(studentDto -> new Student(studentDto.getName(),
				studentDto.getAddress(),
				ts,
				ts))
			.collect(Collectors.toList()));

		return l.stream()
			.map(student -> new StudentDto(student.getId(),
				student.getName(),
				student.getAddress(),
				student.getCreatedAt(),
				student.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	@Transactional
	public void deleteAllStudents(Boolean confirmDeletion) {
		if (confirmDeletion) {
			List<Student> students = studentRepository.findAll();
			students.stream().forEachOrdered(s -> removeStud(s.getId()));
			studentRepository.deleteAll();
		} else {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"To delete ALL students and students-courses relationships, inform confirm-deletion=true as a query param.");
		}
	}

	@Transactional
	public void deleteStudent(Long id, Boolean confirmDeletion) {
		if (confirmDeletion) {
			Student student = studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Student not found."));

			removeStud(id);

			studentRepository.deleteById(id);
		} else {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"To delete the student and student-courses relationships, inform confirm-deletion=true as a query param.");
		}


	}
	public List<Course> criarCursos(List<Long> idsCourses) {
		List<Course> newCourses = new ArrayList<Course>();
		for (int i = 0; i < idsCourses.size(); i++) {
			Optional<Course> optional = this.courseRepository.findById(idsCourses.get(i));
			if (optional.isPresent()) {
				newCourses.add(optional.get());
			} else {
				throw new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Curso(s) não encontrado(s).");
			}

		}
		return newCourses;
	}


	@Transactional
	public List<String> updateStudentCourses(Long ids, List<Long> idc) {
		Optional<Student> optionalStudent = this.studentRepository.findById(ids);
		List<String> lista = new ArrayList<String>();

		if (idc.size() > 5)
			throw new ResponseStatusException(
					HttpStatus.FORBIDDEN,
					"Número de cursos superior a  5.");

		if(optionalStudent.isPresent()){
			Student student = optionalStudent.get();
			List<Course> newCourses = this.criarCursos(idc);

			for(int i = 0; i < newCourses.size();i++){
				if(newCourses.get(i).getStudents().size() > 50){
					throw new ResponseStatusException(
							HttpStatus.FORBIDDEN,
							"Número de alunos superior a  50.");
				}
			}
			removeStud(ids);

			for(int i = 0; i < newCourses.size();i++){
				newCourses.get(i).getStudents().add(student);
				this.courseRepository.save(newCourses.get(i));
			}

			for(int i = 0; i < newCourses.size();i++){
				int id = Math.toIntExact(ids);
				lista.add("StudentId: " + Integer.toString(id));
				lista.add("StudentName: " + student.getName());

				int id2 = Math.toIntExact(newCourses.get(i).getId());
				lista.add("CourseId: " + Integer.toString(id2));
				lista.add("CourseName: " + newCourses.get(i).getName());
				lista.add("----------------------------------------------");

			}
		}
		else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND,
					"Estudante invalido.");
		}
		return lista;
	}




	@Transactional
	public List<Course> getCoursestudent(Long ids){
		Optional<Student> optional = this.studentRepository.findById(ids);

		if(!optional.isPresent()){
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND,
					"Curso não encontrado.");
		}

		Student student = optional.get();
		Set<Course> courses;
		List<Course> resposta;
		courses = student.getCourses();
		//Resetando a lista de estudantes
		resposta = courses.stream().map(s -> new Course(s.getName(),s.getId())).collect(Collectors.toList());
		return resposta;
	}

	@Transactional
	public List<String> getCoursesStudents(){
		List<String> list = new ArrayList<String>();

		List<Student> students = new ArrayList<Student>();
		students = studentRepository.findAll().stream().collect(Collectors.toList());
		for(int i = 0; i < students.size();i++){
			for(int j = 0; j < students.get(i).getCourses().size();j++){
				String nome = students.get(i).getName();
				list.add("Student: " + nome);

				List<Course> cursos = new ArrayList<Course>(students.get(i).getCourses());

				String cursName = cursos.get(j).getName();
				list.add("Course: " + cursName);
				list.add("------------------------------------");
			}

		}
		return list;
	}

}



