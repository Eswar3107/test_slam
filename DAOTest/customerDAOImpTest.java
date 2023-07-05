package DAOTest;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.sql.Timestamp;
import java.time.Instant;

import javax.sql.DataSource;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eStoreProduct.DAO.customerDAOImp;
import eStoreProduct.model.customerMapper;
import eStoreProduct.model.custCredModel;

public class customerDAOImpTest {
    
    private customerDAOImp customerDAO;
    
    @Mock
    private DataSource dataSource;
    
    @Mock
    private JdbcTemplate jdbcTemplate;
    
    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        customerDAO = new customerDAOImp(dataSource);
        customerDAO.jdbcTemplate = jdbcTemplate;
    }
    private final String SQL_INSERT_CUSTOMER = "insert into slam_customers(cust_name,  cust_mobile , cust_regdate ,cust_location , "
    		+ "cust_email ,cust_address, cust_pincode , cust_saddress,cust_spincode,cust_status ,cust_lastlogindate, cust_password ) values(?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String SQL_CHECK_CUSTOMER = "select * from slam_customers where cust_email=? and cust_password=? ";

    @Test
    public void testCreateCustomer() {
        custCredModel customer = new custCredModel();
        when(jdbcTemplate.update(eq(SQL_INSERT_CUSTOMER), any(), any(), any(Timestamp.class), any(), any(), any(), any(), any(), any(),
        		any(), any(Timestamp.class), any())).thenReturn(1); // Assuming update is successful

        boolean result = customerDAO.createCustomer(customer);
        
        assertEquals(result, true);
    }

    @Test
    public void testGetCustomerById() {
        int id = 1; // Set customer ID
        String custSelect = "SELECT * FROM slam_customers WHERE cust_id=?";
        
        custCredModel expectedCustomer = new custCredModel();
        expectedCustomer.setCustName("John Doe"); // Set expected customer name
        when(jdbcTemplate.queryForObject(eq(custSelect), any(Object[].class), any(customerMapper.class)))
        .thenReturn(expectedCustomer); // Assuming queryForObject returns a customer


        custCredModel actualCustomer = customerDAO.getCustomerById(id);

        assertEquals(actualCustomer.getCustName(), expectedCustomer.getCustName());
    }
        @Test
    public void testGetCustomer() {
        String email = "potalameghana2@gmail.com";
        String password = "111";
        custCredModel expectedCustomer = new custCredModel();
        expectedCustomer.setCustEmail(email);

        when(jdbcTemplate.queryForObject(eq(SQL_CHECK_CUSTOMER), any(Object[].class), any(customerMapper.class)))
            .thenReturn(expectedCustomer);

        custCredModel customer = customerDAO.getCustomer(email, password);

       // assertNotNull(customer);
        assertEquals(customer.getCustEmail(), expectedCustomer.getCustEmail());
    }

     
    @Test
    public void testUpdatePassword() {
        String password = "newPassword";
        String email = "test@example.com";
        String sql = "UPDATE slam_customers SET cust_password=? WHERE cust_email=?";

        int rowsAffected = 1; // Set the expected number of affected rows

        doReturn(rowsAffected).when(jdbcTemplate).update(eq(sql), eq(password), eq(email));

        customerDAO.updatePassword(password, email);

        // Assert the behavior or outcome if needed
        // ...
    }
 
    @Test
    public void testGetCustomerByEmail() {
        String email = "test@example.com";
        
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(customerMapper.class)))
            .thenReturn(new custCredModel()); // Assuming queryForObject returns a customer
        
        custCredModel customer = customerDAO.getCustomerByEmail(email);
        
        // Assert the customer
        // ...
    }
    
   
    @Test
    public void testUpdatelastlogin() {
        int cid = 1; // Set customer ID
        String updateQuery = "UPDATE slam_customers SET cust_lastlogindate = CURRENT_TIMESTAMP WHERE cust_id = ?";
        
        int affectedRows = 1; // Set the expected number of affected rows
        
        when(jdbcTemplate.update(anyString(), eq(cid))).thenReturn(affectedRows);
        
        // Call the method under test
        customerDAO.updatelastlogin(cid);
        
        // Verify that the update method was called with the correct arguments
        verify(jdbcTemplate).update(updateQuery, cid);
        
        // Assert any additional behavior or expectations
        // ...    }


}}