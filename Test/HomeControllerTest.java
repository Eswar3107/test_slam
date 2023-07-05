package Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpSession;
import eStoreProduct.DAO.*;
import eStoreProduct.controller.*;
import eStoreProduct.model.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class HomeControllerTest {

    @Mock
    private customerDAO cdao;

    @Mock
    private cartDAO cd;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private homeController HomeController;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetHomePage() {
        // Arrange

        // Act
        String result = HomeController.getHomePage(model);

        // Assert
        assertEquals("home", result);
    }

    @Test
    public void testGetHomeFoeLoggedUser() {
        // Arrange

        // Act
        String result = HomeController.getHomeFoeLoggedUser(model);

        // Assert
        verify(model).addAttribute("fl", true);
        assertEquals("home", result);
    }

    @Test
    public void testGetSignUpPage() {
        // Arrange

        // Act
        String result = HomeController.getSignUpPage(model);

        // Assert
        assertEquals("signUp", result);
    }

    @Test
    public void testGetSignInPage() {
        // Arrange

        // Act
        String result = HomeController.getSignInPage(model);

        // Assert
        assertEquals("signIn", result);
    }

    @Test
    public void testCreateAccount_SuccessfulCreation() {
        // Arrange
        custCredModel ccm = new custCredModel();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(cdao.createCustomer(ccm)).thenReturn(true);

        // Act
        String result = HomeController.createAccount(ccm, model);

        // Assert
        verify(model).addAttribute("customer", ccm);
        assertEquals("createdMsg", result);
    }

    @Test
    public void testCreateAccount_FailedCreation() {
        // Arrange
        custCredModel ccm = new custCredModel();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(cdao.createCustomer(ccm)).thenReturn(false);

        // Act
        String result = HomeController.createAccount(ccm, model);

        // Assert
        assertEquals("createdMsg", result);
    }

    @Test
    public void testUserLogout() {
        // Arrange

        // Act
        String result = HomeController.userlogout(model, session);

        // Assert
        verify(model).addAttribute("fl", false);
        verify(model).addAttribute("customer", null);
        verify(session).invalidate();
        assertEquals("home", result);
    }
}
