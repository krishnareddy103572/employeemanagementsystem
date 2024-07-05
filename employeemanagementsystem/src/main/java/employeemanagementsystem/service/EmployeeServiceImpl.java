package employeemanagementsystem.service;

import employeemanagementsystem.dto.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<Employee> getAllEmployees() {
		//return namedParameterJdbcTemplate.query("CALL ShowAllEmployees();", this::mapRowToEmployee);
		return null;


	}

	@Override
	public Employee getEmployeeById(String id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("employeeID", id);
		List<Employee> employees = namedParameterJdbcTemplate.query("CALL GetEmployeeById(:employeeID);", parameters,
				this::mapRowToEmployee);
		return employees.isEmpty() ? null : employees.get(0);
	}

	// New method to fetch only ID, Name, and Blood Group
	@Override
	public Employee getEmployeeBasicDetailsById(String id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("empID", id);
		List<Employee> employees = namedParameterJdbcTemplate.query("CALL GetEmployeeDetails(:empID);", parameters,
				this::mapRowToBasicEmployeeDetails);
		return employees.isEmpty() ? null : employees.get(0);
	}

	private Employee mapRowToBasicEmployeeDetails(ResultSet rs, int rowNum) throws SQLException {
		Employee employee = new Employee();
		employee.setEmployeeID(rs.getString("EmployeeID"));

		// Assuming the stored procedure concatenates first name and last name as 'Name'
		String fullName = rs.getString("Name");
		if (fullName != null) {
			String[] names = fullName.split(" ", 2);
			employee.setFirstName(names.length > 0 ? names[0] : "");
			employee.setLastName(names.length > 1 ? names[1] : "");
		} else {
			employee.setFirstName("");
			employee.setLastName("");
		}

		return employee;
	}

	private Employee mapRowToEmployee(ResultSet rs, int rowNum) throws SQLException {
		Employee employee = new Employee();
		employee.setEmployeeID(rs.getString("EmployeeID"));
		employee.setFirstName(rs.getString("FirstName"));
		employee.setLastName(rs.getString("LastName"));
		employee.setGender(rs.getString("Gender"));
		employee.setEmail(rs.getString("Email"));
		return employee;
	}






	@Override
	public void saveEmployee(Employee employee) {
		System.out.println("employe::::"+employee);
		MapSqlParameterSource employeeParameters = new MapSqlParameterSource()
				.addValue("employeeID",employee.getEmployeeID())
				.addValue("firstName", employee.getFirstName()).addValue("lastName", employee.getLastName())
				.addValue("gender", employee.getGender()).addValue("email",employee.getEmail());

		if (employee.getEmployeeID() == null || employee.getEmployeeID().trim().isEmpty()) {
			final String insertSql = "INSERT INTO employee (EmployeeID,FirstName, LastName, Gender,Email) "
					+ "VALUES (:employeeID,:firstName, :lastName, :gender,:email)";
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			namedParameterJdbcTemplate.update(insertSql, employeeParameters, keyHolder);
			employee.setEmployeeID(keyHolder.getKey().toString());
		} else {
			employeeParameters.addValue("employeeID", employee.getEmployeeID());
			final String updateSql = "UPDATE employee SET FirstName = :firstName, LastName = :lastName, Gender = :gender,Email = : email  WHERE EmployeeID = :employeeID";
			namedParameterJdbcTemplate.update(updateSql, employeeParameters);
		}

	}

	@Override
	public List<Employee> searchEmployees(String keyword) {
		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("keyword", "%" + keyword + "%");
		return namedParameterJdbcTemplate.query(
				"SELECT * FROM employee WHERE FirstName LIKE :keyword OR LastName LIKE :keyword", parameters,
				this::mapRowToEmployee);
	}





	@Override
	public void deleteEmployee(String id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("employeeID", id);
		namedParameterJdbcTemplate.update("CALL DeleteEmployee(:employeeID);", parameters);
	}
}