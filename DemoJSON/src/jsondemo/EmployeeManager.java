package jsondemo;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class EmployeeManager {

    private static final String FILE_PATH = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\DemoJSON\\src\\jsondemo\\employee.json";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JSONParser jsonParser = new JSONParser();
        JSONArray employees = new JSONArray();

        // Step 1: Read the existing JSON file
        try (FileReader reader = new FileReader(FILE_PATH)) {
            JSONObject data = (JSONObject) jsonParser.parse(reader);
            employees = (JSONArray) data.get("employees");
        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }

        while (true) {
            System.out.println("\nEmployee Manager");
            System.out.println("1. View All Employees");
            System.out.println("2. Add New Employee");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Search Employees by Department");
            System.out.println("6. Search Employees by Hobby");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 ->
                    displayEmployees(employees);
                case 2 ->
                    addEmployee(scanner, employees);
                case 3 ->
                    updateEmployee(scanner, employees);
                case 4 ->
                    deleteEmployee(scanner, employees);
                case 5 ->
                    searchEmployeesByDepartment(scanner, employees);
                case 6 ->
                    searchEmployeesByHobby(scanner, employees);
                case 7 -> {
                    saveEmployeesToFile(employees);
                    System.out.println("Exiting program. Goodbye!");
                    return;
                }
                default ->
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    private static void displayHobbiesArray(JSONObject employee) {
        JSONArray hobbies = (JSONArray) employee.get("hobbies");

        if (hobbies == null || hobbies.isEmpty()) {
            System.out.println("Hobbies: None");
        } else {
            System.out.print("Hobbies: ");
            for (Object hobby : hobbies) {
                System.out.print(hobby + ", ");
            }
            System.out.println(); // Move to the next line after printing hobbies
        }
    }

    private static void searchEmployeesByDepartment(Scanner scanner, JSONArray employees) {
        System.out.print("Enter department to search: ");
        String department = scanner.nextLine().trim();

        boolean found = false;
        System.out.println("\nEmployees in Department: " + department);
        for (Object obj : employees) {
            JSONObject employee = (JSONObject) obj;
            if (department.equalsIgnoreCase((String) employee.get("department"))) {
                System.out.println("ID: " + employee.get("id"));
                System.out.println("Name: " + employee.get("name"));
                System.out.println("Department: " + employee.get("department"));
                displayHobbiesArray(employee);
                System.out.println("----------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No employees found in the department: " + department);
        }
    }

    private static void searchEmployeesByHobby(Scanner scanner, JSONArray employees) {
        System.out.print("Enter hobby to search: ");
        String hobbyToSearch = scanner.nextLine().trim();

        boolean found = false;
        System.out.println("\nEmployees with Hobby: " + hobbyToSearch);
        for (Object obj : employees) {
            JSONObject employee = (JSONObject) obj;
            JSONArray hobbies = (JSONArray) employee.get("hobbies");

            if (hobbies != null && hobbies.contains(hobbyToSearch)) {
                System.out.println("ID: " + employee.get("id"));
                System.out.println("Name: " + employee.get("name"));
                System.out.println("Department: " + employee.get("department"));
                displayHobbiesArray(employee);
                System.out.println("----------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No employees found with the hobby: " + hobbyToSearch);
        }
    }

    private static void displayEmployees(JSONArray employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("\nEmployee List:");
        for (Object obj : employees) {
            JSONObject emp = (JSONObject) obj;
            System.out.println("ID: " + emp.get("id"));
            System.out.println("Name: " + emp.get("name"));
            System.out.println("Department: " + emp.get("department"));
            displayHobbiesArray(emp);
            System.out.println("----------------------");
        }
    }

    private static void addEmployee(Scanner scanner, JSONArray employees) {
        JSONObject newEmployee = new JSONObject();

        // Basic Employee Details
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();
        if (employeeExists(employees, id)) {
            System.out.println("Employee with this ID already exists.");
            return;
        }

        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Employee Department: ");
        String department = scanner.nextLine();

        newEmployee.put("id", id);
        newEmployee.put("name", name);
        newEmployee.put("department", department);

        // Add Hobbies
        JSONArray hobbies = new JSONArray();
        while (true) {
            System.out.print("Enter Hobby Name (or 'done' to finish): ");
            String hobbyName = scanner.nextLine();
            if (hobbyName.equalsIgnoreCase("done")) {
                break;
            }
            hobbies.add(hobbyName);
        }
        newEmployee.put("hobbies", hobbies);

        employees.add(newEmployee);
        System.out.println("Employee with hobbies added successfully.");
    }

    private static boolean employeeExists(JSONArray employees, String id) {
        for (Object obj : employees) {
            JSONObject emp = (JSONObject) obj;
            if (id.equals(emp.get("id"))) {
                return true;
            }
        }
        return false;
    }

    private static void updateEmployee(Scanner scanner, JSONArray employees) {
        System.out.print("Enter Employee ID to update: ");
        String id = scanner.nextLine();

        boolean updated = false;
        for (Object obj : employees) {
            JSONObject employee = (JSONObject) obj;
            if (employee.get("id").equals(id)) {
                // Update Name
                System.out.print("Enter new name (leave blank to keep current): ");
                String newName = scanner.nextLine();
                if (!newName.isBlank()) {
                    employee.put("name", newName);
                }

                // Update Department
                System.out.print("Enter new department (leave blank to keep current): ");
                String newDepartment = scanner.nextLine();
                if (!newDepartment.isBlank()) {
                    employee.put("department", newDepartment);
                }

                // Update Hobbies
                System.out.print("Do you want to update hobbies? (yes/no): ");
                String updateHobbies = scanner.nextLine();
                if (updateHobbies.equalsIgnoreCase("yes")) {
                    JSONArray hobbies = new JSONArray();
                    while (true) {
                        System.out.print("Enter Hobby Name (or 'done' to finish): ");
                        String hobbyName = scanner.nextLine();
                        if (hobbyName.equalsIgnoreCase("done")) {
                            break;
                        }
                        hobbies.add(hobbyName);
                    }
                    employee.put("hobbies", hobbies);
                }

                updated = true;
                break;
            }
        }

        if (updated) {
            System.out.println("Employee updated successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    private static void deleteEmployee(Scanner scanner, JSONArray employees) {
        System.out.print("Enter Employee ID to delete: ");
        String id = scanner.nextLine();

        boolean deleted = false;
        for (int i = 0; i < employees.size(); i++) {
            JSONObject employee = (JSONObject) employees.get(i);
            if (employee.get("id").equals(id)) {
                employees.remove(i);
                deleted = true;
                break;
            }
        }

        if (deleted) {
            System.out.println("Employee deleted successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    private static void saveEmployeesToFile(JSONArray employees) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            JSONObject data = new JSONObject();
            data.put("employees", employees);
            writer.write(data.toJSONString());
            System.out.println("Employee data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }
}
