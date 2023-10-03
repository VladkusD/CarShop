package Garage;

import EnumLists.VehicleType;

import java.util.ArrayDeque;

public class Truck extends Vehicle {

    public Truck(){
    }
    public Truck(VehicleType type,String model, String registrationNumber, String owner, String problem) {
        super(type,model, registrationNumber, owner);
        super.setProblem(problem);
        super.repairedBy=new ArrayDeque<>();
    }
}
