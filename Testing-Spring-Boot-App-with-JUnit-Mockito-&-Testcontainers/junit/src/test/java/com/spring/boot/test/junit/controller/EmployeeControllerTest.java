package com.spring.boot.test.junit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.test.junit.model.Employee;
import com.spring.boot.test.junit.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc é uma classe de teste que permite simular chamadas HTTP

    @MockBean
    private EmployeeService employeeService;  // EmployeeService é um serviço simulado usando Mockito

    @Autowired
    private ObjectMapper objectMapper;  // ObjectMapper é utilizado para converter objetos Java em JSON

    @Test
    @DisplayName("Given employee object when create employee then return saved employee")
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // Criação de um objeto de Employee para simular a entrada do usuário
        Employee employee = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        // Configuração do serviço mock para retornar o mesmo Employee que recebe
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

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

        // Criação de objetos de Employee para simular dados existentes no banco de dados
        Employee employee1 = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("viniciusdsandrade0662@gmail.com")
                .build();

        List<Employee> employees = List.of(employee1, employee2);

        // Configuração do serviço mock para retornar uma lista de funcionários ao chamar getAllEmployees
        given(employeeService.getAllEmployees())
                .willReturn(employees);

        // Act

        // Chamada HTTP simulada para obter todos os Employees
        ResultActions response = mockMvc.perform(get("/api/v1/employee"));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(employees.size())))
                .andExpect(jsonPath("$[0].firstName", is(employee1.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(employee1.getLastName())))
                .andExpect(jsonPath("$[0].email", is(employee1.getEmail())))
                .andExpect(jsonPath("$[1].firstName", is(employee2.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(employee2.getLastName())))
                .andExpect(jsonPath("$[1].email", is(employee2.getEmail())))
                .andDo(print());
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

        // Configuração do serviço mock para retornar um Employee ao chamar getEmployeeById
        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.of(employee));

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
    @DisplayName("Given employee object when get employee by id then return status 404 not found")
    void givenEmployeeObject_whenGetEmployeeById_thenReturnStatusNotFound() throws Exception {
        // Arrange
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();
        
        // Configuração do serviço mock para retornar um Optional vazio ao chamar getEmployeeById
        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.empty());

        // Act
        ResultActions response = mockMvc.perform(get("/api/v1/employee/{id}", employeeId));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Given employee object when update employee by id then return status ok")
    void givenEmployeeObject_whenUpdateEmployeeById_thenReturnStatusOk() throws Exception {
        // Arrange

        long employeeId = 1L;

        // Criação de um objeto de Employee para simular dados existentes no banco de dados
        Employee saveEmployee = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        // Criação de um objeto de Employee para simular a entrada do usuário
        Employee updateEmployee = Employee.builder()
                .firstName("Arthur")
                .lastName("Andrade")
                .email("viniciusdsandrrade0662@gmail.com")
                .build();

        // Configuração do serviço mock para retornar um Employee ao chamar getEmployeeById
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(saveEmployee));

        // Configuração do serviço mock para retornar um Employee ao chamar updateEmployeeById
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(updateEmployee));
        given(employeeService.updateEmployeeById(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));


        // Act
        ResultActions response = mockMvc.perform(put("/api/v1/employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updateEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updateEmployee.getEmail())))
                .andDo(print());
    }

    @Test
    @DisplayName("Given employee object when update employee by id then return status 404 not found")
    void givenEmployeeObject_whenUpdateEmployeeById_thenReturnStatusNotFound() throws Exception {

        long employeeId = 1L;

        // Criação de um objeto de Employee para simular dados existentes no banco de dados
        Employee saveEmployee = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        // Criação de um objeto de Employee para simular a entrada do usuário
        Employee updateEmployee = Employee.builder()
                .firstName("Arthur")
                .lastName("Andrade")
                .email("viniciusdsandrrade0662@gmail.com")
                .build();

        // Configuração do serviço mock para retornar um Optional vazio ao chamar getEmployeeById
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // Configuração do serviço mock para retornar um Optional vazio ao chamar updateEmployeeById
        given(employeeService.updateEmployeeById(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // Act
        ResultActions response = mockMvc.perform(put("/api/v1/employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Given employee object when delete employee by id then return status ok")
    void givenEmployeeObject_whenDeleteEmployeeById_thenReturnStatusOk() throws Exception {

        long employeeId = 1L;

        // Criação de um objeto de Employee para simular dados existentes no banco de dados
        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        // Configuração do serviço mock para retornar um Employee ao chamar getEmployeeById
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(employee));
        
        // Act
        ResultActions response = mockMvc.perform(delete("/api/v1/employee/{id}", employeeId));
        
        // Asserts para verificar se a resposta é conforme o esperado
        response.andExpect(status().isNoContent())
                .andDo(print());

        then(employeeService).should().deleteEmployee(employeeId);
    }

    @Test
    @DisplayName("Given employee object when delete employee by id then return status 404 not found")
    void givenEmployeeObject_whenDeleteEmployeeById_thenReturnStatusNotFound_ifNotFoundObjectToDelete() throws Exception {
        // Given an employee ID
        long employeeId = 123;

        // When trying to delete the employee with the given ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", employeeId))
                // Then expect status 404 (not found)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}