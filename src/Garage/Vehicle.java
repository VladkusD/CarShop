package Garage;

import EnumLists.VehicleStatus;
import EnumLists.VehicleType;
import Menu.ColorScheme;
import Parts.Part;
import Users.Mechanic;

import java.io.Serializable;
import java.util.*;

import static Users.AppUsers.checkVehicleReg;

public abstract class Vehicle implements Serializable, ColorScheme {

    private String owner;
    private VehicleType vehicleType;
    private String model;
    private String registrationNumber;
    private List<String> performedRepairs;
    private List<String> repairHistory;
    private long time;
    protected Map<Part, Integer> usedParts;
    protected Map<Part, Integer> usedPartsHistory;
    protected VehicleStatus vehicleStatus;

    protected String problem;
    public ArrayDeque<Mechanic> repairedBy;


    public Vehicle() {
    }

    public Vehicle(VehicleType type, String model, String registrationNumber, String owner) {
        this.model = model;
        this.owner = owner;
        this.registrationNumber = registrationNumber;
        checkVehicleReg(registrationNumber);
        this.performedRepairs = new ArrayList<>();
        this.usedParts = new HashMap<>();
        this.repairHistory = new ArrayList<>();
        this.usedPartsHistory = new HashMap<>();
        this.setType(type);
    }

    public String getModel() {
        return model;
    }

    public String getOwner() {
        return owner;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public List<String> getPerformedRepairs() {
        return performedRepairs;
    }

    public void setPerformedRepairs(List<String> repairs) {
        this.performedRepairs.addAll(repairs);
    }

    public Map<Part, Integer> getUsedParts() {
        return usedParts;
       // return Collections.unmodifiableMap(usedParts);
    }

    public void setUsedParts(Part part, int quantity) {
        this.usedParts.put(part, quantity);
    }

    public List<String> getRepairHistory() {
        return Collections.unmodifiableList(repairHistory);
    }

    public void setRepairHistory(List<String> repairs) {
        this.repairHistory.addAll(repairs);
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getRepairedBy() {
        if (repairedBy.isEmpty()) {
            System.out.println("no repairs");
        }
       return repairedBy.peek().getName();
    }

    public void setRepairedBy(Mechanic repairedBy) {
        this.repairedBy.push(repairedBy);
    }

    public VehicleStatus getStatus() {
        return vehicleStatus;
    }

    public void setStatus(VehicleStatus status) {
        this.vehicleStatus = status;
    }

    protected void setType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
    public  void displayVehicleInfo(){
        System.out.println(ANSI_BLACK + ANSI_WHITE_BACKGROUND + "Model: " + getModel() + ANSI_RESET);
        System.out.println(ANSI_PURPLE+"Vehicle type: "+ANSI_RESET+ getVehicleType());
        System.out.println(ANSI_PURPLE + "Registration Number: " + ANSI_RESET + getRegistrationNumber());
        System.out.println(ANSI_PURPLE + "Problem: " + ANSI_RESET + getProblem());
        System.out.println(ANSI_PURPLE + "Vehicle status: " + ANSI_RESET + getStatus());
        System.out.println("Owned by: "+ getOwner());
        System.out.println("================================");
    }
    public Map<Part, Integer> getUsedPartsHistory() {
        return usedPartsHistory;
    }

}
