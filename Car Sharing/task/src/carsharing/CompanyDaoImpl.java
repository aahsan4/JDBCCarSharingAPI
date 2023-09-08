package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// comapny DAO implementation class
public class CompanyDaoImpl implements CompanyDao {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    static final String USER = "";
    static final String PASS = "";

    private Connection conn;

    public CompanyDaoImpl() {
        conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(true);

            stmt = conn.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    " NAME VARCHAR UNIQUE NOT NULL)";
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
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM COMPANY ORDER BY ID");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                companies.add(new Company(rs.getInt("ID"), rs.getString("NAME")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public void createCompany(String name) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO COMPANY(NAME) VALUES (?)");
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Company getCompanyByCarId(int carId) {
        String sql = "SELECT C.* FROM CAR CAR JOIN COMPANY C ON CAR.COMPANY_ID = C.ID WHERE CAR.ID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, carId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    return new Company(id, name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Company getCompanyById(int companyId) {
        Company company = null;
        String query = "SELECT * FROM COMPANY WHERE ID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, companyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                company = new Company(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return company;
    }
}
