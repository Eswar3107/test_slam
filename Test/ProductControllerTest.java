package Test;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.util.Arrays;

import javax.servlet.http.HttpSession;

import java.util.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import eStoreProduct.DAO.ProductDAO;
import eStoreProduct.model.Category;
import eStoreProduct.model.custCredModel;
import eStoreProduct.utility.ProductStockPrice;
import eStoreProduct.controller.*;

import static org.mockito.Mockito.*;

public class ProductControllerTest {
  
  @Mock
  private ProductDAO productDAO;

  @Mock
  private Model model;
  @Mock
  private HttpSession session;

  @InjectMocks
  private ProductController productController;

@BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

@Test
public void testDisplayCategories() {
  // Mock data
  List<Category> categories = Arrays.asList(
    new Category(1, "Category 1", "catgeory 1"),
    new Category(2, "Category 2", "category 2")
  );

  when(productDAO.getAllCategories()).thenReturn(categories);

  // Test the method
  String result = productController.displayCategories(model);

  // Verify the result
  verify(productDAO).getAllCategories();
  StringBuilder expectedHtmlContent = new StringBuilder();
  expectedHtmlContent.append("<option disabled selected>Select Product category</option>");
  for (Category category : categories) {
    expectedHtmlContent.append("<option value='").append(category.getPrct_id()).append("'>")
        .append(category.getPrct_title()).append("</option>");
  }
  Assert.assertEquals(result, expectedHtmlContent.toString());
  
  
  }

  @Test
  public void testShowCategoryProducts() {
    // Mock data
    int categoryId = 1;
    List<ProductStockPrice> products =new ArrayList<>();
    ProductStockPrice p1=new ProductStockPrice(null);
    p1.setProd_id(1);
    p1.setProd_title("Iphone13");
    p1.setPrice(65000);
    products.add(p1);
    ProductStockPrice p2=new ProductStockPrice(null);
    p2.setProd_id(2);
    p2.setProd_title("vivo");
    p2.setPrice(5000);
    products.add(p2);
    
    

    when(productDAO.getProductsByCategory(categoryId)).thenReturn(products);

    // Test the method
    String result = productController.showCategoryProducts(categoryId, model);
    verify(productDAO).getProductsByCategory(categoryId);
    verify(model).addAttribute("products", products);
    Assert.assertEquals(result, "productCatalog");
  }

  @Test
  public void testShowAllProducts() {
    // Mock data
	  List<ProductStockPrice> products =new ArrayList<>();
	    ProductStockPrice p1=new ProductStockPrice(null);
	    p1.setProd_id(1);
	    p1.setProd_title("Iphone13");
	    p1.setPrice(65000);
	    products.add(p1);
	    ProductStockPrice p2=new ProductStockPrice(null);
	    p2.setProd_id(2);
	    p2.setProd_title("vivo");
	    p2.setPrice(5000);
	    products.add(p2);
	    

    when(productDAO.getAllProducts()).thenReturn(products);

    // Test the method
    String result = productController.showAllProducts(model);

    verify(productDAO).getAllProducts();
    verify(model).addAttribute("products", products);
    Assert.assertEquals(result, "productCatalog");
  }

  @Test
  public void testGetSignUpPage() {
    // Mock data
    int productId = 1;
    ProductStockPrice product = new ProductStockPrice(null);
    // Set the properties of the product
    product.setProd_id(productId);
    // ...

    when(productDAO.getProductById(productId)).thenReturn(product);
    when(session.getAttribute("customer")).thenReturn(new custCredModel());

    // Test the method
    String result = productController.getSignUpPage(productId, model, session);

    // Verify the result
    verify(productDAO).getProductById(productId);
    verify(model).addAttribute("oneproduct", product);
    verify(model).addAttribute("cust", any(custCredModel.class));
    Assert.assertEquals(result, "prodDescription");
  }

  @Test
  public void testShowProductDetails() {
    // Mock data
    int productId = 1;
    ProductStockPrice product = new ProductStockPrice(null);
    product.setProd_id(productId);
   product.setProd_title("vivo");
   product.setPrice(10000);
    when(productDAO.getProductById(productId)).thenReturn(product);

    // Test the method
    String result = productController.showProductDetails(productId, model);

    // Verify the result
    verify(productDAO).getProductById(productId);
    verify(model).addAttribute("product", product);
    Assert.assertEquals(result, "productDetails");
  }
  
  @Test
  public void testSortProducts() {
    // Mock data
    String sortOrder = "lowToHigh";
    List<ProductStockPrice> productList = new ArrayList<>();
    ProductStockPrice p1=new ProductStockPrice(null);
    p1.setProd_id(1);
    p1.setProd_title("Iphone13");
    p1.setPrice(65000);
    productList.add(p1);
    ProductStockPrice p2=new ProductStockPrice(null);
    p2.setProd_id(2);
    p2.setProd_title("vivo");
    p2.setPrice(5000);
    productList.add(p2);

    when(productDAO.getAllProducts()).thenReturn(productList);

    // Test the method
    String result = productController.sortProducts(sortOrder, model);

    // Verify the result
    verify(productDAO).getAllProducts();
    verify(model).addAttribute(eq("products"), anyList());
    Assert.assertEquals(result, "productCatalog");
  }

  @Test
  public void testGetFilteredProducts() {
    // Mock data
    String priceRange = "1000-2000";
    double minPrice = 1000.0;
    double maxPrice = 2000.0;
    List<ProductStockPrice> productList = new ArrayList<>();
    ProductStockPrice p1=new ProductStockPrice(null);
    p1.setProd_id(1);
    p1.setProd_title("Iphone13");
    p1.setPrice(65000);
    productList.add(p1);
    ProductStockPrice p2=new ProductStockPrice(null);
    p2.setProd_id(2);
    p2.setProd_title("vivo");
    p2.setPrice(5000);
    productList.add(p2);

    when(productDAO.getAllProducts()).thenReturn(productList);

    // Test the method
    String result = productController.getFilteredProducts(priceRange, model);

    // Verify the result
    verify(productDAO).getAllProducts();
    verify(model).addAttribute(eq("products"), anyList());
    Assert.assertEquals(result, "productCatalog");
  }
  @Test
  public void testsearchProducts() {  
	  String search="phones";
	  List<ProductStockPrice> productList = new ArrayList<>();
	    ProductStockPrice p1=new ProductStockPrice(null);
	    p1.setProd_id(1);
	    p1.setProd_title("Iphone13");
	    p1.setPrice(65000);
	    productList.add(p1);
	    ProductStockPrice p2=new ProductStockPrice(null);
	    p2.setProd_id(2);
	    p2.setProd_title("vivo");
	    p2.setPrice(5000);
	    productList.add(p2);

	    when(productDAO.searchproducts(search)).thenReturn(productList);
	    String result = productController.searchProducts(search, model);

	    verify(productDAO).searchproducts(search);
	    verify(model).addAttribute("products", productList);
	    Assert.assertEquals(result, "productCatalog");
	  }
  }
  


