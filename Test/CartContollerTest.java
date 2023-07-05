package Test;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import javax.servlet.http.HttpSession;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.testng.annotations.BeforeMethod;
import java.util.*;
import eStoreProduct.DAO.cartDAO;
import eStoreProduct.DAO.ProductDAO;
import eStoreProduct.DAO.ServicableRegionDAO;
import eStoreProduct.DAO.customerDAO;
import eStoreProduct.controller.CartController;
import eStoreProduct.model.cartModel;
import eStoreProduct.model.custCredModel;
import eStoreProduct.utility.*;
import eStoreProduct.BLL.*;

@Test
class CartControllerTest {
  
  @Mock
  private cartDAO cartDAO;
  
  @Mock
  private ProductDAO productDAO;
  
  @Mock
  private customerDAO customerDAO;
  
  @InjectMocks
  private CartController cartController;
  
  @Mock
  private Model model;
  
  @Mock
  private HttpSession session;
  @Mock
  private FairandGStBLL fairandGStBLL;
  @Mock
  private ServicableRegionDAO servicableRegionDAO;
  
  @BeforeMethod
  void setup() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
void testAddToCart_WithCustomer() throws SQLException {
    // Arrange
    int productId = 123;
    String expectedResponse = "Added to cart";
    custCredModel customer = new custCredModel();
    customer.setCustId(1);
    
    when(session.getAttribute("customer")).thenReturn(customer);
    when(cartDAO.addToCart(productId, customer.getCustId())).thenReturn(expectedResponse);
    
    // Act
    String actualResponse = cartController.addToCart(productId, model, session);
    
    // Assert
    AssertJUnit.assertEquals(expectedResponse, actualResponse);
    verify(cartDAO).addToCart(productId, customer.getCustId());
  }
  
  @Test
void testAddToCart_WithoutCustomer() throws SQLException {
    // Arrange
    int productId = 123;
    String expectedResponse = "Added to cart";
    
    when(session.getAttribute("customer")).thenReturn(null);
    when(productDAO.getProductById(productId)).thenReturn(mock(ProductStockPrice.class));
    
    // Act
    String actualResponse = cartController.addToCart(productId, model, session);
    
    // Assert
    AssertJUnit.assertEquals(expectedResponse, actualResponse);
    verify(model).addAttribute("alist", cartController.alist);
  }
  
  @Test
void testCartDisplay_WithCustomer() {
    // Arrange
    custCredModel customer = new custCredModel();
    customer.setCustId(1);
    List<ProductStockPrice> products = new ArrayList<>();
    double cartCost = 10.0;
    
    when(session.getAttribute("customer")).thenReturn(customer);
    when(cartDAO.getCartProds(customer.getCustId())).thenReturn(products);
    when(fairandGStBLL.getCartCost(customer.getCustId())).thenReturn(cartCost);
    
    // Act
    String viewName = cartController.cartdisplay(model, session);
    
    // Assert
    AssertJUnit.assertEquals("cart", viewName);
    verify(model).addAttribute("products", products);
    verify(model).addAttribute("cartcost", cartCost);
    verify(model).addAttribute("cust", customer);
  }
  
  @Test
void testCartDisplay_WithoutCustomer() {
    // Arrange
    List<ProductStockPrice> productList = new ArrayList<>();
    double cartCost = 10.0;
    
    when(session.getAttribute("customer")).thenReturn(null);
    when(fairandGStBLL.getCartCost(cartController.alist)).thenReturn(cartCost);
    
    // Act
    String viewName = cartController.cartdisplay(model, session);
    
    // Assert
    AssertJUnit.assertEquals("cart", viewName);
    verify(model).addAttribute("products", productList);
    verify(model).addAttribute("cartcost", cartCost);
    verify(model).addAttribute("alist", cartController.alist);
  }
  
  
  @Test
void testRemoveFromCart_WithCustomer() throws SQLException {
    // Arrange
    int productId = 123;
    custCredModel customer = new custCredModel();
    customer.setCustId(1);
    
    when(session.getAttribute("customer")).thenReturn(customer);
    
    // Act
    String actualResponse = cartController.removeFromCart(productId, model, session);
    
    // Assert
    AssertJUnit.assertEquals("Removed from cart", actualResponse);
    verify(cartDAO).removeFromCart(productId, customer.getCustId());
  }
  
  @Test
void testRemoveFromCart_WithoutCustomer() throws NumberFormatException, SQLException {
    // Arrange
    int productId = 123;
    
    when(session.getAttribute("customer")).thenReturn(null);
    
    // Act
    String actualResponse = cartController.removeFromCart(productId, model, session);
    
    // Assert
    AssertJUnit.assertEquals("Removed from cart", actualResponse);
  }
  
  @Test
void testUpdateQuantity_WithCustomer() throws SQLException {
    // Arrange
    int productId = 123;
    int quantity = 2;
    custCredModel customer = new custCredModel();
    customer.setCustId(1);
    List<ProductStockPrice> products = new ArrayList<>();
    double cartCost = 10.0;
    
    when(session.getAttribute("customer")).thenReturn(customer);
    when(cartDAO.getCartProds(customer.getCustId())).thenReturn(products);
    when(fairandGStBLL.getCartCost(customer.getCustId())).thenReturn(cartCost);
    
    // Act
    String actualResponse = cartController.updateQuantity(productId, quantity, model, session);
    
    // Assert
    AssertJUnit.assertEquals(String.valueOf(cartCost), actualResponse);
    verify(cartDAO).updateQty(new cartModel(customer.getCustId(), productId, quantity));
    verify(session).setAttribute("products", products);
  }
  
  @Test
void testUpdateQuantity_WithoutCustomer() throws SQLException {
    // Arrange
    int productId = 123;
    int quantity = 2;
    List<ProductStockPrice> productList = new ArrayList<>();
    double cartCost = 10.0;
    
    when(session.getAttribute("customer")).thenReturn(null);
    when(fairandGStBLL.getCartCost(cartController.alist)).thenReturn(cartCost);
    
    // Act
    String actualResponse = cartController.updateQuantity(productId, quantity, model, session);
    
    // Assert
    AssertJUnit.assertEquals(String.valueOf(cartCost), actualResponse);
  }
  
  @Test
void testUpdateCostOnLoad_WithCustomer() throws SQLException {
    // Arrange
    custCredModel customer = new custCredModel();
    customer.setCustId(1);
    List<ProductStockPrice> products = new ArrayList<>();
    double cartCost = 10.0;
    
    when(session.getAttribute("customer")).thenReturn(customer);
    when(cartDAO.getCartProds(customer.getCustId())).thenReturn(products);
    when(fairandGStBLL.getCartCost(customer.getCustId())).thenReturn(cartCost);
    
    // Act
    String actualResponse = cartController.updateCostOnLoad(model, session);
    
    // Assert
    AssertJUnit.assertEquals(String.valueOf(cartCost), actualResponse);
    verify(session).setAttribute("products", products);
  }
  
  @Test
void testUpdateCostOnLoad_WithoutCustomer() throws SQLException {
    // Arrange
    List<ProductStockPrice> productList = new ArrayList<>();
    double cartCost = 10.0;
    
    when(session.getAttribute("customer")).thenReturn(null);
    when(fairandGStBLL.getCartCost(cartController.alist)).thenReturn(cartCost);
    
    // Act
    String actualResponse = cartController.updateCostOnLoad(model, session);
    
    // Assert
    AssertJUnit.assertEquals(String.valueOf(cartCost), actualResponse);
  }
  
  @Test
void testCheckPincodeValidity() throws SQLException {
    // Arrange
    String pincode = "534213";
    Boolean pincodeValidity = true;
    
    when(servicableRegionDAO.getValidityOfPincode(Integer.parseInt(pincode))).thenReturn(pincodeValidity);
    
    // Act
    String actualResponse = cartController.checkPincodeValidity(pincode, model, session);
    
    // Assert
    AssertJUnit.assertEquals(String.valueOf(pincodeValidity), actualResponse);
  }








}
  


