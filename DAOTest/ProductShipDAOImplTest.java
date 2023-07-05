package eStoreProduct.testing.dao;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import javax.sql.DataSource;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import eStoreProduct.DAO.ProductShipDAOImpl;
import eStoreProduct.model.ProductShip;
import eStoreProduct.model.ProductShipMapper;

public class ProductShipDAOImplTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProductShipDAOImpl productShipDAO;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        productShipDAO = new ProductShipDAOImpl(dataSource);
        productShipDAO.jdbcTemplate = jdbcTemplate;
    }

    private String getAll="SELECT sp.prod_id,prod_title,sps.prod_price,shipment_charges FROM slam_Products sp,slam_productstock sps where sps.prod_id=sp.prod_id";
    private String update="update slam_Products set shipment_charges=? where prod_id=?";
    @Test
    public void testGetAll_Success() {
        List<ProductShip> expectedList = new ArrayList<>();
        expectedList.add(new ProductShip());

        when(jdbcTemplate.query(eq(getAll), any(ProductShipMapper.class))).thenReturn(expectedList);

        List<ProductShip> result = productShipDAO.getAll();

        Assert.assertEquals(result, expectedList);
        verify(jdbcTemplate, times(1)).query(anyString(), any(ProductShipMapper.class));
    }
    @Test
    public void testUpdate_Success() {
        ProductShip productShip = new ProductShip();
        productShip.setShipment_charges(10.0);
        productShip.setProd_id(1);
        boolean result = productShipDAO.update(productShip);
        Assert.assertTrue(result);
        verify(jdbcTemplate, times(1)).update(
            eq(update),
            anyDouble(),
            eq(productShip.getProd_id()) // Match the second argument with the product ID
        );
    }


}
