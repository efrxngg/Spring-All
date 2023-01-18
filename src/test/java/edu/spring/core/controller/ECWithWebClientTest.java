package edu.spring.core.controller;

import edu.spring.core.entity.Employee;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ECWithWebClientTest {

    @Autowired
    private WebTestClient webClient;

    private Employee employee;

    private final String URL = "http://localhost:8080/api/v1/employees";

    @BeforeEach
    public void setup() {
        employee = new Employee(
                2L,
                "Efren", "Galarza",
                "efren@gmail.com"
        );
    }

    @Test
    @Order(1)
    void testEndPointSaveE() {
        webClient.post().uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee)
                .exchange()//envia el request
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(employee.getName());
    }

    @Test
    @Order(2)
    void testEndPointFindAllE() {
        webClient.get().uri(URL)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].name").isEqualTo(employee.getName())
                .jsonPath("$").value(hasSize(1));
    }

    @Test
    @Order(3)
    void testEndPointFindAllEWithCosume() {
        webClient.get().uri(URL)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Employee.class)
                .consumeWith(response -> {
                    List<Employee> employees = response.getResponseBody();
                    assertNotNull(employees);
                    assertEquals(1, employees.size());
                });
    }

    @Test
    @Order(4)
    void testEndPointFindByIdE() {
        webClient.get().uri(URL.concat("/2"))
                .exchange()//envia el request
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(employee.getName());
    }

    @Test
    @Order(5)
    void testEndPointUpdateByIdE() {
        String name = "Gisell";
        employee.setName(name);
        webClient.put().uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee)
                .exchange()//envia el request
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(name);
    }

    @Test
    @Order(6)
    void testEndPointDeleteByIdE() {
        webClient.delete().uri(URL.concat("/2"))
                .exchange()//envia el request
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty();
    }

}
