package Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

import eStoreProduct.DAO.WishlistDAO;
import eStoreProduct.controller.WishlistController;
import eStoreProduct.model.custCredModel;
import eStoreProduct.utility.ProductStockPrice;

public class WishlistControllerTest {
  
  
  @Mock
  private WishlistDAO wishlistDAO;
  
  @InjectMocks
  @Spy
  private WishlistController wishlistController;
  
  @Mock
  private Model model;
  
  @Mock
  private HttpSession session;
  
  @BeforeMethod
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void testAddToWishlist() throws SQLException {
    // Arrange
    int productId = 123;
    custCredModel customer = new custCredModel();
    customer.setCustId(1);
    
    when(session.getAttribute("customer")).thenReturn(customer);
    
    // Act
    String actualResponse = wishlistController.addToWishlist(productId, model, session);
    
    // Assert
    assertEquals("Item added to wishlist", actualResponse);
    verify(wishlistDAO).addToWishlist(productId, customer.getCustId());
  }
  
  @Test
  public void testRemoveFromWishlist() throws SQLException {
    // Arrange
    int productId = 123;
    custCredModel customer = new custCredModel();
    customer.setCustId(1);
    
    when(session.getAttribute("customer")).thenReturn(customer);
    
    // Act
    String actualResponse = wishlistController.removeFromWishlist(productId, model, session);
    
    // Assert
    assertEquals("Item removed from wishlist", actualResponse);
    verify(wishlistDAO).removeFromWishlist(productId, customer.getCustId());
  }
  
  @Test
  public void testUserWishlistItems() throws SQLException {
    // Arrange
    custCredModel customer = new custCredModel();
    customer.setCustId(1);
    List<ProductStockPrice> products = new ArrayList<>();
    
    when(session.getAttribute("customer")).thenReturn(customer);
    when(wishlistDAO.getWishlistProds(customer.getCustId())).thenReturn(products);
    
    // Act
    String actualViewName = wishlistController.userWishlistItems(model, session);
    
    // Assert
    assertEquals("wishlistCatalog", actualViewName);
    verify(wishlistDAO).getWishlistProds(customer.getCustId());
    verify(model).addAttribute("products", products);
  }
}

