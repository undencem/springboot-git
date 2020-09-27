package net.javaguides.springboot.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public List<Employee> getAllEmployees() {
	   return employeeRepository.findAll();
	}

	@Override
	public void saveEmployees(Employee employee) {
		this.employeeRepository.save(employee);
	}

	@Override
	public Employee getEmployeeById(long id) {
		Optional<Employee> optional = this.employeeRepository.findById(id);
		Employee employee = null ; 
		
		if(optional.isPresent()) {
			employee = optional.get();			
		}
		else {
			throw new RuntimeException("No id ::");
		}
		return employee;
	}

	@Override
	public void deleteEmployee(long id) {
		this.employeeRepository.deleteById(id);		
	}

	@Override
	public List<Employee> findDuplicates(Employee employee) {
		List<Employee> employeeList=this.employeeRepository.findAll();
		List<Employee> duplicateList = 
				employeeList
				.stream()
				.filter(c -> 
				        c.getFirstName().startsWith(employee.getFirstName())
						&&
						c.getLastName().startsWith(employee.getLastName()) 
						&&
						c.getEmail().startsWith(employee.getEmail()))
						.collect(Collectors.toList());
		
	    return duplicateList;						
	}

	@Override
	public Page<Employee> findPaginated(int pageNo, int pageSize , String sortField , String sortDirection ) {
		
		Sort sort = sortDirection
				.equalsIgnoreCase(Sort.Direction.ASC.name())
				? Sort.by(sortField).ascending():
				  Sort.by(sortField).descending() ;
		
		//If you want ONLY sorting then change like this
		//Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		//return this.employeeRepository.findAll(pageable);
		
		Pageable pageable = PageRequest.of(pageNo-1, pageSize,sort);
		return this.employeeRepository.findAll(pageable);
	}
}
