package DataStructures;

import Garage.Vehicle;
import Parts.Part;
import Users.Client;
import Users.Mechanic;
import Users.Receptionist;

import java.io.Serializable;
import java.util.*;

public abstract class DataStructures implements Serializable {
    protected static Map<Mechanic,String> mechanicsMap = new HashMap<>();
    protected static Map<Receptionist,String> receptionistMap = new HashMap<>();
    protected static Map<Client,List<Vehicle>> clientVehicleMap = new HashMap<>();
    protected static Map<Part,Integer> partsCatalogue = new HashMap<>();
    protected static Set<Mechanic> availableMechanics = new HashSet<>();
    protected static Set<Vehicle> vehiclesInShop = new HashSet<>();
 }
