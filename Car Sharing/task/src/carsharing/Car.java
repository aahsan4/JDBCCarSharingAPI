package carsharing;

public class Car {
    private int id;
    private String name;
    private int companyId;
    private int IS_RENTED;

    public int getIS_RENTED() {
        return IS_RENTED;
    }

    public void setIS_RENTED(int IS_RENTED) {
        this.IS_RENTED = IS_RENTED;
    }

    public Car(int id,String name, int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.IS_RENTED = 0;
    }
    public Car(String name, int companyId) {
        this.name = name;
        this.companyId = companyId;
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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}

