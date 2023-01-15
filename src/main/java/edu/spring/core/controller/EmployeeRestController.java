package edu.spring.core.controller;

import edu.spring.core.entity.Employee;
import edu.spring.core.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(value = "/api/v1/employees")
public class EmployeeRestController {

    //region Dependencies
    private final EmployeeService employeeService;

    public EmployeeRestController(EmployeeService service) {
        this.employeeService = service;
    }
    //endregion

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        return status(HttpStatus.CREATED).body(employeeService.saveEmployee(employee));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Employee> findEmployee(@PathVariable long id) {
        return employeeService.findEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Employee>> findAllEmployee() {
        List<Employee> employees = employeeService.findAllEmployees();
        return status(employees.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(employees);
    }

    @PutMapping
    public ResponseEntity<Employee> udpateEmployee(@RequestBody Employee employee) {
        return employeeService.updateEmployee(employee)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable long id) {
        employeeService.deleteEmployeeById(id);
        return status(HttpStatus.NO_CONTENT).body("Employee delete correctly");
    }

}
