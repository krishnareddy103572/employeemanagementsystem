package employeemanagementsystem.service;

import employeemanagementsystem.dto.Employee;

import java.util.List;

public interface EmployeeService   {

    /**
     * Retrieves a list of all employees.
     * @return a list of Employee objects.
     */
    List<Employee> getAllEmployees();

    /**
     * Saves an employee's information to the database. This method is used for both adding a new employee
     * and updating an existing employee's information.
     * @param employee the employee object to be saved.
     */
    void saveEmployee(Employee employee);

    /**
     * Retrieves an employee by their ID.
     * @param id the ID of the employee to be retrieved.
     * @return the Employee object corresponding to the provided ID.
     */
    Employee getEmployeeById(String id);

    /**
     * Retrieves basic details (ID, Name, Blood Group) of an employee by their ID.
     * @param id the ID of the employee whose basic details are to be retrieved.
     * @return the Employee object with only basic details populated.
     */
    Employee getEmployeeBasicDetailsById(String id);

    /**
     * Deletes an employee from the database based on their ID.
     * @param id the ID of the employee to be deleted.
     */
    void deleteEmployee(String id);

    /**
     * Searches for employees matching the provided keyword in their first or last name.
     * @param keyword the keyword to search for.
     * @return a list of Employee objects that match the search criteria.
     */
    List<Employee> searchEmployees(String keyword);
}
