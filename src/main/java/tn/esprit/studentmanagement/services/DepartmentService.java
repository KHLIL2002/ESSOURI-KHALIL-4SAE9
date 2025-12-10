package tn.esprit.studentmanagement.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;
import tn.esprit.studentmanagement.entities.Department;
import java.util.List;

@Service              // <--- YOU ARE MISSING THIS
@AllArgsConstructor   // <--- YOU ARE MISSING THIS
public class DepartmentService implements IDepartmentService {

    private DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}