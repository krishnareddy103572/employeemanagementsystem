package employeemanagementsystem.controller;

import employeemanagementsystem.dto.Employee;
import employeemanagementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("employee")
public class HomeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("employees", employeeService.getAllEmployees());
		return "employeeList";
	}

	@GetMapping("/home/employee/add")
	public String showAddEmployeeBasicInfo(Model model) {
		model.addAttribute("employee", new Employee());
		return "addEmployeeBasicInfo";
	}

	@PostMapping("/home/employee/add/details")
	public String addEmployeeDetails(@ModelAttribute("employee") Employee employee, Model model, SessionStatus status) {
		model.addAttribute("employee", employee);
		employeeService.saveEmployee(employee);
		status.setComplete();
		return "redirect:/";
	}





	@PostMapping("/home/employee/save")
	public String saveEmployee(@ModelAttribute("employee") Employee employee, SessionStatus status) {
		employeeService.saveEmployee(employee);
		status.setComplete(); // Clears the @SessionAttributes
		return "redirect:/";
	}

	@GetMapping("/employee/edit/{id}")
	public String showEditForm(@PathVariable("id") String id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);
		model.addAttribute("employee", employee);
		return "editEmployee";
	}

	@PostMapping("/home/employee/update")
	public String updateEmployee(@ModelAttribute("employee") Employee employee) {
		employeeService.saveEmployee(employee);
		return "redirect:/";
	}

	@PostMapping("/home/employee/delete/{id}")
	public String deleteEmployee(@PathVariable("id") String id) {
		employeeService.deleteEmployee(id);
		return "redirect:/";
	}

	@GetMapping("/home/employee/search")
	public String searchEmployee(String keyword, Model model) {
		model.addAttribute("employees", employeeService.searchEmployees(keyword));
		return "employeeList";
	}

	@GetMapping("/employee/basic-details")
	public String getEmployeeBasicDetails(@RequestParam("id") String id, Model model) {
		Employee employee = employeeService.getEmployeeBasicDetailsById(id);
		if (employee != null) {
			model.addAttribute("employee", employee);
			return "viewEmployeeBasicDetails";
		} else {
			// Handle the case where no employee is found
			model.addAttribute("errorMessage", "Employee not found");
			return "applyForNewID"; // Redirect back to the ID form page or an error page
		}
	}

	@GetMapping("/payment/{id}")
	public String paymentForNewId(@PathVariable("id") String employeeId) {
		// Optional: You can use employeeId to fetch specific employee details or
		// process them as needed
		return "paymentQRCodeGenerator"; // Redirects to the payment QR code generator page
	}
}
