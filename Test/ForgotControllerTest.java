package Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import eStoreProduct.controller.*;
import eStoreProduct.DAO.customerDAO;
import eStoreProduct.model.custCredModel;
import eStoreProduct.model.emailSend;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class ForgotControllerTest {

    @Mock
    private customerDAO cdao;

    @Mock
    private Model model;

    @InjectMocks
    private ForgotController forgotController;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testForgotPassword() {
        // Arrange

        // Act
        String result = forgotController.forgotPassword(model);

        // Assert
        assertEquals("forgotPage", result);
    }

    @Test
    public void testVerifyEmail_EmailExists() {
        // Arrange
        String email = "test@example.com";
        custCredModel customer = new custCredModel();
        when(cdao.getCustomerByEmail(email)).thenReturn(customer);

        // Act
        String result = forgotController.verifyEmail(email);

        // Assert
        assertEquals("yes", result);
    }

    @Test
    public void testVerifyEmail_EmailDoesNotExist() {
        // Arrange
        String email = "test@example.com";
        when(cdao.getCustomerByEmail(email)).thenReturn(null);

        // Act
        String result = forgotController.verifyEmail(email);

        // Assert
        assertEquals("no", result);
    }

    @Test
    public void testSendOTP() {
        // Arrange
        String email = "test@example.com";
        String generatedOTP = "123456";
        when((new emailSend()).sendEmail(email)).thenReturn(generatedOTP);

        // Act
        String result = forgotController.sendOTP(email);

        // Assert
        assertEquals(generatedOTP, result);
    }

    @Test
    public void testValidateOTP_ValidOTPSubmittedWithinTime() {
        // Arrange
        String otp = "123456";
        when(java.time.LocalDateTime.now()).thenReturn(java.time.LocalDateTime.of(2023, 1, 1, 12, 0));
        forgotController.sendOTP("test@example.com");

        // Act
        String result = forgotController.validateOTP(otp);

        // Assert
        assertEquals("valid", result);
    }

    @Test
    public void testValidateOTP_ValidOTPSubmittedAfterTimeLimit() {
        // Arrange
        String otp = "123456";
        when(java.time.LocalDateTime.now()).thenReturn(java.time.LocalDateTime.of(2023, 1, 1, 12, 0));
        forgotController.sendOTP("test@example.com");
        when(java.time.LocalDateTime.now()).thenReturn(java.time.LocalDateTime.of(2023, 1, 1, 12, 1));

        // Act
        String result = forgotController.validateOTP(otp);

        // Assert
        assertEquals("no", result);
    }

    @Test
    public void testValidateOTP_InvalidOTP() {
        // Arrange
        String otp = "654321";
        when(java.time.LocalDateTime.now()).thenReturn(java.time.LocalDateTime.of(2023, 1, 1, 12, 0));
        forgotController.sendOTP("test@example.com");

        // Act
        String result = forgotController.validateOTP(otp);

        // Assert
        assertEquals("invalid", result);
    }

    @Test
    public void testUpdateUserNewPassword() {
        // Arrange
        String newPassword = "newpassword";
        String finalEmail = "test@example.com";

        // Act
        String result = forgotController.updateUserNewPassword(newPassword, model);

        // Assert
        verify(cdao).updatePassword(newPassword, finalEmail);
        assertEquals("signIn", result);
    }
}
