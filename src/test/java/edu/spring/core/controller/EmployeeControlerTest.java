package edu.spring.core.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.spring.core.entity.Employee;
import edu.spring.core.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
Esta anotacion nos va a servir para poder probar los controlladores
 */
@WebMvcTest
public class EmployeeControlerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    /*
    Nos sirve para agregar objs simulados al contexto de la aplicacion
    el simulacro reemplazara cualquier bean existente del mismo tipo en el contexto
    de la aplicacion este es un simulacro
     */
    @MockBean
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = new Employee(1L, "Efren", "Galarza", "efren@gmail.com");
    }

    @Test
    void testSaveEmployee() throws Exception {
//        give
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
//        when
        ResultActions response = mockMvc.perform(
                post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
        );
//        then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(employee.getName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    void findAllEmployeesTest() throws Exception {
        List<Employee> employees = List.of(employee);
        given(employeeService.findAllEmployees()).willReturn(employees);

        ResultActions response = mockMvc.perform(get("/api/v1/employees"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(employees.size())));

    }

    @Test
    void findEmployeeById() throws Exception {
        long id = employee.getId();
        given(employeeService.findEmployeeById(id)).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", id));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(employee.getName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    void findEmployeeByIdNotFound() throws Exception {
        long id = employee.getId();
        given(employeeService.findEmployeeById(id)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/api/v1/employees/{id}", id));

        response.andExpect(status().isNotFound());

    }

    @Test
    void updateEmployeeById() throws Exception {
//        given
        String name = "Gisell";
        employee.setName(name);
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocationOnMock -> Optional.of(invocationOnMock.getArgument(0)));
//        when
        ResultActions response = mockMvc
                .perform(put("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)));
//        then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(name)));

    }

    @Test
    void deleteEmployee() throws Exception {
        long id = employee.getId();
        willDoNothing().given(employeeService).deleteEmployeeById(id);
        var response = mockMvc.perform(delete("/api/v1/employees/{id}", id));

        response.andExpect(status().isNoContent())
                .andDo(print());
    }

}
