import java.util.*;

public class EmployeeScheduler {
    static String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    static String[] SHIFTS = {"Morning", "Afternoon", "Evening"};
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Map<String, Employee> employees = new LinkedHashMap<>();

        int numEmployees;
        while (true) {
            System.out.print("Enter number of employees: ");
            try {
                numEmployees = Integer.parseInt(sc.nextLine().trim());
                if (numEmployees < 1) {
                    System.out.println("Number must be greater than 0.");
                    continue;
                }
                if (numEmployees < 9) {
                    System.out.print("Not enough employees to cover all shifts for 7 days. Do you want to continue? (Yes/No): ");
                    String choice = sc.nextLine().trim().toLowerCase();
                    if (choice.equals("no")) continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
            }
        }

        // Input employee info
        for (int i = 0; i < numEmployees; i++) {
            String name;
            do {
                System.out.print("Enter employee " + (i + 1) + " name: ");
                name = sc.nextLine().trim();
                if (name.isEmpty()) System.out.println("Employee name cannot be blank.");
            } while (name.isEmpty());

            Map<String, String> prefs = new HashMap<>();
            System.out.println("Enter " + name + "'s preferred shifts (Morning/Afternoon/Evening):");
            for (String day : DAYS) {
                System.out.print("  " + day + ": ");
                String pref = sc.nextLine().trim();
                if (!(pref.equalsIgnoreCase("Morning") || pref.equalsIgnoreCase("Afternoon") || pref.equalsIgnoreCase("Evening"))) {
                    System.out.println("Invalid input. Assigning random shift.");
                    pref = SHIFTS[new Random().nextInt(SHIFTS.length)];
                }
                prefs.put(day, capitalize(pref));
            }
            employees.put(name, new Employee(name, prefs));
        }

        Map<String, Map<String, List<String>>> schedule = new LinkedHashMap<>();
        for (String day : DAYS) {
            Map<String, List<String>> shifts = new LinkedHashMap<>();
            for (String shift : SHIFTS) {
                shifts.put(shift, new ArrayList<>());
            }
            schedule.put(day, shifts);
        }

        // Assign shifts
        for (String day : DAYS) {
            for (Employee emp : employees.values()) {
                if (emp.assignedDays >= 5) continue;
                String pref = emp.preferences.get(day);
                if (schedule.get(day).get(pref).size() < 2 && !schedule.get(day).get(pref).contains(emp.name)) {
                    schedule.get(day).get(pref).add(emp.name);
                    emp.assignedDays++;
                } else {
                    boolean assigned = false;
                    for (String alt : SHIFTS) {
                        if (schedule.get(day).get(alt).size() < 2 && !schedule.get(day).get(alt).contains(emp.name)) {
                            schedule.get(day).get(alt).add(emp.name);
                            emp.assignedDays++;
                            assigned = true;
                            break;
                        }
                    }
                    if (!assigned) {
                        int nextIdx = (Arrays.asList(DAYS).indexOf(day) + 1) % DAYS.length;
                        String nextDay = DAYS[nextIdx];
                        for (String alt : SHIFTS) {
                            if (schedule.get(nextDay).get(alt).size() < 2 && !schedule.get(nextDay).get(alt).contains(emp.name) && emp.assignedDays < 5) {
                                schedule.get(nextDay).get(alt).add(emp.name);
                                emp.assignedDays++;
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Fill missing slots with unique employees
        for (String day : DAYS) {
            for (String shift : SHIFTS) {
                while (schedule.get(day).get(shift).size() < 2) {
                    List<Employee> available = new ArrayList<>();
                    for (Employee e : employees.values()) {
                        if (e.assignedDays < 5 && !schedule.get(day).get(shift).contains(e.name)) available.add(e);
                    }
                    if (available.isEmpty()) break;
                    Employee chosen = available.get(new Random().nextInt(available.size()));
                    schedule.get(day).get(shift).add(chosen.name);
                    chosen.assignedDays++;
                }
            }
        }

        // Print final schedule: days as rows, shifts as columns
        System.out.println("\nFinal Weekly Schedule:\n");
        System.out.printf("%-12s%-25s%-25s%-25s%n", "Day", "Morning", "Afternoon", "Evening");
        for (String day : DAYS) {
            System.out.printf("%-12s", day);
            for (String shift : SHIFTS) {
                String val = String.join(", ", schedule.get(day).get(shift));
                System.out.printf("%-25s", val);
            }
            System.out.println();
        }
    }

    static class Employee {
        String name;
        Map<String, String> preferences;
        int assignedDays = 0;

        Employee(String n, Map<String, String> p) {
            name = n;
            preferences = p;
        }
    }

    static String capitalize(String s) {
        if (s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
