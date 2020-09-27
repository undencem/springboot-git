package net.javaguides.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> { 
	
	@Query("SELECT employee FROM Employee employee WHERE firstName = ?1 AND lastName = ?2 AND email = ?3")
	List<Employee> findByFirstNameAndLastNameAndEmail(String firstName,String lastName,String email);	
}