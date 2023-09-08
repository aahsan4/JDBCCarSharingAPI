package carsharing;

import java.util.List;
// interface for the Company DAO structure
public interface CompanyDao {
    Company getCompanyById(int companyId);
    Company getCompanyByCarId(int carId);

    List<Company> getAllCompanies();
    void createCompany(String name);
    void exit();
}
