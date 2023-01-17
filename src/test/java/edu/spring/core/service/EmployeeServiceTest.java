package edu.spring.core.service;

import edu.spring.core.entity.Employee;
import edu.spring.core.exception.EmployeeException;
import edu.spring.core.repository.EmployeeRepository;
import edu.spring.core.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

/*
Proporiciona caracteristicas adicionales a las pruebas
Como la capacidad de inyectar objetos dependientes,
limpiar estados antes y despues de pruebas o modificar el comportamiento
 */
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    //    Sirve para crear objetos simulados y pueden ser objs o interfaces
    @Mock
    private EmployeeRepository employeeRepository;

    //    Sirve para inyectar los objs mockeados
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = new Employee(1L, "Efren", "Galarza", "efren@gmail.com");
    }

    @Test
    void saveEmployeeTest() {
//        given
        given(employeeRepository.save(employee)).willReturn(employee);
//        when
        var employeeSave = employeeService.saveEmployee(employee);
        //Then
        assertThat(employeeSave).isNotNull();
    }

    @Test
    void findEmployeeByEmailWithThrowTest() {
//        given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
//        when
        assertThrows(EmployeeException.class, () -> employeeService.saveEmployee(employee));
        //Then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void listarEmpleado() {
        given(employeeRepository.findAll()).willReturn(List.of(employee));

        List<Employee> employees = employeeService.findAllEmployees();

        assertThat(employees).isNotNull();
        assertThat(employees).isNotEmpty();
    }

    @Test
    void findEmployeeById() {
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        var employeeSave = employeeService.findEmployeeById(employee.getId()).orElse(null);

        assertThat(employeeSave).isNotNull();
    }

    @Test
    void updateEmployeeByID() {
        given(employeeRepository.existsById(employee.getId())).willReturn(true);
        given(employeeRepository.save(employee)).willReturn(employee);

        var name = "Gisell";
        employee.setName(name);

        var employeeUpdate = employeeService.updateEmployee(employee).orElseGet(Employee::new);

        assertThat(employeeUpdate.getName()).isEqualTo(name);
    }

    @Test
    void deleteEmployeeById() {
        long id = employee.getId();
        given(employeeRepository.existsById(id)).willReturn(true);
        willDoNothing().given(employeeRepository).deleteById(id);

        employeeService.deleteEmployeeById(id);

        verify(employeeRepository, times(1)).deleteById(id);
    }

}
