package Users;

import EnumLists.PricePlan;
import Garage.Car;


import java.util.ArrayList;

import java.util.List;

public class Client extends AppUsers {

    protected List<Car> ownedCars;
    protected PricePlan plan;
    protected String phoneNumber;


    public Client(String name, String email, PricePlan plan,String phoneNumber) {
        super(name, email);
        checkClientReg(name);
        this.ownedCars=new ArrayList<>();
        this.plan=plan;
        this.phoneNumber=phoneNumber;
    }


    public PricePlan getPlan() {
        return plan;
    }


    @Override
    public void displayMenu() {

    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    protected static void checkClientReg(String name) {
        boolean alreadyRegistered = clientVehicleMap.keySet()
                .stream()
                .anyMatch(client -> client.getName().equalsIgnoreCase(name));
        if (alreadyRegistered){
            throw new IllegalArgumentException("Already Registered Client");
        }
    }


}

