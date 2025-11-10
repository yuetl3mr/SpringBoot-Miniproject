package com.employee.management.controller;

import com.employee.management.dto.EmployeeRequest;
import com.employee.management.entity.Department;
import com.employee.management.entity.Employee;
import com.employee.management.service.DepartmentService;
import com.employee.management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @Autowired
    public EmployeeWebController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/list")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employees/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employeeRequest", new EmployeeRequest());
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "employees/add";
    }

    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute EmployeeRequest employeeRequest, 
                             BindingResult bindingResult, 
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<Department> departments = departmentService.getAllDepartments();
            model.addAttribute("departments", departments);
            return "employees/add";
        }

        try {
            Department department = departmentService.getDepartmentById(employeeRequest.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            
            Employee employee = new Employee();
            employee.setName(employeeRequest.getName());
            employee.setEmail(employeeRequest.getEmail());
            employee.setDepartment(department);
            
            employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully");
            return "redirect:/employees/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error adding employee: " + e.getMessage());
            List<Department> departments = departmentService.getAllDepartments();
            model.addAttribute("departments", departments);
            return "employees/add";
        }
    }

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        return "employees/search";
    }

    @PostMapping("/search")
    public String searchEmployees(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) String department,
                                 Model model) {
        List<Employee> employees = employeeService.searchEmployees(name, department);
        model.addAttribute("employees", employees);
        model.addAttribute("searchName", name);
        model.addAttribute("searchDepartment", department);
        return "employees/search";
    }
}

