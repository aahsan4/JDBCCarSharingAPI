package carsharing;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private CompanyDao companyDao;
    private Scanner scanner;
    private CarDAO carDAO;
    private CustomerDAO customerDAO;

    public Menu() {
        scanner = new Scanner(System.in);
        companyDao = new CompanyDaoImpl();
        carDAO = new CarDAO();
        customerDAO = new CustomerDAOImpl();
    }

    public void mainMenu() {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consuming the newline character
        switch (choice) {
            case 1:
                managerMenu();
                break;
            case 2:
                customerLogin();
                break;
            case 3:
                createCustomer();
                break;
            case 0:
                companyDao.exit();
                break;
            default:
                mainMenu();
                break;
        }
    }

    private void managerMenu() {
        System.out.println();
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
        int choice = scanner.nextInt();
        //scanner.nextLine();
        if (choice == 1) {
            companyList();
        } else if (choice == 2) {
            createCompany();
        } else if (choice == 0) {
            mainMenu();
        } else {
            managerMenu();
        }
    }

    private void companyList() {
        System.out.println();

        List<Company> companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Choose the company:");
            for (Company company : companies) {
                System.out.println(company.getId() + ". " + company.getName());
            }
            System.out.println("0. Back");
            int companyChoice = scanner.nextInt();
            
            if (companyChoice == 0) {
                managerMenu();
            } else if (companyChoice > 0 && companyChoice <= companies.size()) {
                Company chosenCompany = companies.get(companyChoice - 1);
                companyMenu(chosenCompany);
            } else {
                companyList();
            }
        }
        managerMenu();
    }

    private void companyMenu(Company company) {
        System.out.println();
        System.out.println("'" + company.getName() + "' company");
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            carList(company);
        } else if (choice.equals("2")) {
            createCar(company);
        } else if (choice.equals("0")) {
            managerMenu();
        } else {
            companyMenu(company);
        }
    }

    private void carList(Company company) {
        System.out.println();
        List<Car> cars = carDAO.getCarsByCompanyId(company.getId());
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println((i + 1) + ". " + cars.get(i).getName());
            }
        }
        companyMenu(company);
    }

    private void createCar(Company company) {
        System.out.println();
        System.out.println("Enter the car name:");

        // Reading user input as a string
        String input = scanner.nextLine().trim();

        // Parsing the input as needed (e.g., check for numeric values)
        if (input.matches("[0-9]+")) {
            System.out.println("Invalid car name. Please enter a valid name.");
            createCar(company); // Recursively asking for input again
        } else {
            // Car name is valid, proceeding with creating the car
            Car newCar = new Car(input, company.getId());
            carDAO.createCar(newCar);
            System.out.println("The car was added!");
            companyMenu(company);
        }
    }
    private void createCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
    
        if (name.isEmpty()) {
            managerMenu();
        }
        companyDao.createCompany(name);
        System.out.println("The company was created!");
        managerMenu();

    }

    private void customerLogin() {
        System.out.println();
        System.out.println("Choose a customer:");

        List<Customer> customers = customerDAO.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            mainMenu();
            return;
        }

        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i).getName());
        }
        System.out.println("0. Back");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consuming the newline character

        if (choice == 0) {
            mainMenu();
        } else if (choice > 0 && choice <= customers.size()) {
            Customer customer = customers.get(choice - 1);
            customerMenu(customer);
        } else {
            customerLogin();
        }
    }

    private void customerMenu(Customer customer) {
        System.out.println();
        //System.out.println(customer.getName() + " menu:");
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consuming the newline character

        switch (choice) {
            case 1:
                rentACar(customer);
                break;
            case 2:
                returnRentedCar(customer);
                break;
            case 3:
                displayRentedCar(customer);
                break;
            case 0:
                customerLogin();
                break;
            default:
                customerMenu(customer);
                break;
        }
    }

    private void createCustomer() {
        System.out.println();
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();

        if (!name.isEmpty()) {
            customerDAO.createCustomer(name);
            System.out.println("The customer was added!");
        }

        mainMenu();
    }

    private void rentACar(Customer customer) {
        if (customer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!");
            customerMenu(customer);
            return;
        }

        List<Company> companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            customerMenu(customer);
            return;
        }

        System.out.println("Choose a company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println((i + 1) + ". " + companies.get(i).getName());
        }
        System.out.println("0. Back");

        int companyChoice = scanner.nextInt();
        if (companyChoice == 0) {
            customerMenu(customer);
            return;
        }

        Company selectedCompany = companies.get(companyChoice - 1);
        List<Car> availableCars = carDAO.getCarsByCompanyId(selectedCompany.getId());

        if (availableCars.isEmpty()) {
            System.out.println("No available cars in " + selectedCompany.getName() + " company.");
            customerMenu(customer);
            return;
        }

        System.out.println("Choose a car:");
        for (int i = 0; i < availableCars.size(); i++) {
            System.out.println((i + 1) + ". " + availableCars.get(i).getName());
        }
        System.out.println("0. Back");

        int carChoice = scanner.nextInt();
        if (carChoice == 0) {
            customerMenu(customer);
            return;
        }

        Car selectedCar = availableCars.get(carChoice - 1);

        carDAO.rentCar(selectedCar, customer);
        System.out.print("You rented ");
        System.out.println("'"+selectedCar.getName()+"'");
        customer.setRentedCar(selectedCar.getId());

        customerMenu(customer);
    }

    private void returnRentedCar(Customer customer) {
        if (customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
            customerMenu(customer);
            return;
        }

        carDAO.returnCar(customer);
        System.out.println("You've returned a rented car!");
        customerMenu(customer);
    }

    private void displayRentedCar(Customer customer) {
        Integer rentedCarId = customer.getRentedCarId();

        if (rentedCarId == 0) {
            System.out.println("You didn't rent a car!");
            customerMenu(customer);
            return;
        }

        Car rentedCar = carDAO.getCarById(rentedCarId);

        if (rentedCar == null) {
            System.out.println("You didn't rent a car!.");
        } else {
            System.out.println("Your rented car:");
            System.out.println(rentedCar.getName());
            System.out.println("Company:");
            Company company = companyDao.getCompanyByCarId(rentedCar.getId());
            System.out.println(company.getName());
        }

        customerMenu(customer);
    }
}
