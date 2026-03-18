import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Task> taskList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Smart Task Planner ===");

        while (running) {
            System.out.println("\n1. Add Task  2. View Tasks  3. Exit");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                System.out.print("Title: ");
                String title = scanner.nextLine();
                System.out.print("Deadline: ");
                String date = scanner.nextLine();
                System.out.print("Priority (1-5): ");
                int p = scanner.nextInt();
                
                taskList.add(new Task(title, date, p));
                System.out.println("Added!");
            } else if (choice == 2) {
                for (Task t : taskList) {
                    System.out.println(t.toString());
                }
            } else {
                running = false;
            }
        }
        scanner.close();
    }
}
