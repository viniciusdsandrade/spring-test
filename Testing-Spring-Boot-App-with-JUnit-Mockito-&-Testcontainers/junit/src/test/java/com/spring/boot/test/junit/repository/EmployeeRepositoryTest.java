package com.spring.boot.test.junit.repository;

import com.spring.boot.test.junit.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee empregado1;

    @BeforeEach
    public void setUp() {
        empregado1 = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();
    }

    @Test
    @DisplayName("Given Employee Object When Save Then Return Saved Employee")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

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

    @Test
    @DisplayName("Given Employee Object When Find By Id Then Return Employee Object")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {


        Employee empregadoSalvo = employeeRepository.save(empregado1);

        Employee employeeDB = employeeRepository.findById(empregadoSalvo.getId()).orElse(null);
        assertThat(employeeDB).isNotNull();
    }

    @Test
    @DisplayName("Given Employee Object When Find By Email Then Return Employee Object")
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {

        Employee empregadoSalvo = employeeRepository.save(empregado1);

        // when - save employees simultaneously
        Employee employeeDB = employeeRepository.findByEmail(empregadoSalvo.getEmail()).orElse(null);

        // then - verify if the employee was saved correctly
        assertThat(employeeDB).isNotNull();
    }

    @Test
    @DisplayName("Given Employee Object When Update Then Return Updated Employee")
    public void givenEmployeeObject_whenUpdate_thenReturnUpdatedEmployee() {

        employeeRepository.save(empregado1);

        // when - update employee
        Employee savedEmployee = employeeRepository.findById(empregado1.getId()).orElse(null);
        assert savedEmployee != null;
        savedEmployee.setFirstName("Vinícius");
        savedEmployee.setLastName("dos Santos Andrade");
        savedEmployee.setEmail("vinicius_andrade2010@hotmail.com");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify if the employee was updated correctly
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Vinícius");
        assertThat(updatedEmployee.getLastName()).isEqualTo("dos Santos Andrade");
        assertThat(updatedEmployee.getEmail()).isEqualTo("vinicius_andrade2010@hotmail.com");
    }

    @Test
    @DisplayName("Given Employee Object When Delete Then Return Null")
    public void givenEmployeeObject_whenDelete_thenReturnsNull() {

        employeeRepository.save(empregado1);

        // when - delete employee
        employeeRepository.delete(empregado1);

        // then - verify if the employee was deleted correctly
        Optional<Employee> employeeDB = employeeRepository.findById(empregado1.getId());
        assertThat(employeeDB).isEmpty();
    }

    @Test
    @DisplayName("Given Employee Object When Find By JPQL Then Return Employee Object")
    public void givenEmployeeObject_whenFindByJPQL_thenReturnEmployeeObject() {


        employeeRepository.save(empregado1);

        // when - find employee by JPQL
        Employee employeeDB = employeeRepository.findByJPQL(empregado1.getFirstName(), empregado1.getLastName());

        // then - verify if the employee was found correctly
        assertThat(employeeDB).isNotNull();

        // Assertions to verify if the employee was found correctly
        assertEquals("Vinícius", employeeDB.getFirstName());
        assertEquals("Andrade", employeeDB.getLastName());
        assertEquals("vinicius_andrade2010@hotmail.com", employeeDB.getEmail());
    }

    @Test
    @DisplayName("Given Employee Object When Find By Native Query Then Return Employee Object")
    public void givenEmployeeObject_whenFindByNativeQuery_thenReturnEmployeeObject() {

        employeeRepository.save(empregado1);

        // when - find employee by Native Query
        Employee employeeDB = employeeRepository.findByNativeQuery(empregado1.getFirstName(), empregado1.getLastName());

        // then - verify if the employee was found correctly
        assertThat(employeeDB).isNotNull();

        // Assertions to verify if the employee was found correctly
        assertEquals("Vinícius", employeeDB.getFirstName());
        assertEquals("Andrade", employeeDB.getLastName());
        assertEquals("vinicius_andrade2010@hotmail.com", employeeDB.getEmail());
    }

    @Test
    @DisplayName("Given Employee Object When Find By JPQL With Named Parameters Then Return Employee Object")
    public void givenEmployeeObject_whenFindByJPQLWithNamedParameters_thenReturnEmployeeObject() {
        employeeRepository.save(empregado1);

        // when - find employee by JPQL with named parameters
        Employee employeeDB = employeeRepository.findByJPQLWithNamedParameters(empregado1.getFirstName(), empregado1.getLastName());

        // then - verify if the employee was found correctly
        assertThat(employeeDB).isNotNull();

        // Assertions to verify if the employee was found correctly
        assertEquals("Vinícius", employeeDB.getFirstName());
        assertEquals("Andrade", employeeDB.getLastName());
        assertEquals("vinicius_andrade2010@hotmail.com", employeeDB.getEmail());
    }

    @Test
    @DisplayName("Given Employee Object When Find By First Name And Last Name Then Return Employee Object")
    public void givenEmployeeObject_whenFindByFirstNameAndLastName_thenReturnEmployeeObject() {

        employeeRepository.save(empregado1);

        // when - find employee by first name and last name
        Employee employeeDB = employeeRepository.findByFirstNameAndLastName(empregado1.getFirstName(), empregado1.getLastName());

        // then - verify if the employee was found correctly
        assertThat(employeeDB).isNotNull();

        // Assertions to verify if the employee was found correctly
        assertEquals("Vinícius", employeeDB.getFirstName());
        assertEquals("Andrade", employeeDB.getLastName());
        assertEquals("vinicius_andrade2010@hotmail.com", employeeDB.getEmail());
    }

    @Test
    @DisplayName("Given Employee Object When Find By Native Query With Named Parameters Then Return Employee Object")
    public void givenEmployeeObject_whenFindByNativeQueryWithNamedParameters_thenReturnEmployeeObject() {
        employeeRepository.save(empregado1);

        // when - find employee by Native Query with named parameters
        Employee employeeDB = employeeRepository.findByNativeQueryWithNamedParameters(empregado1.getFirstName(), empregado1.getLastName());

        // then - verify if the employee was found correctly
        assertThat(employeeDB).isNotNull();

        // Assertions to verify if the employee was found correctly
        assertEquals("Vinícius", employeeDB.getFirstName());
        assertEquals("Andrade", employeeDB.getLastName());
        assertEquals("vinicius_andrade2010@hotmail.com", employeeDB.getEmail());
    }
}