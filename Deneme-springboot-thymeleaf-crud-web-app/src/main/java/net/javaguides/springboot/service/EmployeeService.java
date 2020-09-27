package net.javaguides.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;

import net.javaguides.springboot.model.Employee;

public interface EmployeeService {
     List<Employee> getAllEmployees();
     
     void saveEmployees(Employee employee);
     
     Employee getEmployeeById(long id);
     
     void deleteEmployee(long id);
     
     List<Employee> findDuplicates(Employee employee);     
     
     Page<Employee> findPaginated(int pageNo,int pageSize, String sortField , String sortDirection);
     
}
