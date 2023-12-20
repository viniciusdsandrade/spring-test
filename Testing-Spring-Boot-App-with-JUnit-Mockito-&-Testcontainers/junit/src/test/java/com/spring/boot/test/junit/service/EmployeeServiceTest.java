package com.spring.boot.test.junit.service;

import com.spring.boot.test.junit.exception.ResourceNotFoundException;
import com.spring.boot.test.junit.model.Employee;
import com.spring.boot.test.junit.repository.EmployeeRepository;
import com.spring.boot.test.junit.service.impl.EmployeeServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    /**
     * Mocked instance of {@link EmployeeRepository}.
     */
    @Mock
    private EmployeeRepository employeeRepository;

    /**
     * Instance of {@link EmployeeServiceImpl} with mocked dependencies.
     */
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();
    }
    
    @Test
    @DisplayName("Test save employee when employee already exists with this email")
    void givenEmployee_whenSaveEmployee_thenSuccess() {
        // Configuração: Configura o comportamento esperado do employeeRepository ao chamar findByEmail, retornando Optional.empty().
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        // Configuração: Configura o comportamento esperado do employeeRepository ao chamar save, retornando o objeto employee.
        given(employeeRepository.save(employee)).willReturn(employee);

        // Ação: Chama o método saveEmployee do employeeService.
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // Exibe o objeto savedEmployee no console (opcional).
        System.out.println(savedEmployee);

        // Assertiva: Verifica se o objeto savedEmployee não é nulo.
        assertThat(savedEmployee).isNotNull();
    }
    
    @Test
    @DisplayName("Which throws exception when employee already exists with this email")
    void givenEmployee_whenSaveEmployee_thenThrowException() {
        // Configuração: Configura o comportamento esperado do employeeRepository ao chamar findByEmail, retornando Optional.of(employee).
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // Assertiva: Verifica se chamar o método saveEmployee do employeeService lança uma exceção ResourceNotFoundException com a mensagem esperada.
        assertThatThrownBy(
                () -> employeeService.saveEmployee(employee))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee already exists with this email");
    }

    @Test
    @DisplayName("Test get all employees")
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // Configuração: Cria um segundo objeto Employee para simular uma lista com dois funcionários.
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Arthur")
                .lastName("Andrade")
                .email("vinciusdsandrade0662@gmail.com")
                .build();

        // Configuração: Configura o comportamento esperado do employeeRepository ao chamar findAll, retornando uma lista com os objetos employee e employee1.
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // Ação: Chama o método getAllEmployees do employeeService.
        List<Employee> employeeList = employeeService.getAllEmployees();

        // Assertivas: Verifica se a lista de funcionários retornada não é nula e tem tamanho dois.
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(2);
    }


    @Test
    @DisplayName("Test get all employees when empty list")
    void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // Configuração: Configura o comportamento esperado do employeeRepository ao chamar findAll, retornando uma lista vazia.
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // Ação: Chama o método getAllEmployees do employeeService.
        List<Employee> employeeList = employeeService.getAllEmployees();

        // Assertivas: Verifica se a lista de funcionários retornada não é nula e tem tamanho zero.
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(0);
    }


    @Test
    @DisplayName("Test get employee by id")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
        // Configuração: Configura o comportamento esperado do employeeRepository ao chamar findById com o ID do funcionário, retornando um Optional contendo o objeto employee.
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // Ação: Chama o método getEmployeeById do employeeService com o ID do funcionário.
        Optional<Employee> optionalEmployee = employeeService.getEmployeeById(employee.getId());

        // Assertivas: Verifica se o Optional<Employee> retornado não é nulo e não está vazio, e se o ID do objeto employee dentro do Optional corresponde ao ID do funcionário.
        assertThat(optionalEmployee).isNotNull();
        assertThat(optionalEmployee).isNotEmpty();
        assertThat(optionalEmployee.get().getId()).isEqualTo(employee.getId());
    }


    @Test
    @DisplayName("Test for update employee")
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // Configuração: Configura o comportamento esperado do employeeRepository ao chamar save com o objeto employee.
        given(employeeRepository.save(employee)).willReturn(employee);

        // Assertivas iniciais: Verifica as propriedades do objeto employee antes da atualização.
        assertThat(employee).isNotNull();
        assertThat(employee.getId()).isEqualTo(1L);
        assertThat(employee.getFirstName()).isEqualTo("Vinícius");
        assertThat(employee.getLastName()).isEqualTo("Andrade");
        assertThat(employee.getEmail()).isEqualTo("vinicius_andrade2010@hotmail.com");

        // Ação: Atualiza as propriedades do objeto employee.
        employee.setFirstName("Arthur");
        employee.setLastName("Andrade");
        employee.setEmail("arthurdsandrade0662@gmail.com");

        // Ação: Chama o método updateEmployeeById do employeeService.
        Employee updatedEmployee = employeeService.updateEmployeeById(employee);

        // Assertivas pós-atualização: Verifica se o objeto updatedEmployee foi retornado corretamente.
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Arthur");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Andrade");
        assertThat(updatedEmployee.getEmail()).isEqualTo("arthurdsandrade0662@gmail.com");
    }


    @Test
    @DisplayName("Test for delete employee")
    void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // Configuração: Configura o comportamento do employeeRepository para não fazer nada ao chamar deleteById com o ID do funcionário.
        willDoNothing().given(employeeRepository).deleteById(employee.getId());

        // Ação: Chama o método deleteEmployee do employeeService com o ID do funcionário.
        employeeService.deleteEmployee(employee.getId());

        // Verificação: Verifica se o método deleteById do employeeRepository foi chamado exatamente uma vez com o ID do funcionário.
        verify(employeeRepository, times(1)).deleteById(employee.getId());

        // Assertiva: Garante que o objeto employee não é nulo.
        assertThat(employee).isNotNull();
    }

}