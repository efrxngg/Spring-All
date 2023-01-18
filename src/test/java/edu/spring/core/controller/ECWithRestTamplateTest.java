package edu.spring.core.controller;

import edu.spring.core.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/*
Siempre que trabajes con TestRestTemplate o WebTestClient
el proyecto principal debe estar activo para su correcto funcionamiento
 */
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ECWithRestTamplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Employee employee;

    private final String URL = "http://localhost:8080/api/v1/employees";

    @BeforeEach
    public void setup() {
        employee = new Employee(
                1L,
                "Efren", "Galarza",
                "efren@gmail.com"
        );
    }

    @Test
    @Order(1)
    void testEndPointSaveEmployee() {
        ResponseEntity<Employee> response = restTemplate
                .postForEntity(URL, employee, Employee.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(2)
    void testEndPointFindAllEmployees() {
        ResponseEntity<Employee[]> response = restTemplate.getForEntity(URL, Employee[].class);

        assertThat(response.getStatusCode())
                .matches(status -> status.equals(HttpStatus.OK) || status.equals(HttpStatus.NO_CONTENT));
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    @Order(3)
    void testEndPointFindEmployeeById() {
        ResponseEntity<Employee> response = restTemplate
                .getForEntity(URL.concat("/1"), Employee.class);
        var employee = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(employee);
    }

    @Test
    @Order(4)
    void testEndPointUpdateEmployeeById() {
        String name = "Gisell";
        employee.setName(name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Employee> requestEntity = new HttpEntity<>(employee, headers);

        ResponseEntity<Employee> response = restTemplate.exchange(
                URL,
                HttpMethod.PUT,
                requestEntity,
                Employee.class,
                Map.of());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(name, response.getBody().getName());

    }

    @Test
    @Order(5)
    void testEndPointDeleteEmployeeById() {
        ResponseEntity<String> response = restTemplate
                .exchange(URL.concat("/{id}"),
                        HttpMethod.DELETE, null,
                        String.class,
                        Map.of("id", 1L));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(String.class, response.getBody());
    }

}
