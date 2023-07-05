package eStoreProduct.testing.dao;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eStoreProduct.DAO.ServicableRegionDAOImp;
import eStoreProduct.DAO.WishlistDAOImp;
import eStoreProduct.model.ServicableRegionMapper;
import eStoreProduct.model.ServiceableRegion;

public class ServicableRegionDAOImpTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ServicableRegionDAOImp servicableRegionDAO;

    @BeforeMethod
    public void setUp() {
    	 MockitoAnnotations.initMocks(this);
    	 servicableRegionDAO = new ServicableRegionDAOImp(dataSource);
    	 servicableRegionDAO.jdbcTemplate = jdbcTemplate;
    }
    private String getPincodes = "select * from slam_regions";
    @Test
    public void testGetValidityOfPincode_ValidPincode() {
        int pincode = 12345;
        List<ServiceableRegion> regions = new ArrayList<>();
        regions.add(new ServiceableRegion(1, "Region A", 10000, 20000, 10.0, 5.0)); // Assuming a region with pin codes from 10000 to 20000

        when(jdbcTemplate.query(eq(getPincodes), any(ServicableRegionMapper.class))).thenReturn(regions);

        boolean result = servicableRegionDAO.getValidityOfPincode(pincode);

        Assert.assertTrue(result);
        verify(jdbcTemplate, times(1)).query(eq(getPincodes), any(ServicableRegionMapper.class));
    }


    @Test
    public void testGetValidityOfPincode_InvalidPincode() {
        int pincode = 54321;
        List<ServiceableRegion> regions = new ArrayList<>();
        regions.add(new ServiceableRegion(1, "Region A", 10000, 20000, 10.0, 5.0)); // Assuming a region with pin codes from 10000 to 20000

        when(jdbcTemplate.query(eq(getPincodes), any(ServicableRegionMapper.class))).thenReturn(regions);

        boolean result = servicableRegionDAO.getValidityOfPincode(pincode);

        Assert.assertFalse(result);
        verify(jdbcTemplate, times(1)).query(anyString(), any(ServicableRegionMapper.class));
    }
}
