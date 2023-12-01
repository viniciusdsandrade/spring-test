package com.spring.boot.test.junit.service;

import com.spring.boot.test.junit.exception.ResourceNotFoundException;
import com.spring.boot.test.junit.model.Employee;
import com.spring.boot.test.junit.repository.EmployeeRepository;
import com.spring.boot.test.junit.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();
    }

    @Test
    @DisplayName("Test save employee when employee already exists with this email")
    public void givenEmployee_whenSaveEmployee_thenSuccess() {
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);
        Employee savedEmployee = employeeService.saveEmployee(employee);
        Assertions.assertThat(savedEmployee).isNotNull();
    }
    
    @Test
    @DisplayName("Which throws exception when employee already exists with this email")
    public void givenEmployee_whenSaveEmployee_thenThrowException() {
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        Assertions.assertThatThrownBy(
                () -> employeeService.saveEmployee(employee))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee already exists with this email");
    }
}