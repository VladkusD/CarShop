package Users;

import EnumLists.VehicleType;
import Garage.Car;
import Garage.Motorcycle;
import Garage.Truck;
import Garage.Vehicle;
import Menu.ColorScheme;
import Parts.Part;

import java.util.*;

import static EnumLists.VehicleStatus.*;

public class Receptionist extends AppUsers implements User,ColorScheme {

    public Receptionist(){
        super();
    }
    public Receptionist(String name, String email) {
        super(name, email);
        checkReceptionistReg(name);
    }

    @Override
    public void displayMenu() {
        System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Receptionist menu:" + ANSI_RESET);
        System.out.println("1. Register client");
        System.out.println("2. Register vehicle");
        System.out.println("3. Check vehicle");
        System.out.println("4. List available mechanics");
        System.out.println("5. Charge/Finish repair");
        System.out.println("6. Check vehicles in shop");
        System.out.println("7. Check info");
        System.out.println("8. Change password");
        System.out.println(ANSI_RED+"9. Exit "+ANSI_RESET);
    }

    public static void receptionistMenuOptionSelect(Receptionist receptionist, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            receptionist.displayMenu();
            int choice = getValidIntInput("Enter number from menu:\n", scanner);
            switch (choice) {
                case 1:
                    Administrator.addNewClient(scanner);
                    break;
                case 2:
                    registerVehicle(scanner);
                    break;
                case 3:
                    checkVehicle(scanner);
                    break;
                case 4:
                    listAvailableMechanics();
                    break;
                case 5:
                    chargeRepair(scanner);
                    break;
                case 6:
                    checkVehiclesInShop();
                    break;
                case 7:
                    checkInfo(scanner);
                    break;
                case 8:
                    receptionist.changePassword(scanner);
                    break;
                case 9:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void registerVehicle(Scanner scanner) {
        String owner = getValidStringInput("Enter client's name:\n", scanner);
        boolean clientFound = false;

        for (Map.Entry<Client, List<Vehicle>> entry : clientVehicleMap.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(owner)) {
                clientFound = true;
                System.out.println("Enter vehicle registration number:");
                String registrationNumber = scanner.nextLine();
                boolean vehicleFound = false;
                for (Vehicle vehicle : entry.getValue()) {
                    if (vehicle.getRegistrationNumber().equals(registrationNumber)) {
                        System.out.println("Enter new problem description:");
                        String problem = scanner.nextLine();
                        vehicle.setProblem(problem);
                        vehicle.setStatus(PENDING_REPAIR);
                        vehicleFound = true;
                        System.out.println("Vehicle status updated to PENDING_REPAIR.");
                        System.out.println("===============================");
                        System.out.println();
                        break;
                    }
                }
                if (!vehicleFound) {
                    VehicleType type = getValidType ("Enter vehicle type: CAR / TRUCK / MOTORCYCLE",scanner);
                    String model = getValidStringInput("Enter model:\n", scanner);
                    String problem = getValidStringInput("Enter problem description:\n", scanner);
                    if (type == VehicleType.CAR){
                    Car car = new Car(VehicleType.CAR,model, registrationNumber, owner, problem);
                        car.setStatus(PENDING_REPAIR);
                        entry.getValue().add(car);}
                    else if (type == VehicleType.TRUCK) {
                        Truck truck = new Truck(VehicleType.TRUCK,model, registrationNumber, owner, problem);
                        truck.setStatus(PENDING_REPAIR);
                        entry.getValue().add(truck);
                    } else if (type == VehicleType.MOTORCYCLE){
                        Motorcycle motorcycle = new Motorcycle(VehicleType.MOTORCYCLE,model,
                                registrationNumber, owner, problem);
                        motorcycle.setStatus(PENDING_REPAIR);
                        entry.getValue().add(motorcycle);
                    }
                    System.out.println(ANSI_BLUE+"Vehicle successfully added for repair!"+ANSI_RESET);
                    System.out.println("==================================");
                    System.out.println();
                }
                break;
            }
        }
        if (!clientFound) {
            System.out.println("No client found! Register client first");
        }
    }

    private static void checkVehicle(Scanner scanner) {
        String name = getValidStringInput("Enter clients name\n", scanner);
        boolean clientFound = false;

        for (Client client : clientVehicleMap.keySet()) {
            if (client.getName().equalsIgnoreCase(name)) {
                clientFound = true;
                for (Vehicle vehicle : clientVehicleMap.get(client)) {
                    System.out.println(ANSI_WHITE_BACKGROUND+ANSI_BLACK+"Vehicle model -> "
                            + vehicle.getModel()+ANSI_RESET);
                    System.out.println("Vehicle type ->" + vehicle.getVehicleType().toString());
                    System.out.println("Vehicle problem -> " + vehicle.getProblem());
                    System.out.println("Vehicle status -> " + vehicle.getStatus());
                    vehicle.getUsedParts().forEach((part, quantity) -> System.out.printf("Used parts:" +
                                    " %s, Quantity: %d\n",
                            part.getName(), quantity));
                    System.out.println("Vehicle repair history -> " + vehicle.getRepairHistory());

                    System.out.print("Vehicle used parts history : ");
                    vehicle.getUsedPartsHistory()
                            .forEach((key, value) -> System.out.print(key.getName() + " Qty: " + value + "/ "));
                    System.out.println();
                    if (vehicle.repairedBy.isEmpty()) {
                        System.out.println("No repairs yet.");
                    } else {
                        System.out.println("Vehicle repaired by -> " + vehicle.getRepairedBy());
                    }
                }
                break;
            }
        }

        if (!clientFound) {
            System.out.println("Client not found! Please register client first");
        }
    }

    private static void listAvailableMechanics() {
        boolean mechanicAvailable = false;
        for (Mechanic availableMechanic : availableMechanics) {
            if (availableMechanic.getJobsTaken().size()<2) {
                System.out.println(availableMechanic.getName());
                mechanicAvailable = true;
            }
        }
        if (!mechanicAvailable) {
            System.out.println(ANSI_RED + "Everyone is busy right now!!" + ANSI_RESET);
        }
    }

    private static void chargeRepair(Scanner scanner) {
        System.out.println("Vehicles that are ready : ");
        for (Map.Entry<Client,List<Vehicle>> clientVehicleMap: clientVehicleMap.entrySet()){
            for (Vehicle vehicle : clientVehicleMap.getValue()){
               if (vehicle.getStatus().equals(REPAIR_READY))
               {
                   System.out.println(vehicle.getOwner()+" "+vehicle.getRegistrationNumber()
                   +" "+vehicle.getStatus().toString());
               }
            }
        }
        String name = getValidStringInput("Enter customer name :\n", scanner);
        double partsPrice = 0;
        float timeTotal = 0.00f;
        int mechanicHourlyRate = 50;
        boolean clientFound = false;
        for (Client client : clientVehicleMap.keySet()) {
            if (client.getName().equalsIgnoreCase(name)) {
                for (Vehicle vehicle : clientVehicleMap.get(client)) {
                    if (vehicle.getStatus().equals(REPAIR_READY)) {
                        System.out.println("-----------------");
                        System.out.print("Performed repairs: ");
                        for (String s : vehicle.getPerformedRepairs()) {
                            System.out.print(s + " ");
                        }
                        System.out.println();
                        long hours = (vehicle.getTime() / (1000 * 60 * 60)) % 24;
                        long minutes =(vehicle.getTime() / (1000 * 60)) % 60;
                        timeTotal= (vehicle.getTime() / (1000f * 60f)) % 60f;
                        System.out.printf("Time spent on repair ->%d Hours and %d Minutes\n", hours,minutes);
                        vehicle.setTime(0);
                        vehicle.setRepairHistory(vehicle.getPerformedRepairs());
                        vehicle.getPerformedRepairs().clear();
                        vehicle.setStatus(PAID);
                        vehicle.getUsedPartsHistory().putAll(vehicle.getUsedParts());
                        for (Map.Entry<Part, Integer> usedParts : vehicle.getUsedParts().entrySet()) {
                            System.out.println("Part used -> " + usedParts.getKey().getName());
                            System.out.println("Quantity used -> " + usedParts.getValue());
                            System.out.println("Price per part unit -> " + usedParts.getKey().getPrice());
                            System.out.println("-----------------");
                            partsPrice += usedParts.getKey().getPrice() * usedParts.getValue();
                            vehicle.getUsedParts().clear();
                        }
                        clientFound = true;
                    }
                    if (vehicle.getStatus()==PAID){
                        System.out.printf("Total Work time in minutes: %.2f\n" , timeTotal);
                        System.out.printf("Total price of work ->%.2f\n" , timeTotal / 60f * mechanicHourlyRate);
                        System.out.println("Total price of parts: " + partsPrice);
                        System.out.println("============================================");
                        System.out.println("Client payment plan -> " + client.getPlan());
                        double totalPrice = (((timeTotal / 60f) * mechanicHourlyRate + partsPrice)
                                * client.getPlan().getPrice()) / 100d;
                        System.out.printf("Total price after reduction -> %.2f\n", +totalPrice);
                        System.out.println(ANSI_BLUE+"============================================"+ANSI_RESET);
                    }
                }
            }
            if (clientFound) {
                break;
            }
        }
        if (!clientFound) {
            System.out.println(ANSI_RED + "Client not found or no repairs are ready!" + ANSI_RESET);
        }
    }

    private static void checkVehiclesInShop() {
        if (vehiclesInShop.isEmpty()) {
            System.out.println(ANSI_BLUE+"No vehicles undergoing repair at this moment"+ANSI_RESET);
        } else {
            for (Vehicle vehicle : vehiclesInShop) {
                System.out.println(vehicle.getVehicleType().toString());
                System.out.println(vehicle.getModel());
                System.out.println(vehicle.getRegistrationNumber());
                System.out.println(vehicle.getProblem());
                System.out.println("====================");
            }
        }
    }
    private static void checkInfo(Scanner scanner){
        System.out.print("1.List vehicles \n2.List all clients \n3.List all Parts\n4. Return");
        int choice = getValidIntInput("\nSelect info category: ", scanner);
        switch (choice){
            case 1:
                listAllVehicles(scanner);
                break;
            case 2:
                listAllClients();
                break;
            case 3:
                listAllParts(scanner);
                break;
            default:
                break;
        }
    }
    public  void login(Scanner scanner) {
        System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Enter your name:" + ANSI_RESET);
        String name = scanner.nextLine();
        Receptionist receptionist = getReceptionist(name);
        if (receptionist != null) {
            System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Enter your password:" + ANSI_RESET);
            String password = scanner.nextLine();

            if (receptionistMap.get(receptionist).equals(password)) {
                Receptionist.receptionistMenuOptionSelect(receptionist, scanner);
            } else {
                System.out.println(ANSI_RED + "Invalid password. Please try again." + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "Receptionist not found. Please try again." + ANSI_RESET);
        }
    }

    private static Receptionist getReceptionist(String name) {
        for (Receptionist receptionist : receptionistMap.keySet()) {
            if (receptionist.getName().equalsIgnoreCase(name)) {
                return receptionist;
            }
        }
        return null;
    }
    @Override
    public void changePassword(Scanner scanner) {
        System.out.println("Enter you current password: ");
        String oldPass = scanner.nextLine();
        receptionistMap.forEach((key, value) -> {
            if (value.equals(oldPass)) {
                String newPass = scanner.nextLine();
                receptionistMap.put(key, newPass);
            }
        });
    }
    private static void checkReceptionistReg (String name){
        System.out.println();
        boolean validName = checkName(name);
        boolean alreadyRegisteredRecep = receptionistMap.keySet()
                .stream()
                .anyMatch(receptionist -> receptionist.getName().equalsIgnoreCase(name));
        if (alreadyRegisteredRecep){
            throw new IllegalArgumentException("Already Registered Receptionist!!!");
        }
        if (!validName){
            throw new IllegalArgumentException("Invalid name for Receptionist!!!");
        }
    }

}