package edu.spring.core.repository;

import edu.spring.core.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    public void setup() {
        employee = new Employee(null, "Efren", "Galarza", "efren@gmail.com");
    }

    @Test
    void testSaveEmployee() {
//        Give - dado una condicion
        var employee = new Employee(null, "Efren", "Galarza", "efren@gmail.com");
//        When - accion o el comportamiento que vamos a probar
        var employeeSave = employeeRepository.save(employee);
//        Then - verificar la salida
        assertThat(employeeSave).isNotNull();
        assertThat(employeeSave.getId()).isGreaterThan(0);
    }

    @Test
    void testAllEmployees() {
//        Give - Se especifica el escenario, las precondiciones
        employeeRepository.save(employee);
//        When - Las condiciones de la lista que se van a ejecutar
        List<Employee> employees = employeeRepository.findAll();
//        Then - El resultado esperado, las validaciones a realizar
        assertThat(employees.size()).isEqualTo(1);
    }

    @Test
    void testFindEmployeeById() {
//        Give - Dado el empleado guardado en la db
        employeeRepository.save(employee);
//        When - Cuando se busque a ese empleado por id
        var employeeR = employeeRepository.findById(employee.getId());
//        Then - Confirmar que el empleado buscado no sea nulo
        assertThat(employeeR.orElse(null)).isNotNull();
    }

    @Test
    void testUpdateEmployeeById() {
//        Give - Dado un employee en la db
        var employeeSave = employeeRepository.save(employee);
//        When - Cuando sea actualizado
        employeeSave.setName("Gisell");
        var employeeUpdate = employeeRepository.save(employeeSave);
//        Then - Verificar que los cambios realizados sean correctos
        assertThat(employeeUpdate.getName()).isEqualTo("Gisell");
    }

    @Test
    void testDeleteEmployee() {
//        Give - Dado un employee en la db
        var employeeSave = employeeRepository.save(employee);
//        When - Cuando el empleado sea borrado por id
        long id = employeeSave.getId();
        employeeRepository.deleteById(id);
//        Then - Verificar que el empleado no exista en db
        assertFalse(employeeRepository.existsById(id));
    }

}
