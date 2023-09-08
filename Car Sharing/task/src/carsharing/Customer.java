package carsharing;

public class Customer {
    private int id;
    private String name;
    private Integer rentedCarId; // Nullable, in case of a customer who doesn't have any rental

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
        this.rentedCarId = null; // Initially no rented car
    }

    // Constructor for creating a customer with a rented car
    public Customer(int id, String name, Integer rentedCar) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRentedCarId() {
        return this.rentedCarId;
    }

    public void setRentedCar(int rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
    @Override
    public String toString() {
        return this.getId() + ", "+this.getName()+", "+this.getRentedCarId();
    }
}

