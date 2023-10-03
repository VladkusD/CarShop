package Menu;

import DataStructures.DataStructures;
import Users.Administrator;
import Users.Mechanic;
import Users.Receptionist;

import java.util.Scanner;

public abstract class MainMenu extends DataStructures implements ColorScheme {
    public static void entryMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Welcome to CarShopApp");
            System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Login as:" + ANSI_RESET);
            System.out.println("1. Administrator");
            System.out.println(ANSI_BLUE + "2. Receptionist" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "3. Mechanic" + ANSI_RESET);
            System.out.println(ANSI_RED + "4. Exit" + ANSI_RESET);
            System.out.println("Enter number from menu: ");
            String input = scanner.nextLine();
            while (!input.matches("\\d+$")) {
                System.out.println("Invalid input, enter number from menu");
                input = scanner.nextLine();
            }
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    Administrator administrator = new Administrator();
                    administrator.login(scanner);
                    break;
                case 2:
                    Receptionist receptionist = new Receptionist();
                    receptionist.login(scanner);
                    break;
                case 3:
                    Mechanic mechanic = new Mechanic();
                    mechanic.login(scanner);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                    break;
            }

        }
    }
}