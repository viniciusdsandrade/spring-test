package com.spring.boot.test.junit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.test.junit.JunitApplication;
import com.spring.boot.test.junit.config.TestConfig;
import com.spring.boot.test.junit.model.Employee;
import com.spring.boot.test.junit.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {JunitApplication.class, TestConfig.class})
@AutoConfigureMockMvc
//@Testcontainers
public class EmployeeControllerIT {
    
//    @Container
//    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:lastest");
            
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    // write test cases here
    @Test
    @DisplayName("Given employee object when create employee then return saved employee")
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // Criação de um objeto de Employee para simular a entrada do usuário
        Employee employee = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        // Chamada HTTP simulada para criar um novo Employee
        ResultActions response = mockMvc.perform(post("/api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Vinícius"))
                .andExpect(jsonPath("$.lastName").value("Andrade"))
                .andExpect(jsonPath("$.email").value("vinicius_andrade2010@hotmail.com"));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    @DisplayName("Given employee object when get all employees then return status ok")
    void givenEmployeeObject_whenGetAllEmployees_thenReturnStatusOk() throws Exception {
        // Arrange
        List<Employee> listOfEmployees = new ArrayList<>();

        listOfEmployees.add(Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build());

        listOfEmployees.add(Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("viniciusdsandrade0662@gmail.com")
                .build());

        employeeRepository.saveAll(listOfEmployees);

        ResultActions response = mockMvc.perform(get("/api/v1/employee"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(listOfEmployees.size())));
    }


    @Test
    @DisplayName("Given employee object when get employee by id then return status ok")
    void givenEmployeeObject_whenGetEmployeeById_thenReturnStatusOk() throws Exception {
        // Arrange

        // Criação de um objeto de Employee para simular dados existentes no banco de dados
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        employeeRepository.save(employee);

        // Act
        ResultActions response = mockMvc.perform(get("/api/v1/employee/{id}", employee.getId()));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(employee.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andDo(print());
    }

    @Test
    @DisplayName("Given employee object when get employee by id then return status not found")
    void givenEmployeeObject_whenGetEmployeeById_thenReturnStatusNotFound() throws Exception {
        // Arrange
        long employeeId = 1L;
        // Criação de um objeto de Employee para simular dados existentes no banco de dados
        Employee employee = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("viniciusdsandrade0662@gmail.com")
                .build();

        employeeRepository.save(employee);

        // Act
        ResultActions response = mockMvc.perform(get("/api/v1/employee/{id}", employeeId));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("Given employee object when update employee then return updated employee")
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        // Arrange
        long employeeId = 1L;
        // Criação de um objeto de Employee para simular dados existentes no banco de dados
        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Vinicius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        employeeRepository.save(employee); // Salva o objeto no banco de dados

        // Criação de um objeto de Employee para simular a entrada do usuário
        Employee employeeUpdate = Employee.builder()
                .firstName("Vinícius")
                .lastName("dos Santos Andrade")
                .email("viniciusdsandrade0662@gmail.com")
                .build();

        // Act
        ResultActions response = mockMvc.perform(put("/api/v1/employee/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeUpdate)));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employeeUpdate.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeUpdate.getLastName())))
                .andExpect(jsonPath("$.email", is(employeeUpdate.getEmail())))
                .andDo(print());
    }

    @Test
    @DisplayName("Given Employee Object when update Employee then return 404 not found")
    void givenEmployeeObject_whenUpdateEmployee_thenReturn404() throws Exception {
        // Arrange
        long employeeId = 1L;
        // Criação de um objeto de Employee para simular dados existentes no banco de dados
        Employee employee = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("viniciusdsandrade0662@gmail.com")
                .build();

        employeeRepository.save(employee); // Salva o objeto no banco de dados

        // Criação de um objeto de Employee para simular a entrada do usuário
        Employee employeeUpdate = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        // Act
        ResultActions response = mockMvc.perform(put("/api/v1/employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeUpdate)));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Given employee object when delete employee then return status no content")
    void givenEmployeeObject_whenDeleteEmployee_thenReturnStatusNoContent() throws Exception {
        // Arrange
        long employeeId = 1L;
        // Criação de um objeto de Employee para simular dados existentes no banco de dados
        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        employeeRepository.save(employee); // Salva o objeto no banco de dados

        // Act
        ResultActions response = mockMvc.perform(delete("/api/v1/employee/{id}", employee.getId()));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("Given employee object when delete employee then return status not found")
    void givenEmployeeObject_whenDeleteEmployee_thenReturnStatusNotFound() throws Exception {
        // Arrange
        long employeeId = 1L;
        // Criação de um objeto de Employee para simular dados existentes no banco de dados
        Employee employee = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("viniciusdsandrade0662@gmail.com")
                .build();

        employeeRepository.save(employee); // Salva o objeto no banco de dados

        // Act
        ResultActions response = mockMvc.perform(delete("/api/v1/employee/{id}", employeeId));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}