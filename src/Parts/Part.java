package Parts;

import java.io.Serializable;

import static Users.AppUsers.checkPart;

public class Part implements Serializable {
    private final String name;
    private final int id;
    private final String manufacturer;
    private final String category;

    private final double price;

    public Part(String name,int id, String manufacturer, String category , double price) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.category = category;
        this.id=id;
        checkPart(id);
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public String getCategory() {
        return category;
    }
    public double getPrice() {
        return price;
    }

}
