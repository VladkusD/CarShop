package Garage;

import EnumLists.VehicleType;

import java.util.ArrayDeque;

public class Car extends Vehicle {

    public Car(){
        super();
    }

    public Car(VehicleType type,String model, String registrationNumber, String owner, String problem) {
        super(type, model, registrationNumber, owner);
        super.setProblem(problem);
        super.repairedBy=new ArrayDeque<>();
    }
}
