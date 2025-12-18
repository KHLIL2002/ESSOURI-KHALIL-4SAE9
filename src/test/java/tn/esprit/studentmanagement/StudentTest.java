package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import tn.esprit.studentmanagement.entities.Student; // Assure-toi que l'import est bon

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentTest {

    @Test
    void testStudentCreation() {
        Student student = new Student();

        // On utilise les vrais noms de tes variables
        student.setFirstName("Jenkins");
        student.setLastName("Pipeline");
        student.setEmail("jenkins@devops.com");

        // Vérifications
        assertEquals("Jenkins", student.getFirstName());
        assertEquals("Pipeline", student.getLastName());
        assertEquals("jenkins@devops.com", student.getEmail());
    }
}