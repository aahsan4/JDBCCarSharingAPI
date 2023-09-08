package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing"; // path to the db
    static final String USER = ""; // no password set
    static final String PASS = "";
    private CompanyDao companyDao;
    private CustomerDAO customerDAO;
    private Connection conn;

    // constructor of Car DAO
    public CarDAO() {
        conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(true);

            stmt = conn.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS CAR (\n" +
                    "    ID INT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    NAME VARCHAR(255) NOT NULL UNIQUE,\n" +
                    "    COMPANY_ID INT NOT NULL,\n" +
                    "    IS_RENTED INT,\n"+
                    "    FOREIGN KEY (COMPANY_ID) REFERENCES company (id)\n" +
                    ");";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            }
        }
    }

    // Creating a new car in the database
    public void createCar(Car car) {
        String insertCarSQL = "INSERT INTO CAR (NAME, COMPANY_ID,IS_RENTED) VALUES (?, ?,?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertCarSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, car.getName());
            preparedStatement.setInt(2, car.getCompanyId());
            preparedStatement.setInt(3, 0);
            conn.setAutoCommit(true);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating car failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    car.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating car failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieving a list of cars by company ID
    public List<Car> getCarsByCompanyId(int companyId) {
        List<Car> cars = new ArrayList<>();
        String selectCarsSQL = "SELECT * FROM CAR WHERE COMPANY_ID = ? AND IS_RENTED = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(selectCarsSQL)) {
            preparedStatement.setInt(1, companyId);
            preparedStatement.setInt(2, 0);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    cars.add(new Car(id, name, companyId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    // method to rent a car
    public void rentCar(Car car, Customer customer) {
        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, car.getId());
            preparedStatement.setInt(2, customer.getId());
            preparedStatement.executeUpdate();

            conn.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String updateCarSQL = "UPDATE CAR SET IS_RENTED = ? WHERE ID = ?";
        try (PreparedStatement carStatement = conn.prepareStatement(updateCarSQL)) {
            carStatement.setInt(1, 1); // setting 1 as is_rented variable indicating the car is rented
            carStatement.setInt(2, car.getId()); // setting the car ID
            carStatement.executeUpdate();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // method to return car
    public void returnCar(Customer customer) {
        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, customer.getId()); // setting the rented car id as null of the customer returning the car
            conn.setAutoCommit(true);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("You've returned a rented car!");
            } else {
                System.out.println("You didn't rent a car!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // method to get car by carId
    public Car getCarById(int carId) {
        String sql = "SELECT * FROM CAR WHERE ID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, carId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    int companyId = resultSet.getInt("COMPANY_ID");
                    int is_rented = resultSet.getInt("IS_RENTED");

                    return new Car(id,name, companyId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}

