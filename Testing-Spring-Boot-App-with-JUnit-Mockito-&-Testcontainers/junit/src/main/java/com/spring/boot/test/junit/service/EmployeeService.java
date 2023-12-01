package com.spring.boot.test.junit.service;

import com.spring.boot.test.junit.model.Employee;

public interface EmployeeService {
    
    Employee saveEmployee(Employee employee);

    Employee getEmployeeById(Long id);
    
    Employee getEmployeeByFirstName(String name);
    
    Employee updateEmployeeById(Employee employee);
    
    void deleteEmployee(Long id);
}
