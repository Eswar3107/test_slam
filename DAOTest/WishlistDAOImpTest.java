package eStoreProduct.testing.dao;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import eStoreProduct.DAO.ProdStockDAO;
import eStoreProduct.DAO.WishlistDAOImp;
import eStoreProduct.DAO.customerDAOImp;
import eStoreProduct.model.WishlistRowMapper;
import eStoreProduct.utility.ProductStockPrice;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class WishlistDAOImpTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ProdStockDAO prodStockDAO;
    
    @Mock
    private DataSource dataSource;

    @InjectMocks
    private WishlistDAOImp wishlistDAOImp;

    @BeforeClass
    public void setUp() {
    	 MockitoAnnotations.initMocks(this);
    	 wishlistDAOImp = new WishlistDAOImp(dataSource,prodStockDAO);
    	 wishlistDAOImp.jdbcTemplate = jdbcTemplate;
        //MockitoAnnotations.openMocks(this);
    }
    private String insert_slam_wishlist = "INSERT INTO slam_wishlist (cust_id,prod_id) VALUES (?, ?)";
	private String delete_slam_wishlist = "DELETE FROM slam_wishlist WHERE cust_id=? AND prod_id=?";
	private String select_slam_wishlist = "SELECT pd.* FROM slam_Products pd, slam_wishlist sc WHERE sc.cust_id = ? AND sc.prod_id = pd.prod_id";


	@Test
	public void testAddToWishlist() {
	    int productId = 1;
	    int customerId = 1;

	    when(jdbcTemplate.update(eq(insert_slam_wishlist), anyInt(), anyInt())).thenReturn(1);

	    int result = wishlistDAOImp.addToWishlist(productId, customerId);

	    assertEquals(result, productId);

	    verify(jdbcTemplate, times(1)).update(eq(insert_slam_wishlist), eq(customerId), eq(productId));
	}
	@Test
	public void testRemoveFromWishlist() {
	    int productId = 1;
	    int customerId = 1;
	    int expectedResult = 1;

	    when(jdbcTemplate.update(eq(delete_slam_wishlist), anyInt(), anyInt())).thenReturn(1);

	    int result = wishlistDAOImp.removeFromWishlist(productId, customerId);

	    assertEquals(result, expectedResult);

	    verify(jdbcTemplate, times(1)).update(eq(delete_slam_wishlist), eq(customerId), eq(productId));
	}

	@Test
	public void testGetWishlistProds() {
	    int customerId = 1;

	    List<ProductStockPrice> expectedProducts = new ArrayList<>();
	    expectedProducts.add(new ProductStockPrice(/* provide necessary parameters */));

	    when(jdbcTemplate.query(eq(select_slam_wishlist), any(WishlistRowMapper.class), eq(customerId))).thenReturn(expectedProducts);

	    List<ProductStockPrice> result = wishlistDAOImp.getWishlistProds(customerId);

	    assertEquals(result.size(), expectedProducts.size());

	    verify(jdbcTemplate, times(1)).query(anyString(), any(WishlistRowMapper.class), eq(customerId));
	}


}
