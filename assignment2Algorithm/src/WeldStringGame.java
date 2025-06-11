// WeldStringGame.java
// Part II: Decode a welded string of IDs into employee commits,
// using simple recursion to find all decompositions and pick the best.

import java.io.*;
import java.util.*;


public class WeldStringGame {
    // Inner class to hold one employee’s data
    static class Employee {
        String id;
        String fullName;
        Employee(String id, String lastName, String firstName) {
            this.id = id;
            this.fullName = lastName + " " + firstName;
        }
    }

    // Map from ID → Employee object
    static HashMap<String, Employee> employeeMap = new HashMap<String, Employee>();
    // List of all valid decompositions (each is a list of Employees)
    static ArrayList<ArrayList<Employee>> allCombinations =
            new ArrayList<ArrayList<Employee>>();

    public static void main(String[] args) throws IOException {
        // 1. Check command-line arguments
        if (args.length != 2) {
            System.out.println("Usage: java WeldStringGame <employees.csv> <weldString>");
            return;
        }
        String fileName = args[0];  // e.g. "employees.csv"
        String weld = args[1];      // e.g. "1234512342123"

        // 2. Load employees from the CSV file
        loadEmployees(fileName);

        // 3. Recursively find all ways to split 'weld'
        findCombinations(weld, 0, new ArrayList<Employee>());

        // 4. If none found, report and exit
        if (allCombinations.isEmpty()) {
            System.out.println("No valid decomposition found.");
            return;
        }

        // 5. Pick the decomposition with the most commits (largest list)
        ArrayList<Employee> best = allCombinations.get(0);
        for (ArrayList<Employee> combo : allCombinations) {
            if (combo.size() > best.size()) {
                best = combo;
            }
        }

        // 6. Print the best decomposition
        System.out.println("Best decomposition (most commits):");
        for (Employee e : best) {
            System.out.println(e.id + ": " + e.fullName);
        }
        System.out.println("\nTotal valid decompositions: " + allCombinations.size());
    }

    // Reads the CSV file and fills employeeMap
    static void loadEmployees(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split(",");
            if (parts.length >= 3) {
                String id = parts[0].trim();
                String last = parts[1].trim();
                String first = parts[2].trim();
                if (!id.isEmpty()) {
                    employeeMap.put(id, new Employee(id, last, first));
                }
            }
        }
        reader.close();
    }

    // Recursive backtracking: try every substring at 'index'
    static void findCombinations(String weld, int index, ArrayList<Employee> current) {
        // If we've consumed the whole string, store the current list
        if (index == weld.length()) {
            allCombinations.add(new ArrayList<Employee>(current));
            return;
        }
        // Try every end position for the next ID
        for (int i = index + 1; i <= weld.length(); i++) {
            String part = weld.substring(index, i);
            if (employeeMap.containsKey(part)) {
                // Use this ID and recurse from position i
                current.add(employeeMap.get(part));
                findCombinations(weld, i, current);
                // Backtrack: remove last before next try
                current.remove(current.size() - 1);
            }
        }
    }
}