package DAOTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eStoreProduct.DAO.ProdStockDAO;
import eStoreProduct.DAO.ProductDAOImp;
import eStoreProduct.model.Product;
import eStoreProduct.model.Category;
import eStoreProduct.model.CategoryRowMapper;
import eStoreProduct.model.ProductRowMapper;
import eStoreProduct.utility.ProductStockPrice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProductDAOImpTest {

    @Mock
    private JdbcTemplate jdbcTemplate;
    
    @Mock
    private DataSource dataSource;

    @Mock
    private ProdStockDAO prodStockDAO;

    @InjectMocks
    private ProductDAOImp productDAO;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        productDAO = new ProductDAOImp(dataSource, prodStockDAO);
        productDAO.jdbcTemplate = jdbcTemplate;
    }
    private final String SQL_INSERT_PRODUCT = "insert into slam_products(prod_id, prod_title, prod_prct_id, prod_gstc_id, prod_brand, image_url, prod_desc, reorderlevel)  values(?, ?, ?, ?, ?, ?, ?, ?)";
	private final String SQL_GET_TOP_PRODID = "select prod_id from slam_products order by prod_id desc limit 1";
	private String get_products_by_catg = "select p.prod_id, p.prod_title, p.prod_brand, p.image_url, p.prod_desc, ps.prod_price FROM slam_Products p, slam_productstock ps where p.prod_id = ps.prod_id and p.prod_prct_id = ?";
	private String products_query = "SELECT p.prod_id, p.prod_title, p.prod_brand, p.image_url, p.prod_desc, ps.prod_price FROM slam_Products p, slam_productstock ps where p.prod_id = ps.prod_id";
	private String prdt_catg = "SELECT * FROM slam_ProductCategories";
	//private String get_prd = "SELECT p.*, ps.prod_price,ps.prod_mrp FROM slam_Products p,slam_productstock ps where p.prod_id = ps.prod_id and ps.prod_id=?";
	private String get_prd="SELECT p.prod_id, p.prod_title, p.prod_brand, p.image_url, p.prod_desc,p.prod_gstc_id, ps.prod_price FROM slam_Products p, slam_productstock ps where p.prod_id = ps.prod_id and ps.prod_id=?";
	 @Test
	    public void testCreateProduct_Success() {
	        // Mock the behavior of the JdbcTemplate queryForObject method
	        when(jdbcTemplate.queryForObject(eq(SQL_GET_TOP_PRODID), eq(int.class))).thenReturn(10);

	        // Mock the behavior of the JdbcTemplate update method
	        when(jdbcTemplate.update(
	                eq(SQL_INSERT_PRODUCT),
	                anyInt(),
	                anyString(),
	                anyInt(),
	                anyInt(),
	                anyString(),
	                anyString(),
	                anyString(),
	                anyInt()
	        )).thenReturn(1);

	        // Create a sample product
	        Product product = new Product();
	        product.setProd_title("Sample Product");
	        product.setProd_gstc_id(123);
	        product.setProd_brand("Brand");
	        product.setImage_url("sample_image.png");
	        product.setProd_desc("Sample description");
	        product.setReorderLevel(10);

	        // Call the createProduct method
	        boolean result = productDAO.createProduct(product);

	        // Verify that the result is true
	        Assert.assertTrue(result);

	        // Verify that the JdbcTemplate queryForObject method is called with the expected arguments
	        verify(jdbcTemplate).queryForObject(eq(SQL_GET_TOP_PRODID), eq(int.class));

	        // Verify that the JdbcTemplate update method is called with the expected arguments
	        verify(jdbcTemplate).update(
	                eq(SQL_INSERT_PRODUCT),
	                anyInt(),
	                anyString(),
	                anyInt(),
	                anyInt(),
	                anyString(),
	                anyString(),
	                anyString(),
	                anyInt()
	        );
	    }

	    @Test
	    public void testGetProductsByCategory() {
	        // Mock the behavior of the JdbcTemplate query method
	        List<ProductStockPrice> expectedProducts = new ArrayList<>();
	        expectedProducts.add(new ProductStockPrice(prodStockDAO));
	        expectedProducts.add(new ProductStockPrice(prodStockDAO));
	        when(jdbcTemplate.query(
	                eq(get_products_by_catg),
	                any(ProductRowMapper.class),
	                eq(123)
	        )).thenReturn(expectedProducts);

	        // Call the getProductsByCategory method
	        List<ProductStockPrice> actualProducts = productDAO.getProductsByCategory(123);

	        // Verify that the actual products list matches the expected products list
	        Assert.assertEquals(actualProducts, expectedProducts);

	        // Verify that the JdbcTemplate query method is called with the expected arguments
	        verify(jdbcTemplate).query(
	                eq(get_products_by_catg),
	                any(ProductRowMapper.class),
	                eq(123)
	        );
	    }
	    @Test
	    public void testGetAllProducts() {
	        // Mock the behavior of the JdbcTemplate query method
	        List<ProductStockPrice> expectedProducts = new ArrayList<>();
	        expectedProducts.add(new ProductStockPrice(prodStockDAO));
	        expectedProducts.add(new ProductStockPrice(prodStockDAO));
	        when(jdbcTemplate.query(
	                eq(products_query),
	                any(ProductRowMapper.class)
	        )).thenReturn(expectedProducts);

	        // Call the getAllProducts method
	        List<ProductStockPrice> actualProducts = productDAO.getAllProducts();

	        // Verify that the actual products list matches the expected products list
	        Assert.assertEquals(actualProducts, expectedProducts);

	        // Verify that the JdbcTemplate query method is called with the expected arguments
	        verify(jdbcTemplate).query(
	                eq(products_query),
	                any(ProductRowMapper.class)
	        );
	    }
	    @Test
	    public void testGetAllCategories() {
	        // Mock the behavior of the JdbcTemplate query method
	        List<Category> expectedCategories = new ArrayList<>();
	        expectedCategories.add(new Category(1, "Category 1","1"));
	        expectedCategories.add(new Category(2, "Category 2","2"));
	        when(jdbcTemplate.query(
	                eq(prdt_catg),
	                any(CategoryRowMapper.class)
	        )).thenReturn(expectedCategories);

	        // Call the getAllCategories method
	        List<Category> actualCategories = productDAO.getAllCategories();

	        // Verify that the actual categories list matches the expected categories list
	        Assert.assertEquals(actualCategories, expectedCategories);

	        // Verify that the JdbcTemplate query method is called with the expected arguments
	        verify(jdbcTemplate).query(
	                eq(prdt_catg),
	                any(CategoryRowMapper.class)
	        );
	    }
	    @Test
	    public void testGetProductById() {
	        // Mock the behavior of the JdbcTemplate query method
	        List<ProductStockPrice> expectedProducts = new ArrayList<>();
	        expectedProducts.add(new ProductStockPrice(prodStockDAO));
	        expectedProducts.add(new ProductStockPrice(prodStockDAO));
	        when(jdbcTemplate.query(
	                eq(get_prd),
	                any(ProductRowMapper.class),
	                eq(1) // productId argument
	        )).thenReturn(expectedProducts);

	        // Call the getProductById method
	        ProductStockPrice actualProduct = productDAO.getProductById(1);

	        // Verify that the actual product matches the expected product
	        Assert.assertEquals(actualProduct, expectedProducts.get(0));

	        // Verify that the JdbcTemplate query method is called with the expected arguments
	        verify(jdbcTemplate).query(
	                eq(get_prd),
	                any(ProductRowMapper.class),
	                eq(1) // productId argument
	        );
	    }
	    @Test
	    public void testSortProductsByPrice_LowToHigh() {
	        // Create a list of products with different prices
	        List<ProductStockPrice> productList = new ArrayList<>();
	        productList.add(new ProductStockPrice(prodStockDAO));
	      
	        // Sort the products by price in low to high order
	        List<ProductStockPrice> sortedList = productDAO.sortProductsByPrice(productList, "lowToHigh");

	        // Verify that the products are sorted in low to high order based on price
	        Assert.assertEquals(sortedList.get(0).getPrice(), 5.0);
	        Assert.assertEquals(sortedList.get(1).getPrice(), 10.0);
	        Assert.assertEquals(sortedList.get(2).getPrice(), 20.0);
	    }
	    @Test
	    public void testSortProductsByPrice_HighToLow() {
	        // Create a list of products with different prices
	    	  List<ProductStockPrice> products = new ArrayList<>();
	          products.add((new ProductStockPrice(prodStockDAO)));
//	          products.add(new ProductStockPrice("Product 2", 20.0));
//	          products.add(new ProductStockPrice("Product 3", 30.0));
//	          products.add(new ProductStockPrice("Product 4", 40.0));

	        // Sort the products by price in high to low order
	        List<ProductStockPrice> sortedList = productDAO.sortProductsByPrice(products, "highToLow");

	        // Verify that the products are sorted in high to low order based on price
	        Assert.assertEquals(sortedList.get(0).getPrice(), 20.0);
	        Assert.assertEquals(sortedList.get(1).getPrice(), 10.0);
	        Assert.assertEquals(sortedList.get(2).getPrice(), 5.0);
	    }
	    @Test
	    public void testFilterProductsByPriceRange() {
	        // Create a list of products with different prices
	        List<ProductStockPrice> productList = new ArrayList<>();
	        productList.add(new ProductStockPrice(prodStockDAO));
			/*
			 * productList.add(new ProductStockPrice(2, "Product 2", "Brand 2",
			 * "image2.jpg", "Description 2", 2, 20.0)); productList.add(new
			 * ProductStockPrice(3, "Product 3", "Brand 3", "image3.jpg", "Description 3",
			 * 3, 5.0));
			 */

	        // Define the minimum and maximum price range
	        double minPrice = 5.0;
	        double maxPrice = 15.0;

	        // Filter the products by price range
	        List<ProductStockPrice> filteredList = productDAO.filterProductsByPriceRange(productList, minPrice, maxPrice);

	        // Verify that only the products within the specified price range are returned
	        Assert.assertEquals(filteredList.size(), 2);
	        Assert.assertEquals(filteredList.get(0).getPrice(), 10.0);
	        Assert.assertEquals(filteredList.get(1).getPrice(), 5.0);
	    }
	    @Test
	    public void testIsPincodeValid() {
	        // Define a valid pincode
	        int validPincode = 12345;

	        // Define an invalid pincode
	        int invalidPincode = 99999;

	        // Test with a valid pincode
	        String query = "SELECT COUNT(*) FROM slam_regions WHERE ? BETWEEN region_pin_from AND region_pin_to";
	        when(jdbcTemplate.queryForObject(eq(query), eq(Integer.class), eq(validPincode))).thenReturn(1);
	        boolean isValid = productDAO.isPincodeValid(validPincode);
	        Assert.assertTrue(isValid);

	        // Test with an invalid pincode
	        when(jdbcTemplate.queryForObject(eq(query), eq(Integer.class), eq(invalidPincode))).thenReturn(0);
	        boolean isInvalid = productDAO.isPincodeValid(invalidPincode);
	        Assert.assertFalse(isInvalid);
	    }

	    @Test
	    public void testGetProductGstcId() {
	        // Define a product ID
	        int productId = 123;

	        // Define the expected GSTC ID
	        int expectedGstcId = 456;

	        // Mock the queryForObject method to return the expected GSTC ID
	        String sql = "SELECT prod_gstc_id FROM slam_products WHERE prod_id = ?";
	        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{productId}), eq(Integer.class))).thenReturn(expectedGstcId);

	        // Call the method under test
	        int actualGstcId = productDAO.getproductgstcid(productId);

	        // Verify the result
	        Assert.assertEquals(actualGstcId, expectedGstcId);
	    }


}
