package edu.spring.core.repository;

import edu.spring.core.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
Sirve para probar componentes solo de la capa de persistencia
Solo buscara las clases anotadas con @Entity
 */
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;

    @BeforeEach
    public void poblateData() {
        employee = new Employee(null, "Efren", "Galarza", "efren@gmail.com");
    }

    @Test
    void testSaveEmployee() {
//        Give
        var employee = new Employee(null, "Efren", "Galarza", "efren@gmail.com");
//        When
        var employeeSave = employeeRepository.save(employee);
//        Then
        assertThat(employeeSave).isNotNull();
        assertThat(employeeSave.getId()).isGreaterThan(0);
    }

    @Test
    void testAllEmployees() {
//        Give
        employeeRepository.save(employee);
//        When
        List<Employee> employees = employeeRepository.findAll();
//        Then
        assertThat(employees.size()).isEqualTo(1);
    }

    @Test
    void testFindEmployeeById() {
        employeeRepository.save(employee);
//        when
        var employeeR = employeeRepository.findById(employee.getId()).get();
//        then
        assertThat(employeeR).isNotNull();
    }
    
}
