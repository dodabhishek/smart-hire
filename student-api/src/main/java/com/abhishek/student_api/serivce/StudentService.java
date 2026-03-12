package com.abhishek.student_api.serivce;

import com.abhishek.student_api.dto.StudentRequest;
import com.abhishek.student_api.dto.StudentResponse;
import com.abhishek.student_api.entity.Student;
import com.abhishek.student_api.exception.StudentNotFoundException;
import com.abhishek.student_api.repositry.StudentRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Convert Entity → Response DTO
    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCourse()
        );
    }

    // Convert Request DTO → Entity using Builder
    private Student toEntity(StudentRequest request) {
        return Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .course(request.getCourse())
                .build();
    }

    // Get all students
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get student by id
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return toResponse(student);
    }

    // Create student
    public StudentResponse createStudent(StudentRequest request) {
        Student student = toEntity(request);
        Student saved = studentRepository.save(student);
        return toResponse(saved);
    }

    // Update student - fixed: correct field + save to DB
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setCourse(request.getCourse()); // ✅ was request.getEmail() before
        Student updated = studentRepository.save(existing); // ✅ was missing before

        return toResponse(updated);
    }

    // Delete student
    public void deleteStudent(Long id) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.deleteById(existing.getId());
    }

    // Search by course
    public List<StudentResponse> getStudentsByCourse(String course) {
        return studentRepository.findByCourse(course)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Search by name keyword
    public List<StudentResponse> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get by email
    public StudentResponse getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
        return toResponse(student);
    }

    public Page<StudentResponse> getAllStudentsPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return studentRepository.findAll(pageable)
                .map(this::toResponse);
    }
}