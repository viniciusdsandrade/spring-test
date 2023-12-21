package com.spring.boot.test.junit.repository;

import com.spring.boot.test.junit.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testes de integração para a classe {@link EmployeeRepository} usando a anotação {@link DataJpaTest}.
 * Esses testes focam na interação entre o código da aplicação e o banco de dados subjacente.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIntegrationTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee empregado1;

    /**
     * Método de configuração executado antes de cada caso de teste.
     * Exclui todos os funcionários do repositório e inicializa um objeto {@link Employee} de exemplo.
     */
    @BeforeEach
    public void setUp() {

        employeeRepository.deleteAll();

        empregado1 = Employee.builder()
                .firstName("Vinícius")
                .lastName("Andrade")
                .email("vinicius_andrade2010@hotmail.com")
                .build();
    }

    /**
     * Caso de teste para verificar o salvamento de um único objeto {@link Employee} no repositório.
     * <p>
     * Dado um objeto {@link Employee},
     * quando o objeto é salvo usando o {@link EmployeeRepository},
     * então o funcionário salvo não deve ser nulo,
     * o ID do funcionário salvo não deve ser nulo,
     * e os atributos do funcionário salvo devem corresponder aos do funcionário de entrada.
     */
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


    /**
     * Caso de teste para verificar o salvamento de uma lista de objetos {@link Employee} no repositório.
     * <p>
     * Dados dois objetos {@link Employee},
     * quando os objetos são salvos simultaneamente usando o {@link EmployeeRepository},
     * então ambos os IDs dos funcionários não devem ser nulos,
     * o tamanho da lista de funcionários salvos deve ser 2,
     * e os atributos dos funcionários salvos devem corresponder aos dos funcionários de entrada.
     */
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

    /**
     * Caso de teste para verificar a busca de um objeto {@link Employee} pelo ID no repositório.
     * <p>
     * Dado um objeto {@link Employee} salvo,
     * quando o ID desse objeto é utilizado para buscar um funcionário usando o {@link EmployeeRepository},
     * então o funcionário retornado não deve ser nulo.
     */
    @Test
    @DisplayName("Given Employee Object When Find By Id Then Return Employee Object")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {


        Employee empregadoSalvo = employeeRepository.save(empregado1);

        Employee employeeDB = employeeRepository.findById(empregadoSalvo.getId()).orElse(null);
        assertThat(employeeDB).isNotNull();
    }

    /**
     * Caso de teste para verificar a busca de um objeto {@link Employee} pelo e-mail no repositório.
     * <p>
     * Dado um objeto {@link Employee} salvo,
     * quando o e-mail desse objeto é utilizado para buscar um funcionário usando o {@link EmployeeRepository},
     * então o funcionário retornado não deve ser nulo.
     */
    @Test
    @DisplayName("Given Employee Object When Find By Email Then Return Employee Object")
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {

        Employee empregadoSalvo = employeeRepository.save(empregado1);

        // when - save employees simultaneously
        Employee employeeDB = employeeRepository.findByEmail(empregadoSalvo.getEmail()).orElse(null);

        // then - verify if the employee was saved correctly
        assertThat(employeeDB).isNotNull();
    }

    /**
     * Caso de teste para verificar a atualização de um objeto {@link Employee} no repositório.
     * <p>
     * Dado um objeto {@link Employee} salvo,
     * quando esse objeto é atualizado e salvo usando o {@link EmployeeRepository},
     * então o funcionário atualizado não deve ser nulo,
     * o ID do funcionário atualizado não deve ser nulo,
     * e os atributos do funcionário atualizado devem corresponder às alterações realizadas.
     */
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

    /**
     * Caso de teste para verificar a exclusão de um objeto {@link Employee} no repositório.
     * <p>
     * Dado um objeto {@link Employee} salvo,
     * quando esse objeto é excluído usando o {@link EmployeeRepository},
     * então uma busca pelo ID do funcionário deve retornar um {@link Optional} vazio.
     */
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

    /**
     * Caso de teste para verificar a busca de um objeto {@link Employee} usando JPQL (Java Persistence Query Language) no repositório.
     * <p>
     * Dado um objeto {@link Employee} salvo,
     * quando a busca é realizada usando JPQL no {@link EmployeeRepository},
     * então o funcionário retornado não deve ser nulo e seus atributos devem corresponder ao funcionário salvo.
     */
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

    /**
     * Caso de teste para verificar a busca de um objeto {@link Employee} usando uma consulta nativa no repositório.
     * <p>
     * Dado um objeto {@link Employee} salvo,
     * quando a busca é realizada usando uma consulta nativa no {@link EmployeeRepository},
     * então o funcionário retornado não deve ser nulo e seus atributos devem corresponder ao funcionário salvo.
     */
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

    /**
     * Caso de teste para verificar a busca de um objeto {@link Employee} usando JPQL (Java Persistence Query Language) com parâmetros nomeados no repositório.
     * <p>
     * Dado um objeto {@link Employee} salvo,
     * quando a busca é realizada usando JPQL com parâmetros nomeados no {@link EmployeeRepository},
     * então o funcionário retornado não deve ser nulo e seus atributos devem corresponder ao funcionário salvo.
     */
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

    /**
     * Caso de teste para verificar a busca de um objeto {@link Employee} usando o primeiro nome e o último nome no repositório.
     * <p>
     * Dado um objeto {@link Employee} salvo,
     * quando a busca é realizada usando o primeiro nome e o último nome no {@link EmployeeRepository},
     * então o funcionário retornado não deve ser nulo e seus atributos devem corresponder ao funcionário salvo.
     */
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

    /**
     * Caso de teste para verificar a busca de um objeto {@link Employee} usando uma consulta nativa com parâmetros nomeados no repositório.
     * <p>
     * Dado um objeto {@link Employee} salvo,
     * quando a busca é realizada usando uma consulta nativa com parâmetros nomeados no {@link EmployeeRepository},
     * então o funcionário retornado não deve ser nulo e seus atributos devem corresponder ao funcionário salvo.
     */
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