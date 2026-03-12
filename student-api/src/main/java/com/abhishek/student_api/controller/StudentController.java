package com.abhishek.student_api.controller;

import com.abhishek.student_api.dto.StudentRequest;
import com.abhishek.student_api.dto.StudentResponse;
import com.abhishek.student_api.serivce.StudentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;








@ToString
@Data
@Setter
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RestController //Combines two things in one. It marks this as a Controller and also tells Spring to automatically convert all return values to JSON. Every response your API sends will be JSON automatically.
@RequestMapping("/api/students") // base URL for all endpoints in this controller. So every method here starts with /api/students.
public class StudentController {

    @Autowired
    private StudentService studentService;

    // These map HTTP methods to your Java methods. 
    //When someone sends a GET request to /api/students, Spring knows to call getAllStudents().

    // GET all students
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // GET student by id
    // @PathVariable — Extracts the id from the URL. So /api/students/5 gives you id = 5.
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // POST create student
    // @RequestBody — Takes the JSON from the request body and automatically converts it into a Student object. 
    // So when you send {"name":"John"} in Postman, Spring converts it to a Student object for you.
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(request));
    }

    // PUT update student
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
          @Valid  @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }
    // Why @Valid here — The annotations on Student.java just define the rules. 
    // @Valid is what actually triggers Spring to check those rules when a request comes in. Without 
    // @Valid, the rules are defined but never enforced.
    
    // DELETE student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    // GET /api/students/paginated?page=0&size=5&sortBy=name
    @GetMapping("/paginated")
    public ResponseEntity<Page<StudentResponse>> getStudentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity.ok(studentService.getAllStudentsPaginated(page, size, sortBy));
    }
}