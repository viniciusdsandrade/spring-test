package com.spring.boot.test.junit.repository;

import com.spring.boot.test.junit.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Given Employee Object When Save Then Return Saved Employee")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        Employee empregado1 = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        Employee empregadoSalvo = employeeRepository.save(empregado1);

        assertThat(empregadoSalvo).isNotNull();
        assertThat(empregadoSalvo.getId()).isNotNull();
        assertThat(empregadoSalvo.getFirstName()).isEqualTo(empregado1.getFirstName());
        assertThat(empregadoSalvo.getLastName()).isEqualTo(empregado1.getLastName());
        assertThat(empregadoSalvo.getEmail()).isEqualTo(empregado1.getEmail());
    }

    @Test
    @DisplayName("Test saving two employees simultaneously")
    public void givenTwoEmployees_whenSaveAll_thenReturnListOfEmployees() {
        // given - preconditions or the input
        Employee empregado1 = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        Employee empregado2 = Employee.builder()
                .firstName("Arthur")
                .lastName("Andrade")
                .email("arthurdsandrade2008@gmail.com")
                .build();

        // when - save employees simultaneously
        List<Employee> employeeList = employeeRepository.saveAll(List.of(empregado1, empregado2));

        assertThat(empregado1.getId()).isNotNull();
        assertThat(empregado2.getId()).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

        // Retrieve saved employees from the repository
        Employee savedEmpregado1 = employeeRepository.findById(empregado1.getId()).orElse(null);
        Employee savedEmpregado2 = employeeRepository.findById(empregado2.getId()).orElse(null);

        // Assertions to verify if the employees were saved correctly
        assertNotNull(savedEmpregado1);
        assertNotNull(savedEmpregado2);

        assertEquals("Vinícius", savedEmpregado1.getFirstName());
        assertEquals("Andrade", savedEmpregado1.getLastName());
        assertEquals("vinicius_andrade2010@hotmail.com", savedEmpregado1.getEmail());

        assertEquals("Arthur", savedEmpregado2.getFirstName());
        assertEquals("Andrade", savedEmpregado2.getLastName());
        assertEquals("arthurdsandrade2008@gmail.com", savedEmpregado2.getEmail());
    }

    //Junit test for
    @Test
    @DisplayName("Given Employee Object When Find By Id Then Return Employee Object")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        Employee empregado1 = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("viniciusdsandrade0662@gmail.com")
                .build();

        Employee empregadoSalvo = employeeRepository.save(empregado1);

        Employee employeeDB = employeeRepository.findById(empregadoSalvo.getId()).orElse(null);
        assertThat(employeeDB).isNotNull();
    }

    //Junit test for
    @Test
    @DisplayName("Given Employee Object When Find By Email Then Return Employee Object")
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {
        // given - preconditions or the input
        Employee empregado1 = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();

        Employee empregadoSalvo = employeeRepository.save(empregado1);

        // when - save employees simultaneously
        Employee employeeDB = employeeRepository.findByEmail(empregado1.getEmail()).orElse(null);

        // then - verify if the employee was saved correctly
        assertThat(employeeDB).isNotNull();
    }
}