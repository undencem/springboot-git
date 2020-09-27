package net.javaguides.springboot.controller;


import java.util.List;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/")
	public String viewHomePage(Model model) {
		// "firstName" and "asc" are default values
		return findPaginated(1,"firstName","asc",model);
	}
	
	@GetMapping("/showNewEmployeeForm")
	public String showNewForm(Model model) {
		Employee employee = new Employee();
	    model.addAttribute("employee", employee);
	    return "newEmployeeForm";
	}
	
	@PostMapping(path="/new")
	public String saveEmployee(
			@Valid @ModelAttribute("employee") Employee employee , 
			BindingResult bindingResult ,
			Model model )  {
		
		if(bindingResult.hasErrors()) {
			return "newEmployeeForm";
		}
		
		List<Employee> duplicateEntry = null ;
		
		duplicateEntry = employeeService.findDuplicates(employee);
				
		if(duplicateEntry.size() == 0) {
			employeeService.saveEmployees(employee);			
		}
		
		duplicateEntry = null;
		return "redirect:/";
		
	}
	
	@PostMapping(path="/updateNew")
	public String updateEmployee(
			@Valid @ModelAttribute("employee") Employee employee , 
			BindingResult bindingResult ,
			Model model )  {
		
		if(bindingResult.hasErrors()) {
			return "updateNewForm";
		}
		
		List<Employee> duplicateEntry = null ;
		
		duplicateEntry = employeeService.findDuplicates(employee);
				
		if(duplicateEntry.size() == 0) {
			employeeService.saveEmployees(employee);			
		}
		
		duplicateEntry = null;
		return "redirect:/";		
	}
	
	@GetMapping("/showUpdate/{id}")
	public String updateEmployee(@PathVariable(value="id") Long id , Model model){
		
		// get employee from service
		Employee employee = employeeService.getEmployeeById(id);
		
		// set employee as model attribute to pre-populate form
		model.addAttribute("employee", employee);
		return "updateNewForm";		
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable Long id , Model model) {
		this.employeeService.deleteEmployee(id);
		return "redirect:/";
	}
	
	// pagination and sorting
		// while sorting 
		//  /page/1?sortField=name&sortDir=asc
		// we send a url like this
		// for sortField and sortDir we use RequestParam	
	
	// Pagination
	@GetMapping("/page/{pageNo}")
	public String findPaginated(
			@PathVariable(value="pageNo") int pageNo ,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model){
		
		int pageSize = 5 ; 
		
		Page<Employee> page = this.employeeService.findPaginated(pageNo, pageSize , sortField , sortDir);
		
		List<Employee> pagedList = page.getContent();
		
		model.addAttribute("currentPage",pageNo);
		
		model.addAttribute("totalPages", page.getTotalPages());
		
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("listEmployees", pagedList);
		
		model.addAttribute("sortField", sortField);
		
		model.addAttribute("sortDir", sortDir);
		
		model.addAttribute("reverseSortDir", sortDir.equals("asc")?"desc":"asc");
		
		model.addAttribute("stableSortDir", sortDir.equals("asc")?"asc":"desc");
		
		return "homepage";	
	}
}
