package Users;

import Garage.Vehicle;
import Menu.ColorScheme;
import Parts.Part;

import java.util.*;

import static EnumLists.VehicleStatus.*;

public class Mechanic extends AppUsers implements User,ColorScheme {
    private  List<Vehicle> jobsTaken;
    private long startTime;
    private long endTime;
    private long timePassed;

    public Mechanic (){
        super();
    }

    public List<Vehicle> getJobsTaken() {
        return jobsTaken;
    }

    public Mechanic(String name, String email) {
        super(name, email);
        this.jobsTaken = new LinkedList<>();
        checkMechanicReg(name);
    }

    @Override
    public void displayMenu() {
        System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Mechanic menu:" + ANSI_RESET);
        System.out.println("1. List all vehicles pending repair");
        System.out.println("2. Select vehicle for repair");
        System.out.println("3. List my Jobs");
        System.out.println("4. Complete repair");
        System.out.println("5. List parts in catalogue");
        System.out.println("6. Change Password");
        System.out.println(ANSI_RED+"7. Exit"+ANSI_RESET);
    }

    public static void mechanicOptionSelect(Mechanic mechanic, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            mechanic.displayMenu();
            int choice = getValidIntInput("Choose number from menu:\n",scanner);
            switch (choice) {
                case 1:
                    listAllPendingRepair();
                    break;
                case 2:
                    selectRepair(mechanic, scanner);
                    break;
                case 3:
                    listMyJobs (mechanic);
                    break;
                case 4:
                    completeRepair(mechanic, scanner);
                    break;
                case 5:
                    listAllParts(scanner);
                    break;
                case 6:
                    mechanic.changePassword(scanner);
                    break;
                case 7:
                    exit=true;
                    break;
                default:
                    System.out.println(ANSI_RED+"Invalid choice. Please try again."+ANSI_RESET);
                    break;
            }
        }
    }

    protected static void listAllPendingRepair() {
        if (clientVehicleMap.isEmpty()) {
            System.out.println("No clients or vehicles in database");
        } else {
            boolean vehicleFound = false;
            for (List<Vehicle> carList : clientVehicleMap.values()) {
                for (Vehicle vehicle : carList) {
                    if (vehicle.getStatus().equals(PENDING_REPAIR)) {
                        vehicleFound = true;
                        System.out.println(ANSI_BLACK + ANSI_WHITE_BACKGROUND + "Vehicle: " +
                                vehicle.getModel() + ANSI_RESET);
                        System.out.println(ANSI_PURPLE + "Registration Number: " + ANSI_RESET
                                + vehicle.getRegistrationNumber());
                        System.out.println(ANSI_PURPLE + "Problem: " + ANSI_RESET + vehicle.getProblem());
                        System.out.println(ANSI_PURPLE + "Status: " + ANSI_RESET + vehicle.getStatus());
                        System.out.println("================================");
                    }
                }
            }
            if (!vehicleFound) {
                System.out.println("No cars pending repair found!");
            }
        }
    }

    private static void selectRepair(Mechanic mechanic, Scanner scanner) {
        if (mechanic.jobsTaken.size() >= 2) {
            System.out.println(ANSI_RED+"You cannot work on more than 2 vehicles simultaneously"+ANSI_RESET);
        } else {
            System.out.println("Enter registration number to start work");
            String regNumber = scanner.nextLine();
            boolean vehicleFound=false;
            for (List<Vehicle> carList : clientVehicleMap.values()) {
                for (Vehicle vehicle : carList) {
                    if (vehicle.getRegistrationNumber().equalsIgnoreCase(regNumber)) {
                        vehicleFound = true;
                        if (mechanic.jobsTaken.size() > 1) {
                            availableMechanics.remove(mechanic);
                        }
                        if (vehiclesInShop.contains(vehicle)){
                            System.out.println(ANSI_BLUE+"Already taken"+ANSI_RESET);
                            break;
                        }else {
                            vehicle.setStatus(IN_SHOP);
                            mechanic.startTime = System.currentTimeMillis();
                            vehiclesInShop.add(vehicle);
                            mechanic.jobsTaken.add(vehicle);
                            System.out.println(ANSI_BLUE+vehicle.getModel() + " "
                                    + vehicle.getRegistrationNumber() + " " + "registered for repair"+ANSI_RESET);
                            System.out.println("===================");
                        }
                    }
                }
                if (vehicleFound) {
                    break;
                }
            }
            if (!vehicleFound) {
                System.out.println(ANSI_RED + "No vehicles with this registration found!" + ANSI_RESET);
            }
        }
    }
    private static void listMyJobs(Mechanic mechanic) {
        System.out.println(ANSI_BLUE+"Currently working on: "+ANSI_RESET);
        if (mechanic.jobsTaken.isEmpty()){
            System.out.println(ANSI_RED+"You have 0 ongoing jobs"+ANSI_RESET);
        }
        for (Vehicle vehicle: mechanic.jobsTaken){
            System.out.println(vehicle.getModel());
            System.out.println(vehicle.getRegistrationNumber());
        }
        System.out.println("===================");
    }

    private static void completeRepair(Mechanic mechanic, Scanner scanner) {
        System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Enter registration" +
                " number to complete repair" + ANSI_RESET);
        String regNumber = scanner.nextLine();
        boolean vehicleFound=false;

        for (List<Vehicle> carList : clientVehicleMap.values()) {
            for (Vehicle vehicle : carList) {
                if (vehicle.getRegistrationNumber().equals(regNumber)) {
                    if (vehicle.getStatus()==PENDING_REPAIR){
                        System.out.println("Start work first!");
                        break;
                    }
                    vehicleFound=true;
                    vehicle.setStatus(REPAIR_READY);
                    mechanic.endTime = System.currentTimeMillis();
                    mechanic.timePassed = mechanic.endTime-mechanic.startTime;
                    vehiclesInShop.remove(vehicle);
                    performedRepairs(vehicle, scanner);
                    vehicle.setTime(mechanic.timePassed);
                    vehicle.setRepairedBy(mechanic);
                    mechanic.jobsTaken.remove(vehicle);
                    if (mechanic.jobsTaken.size() < 2) {
                        availableMechanics.add(mechanic);
                    }

                }
            }
            if (vehicleFound){
                break;
            }
        }
        if (!vehicleFound){
            System.out.println(ANSI_RED + "Vehicle not found!" + ANSI_RESET);
        }
    }

    private static void performedRepairs(Vehicle repairedCar, Scanner scanner) {
        System.out.println("Enter performed repairs on new lines, type "+ ANSI_RED+"END"+ANSI_RESET+" to finish");
        String input = scanner.nextLine();
        List<String> repairs = new ArrayList<>();
        while (!input.equalsIgnoreCase("end")) {
            repairs.add(input);
            input = scanner.nextLine();
        }
        repairedCar.setPerformedRepairs(repairs);
        System.out.println("Enter part category used or"+ANSI_RED+" END "+ANSI_RESET+"to finish report");
        System.out.println("Part categories: ");
        displayCategories();
        System.out.println();
        String partCategory = scanner.nextLine();
        while (!partCategory.equalsIgnoreCase("end")) {
            for (Map.Entry<Part,Integer> part : partsCatalogue.entrySet()) {
                if (partCategory.equalsIgnoreCase(part.getKey().getCategory())) {
                    System.out.printf(ANSI_BLUE+"\nPart id: %d :: Part name: %s" +
                                    " :: Part quantity: %d :: Part Manufacturer: %s\n"+ANSI_RESET
                            , part.getKey().getId(), part.getKey().getName(),
                            part.getValue(), part.getKey().getManufacturer());
                }
            }
            int id = getValidIntInput("Enter part id:\n",scanner);
            boolean partExists = partsCatalogue.keySet().stream()
                    .anyMatch(part -> part.getId() == id);
            if (!partExists){
                System.out.println(ANSI_RED+"Enter correct id"+ANSI_RESET);
                continue;
            }
            int quantity = getValidIntInput("Enter quantity used:\n",scanner);
            getPart(id, quantity, repairedCar);
            System.out.println("Enter new part category or type 'END'");
            partCategory = scanner.nextLine();
        }
    }


    private static void getPart(int id, int quantity, Vehicle car) {
        for (Map.Entry<Part, Integer> parts : partsCatalogue.entrySet()) {
            if (parts.getKey().getId() == id) {
                if (quantity > parts.getValue()) {
                    System.out.println("No sufficient quantity available");
                    break;
                } else {
                    partsCatalogue.put(parts.getKey(),parts.getValue()-quantity);
                    car.setUsedParts(parts.getKey(), quantity);
                }
            }
        }
    }

    @Override
    public  void login(Scanner scanner) {
        System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Enter your name:" + ANSI_RESET);
        String name = scanner.nextLine();
        Mechanic mechanic = getMechanic(name);
        if (mechanic != null) {
            System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Enter your password:" + ANSI_RESET);
            String password = scanner.nextLine();
            if (mechanicsMap.get(mechanic).equals(password)) {
                Mechanic.mechanicOptionSelect(mechanic, scanner);
            } else {
                System.out.println(ANSI_RED + "Invalid password. Please try again." + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "Mechanic not found. Please try again." + ANSI_RESET);
        }
    }

    private static Mechanic getMechanic(String name) {
        for (Mechanic mechanic : mechanicsMap.keySet()) {
            if (mechanic.getName().equalsIgnoreCase(name)) {
                return mechanic;
            }
        }
        return null;
    }

    @Override
    public void changePassword(Scanner scanner) {
        System.out.println("Enter you current password: ");
        String oldPass = scanner.nextLine();
        mechanicsMap.forEach((key, value) -> {
            if (value.equals(oldPass)) {
                String newPass = scanner.nextLine();
                mechanicsMap.put(key, newPass);
            }
        });
    }
    private static void checkMechanicReg (String name){
        boolean validName = checkName(name);
        boolean alreadyRegisteredMechanic = mechanicsMap.keySet()
                .stream()
                .anyMatch(mechanic -> mechanic.getName().equalsIgnoreCase(name));
        if (alreadyRegisteredMechanic){
            throw new IllegalArgumentException("Already Registered Mechanic!!!");
        }
        if (!validName){
            throw new IllegalArgumentException("Invalid name for Mechanic !!!");
        }
    }
}
