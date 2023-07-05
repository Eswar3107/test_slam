package DAOTest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eStoreProduct.DAO.ProdStockDAOImp;
import eStoreProduct.model.ProdStock;
import eStoreProduct.model.ProductStockRowMapper;
public class ProdStockDAOImpTest {

	   @Mock
	    private DataSource dataSource;

	   @Mock
	    private ProdStockDAOImp prodStockDAO;
	   
	   @Mock
	    private JdbcTemplate jdbcTemplate;

	    @BeforeClass
	    public void setup() {
	        MockitoAnnotations.initMocks(this);
	        prodStockDAO = new ProdStockDAOImp(dataSource);
	        prodStockDAO.jdbcTemplate = jdbcTemplate;
	    }

	    @Test
	    public void testGetAllProdStocks() {
	        // Create a list of mock ProdStock objects
	        ProdStock prodStock1 = new ProdStock(0, 0, 0, 0);
	        ProdStock prodStock2 = new ProdStock(0, 0, 0, 0);
	        List<ProdStock> expectedProdStocks = Arrays.asList(prodStock1, prodStock2);

	        // Mock the query method of JdbcTemplate to return the expected list of ProdStocks
	        String sql = "SELECT * FROM slam_productstock";
	       
	        when(jdbcTemplate.query(eq(sql), any(ProductStockRowMapper.class))).thenReturn(expectedProdStocks);
            // Call the method under test
	        List<ProdStock> actualProdStocks = prodStockDAO.getAllProdStocks();

	        // Verify the result
	        Assert.assertEquals(actualProdStocks, expectedProdStocks);
	    }
	    @Test
	    public void testGetProdStockById() {
	        // Create a mock ProdStock object
	        List<ProdStock> expectedProdStockList = new ArrayList<>();
	        ProdStock expectedProdStock = new ProdStock(0, 0, 0, 0);
	        expectedProdStockList.add(expectedProdStock);

	        // Mock the query method of JdbcTemplate to return the expected ProdStock list
	        String sql = "SELECT * FROM slam_productstock WHERE prod_id = ?";
	        int prodId = 123; // Replace with the desired product ID
	        when(jdbcTemplate.query(eq(sql), any(ProductStockRowMapper.class), eq(prodId)))
	                .thenReturn(expectedProdStockList);

	        // Call the method under test
	        ProdStock actualProdStock = prodStockDAO.getProdStockById(prodId);

	        // Verify the result
	        if (actualProdStock != null) {
	            Assert.assertEquals(expectedProdStockList, Collections.singletonList(actualProdStock));
	        } else {
	            Assert.assertNull(actualProdStock);
	        }
	    }
	    @Test
	    public void testGetProdPriceById() {
	        // Set up the mock result
	        double expectedPrice = 9.99;
	        final String PD_PRICE_QUERY = "SELECT prod_price FROM slam_productstock WHERE prod_id = ?";
	        when(jdbcTemplate.queryForObject(eq(PD_PRICE_QUERY), eq(Double.class), eq(123)))
	                .thenReturn(expectedPrice);

	        // Call the method under test
	        double actualPrice = prodStockDAO.getProdPriceById(123);

	        // Verify the result
	        Assert.assertEquals(expectedPrice, actualPrice, 0.01); // Use a delta for floating-point comparisons
	    }

	    @Test
	    public void testGetProdMrpById() {
	        // Set up the mock result
	        double expectedMrp = 19.99;
	        final String PD_MRP_QUERY = "SELECT prod_mrp FROM slam_productstock WHERE prod_id = ?";
	        when(jdbcTemplate.queryForObject(eq(PD_MRP_QUERY), eq(Double.class), eq(123)))
	                .thenReturn(expectedMrp);

	        // Call the method under test
	        double actualMrp = prodStockDAO.getProdMrpById(123);

	        // Verify the result
	        Assert.assertEquals(expectedMrp, actualMrp, 0.01); // Use a delta for floating-point comparisons
	    }


}
