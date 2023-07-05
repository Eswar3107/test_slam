package eStoreProduct.testing.dao;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eStoreProduct.DAO.RegionDAOImpl;
import eStoreProduct.model.Regions;
import eStoreProduct.model.RegionsMapper;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class RegionDAOImplTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private RegionDAOImpl regionDAO;

    @BeforeMethod
    public void setUp() {
    	 MockitoAnnotations.initMocks(this);
    	 regionDAO = new RegionDAOImpl(dataSource);
    	 regionDAO.jdbcTemplate = jdbcTemplate;
        
    }
    private String get_all = "select * from slam_regions";
	private String add_region = "INSERT INTO slam_regions (region_name, region_pin_from, region_pin_to, region_surcharge, region_pricewaiver)\r\n"
			+ "VALUES\r\n" + "  (?, ?, ?, ?, ?);";
	private String del_region = "delete from slam_regions where region_id=?";

	@Test
	public void testGetRegions_Success() {
	    List<Regions> expectedRegions = Collections.singletonList(new Regions());
	    when(jdbcTemplate.query(eq(get_all), any(RegionsMapper.class))).thenReturn(expectedRegions);

	    List<Regions> result = regionDAO.getRegions();

	    Assert.assertEquals(result, expectedRegions);
	    verify(jdbcTemplate, times(1)).query(eq(get_all), any(RegionsMapper.class));
	}


    @Test
    public void testGetRegions_ExceptionThrown_ReturnsEmptyList() {
        when(jdbcTemplate.query(eq(get_all), any(RegionsMapper.class))).thenThrow(new RuntimeException());

        List<Regions> result = regionDAO.getRegions();

        Assert.assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1)).query(anyString(), any(RegionsMapper.class));
    }

    @Test
    public void testAddRegion() {
        Regions region = new Regions(1, "Region A", 10000, 20000, 10.0, 5.0);

        regionDAO.addRegion(region);

        verify(jdbcTemplate, times(1)).update(eq(add_region), any(), anyInt(), anyInt(), anyDouble(), anyDouble());
    }


    @Test
    public void testRemoveRegion() {
        int regionId = 1;

        regionDAO.removeRegion(regionId);

        verify(jdbcTemplate, times(1)).update(eq(del_region), anyInt());
    }
}
