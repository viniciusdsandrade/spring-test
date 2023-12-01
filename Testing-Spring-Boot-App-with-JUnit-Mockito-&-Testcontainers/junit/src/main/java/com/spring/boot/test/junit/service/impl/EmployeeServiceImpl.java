package com.spring.boot.test.junit.service.impl;

import com.spring.boot.test.junit.exception.ResourceNotFoundException;
import com.spring.boot.test.junit.model.Employee;
import com.spring.boot.test.junit.repository.EmployeeRepository;
import com.spring.boot.test.junit.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (savedEmployee.isPresent())
            throw new ResourceNotFoundException("Employee already exists with this email");
        
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return null;
    }

    @Override
    public Employee getEmployeeByFirstName(String name) {
        return null;
    }

    @Override
    public Employee updateEmployeeById(Employee employee) {
        return null;
    }

    @Override
    public void deleteEmployee(Long id) {

    }
}
