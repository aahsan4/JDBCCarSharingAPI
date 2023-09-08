package carsharing;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    static final String USER = "";
    static final String PASS = "";
    private Connection conn;

    public CustomerDAOImpl() {
        conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(true);

            stmt = conn.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS CUSTOMER (\n" +
                    "    ID INT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    NAME VARCHAR(255) NOT NULL UNIQUE,\n" +
                    "    RENTED_CAR_ID INT,\n" +
                    "    FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID)\n" +
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
    @Override
    public void createCustomer(String name) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CUSTOMER (NAME,RENTED_CAR_ID) VALUES (?,?)")) {
            connection.setAutoCommit(true);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, null);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMER");
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                Integer rentedCarId = resultSet.getInt("RENTED_CAR_ID");
                customers.add(new Customer(id, name, rentedCarId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM CUSTOMER WHERE ID = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int rentedCarId = resultSet.getInt("RENTED_CAR_ID");
                return new Customer(id, name, rentedCarId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Returning null if customer with the given ID is not found
    }

    @Override
    public void updateRentedCar(int customerId, Integer rentedCarId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?")) {
            preparedStatement.setObject(1, rentedCarId);
            preparedStatement.setInt(2, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

