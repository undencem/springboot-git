package net.javaguides.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;

@Controller
public class EmployeeController {

	// creating method handler for home page (index.html)
	// to display list of employees
	
	@Autowired
	private EmployeeService employeeService;
	
	// list of Employees
	@GetMapping("/")
	public String viewHomePage(Model model) {
		model.addAttribute("listEmployees", employeeService.getAllEmployees());
		return "index";
	}
	
	// show new employee form
	@GetMapping("/showNewEmployeeForm")
	public String showNewEmployeeForm(Model model){
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "new_employee";
	}
	
	// save Employee
	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee) {
		
		List<Employee> duplicateEntry = null ;
		
		duplicateEntry = employeeService.checkDuplicates(
				employee.getFirstName(),
				employee.getLastName(), 
				employee.getEmail());
		
		if(duplicateEntry.size() == 0) {
			employeeService.saveEmployee(employee);	
			duplicateEntry = null ;
		}
		return "redirect:/";		
	}
	
	// update
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable(value="id") Long id , Model model) {
		
		// get employee from the service
		Employee employee = employeeService.getEmployeeById(id);
	
		
		// set employee as a model attribute to pre-populate form
		model.addAttribute("employee", employee);
		return "update_employee";
	}
	
	// delete
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable(value="id") Long id , Model model) {
		
		// call delete Employee method
		this.employeeService.deleteEmployeeById(id);
		return("redirect:/");
	}
	
	
}
