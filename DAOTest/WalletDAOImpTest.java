package eStoreProduct.testing.dao;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

import javax.sql.DataSource;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eStoreProduct.DAO.walletDAOImp;
import eStoreProduct.model.wallet;

public class WalletDAOImpTest {
	@Mock
    private walletDAOImp walletDAO;
	@Mock
    private JdbcTemplate jdbcTemplate;
	@Mock
    private DataSource dataSource;

    @BeforeMethod
    public void setUp() {
    	 MockitoAnnotations.initMocks(this);
    	 walletDAO = new walletDAOImp(dataSource);
    	 walletDAO.jdbcTemplate = jdbcTemplate;
    }
    
	String get_wallet_amt = "select * from slam_wallet where cust_id=?";
	String update_wallet = "update slam_wallet set amount=? where cust_id=?";

	@Test
	public void testGetWalletAmount() {
	    int custid = 1;
	    double expectedAmount = 100.0;
	    wallet expectedWallet = new wallet();
	    expectedWallet.setCustid(custid);
	    expectedWallet.setAmount(expectedAmount);

	    when(jdbcTemplate.queryForObject(eq(get_wallet_amt), any(Object[].class), any(RowMapper.class))).thenReturn(expectedWallet);

	    wallet result = walletDAO.getWalletAmount(custid);

	    assertEquals(result.getCustid(), custid);
	    assertEquals(result.getAmount(), expectedAmount);

	    verify(jdbcTemplate, times(1)).queryForObject(eq(get_wallet_amt), any(Object[].class), any(RowMapper.class));
	}



//    @Test
//    public void testUpdateWallet() {
//        double amt = 200.0;
//        int custid = 1;
//        int affectedRows = 1;
//        when(jdbcTemplate.update(update_wallet, eq(amt),eq(custid))).thenReturn(affectedRows);
//
//        walletDAO.updatewallet(amt, custid);
//
//        verify(jdbcTemplate, times(1)).update(update_wallet, eq(amt), eq(custid));
//    }
    @Test
    public void testUpdateWallet() {
        double amt = 150.0;
        int custid = 1;

        walletDAO.updatewallet(amt, custid);

        verify(jdbcTemplate, times(1)).update(eq(update_wallet), eq(amt), eq(custid));
    }

}
