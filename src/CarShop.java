import Garage.Vehicle;
import Menu.MainMenu;
import Parts.Part;
import Users.Client;
import Users.Mechanic;
import Users.Receptionist;

import java.io.*;
import java.util.*;

public class CarShop extends MainMenu {
    private static final String USERS_FILE_PATH = "users.txt";
    private static final String CARS_CLIENTS_FILE_PATH = "cars.txt";

    public static void startApp(){
        loadData();
        loadUsersFromFile();

        Scanner scanner = new Scanner(System.in);
        entryMenu(scanner);

        saveUsersToFile();
        saveData();
    }
    private static void loadUsersFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(USERS_FILE_PATH))) {
            mechanicsMap = (Map<Mechanic, String>) objectInputStream.readObject();
            receptionistMap = (Map<Receptionist, String>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(ANSI_RED+"Failed to load user data"+ANSI_RESET);
            mechanicsMap = new HashMap<>();
            receptionistMap = new HashMap<>();
        }
    }
    private static void saveUsersToFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(USERS_FILE_PATH))) {
            objectOutputStream.writeObject(mechanicsMap);
            objectOutputStream.writeObject(receptionistMap);
        } catch (IOException e) {
            System.out.println(ANSI_RED+"Failed to save user data."+ANSI_RESET);
        }
    }

    private static void loadData() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(CARS_CLIENTS_FILE_PATH))) {
            clientVehicleMap = (Map<Client,List<Vehicle>>) objectInputStream.readObject();
            partsCatalogue = (Map<Part, Integer>) objectInputStream.readObject();
            availableMechanics = (Set<Mechanic>) objectInputStream.readObject();
            vehiclesInShop = (Set<Vehicle>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(ANSI_RED+"Failed to load data!"+ANSI_RESET);
            //e.printStackTrace();
            clientVehicleMap = new HashMap<>();
        }
    }

    private static void saveData() {
        try (ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(CARS_CLIENTS_FILE_PATH))) {
            objectOutputStream.writeObject(clientVehicleMap);
            objectOutputStream.writeObject(partsCatalogue);
            objectOutputStream.writeObject(availableMechanics);
            objectOutputStream.writeObject(vehiclesInShop);
        } catch (IOException e) {
            System.out.println(ANSI_RED+"Failed to save data!"+ANSI_RESET);
        }
    }
}
