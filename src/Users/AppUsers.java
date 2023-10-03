package Users;

import DataStructures.DataStructures;
import EnumLists.VehicleType;

import Garage.Vehicle;
import Menu.ColorScheme;
import Parts.Part;

import java.util.*;


public abstract class AppUsers extends DataStructures implements ColorScheme {
    private String name;

    private String password;

    public AppUsers() {

    }

    public AppUsers(String name, String email) {
        this.name = name;
        this.password = email;
    }

    public String getName() {
        return name;
    }

    protected String getEmail() {
        return password;
    }

    public abstract void displayMenu();

    protected static void listAllClients() {
        if (clientVehicleMap.size() == 0) {
            System.out.println("No Clients in database");
        } else {
            System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Clients Registry" + ANSI_RESET);
            System.out.println();
            clientVehicleMap.forEach((Client, Vehicles) -> {
                String clientInfo = "Name:" + Client.getName() + "\n" +
                        "Email: " + Client.getEmail() + "\n" + "Phone number: " +
                        Client.getPhoneNumber() + "\n" + "Payment plan: " + Client.getPlan()
                        + "\n" + "=====================\n" +
                        "Registered cars:";
                System.out.println(clientInfo);
                for (Vehicle vehicle : Vehicles) {
                    String carInfo = vehicle.getModel() + " " + vehicle.getRegistrationNumber();
                    System.out.println(ANSI_BLUE + carInfo + ANSI_RESET);
                }
                System.out.println("=====================");
            });
        }
    }

    protected static void listAllVehicles(Scanner scanner) {
        int choice = getValidIntInput("1. Display all vehicles\n2. Display cars\n" +
                "3. Display trucks\n4. Display motorcycles\n", scanner);

        switch (choice) {
            case 1:
                displayVehiclesByType(null);
                break;
            case 2:
                displayVehiclesByType(VehicleType.CAR);
                break;
            case 3:
                displayVehiclesByType(VehicleType.TRUCK);
                break;
            case 4:
                displayVehiclesByType(VehicleType.MOTORCYCLE);
                break;
            default:
                System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                break;
        }
    }

    private static void displayVehiclesByType(VehicleType type) {
        boolean vehicleFound = false;
        for (List<Vehicle> vehicleList : clientVehicleMap.values()) {
            for (Vehicle vehicle : vehicleList) {
                if (type == null || vehicle.getVehicleType() == type) {
                    vehicleFound = true;
                    vehicle.displayVehicleInfo();
                }
            }
        }
        if (!vehicleFound) {
            System.out.println("No vehicles registered yet !");
        }
    }

    protected static void listAllParts(Scanner scanner) {
        displayCategories();
        String category = getValidStringInput("\nEnter part category or " +
                "enter ALL to display all parts\n", scanner);
        displayPartsByCategory(category);
    }

    protected static void displayPartsByCategory(String category) {
        System.out.println(ANSI_BLACK + ANSI_WHITE_BACKGROUND + "PARTS CATALOGUE:" + ANSI_RESET);
        boolean partsFound = false;
        for (Map.Entry<Part, Integer> parts : partsCatalogue.entrySet()) {
            Part part = parts.getKey();
            if (category.equalsIgnoreCase("all") || part.getCategory().equalsIgnoreCase(category)) {
                partsFound = true;
                System.out.printf(ANSI_BLUE + "Part name: %s\nPart Manufacturer: %s\nPart category: %s\n" +
                                "Part quantity: %d\nPart price: %.2f" + ANSI_RESET + "\n",
                        part.getName(), part.getManufacturer(), part.getCategory(),
                        partsCatalogue.get(part), part.getPrice());
                System.out.println("=====================");
            }
        }
        if (!partsFound) {
            System.out.println("no parts found");
        }
    }

    protected static void displayCategories() {
        Set<String> uniqueCategories = new HashSet<>();
        for (Part category : partsCatalogue.keySet()) {
            String categoryStr = category.getCategory();
            if (!uniqueCategories.contains(categoryStr)) {
                System.out.print(categoryStr + " / ");
                uniqueCategories.add(categoryStr);
            }
        }
    }

    protected static int getValidIntInput(String message, Scanner scanner) {
        int input;
        while (true) {
            System.out.print(message);
            String inputString = scanner.nextLine();
            if (checkIntInput(inputString)) {
                input = Integer.parseInt(inputString);
                break;
            } else {
                System.out.println("Enter digit/s only.");
            }
        }
        return input;
    }

    protected static String getValidStringInput(String message, Scanner scanner) {
        String input;
        while (true) {
            System.out.print(message);
            String inputString = scanner.nextLine();
            if (checkName(inputString)) {
                input = inputString;
                break;
            } else {
                System.out.println("Enter valid input (only letters) at least 2 symbols long.");
            }
        }
        return input;
    }

    protected static double getValidDoubleInput(String message, Scanner scanner) {
        double input;
        while (true) {
            System.out.print(message);
            String inputString = scanner.nextLine();
            if (checkDoubleInput(inputString)) {
                input = Double.parseDouble(inputString);
                break;
            } else {
                System.out.println("Enter valid price in format 10.0 / 22.25 ");
            }
        }
        return input;
    }

    protected static String getValidEmail(String message, Scanner scanner) {
        String email;
        while (true) {
            System.out.println(message);
            String inputString = scanner.nextLine();
            if (checkValidEmail(inputString)) {
                email = inputString;
                break;
            } else {
                System.out.println("Enter valid email");
            }
        }
        return email;
    }

    protected static VehicleType getValidType(String message, Scanner scanner) {
        VehicleType type;
        while (true) {
            System.out.println(message);
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("car")) {
                type = VehicleType.CAR;
                break;
            } else if (userInput.equalsIgnoreCase("truck")) {
                type = VehicleType.TRUCK;
                break;
            } else if (userInput.equalsIgnoreCase("motorcycle")) {
                type = VehicleType.MOTORCYCLE;
                break;
            } else {
                System.out.println(ANSI_RED + "Invalid input try again" + ANSI_RESET);
            }
        }
        return type;
    }

    public static boolean checkDoubleInput(String s) {
        return s.matches("^-?\\d+(?:\\.\\d+)?$");
    }

    public static boolean checkIntInput(String s) {
        return s.matches("\\d+$");
    }

    public static boolean checkName(String name) {
        return name.matches("^[A-Za-z\\s]+$");
    }

    protected static boolean checkValidEmail(String s) {
        boolean isValid = false;
        if (s.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            isValid = true;
        }
        return isValid;
    }

    public static void checkPart(int id){
        boolean alreadyRegisteredPart =partsCatalogue.keySet()
                .stream()
                .anyMatch(part -> part.getId()==id);
        if (alreadyRegisteredPart){
            throw new IllegalArgumentException("Part Already Registered !!!");
        }

    }
    public static void checkVehicleReg(String regNum) {

        for (Map.Entry<Client, List<Vehicle>> entry : clientVehicleMap.entrySet()) {
            for (Vehicle vehicle : entry.getValue()) {
                if (vehicle.getRegistrationNumber().equalsIgnoreCase(regNum)) {
                    throw new IllegalArgumentException("Already Registered Vehicle!!!");
                }
            }
        }
    }
}
