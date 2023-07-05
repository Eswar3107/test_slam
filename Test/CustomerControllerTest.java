package Test;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import eStoreProduct.BLL.FairandGStBLL;
import eStoreProduct.BLL.OrderIdCreationBLL;
import eStoreProduct.BLL.WalletCalculationBLL;
import eStoreProduct.DAO.OrderDAO;
import eStoreProduct.DAO.ProductDAO;
import eStoreProduct.DAO.StockUpdaterDAO;
import eStoreProduct.DAO.cartDAO;
import eStoreProduct.DAO.customerDAO;
import eStoreProduct.DAO.*;
import eStoreProduct.controller.CustomerController;
import eStoreProduct.model.custCredModel;
import eStoreProduct.model.orderModel;
import eStoreProduct.model.wallet;
import eStoreProduct.utility.ProductStockPrice;
import javax.servlet.http.HttpSession;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.*;


class CustomerControllerTest {

    @Mock
    private cartDAO cartDAO;

    @Mock
    private customerDAO customerDAO;

    @Mock
    private StockUpdaterDAO stockUpdaterDAO;

    @Mock
    private orderModel orderModel;

    @Mock
    private OrderIdCreationBLL orderIdCreationBLL;

    @Mock
    private FairandGStBLL fairandGStBLL;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private walletDAO walletDAO;

    @Mock
    private WalletCalculationBLL walletCalculationBLL;
    @Mock
    private HttpSession session;
   
    

    @Mock
    private Model model;
    
  
    @InjectMocks
    private CustomerController customerController;

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testSendProfilePage() {
        // Mock customer information in the session
        custCredModel customer = new custCredModel();
        when(session.getAttribute("customer")).thenReturn(customer);

        // Call the method
        String result = customerController.sendProfilePage(model, session);

        // Verify that the customer information was added to the model
        verify(model).addAttribute("cust", customer);
        // Verify the returned view name
        AssertJUnit.assertEquals("profile", result);
    }

    void userUpdate() {
        // Mock customer information and Model
        custCredModel cust = new custCredModel();
        Model model = mock(Model.class);
        session.setAttribute("customer", cust);

        // Mock updated customer information
        custCredModel updatedCust = new custCredModel();
        when(customerDAO.getCustomerById(cust.getCustId())).thenReturn(updatedCust);

        // Call the method
        String result = customerController.userupdate(cust, model, session);

        // Verify that the customer information was updated in the database
        verify(customerDAO).updatecustomer(cust);
        // Verify that the updated customer information was added to the model
        verify(model).addAttribute("cust", updatedCust);
        // Verify the returned view name
        AssertJUnit.assertEquals("profile", result);
    }

    @Test
    public void testConfirmBuyCart_WithValidCustomer() {
        // Mock customer information in the session
        custCredModel customer = new custCredModel();
        when(session.getAttribute("customer")).thenReturn(customer);

        // Mock BLL method
        doNothing().when(fairandGStBLL).calculateTotalfair(customer);

        // Mock BLL method
        List<ProductStockPrice> products = new ArrayList<>();
        when(fairandGStBLL.GetQtyItems2()).thenReturn(products);

        // Mock DAO method
        wallet Wallet = new wallet();
        when(walletDAO.getWalletAmount(customer.getCustId())).thenReturn(Wallet);

        // Call the method
        String result = customerController.confirmbuycart(model, session);

        // Verify that the necessary methods were called
        verify(fairandGStBLL).calculateTotalfair(customer);
        verify(fairandGStBLL).GetQtyItems2();
        verify(model).addAttribute("products", products);
        verify(walletDAO).getWalletAmount(customer.getCustId());

        // Verify the returned view name
        AssertJUnit.assertEquals("paymentpreview", result);
    }

    @Test
    public void testConfirmBuyCart_WithInvalidCustomer() {
        // Set the customer in the session as null
        when(session.getAttribute("customer")).thenReturn(null);

        // Call the method
        String result = customerController.confirmbuycart(model, session);

        // Verify that the necessary methods were not called
        verifyZeroInteractions(fairandGStBLL);
        verifyZeroInteractions(model);
        verifyZeroInteractions(walletDAO);

        // Verify the returned view name
        AssertJUnit.assertEquals("signIn", result);
    }
    
    @Test
    public void testGetOrderId() {
        // Mock the amount parameter
        double amount = 100.0;

        // Mock BLL method
        String orderId = "1234567890";
        when(orderIdCreationBLL.createRazorpayOrder(amount)).thenReturn(orderId);

        // Call the method
        String result = customerController.getOrderId(amount, session);

        // Verify that the necessary methods were called
        verify(orderIdCreationBLL).createRazorpayOrder(amount);

        // Verify the returned order ID
        AssertJUnit.assertEquals(orderId, result);
    }
    
    @Test
    public void testHandleFormSubmission_ValidPincode() {
        // Mock the request parameters
        String name = "lucky";
        String caddress = "mvp Street";
        String pincode = "534409";
        HttpSession session = mock(HttpSession.class);

        // Mock the session attribute
        custCredModel cust = new custCredModel();
        when(session.getAttribute("customer")).thenReturn(cust);

        // Mock the DAO methods
        when(productDAO.isPincodeValid(Integer.parseInt(pincode))).thenReturn(true);
        when(customerDAO.updateShpimentDetails(cust)).thenReturn("Updated");

        // Call the method
        String result = customerController.handleFormSubmission(name, caddress, pincode, session);

        // Verify that the necessary methods were called
        verify(session).getAttribute("customer");
        verify(productDAO).isPincodeValid(Integer.parseInt(pincode));
        verify(customerDAO).updateShpimentDetails(cust);

        // Verify the returned result
        AssertJUnit.assertEquals("Valid", result);
    }
    
    @Test
    public void testInvoice_CartProducts() throws Exception {
        // Mock the request parameters
        String paymentReference = "payment123";
        String total = "100";
        HttpSession session = mock(HttpSession.class);
        orderModel om = new orderModel();

        // Mock the session attribute
        custCredModel cust1 = new custCredModel();
        when(session.getAttribute("customer")).thenReturn(cust1);

        // Mock the DAO methods
        wallet Wallet = new wallet();
        when(walletDAO.getWalletAmount(cust1.getCustId())).thenReturn(Wallet);
        double payamount = Double.parseDouble(total);
        double totalamount = ProductStockPrice.getTotal();
        double walletusedamount = totalamount - payamount;
        if (walletusedamount > 0) {
            double x = Wallet.getAmount() - walletusedamount;
            doNothing().when(walletDAO).updatewallet(x, cust1.getCustId());
        }
        List<ProductStockPrice> products = new ArrayList<>(); // Mock the list of products
        when(fairandGStBLL.GetQtyItems2()).thenReturn(products);
        double orderGST = 5.0; // Mock the order GST
        when(fairandGStBLL.getOrderGST(products)).thenReturn(orderGST);

        // Call the method
        String result = customerController.invoice(paymentReference, total, model, session, om, null, null);

        // Verify that the necessary methods were called
        verify(session).getAttribute("customer");
        verify(walletDAO).getWalletAmount(cust1.getCustId());
        verify(walletDAO).updatewallet(anyDouble(), eq(cust1.getCustId()));
        verify(fairandGStBLL).GetQtyItems2();
        verify(fairandGStBLL).getOrderGST(products);
        verify(orderDAO).insertIntoOrders(om, products);
        // Verify any additional necessary methods

        // Verify the returned result
        AssertJUnit.assertEquals("invoice", result);
    }
	

    @Test
    public void testHandleFormSubmission_InvalidPincode() {
        // Mock the request parameters
        String name = "king kalyan";
        String caddress = "power Street";
        String pincode = "530006";
        HttpSession session = mock(HttpSession.class);

        // Mock the session attribute
        custCredModel cust = new custCredModel();
        when(session.getAttribute("customer")).thenReturn(cust);

        // Mock the DAO method
        when(productDAO.isPincodeValid(Integer.parseInt(pincode))).thenReturn(false);

        // Call the method
        String result = customerController.handleFormSubmission(name, caddress, pincode, session);

        // Verify that the necessary methods were called
        verify(session).getAttribute("customer");
        verify(productDAO).isPincodeValid(Integer.parseInt(pincode));

        // Verify the returned result
        AssertJUnit.assertEquals("Not Valid", result);
    }
   
    
	

    @Test
    public void testCheckLoginOrNotWhenCustomerLoggedIn() throws NumberFormatException, SQLException {
        // Set up
        custCredModel cust = new custCredModel();

        when(session.getAttribute("customer")).thenReturn(cust);

        // Execute
        String result = customerController.buyproduct(null, session);

        // Verify
        assertEquals(result, "true");
    }

    @Test
    public void testCheckLoginOrNotWhenCustomerNotLoggedIn() throws NumberFormatException, SQLException {
        // Set up
        when(session.getAttribute("customer")).thenReturn(null);

        // Execute
        String result = customerController.buyproduct(null, session);

        // Verify
        assertEquals(result, "false");
    }

    @Test
    public void testWallet() throws NumberFormatException, SQLException {
        // Set up
        custCredModel cust = new custCredModel();

        int custId = 1;
        double walletamt = 100.0;
        double orderamt = 50.0;
        wallet Wallet=new wallet();
        when(session.getAttribute("customer")).thenReturn(cust);
        when(cust.getCustId()).thenReturn(custId);
        when(walletDAO.getWalletAmount(custId)).thenReturn(Wallet);
        when(Wallet.getAmount()).thenReturn(walletamt);

        // Execute
        String result = customerController.wallet(walletamt, orderamt, null, session);

        // Verify
        AssertJUnit.assertEquals("50.0",result);
    }

    @Test
    public void testBuyThisProduct() throws NumberFormatException, SQLException {
        // Set up
        int productId = 1;
        int qty = 2;
        double walletAmt = 100.0;
        wallet Wallet=new wallet();
        custCredModel cust = new custCredModel();
        when(session.getAttribute("customer")).thenReturn(cust);
        when(cust.getCustId()).thenReturn(1);
        when(walletDAO.getWalletAmount(1)).thenReturn(Wallet);
        when(Wallet.getAmount()).thenReturn(walletAmt);

        // Execute
        String result = customerController.buythisproduct(productId, qty, null, session);

        // Verify
        verify(walletDAO, times(1)).getWalletAmount(1);
        verify(Wallet, times(1)).getAmount();
        AssertJUnit.assertEquals("paymentpreview",result);

    }
}

