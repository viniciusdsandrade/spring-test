package com.spring.boot.test.junit.repository;

import com.spring.boot.test.junit.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface de repositório para gerenciar entidades Employee.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Recupera um funcionário pelo endereço de e-mail.
     *
     * @param email O endereço de e-mail do funcionário.
     * @return Um {@link Optional} contendo o funcionário com o e-mail especificado, ou vazio se não encontrado.
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Método de consulta derivada para encontrar um funcionário pelo primeiro nome e sobrenome.
     *
     * @param firstName O primeiro nome do funcionário.
     * @param lastName  O sobrenome do funcionário.
     * @return O funcionário com o primeiro nome e sobrenome especificados.
     */
    Employee findByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Método de consulta JPQL para encontrar um funcionário usando o primeiro nome e sobrenome.
     *
     * @param firstName O primeiro nome do funcionário.
     * @param lastName  O sobrenome do funcionário.
     * @return O funcionário com o primeiro nome e sobrenome especificados.
     */
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    /**
     * Método de consulta JPQL com parâmetros nomeados para encontrar um funcionário.
     *
     * @param firstName O primeiro nome do funcionário.
     * @param lastName  O sobrenome do funcionário.
     * @return O funcionário com o primeiro nome e sobrenome especificados.
     */
    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName")
    Employee findByJPQLWithNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * Realiza uma consulta nativa para encontrar um funcionário usando SQL nativo.
     *
     * @param firstName O primeiro nome do funcionário.
     * @param lastName  O sobrenome do funcionário.
     * @return O funcionário com o primeiro nome e sobrenome especificados.
     */
    @Query(value = "SELECT * FROM employee WHERE first_name = ?1 AND last_name = ?2", nativeQuery = true)
    Employee findByNativeQuery(String firstName, String lastName);

    /**
     * Realiza uma consulta nativa com parâmetros nomeados para encontrar um funcionário usando SQL nativo.
     *
     * @param firstName O primeiro nome do funcionário.
     * @param lastName  O sobrenome do funcionário.
     * @return O funcionário com o primeiro nome e sobrenome especificados.
     */
    @Query(value = "SELECT * FROM employee WHERE first_name = :firstName AND last_name = :lastName", nativeQuery = true)
    Employee findByNativeQueryWithNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * Realiza uma consulta nativa para encontrar um funcionário usando SQL nativo, considerando apenas o primeiro nome.
     *
     * @param name O primeiro nome do funcionário.
     * @return O funcionário com o primeiro nome especificado.
     */
    @Query(value = "SELECT * FROM employee WHERE first_name = :firstName", nativeQuery = true)
    Optional<Employee> findByFirstName(@Param("firstName") String name);
}