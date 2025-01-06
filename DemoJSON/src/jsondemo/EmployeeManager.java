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
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> displayEmployees(employees);
                case 2 -> addEmployee(scanner, employees);
                case 3 -> {
                    saveEmployeesToFile(employees);
                    System.out.println("Exiting program. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
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
            System.out.println("----------------------");
        }
    }

    private static void addEmployee(Scanner scanner, JSONArray employees) {
        JSONObject newEmployee = new JSONObject();

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

        employees.add(newEmployee);
        System.out.println("Employee added successfully.");
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

