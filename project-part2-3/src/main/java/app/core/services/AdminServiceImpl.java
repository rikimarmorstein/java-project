package app.core.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exception.CouponSystemException;
import lombok.Getter;

@Service
@Transactional
public class AdminServiceImpl extends ClientService implements AdminService {

	@Getter
	@Value("${admin-email}")
	private String email;

	@Getter
	@Value("${admin-password}")
	private String password;

	/**
	 * The method receives an administrator's email and password, and checks whether
	 * the login details are correct.
	 * 
	 * @param email    - of administrator.
	 * @param password - of administrator.
	 * @return boolean value if the login details are correct.
	 */
	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		if (!(email.equalsIgnoreCase(this.email) && password.equals(this.password))) {
			return false;
		}
		return true;
	}

	/**
	 * The method adds a company to the database after checking that the company
	 * name and email do not exist in the system.
	 * 
	 * @param company - Company with details: name, email and password.
	 * @throws CouponSystemException if name and email exists in the database.
	 */
	@Override
	public Company addCompany(Company company) throws CouponSystemException {
		if (companyRepository.existsByNameAndEmail(company.getName(), company.getEmail())) {
			throw new CouponSystemException(
					"Company " + company.getName() + " cannot be added - Name and email exists");
		}
		return companyRepository.save(company);
	}

	/**
	 * The method updates a company's email and/or password after checking that the
	 * company exists in the system by company code.
	 * 
	 * @param company - Company with details: code, name, email and password after
	 *                updating.
	 * @throws CouponSystemException if company does not exist in the database.
	 */
	@Override
	public Company updateCompany(Company company) throws CouponSystemException {
		Company companyFromDb = companyRepository.findById(company.getId())
				.orElseThrow(() -> new CouponSystemException("Company code cannot be updated"));
		if (!company.getName().equals(companyFromDb.getName())) {
			throw new CouponSystemException("Company name cannot be updated");
		}
		companyFromDb.setEmail(company.getEmail());
		companyFromDb.setPassword(company.getPassword());
		return companyRepository.save(companyFromDb);
	}

	/**
	 * The method deletes a company if it exists, and deletes all the company's
	 * coupons and also deletes the history of the purchase of this coupon from the
	 * purchase table.
	 * 
	 * @param companyID - the company code.
	 * @throws CouponSystemException if company does not exist in the database.
	 */
	public void deleteCompany(int companyID) throws CouponSystemException {
		if (!companyRepository.existsById(companyID)) {
			throw new CouponSystemException("Company " + companyID + " does not exists - cannot be deleted");
		}
		companyRepository.deleteById(companyID);
	}

	/**
	 * The method creates a list of all the companies in the database.
	 * 
	 * @return list of companies details in the database.
	 * @throws CouponSystemException if there are no registered companies in the
	 *                               database.
	 */
	@Override
	public List<Company> getAllCompany() throws CouponSystemException {
		List<Company> allCompanies = companyRepository.findAll();
		return allCompanies;
	}

	/**
	 * The method fetches the company details from the database according to the
	 * company code number.
	 * 
	 * @param companyID - the company code.
	 * @return company details from the database by company code.
	 * @throws CouponSystemException if company code not exists in the database.
	 */
	@Override
	public Company getOneCompany(int companyID) throws CouponSystemException {
		return companyRepository.findById(companyID)
				.orElseThrow(() -> new CouponSystemException("Company " + companyID + " not found"));
	}

	/**
	 * The method adds a customer to the database after checking that the customer
	 * email do not exist in the system.
	 * 
	 * @param customer - Customer with details: first name, last name, email and
	 *                 password.
	 * @throws CouponSystemException if email is exists in the database.
	 */
	@Override
	public void addCustomer(Customer customer) throws CouponSystemException {
		if (customerRepository.existsByEmail(customer.getEmail())) {
			throw new CouponSystemException("mail is exists");
		}
		customerRepository.save(customer);
	}

	/**
	 * The method updates a customer's firstName, lastName, email and/or password
	 * after checking that the customer exists in the system by id.
	 * 
	 * @param customer - Customer with details: first name, last name, email and
	 *                 password after update.
	 * @throws CouponSystemException if customer is not exists in the database.
	 */
	@Override
	public void updateCustomer(Customer customer) throws CouponSystemException {
		Customer customerFromDb = customerRepository.findById(customer.getId())
				.orElseThrow(() -> new CouponSystemException("Customer id cannot be updated"));
		customerFromDb.setFirstName(customer.getFirstName());
		customerFromDb.setLastName(customer.getLastName());
		customerFromDb.setEmail(customer.getEmail());
		customerFromDb.setPassword(customer.getPassword());
		customerRepository.save(customerFromDb);
	}

	/**
	 * The method deletes a customer and deletes the coupons he purchased from the
	 * purchases table.
	 * 
	 * @param customerID - customer ID number.
	 * @throws CouponSystemException if customer does not exist in the database.
	 */
	@Override
	public void deleteCustomer(int customerID) throws CouponSystemException {
		if (!customerRepository.existsById(customerID)) {
			throw new CouponSystemException("the customer is not exists");
		}
		customerRepository.deleteById(customerID);
	}

	/**
	 * The method creates a list of all customer details registered in the database.
	 * 
	 * @return the list of customers details in the database.
	 * @throws CouponSystemException if there are no registered customers in the
	 *                               database.
	 */
	@Override
	public List<Customer> getAllCustomers() throws CouponSystemException {
		List<Customer> allCustomers = customerRepository.findAll();
		return allCustomers;

	}

	/**
	 * The method fetches the customer details from the database according to the
	 * customer id number.
	 * 
	 * @param customerID - customer ID number.
	 * @return customer details from the database by customerID.
	 * @throws CouponSystemException if customer id not exists in the database.
	 */
	@Override
	public Customer getOneCustomer(int customerId) throws CouponSystemException {
		return customerRepository.findById(customerId)
				.orElseThrow(() -> new CouponSystemException("customer " + customerId + " is not found"));
	}

}
