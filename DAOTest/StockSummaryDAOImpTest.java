package eStoreProduct.testing.dao;


import eStoreProduct.DAO.WishlistDAOImp;
import eStoreProduct.DAO.stockSummaryDAOImp;
import eStoreProduct.model.stockSummaryModel;
import eStoreProduct.model.stockMapperMapper;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class StockSummaryDAOImpTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private stockSummaryDAOImp stockSummaryDAO;

    @BeforeMethod
    public void setUp() {
    	 MockitoAnnotations.initMocks(this);
    	// wishlistDAOImp = new WishlistDAOImp(dataSource,prodStockDAO);
        stockSummaryDAO = new stockSummaryDAOImp(dataSource);
        stockSummaryDAO.jdbcTemplate = jdbcTemplate;
    }
    @Test
    public void testGetStocks() {
        String selectQuery = "SELECT slp.*, spc.prct_title, shc.sgst, shc.igst, shc.cgst, shc.gst, sps.prod_price, sps.prod_stock, sps.prod_mrp " +
                "FROM slam_products slp " +
                "JOIN slam_Productstock sps ON slp.prod_id = sps.prod_id " +
                "JOIN slam_hsn_code shc ON slp.prod_gstc_id = shc.hsn_code " +
                "JOIN SLAM_PRODUCTCATEGORIES spc ON spc.prct_id = slp.prod_prct_id";

        List<stockSummaryModel> expectedStocks = Collections.emptyList();

        when(jdbcTemplate.query(eq(selectQuery), any(stockMapperMapper.class))).thenReturn(expectedStocks);

        List<stockSummaryModel> result = stockSummaryDAO.getStocks();

        verify(jdbcTemplate, times(1)).query(eq(selectQuery), any(stockMapperMapper.class));
    }


    @Test
    public void testUpdateStocks() {
        int prodid = 1;
        String imageurl = "image.jpg";
        int gstcid = 2;
        int reorderlevel = 10;
        int stock = 50;
        double mrp = 100.0;
        double gstPercent = 5.0;
        double updated_mrp = ((mrp * gstPercent * 0.01) + mrp);

        String query1 = "UPDATE slam_products SET reorderlevel = ?, image_url = ?, prod_gstc_id = ? WHERE prod_id = ?";
        String query2 = "UPDATE slam_productstock SET prod_stock = ?, prod_mrp = ?, prod_price = ? WHERE prod_id = ?";
        String query3 = "SELECT gst FROM slam_hsn_code WHERE hsn_code = ?";

        when(jdbcTemplate.queryForObject(eq(query3), eq(new Object[]{gstcid}), eq(Double.class))).thenReturn(gstPercent);

        stockSummaryDAO.updateStocks(prodid, imageurl, gstcid, reorderlevel, stock, mrp);

        verify(jdbcTemplate, times(1)).update(eq(query1), eq(reorderlevel), eq(imageurl), eq(gstcid), eq(prodid));
        verify(jdbcTemplate, times(1)).update(eq(query2), eq(stock), eq(mrp), eq(updated_mrp), eq(prodid));
    }
}
