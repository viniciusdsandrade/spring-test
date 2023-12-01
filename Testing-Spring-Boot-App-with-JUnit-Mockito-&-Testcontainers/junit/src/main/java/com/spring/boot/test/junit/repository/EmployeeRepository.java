package com.spring.boot.test.junit.repository;

import com.spring.boot.test.junit.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    // Derived Query
    Employee findByFirstNameAndLastName(String firstName, String lastName);

    // JPQL
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    // JPQL with Named Parameters
    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName")
    Employee findByJPQLWithNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // Native Query
    @Query(value = "SELECT * FROM employee WHERE first_name = ?1 AND last_name = ?2", nativeQuery = true)
    Employee findByNativeQuery(String firstName, String lastName);
    
    // Native Query with Named Parameters
    @Query(value = "SELECT * FROM employee WHERE first_name = :firstName AND last_name = :lastName", nativeQuery = true)
    Employee findByNativeQueryWithNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
