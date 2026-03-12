package com.abhishek.student_api.repositry;

import com.abhishek.student_api.entity.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByCourse(String course);
    List<Student> findByNameContainingIgnoreCase(String name);
    Optional<Student> findByEmail(String email);
    List<Student> findByCourseAndNameContainingIgnoreCase(String course, String name);

    // Paginated version of findAll
    Page<Student> findAll(Pageable pageable);
}