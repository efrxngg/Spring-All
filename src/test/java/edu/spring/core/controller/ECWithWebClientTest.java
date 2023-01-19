package edu.spring.core.controller;

import edu.spring.core.entity.Employee;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ECWithWebClientTest {

    @Autowired
    private WebTestClient webClient;

    private final Employee employee = new Employee(
            null,
            "Test",
            "Test",
            null);

    private final String URL = "/api/v1/employees";

    @Test
    @Order(1)
    void testEndPointSaveE() {
        employee.setEmail("testwebclient@%s.com".formatted(
                UUID.randomUUID().toString().replaceAll("-", ""))
        );
        webClient.post().uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee)
                .exchange()//envia el request
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Employee.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    employee.setId(response.getResponseBody().getId());
                });
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
        webClient.get().uri(URL.concat("/%s".formatted(employee.getId())))
                .exchange()//envia el request
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo(employee.getName());
    }

    @Test
    @Order(5)
    void testEndPointUpdateByIdE() {
        String name = "test2";
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
        webClient.delete().uri(URL.concat("/%s".formatted(employee.getId())))
                .exchange()//envia el request
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty();
    }

}
