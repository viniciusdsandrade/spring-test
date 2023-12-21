package com.spring.boot.test.junit.service.impl;

import com.spring.boot.test.junit.exception.EmailAlreadyExistsException;
import com.spring.boot.test.junit.exception.ResourceNotFoundException;
import com.spring.boot.test.junit.model.Employee;
import com.spring.boot.test.junit.repository.EmployeeRepository;
import com.spring.boot.test.junit.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (savedEmployee.isPresent())
            throw new EmailAlreadyExistsException(employee.getEmail());

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isEmpty())
            throw new ResourceNotFoundException("Employee not found with id: " + id);

        return employee;
    }

    @Override
    public Optional<Employee> getEmployeeByFirstName(String name) {
        return employeeRepository.findByFirstName(name);
    }

    @Override
    public Employee updateEmployeeById(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isEmpty())
            throw new ResourceNotFoundException("Employee not found with id: " + id);

        employeeRepository.delete(employee.get());
    }
}