package Users;


import EnumLists.PricePlan;
import Menu.ColorScheme;
import Parts.Part;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Administrator extends AppUsers implements User, ColorScheme {

    private String password;

    public Administrator() {
        super();
    }

    public Administrator(String name, String email, String password) {
        super(name, email);
        this.password=password;
    }


    public void displayMenu() {
        System.out.println(ANSI_WHITE_BACKGROUND+ANSI_BLACK+"Administrator Menu:"+ANSI_RESET);
        System.out.println("1. Add Mechanic");
        System.out.println("2. Add Receptionist");
        System.out.println("3. Add Client");
        System.out.println("4. Add part to catalogue");
        System.out.println("5. Add additional quantity of parts");
        System.out.println("6. List all clients");
        System.out.println("7. List all vehicles");
        System.out.println("8. List all parts");
        System.out.println(ANSI_RED+"0. Exit"+ANSI_RESET);
    }
    public static void displayAdministratorMenu(Administrator administrator, Scanner scanner) {
        while (true) {
            administrator.displayMenu();
            int choice = getValidIntInput("Choose number from menu:\n" ,scanner);
            switch (choice) {
                case 1:
                    addNewMechanic(scanner);
                    break;
                case 2:
                    addNewReceptionist(scanner);
                    break;
                case 3:
                    addNewClient(scanner);
                    break;
                case 4:
                    addNewPart(scanner);
                    break;
                case 5:
                    addPartQuantity(scanner);
                    break;
                case 6:
                    listAllClients();
                    break;
                case 7:
                    listAllVehicles(scanner);
                    break;
                case 8:
                    listAllParts(scanner);
                    break;
                // използвано за тест по време на разработака
//                case 9:
//                    addTestData();
//                    break;
                case 0:
                    return;

                default:
                    System.out.println(ANSI_RED+"Invalid choice. Please try again."+ANSI_RESET);
                    break;
            }
        }
    }

    private static void addNewMechanic(Scanner scanner) {
        String name = getValidStringInput("Enter mechanic name:\n",scanner);
        boolean mechanicExist = mechanicsMap.keySet()
                .stream()
                .anyMatch(mechanic -> mechanic.getName().equalsIgnoreCase(name));
        if (mechanicExist) {
            System.out.println(ANSI_RED+"Mechanic already registered!"+ANSI_RESET);
        } else {
            String email = getValidEmail("Enter mechanic email:",scanner);
            System.out.println("Enter mechanic password: ");
            String password = scanner.nextLine();
            while (password.isEmpty()){
                System.out.println("Enter valid password");
                password=scanner.nextLine();
            }
            Mechanic mechanic = new Mechanic(name, email);
            mechanicsMap.put(mechanic, password);
            availableMechanics.add(mechanic);
            System.out.println(ANSI_BLUE+"Mechanic registered successfully!"+ANSI_RESET);
        }
    }

    private static void addNewReceptionist(Scanner scanner) {
        String name = getValidStringInput("Enter receptionist name:\n", scanner);
        boolean receptionistExist = receptionistMap.keySet()
                .stream()
                .anyMatch(receptionist -> receptionist.getName().equalsIgnoreCase(name));
        if (receptionistExist) {
            System.out.println(ANSI_RED+"Receptionist already registered!"+ANSI_RESET);
        } else{
            String email = getValidEmail("Enter receptionist email:",scanner);
            System.out.println("Enter receptionist password: ");
            String password = scanner.nextLine();
            while (password.isEmpty()){
                System.out.println("Enter valid password");
                password=scanner.nextLine();
            }
            Receptionist receptionist = new Receptionist(name,email);
            receptionistMap.put(receptionist,password);
            System.out.println(ANSI_BLUE+"Receptionist registered successfully!"+ANSI_RESET);
        }
    }
    static protected void addNewClient(Scanner scanner) {
        String name = getValidStringInput("Enter client name:\n", scanner);
        boolean clientExists = clientVehicleMap.keySet()
                .stream()
                .anyMatch(client -> client.getName().equalsIgnoreCase(name));
        if (clientExists) {
            System.out.println("Client already registered!");
        }else {
            String email = getValidEmail("Enter client email:",scanner);
            System.out.println("Enter client phone number:");
            String phoneNumber = scanner.nextLine();
            PricePlan plan = null;
            while (plan == null) {
                System.out.println("Enter client payment plan - choose from : normal/premium/exclusive :");
                String clientPlan = scanner.nextLine().toLowerCase();
                switch (clientPlan) {
                    case "normal":
                        plan = PricePlan.NORMAL;
                        break;
                    case "premium":
                        plan = PricePlan.PREMIUM;
                        break;
                    case "exclusive":
                        plan = PricePlan.EXCLUSIVE;
                        break;
                    default:
                        System.out.println("wrong input,please try again");
                        break;
                }
            }
            Client client = new Client(name, email,plan ,phoneNumber);
            clientVehicleMap.put(client, new ArrayList<>());
            System.out.println(ANSI_BLUE+"Client registered successfully!"+ANSI_RESET);
        }
    }
    private static void addNewPart(Scanner scanner) {
        String name = getValidStringInput("Enter part name\n",scanner);
        int id = getValidIntInput("Enter part ID\n",scanner);
        boolean partExist = partsCatalogue.keySet()
                .stream()
                .anyMatch(part -> part.getId()==id);
        if (partExist){
            System.out.println("Part with that ID already Exist");
        } else {
            System.out.println("Enter part manufacturer:");
            String manufacturer = scanner.nextLine();
            String category = getValidStringInput("Enter part category:\n", scanner);
            int quantity = getValidIntInput("Enter part quantity\n", scanner);
            double price = getValidDoubleInput("Enter part price\n", scanner);
            Part part = new Part(name, id, manufacturer, category, price);
            partsCatalogue.put(part, quantity);
            System.out.println(ANSI_BLUE + "Part registered successfully!" + ANSI_RESET);
        }
    }
    private static void addPartQuantity(Scanner scanner) {

        int id = getValidIntInput("Enter part id: \n",scanner);
        int amount = getValidIntInput("Enter amount: \n",scanner);
        boolean partFound = false;
        for (Map.Entry<Part, Integer> parts : partsCatalogue.entrySet()) {
            Part part = parts.getKey();
            if (part.getId() == id) {
                partFound=true;
                partsCatalogue.put(part,parts.getValue()+amount);
                System.out.printf(ANSI_BLUE+"Increased by %d for total of %d\n"+ANSI_RESET,
                        amount,parts.getValue());
                break;
            }
        }
        if (!partFound){
            System.out.println(ANSI_RED+"no part found"+ANSI_RESET);
        }
    }
// Използван за тест по време на разработка.
    /*private static void addTestData() {
        Mechanic mechanic = new Mechanic("Vlad", "vlad@mail.bg");
        mechanicsMap.put(mechanic,"vlad1");
        availableMechanics.add(mechanic);
        Mechanic mechanic1 = new Mechanic("Trifon", "trigon@yahoo.com");
        mechanicsMap.put(mechanic1,"trifon1");
        availableMechanics.add(mechanic1);
        Mechanic mechanic2 = new Mechanic("Stamat","stamat@yahoo.com");
        mechanicsMap.put(mechanic2,"stamat1");
        availableMechanics.add(mechanic2);
        Receptionist receptionist = new Receptionist("Niki","niki@mail.bg");
        receptionistMap.put(receptionist,"niki1");
        Client client = new Client("Petar Petrov","petar@gmail.com", PricePlan.PREMIUM,"0888125687");
        Client client2 = new Client("Ivan Ivanov","ivan@abv.bg", PricePlan.PREMIUM,"0884236985");
        Client client3 = new Client("Radostin Radostinov", "rado@mail.bg",PricePlan.NORMAL, "0887564877");

        List<Vehicle> testList = new ArrayList<>();
        List<Vehicle> testList2 = new ArrayList<>();
        List<Vehicle> testList3 = new ArrayList<>();
        Vehicle car = new Car(VehicleType.CAR,"TOYOTA","CA1919PP","Petar Petrov","does not start");
        Vehicle car2 = new Motorcycle(VehicleType.MOTORCYCLE,"BMW","X2865TH","Petar Petrov","oil change");
        Vehicle car3 = new Truck(VehicleType.TRUCK,"DAF","PP4374TT","Ivan Ivanov","tyre change");
        Vehicle car4 = new Car(VehicleType.CAR,"MAZDA","PB1762KC","Radostin Radostinov","undefined problem");
        List<Vehicle> testCars = new ArrayList<>();
        testCars.add(car);
        testCars.add(car2);
        testCars.add(car3);
        testCars.add(car4);
        for (Vehicle testCar : testCars) {
            testCar.setStatus(VehicleStatus.PENDING_REPAIR);
        }
        testList.add(car);
        testList.add(car2);
        testList2.add(car3);
        testList3.add(car4);
        clientVehicleMap.put(client,testList);
        clientVehicleMap.put(client2,testList2);
        clientVehicleMap.put(client3,testList3);

        Part part1 = new Part("Pirelli tyre",100100,"Pirelli","Tyre",120);
        Part part2 = new Part("Continental tyre",100200,"Continental AG","Tyre",110);
        Part part3 = new Part("Michelin tyre",100300,"Michelin","Tyre",150);
        Part part4 = new Part("Bridgestone tyre",100400,"Bridgestone","Tyre",105);

        Part part5 = new Part("ACT Clutch",200100,"ACT","Clutch",140);
        Part part6 = new Part("Vale Clutch",200200,"Valeo","Clutch",180);
        Part part7 = new Part("Luk Clutch",200300,"Luk","Clutch",100);
        Part part8 = new Part("Sach Clutch",200400,"Sach","Clutch",80);

        Part part9 = new Part("AC Delco brakes",300100,"AC Delco","Brakes",120);
        Part part10 = new Part("Bosh brakes",300200,"Bosh","Brakes",140);
        Part part11 = new Part("Power stop brakes",300300,"Power Stop","Brakes",90);

        Part part12 = new Part("Mobile oil",400100,"Mobile","Oil",14);
        Part part13 = new Part("Castrol oil",400200,"Castrol","Oil",8);
        Part part14 = new Part("Valvoline oil",400300,"Valvoline","Oil",6);

        Part part15 = new Part("Hella lights",500100,"Hella","Lights",60);
        Part part16 = new Part("Koito lights",500200,"Koito","Lights",85);
        Part part17 = new Part("Osram lights",500300,"Osram","Lights",105);



        partsCatalogue.put(part1,6);
        partsCatalogue.put(part3,1);
        partsCatalogue.put(part2,40);
        partsCatalogue.put(part4,18);
        partsCatalogue.put(part5,12);
        partsCatalogue.put(part6,2);
        partsCatalogue.put(part7,1);
        partsCatalogue.put(part8,10);
        partsCatalogue.put(part9,8);
        partsCatalogue.put(part10,16);
        partsCatalogue.put(part11,3);
        partsCatalogue.put(part12,5);
        partsCatalogue.put(part13,1);
        partsCatalogue.put(part14,3);
        partsCatalogue.put(part15,12);
        partsCatalogue.put(part16,4);
        partsCatalogue.put(part17,1);
    }*/
    public boolean authenticate (String inputPassword){
        return password.equals(inputPassword);
    }

    @Override
    public void login(Scanner scanner) {
        System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Enter your name:" + ANSI_RESET);
        String name = scanner.nextLine();
        Administrator administrator = getAdmin(name);
        if (administrator != null) {
            System.out.println(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "Enter your password:" + ANSI_RESET);
            String password = scanner.nextLine();

            if (administrator.authenticate(password)) {
                Administrator.displayAdministratorMenu(administrator, scanner);
            } else {
                System.out.println(ANSI_RED + "Invalid password. Please try again." + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "Administrator not found. Please try again." + ANSI_RESET);
        }
    }

    private static Administrator getAdmin(String name) {
        if (name.equals("admin")) {
            return new Administrator("admin", "admin@mail.bg", "admin123");
        } else return null;
    }

    @Override
    public void changePassword(Scanner scanner) {
        // неприложимо с тази логика
    }
}
