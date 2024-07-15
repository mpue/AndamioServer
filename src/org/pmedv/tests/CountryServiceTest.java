package org.pmedv.tests;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.CountryBean;
import org.pmedv.context.AppContext;
import org.pmedv.services.RemoteAccessService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class CountryServiceTest extends TestCase{

	private ApplicationContext ctx;
	private RemoteAccessService<CountryBean> countryService;
	
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
	    ctx = AppContext.getApplicationContext();
	    assertNotNull(ctx);
	    countryService = (RemoteAccessService<CountryBean>)ctx.getBean("countryService");
	    assertNotNull(countryService);
		
	}
	
	
	@Test	
	public void testCreate() {

	    ctx = AppContext.getApplicationContext();
	    assertNotNull(ctx);
	    countryService = (RemoteAccessService<CountryBean>)ctx.getBean("countryService");
	    assertNotNull(countryService);

		
	    CountryBean c1 = new CountryBean();
	    
	    c1.setName("Germany");
	    c1.setCode("de");
	    
	    Long id = countryService.create(c1);	    
	    assertNotNull(id);
	    
	    assertEquals(id, Long.valueOf(1L));
	    
	    CountryBean c2 = new CountryBean();
	    
	    c2.setName("Dubai");
	    c2.setCode("du");

	    id = countryService.create(c2);	    
	    assertNotNull(id);
	    
	    assertEquals(id, Long.valueOf(2L));
	    
	}

	
	@Test	
	public void testFindAndCompare() {
		
	    ctx = AppContext.getApplicationContext();
	    assertNotNull(ctx);
	    countryService = (RemoteAccessService<CountryBean>)ctx.getBean("countryService");
	    assertNotNull(countryService);
	
		ArrayList<CountryBean> countries = countryService.findAll();
		assertNotNull(countries);
		
		assertEquals(2, countries.size());

		CountryBean c1 = countryService.findById(1L);
		assertNotNull(c1);
		
		assertEquals("Germany", c1.getName());
		assertEquals("de", c1.code);

		CountryBean c2 = countryService.findById(2L);
		assertNotNull(c2);
		
		assertEquals("Dubai", c2.getName());
		assertEquals("du", c2.code);
		
	}
	
	@Test	
	public void testDelete() {
		
	    ctx = AppContext.getApplicationContext();
	    assertNotNull(ctx);
	    countryService = (RemoteAccessService<CountryBean>)ctx.getBean("countryService");
	    assertNotNull(countryService);
		
		CountryBean c1 = countryService.findById(1L);
		assertNotNull(c1);
		
		assertEquals(true,countryService.delete(c1));

		CountryBean c2 = countryService.findById(2L);
		assertNotNull(c2);

		assertEquals(true,countryService.delete(c2));

		ArrayList<CountryBean> countries = countryService.findAll();
		assertNotNull(countries);
		
		assertEquals(0, countries.size());

		
	}
	
}
