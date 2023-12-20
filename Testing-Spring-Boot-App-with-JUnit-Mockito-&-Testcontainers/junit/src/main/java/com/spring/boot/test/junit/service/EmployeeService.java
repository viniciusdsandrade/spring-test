package com.spring.boot.test.junit.service;

import com.spring.boot.test.junit.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(Long id);

    Optional<Employee> getEmployeeByFirstName(String name);

    Employee updateEmployeeById(Employee employee);

    void deleteEmployee(Long id);
}