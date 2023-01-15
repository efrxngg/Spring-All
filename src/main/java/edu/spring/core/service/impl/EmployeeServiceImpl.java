package edu.spring.core.service.impl;

import edu.spring.core.entity.Employee;
import edu.spring.core.exception.EmployeeException;
import edu.spring.core.exception.NotFoundException;
import edu.spring.core.repository.EmployeeRepository;
import edu.spring.core.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository repo) {
        this.employeeRepository = repo;
    }

    @Override
    @Transactional
    public Employee saveEmployee(Employee employee) throws EmployeeException {
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent())
            throw new EmployeeException(String
                    .format("Ya existe un empleado registrado con %s", employee.getEmail()));
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Employee> updateEmployee(Employee employee) {
        return employeeRepository
                .existsById(employee.getId()) ?
                Optional.of(employeeRepository.save(employee)) : Optional.empty();
    }

    @Override
    @Transactional
    public void deleteEmployeeById(long id) throws NotFoundException {
        if (!employeeRepository.existsById(id))
            throw new NotFoundException(String.format("Employee with id '%s' not found ", id));
        employeeRepository.deleteById(id);
    }

}
