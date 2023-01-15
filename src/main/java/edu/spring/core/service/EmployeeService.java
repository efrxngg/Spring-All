package edu.spring.core.service;

import edu.spring.core.entity.Employee;
import edu.spring.core.exception.EmployeeException;
import edu.spring.core.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee) throws EmployeeException;

    List<Employee> findAllEmployees();

    Optional<Employee> findEmployeeById(long id);

    Optional<Employee> updateEmployee(Employee employee);

    void deleteEmployeeById(long id) throws NotFoundException;

}
