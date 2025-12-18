package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;
import tn.esprit.studentmanagement.services.StudentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student createStudent() {
        return new Student(
                1L,
                "Ali",
                "Ben Salah",
                "ali@mail.com",
                "12345678",
                LocalDate.of(2000, 1, 1),
                "Tunis",
                null,
                null
        );
    }

    @Test
    void getAllStudents_shouldReturnList() {
        when(studentRepository.findAll()).thenReturn(List.of(createStudent()));

        List<Student> students = studentService.getAllStudents();

        assertEquals(1, students.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById_shouldReturnStudent() {
        Student student = createStudent();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals("Ali", result.getFirstName());
    }

    @Test
    void getStudentById_shouldReturnNull_whenNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Student result = studentService.getStudentById(1L);

        assertNull(result);
    }

    @Test
    void saveStudent_shouldSaveStudent() {
        Student student = createStudent();
        when(studentRepository.save(student)).thenReturn(student);

        Student saved = studentService.saveStudent(student);

        assertNotNull(saved);
        verify(studentRepository).save(student);
    }

    @Test
    void deleteStudent_shouldCallRepository() {
        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }
}
