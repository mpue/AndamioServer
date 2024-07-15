package org.pmedv.tests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.CountryBean;
import org.pmedv.context.AppContext;
import org.pmedv.importer.CSVReader;
import org.pmedv.services.RemoteAccessService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class CountryImportTest {

	@Test
	
	public void testCreate() {
	
		
		CSVReader csvReader = new CSVReader("/home/pueski/devel/j2ee_workspace/andamio/resources/","countries.txt");
		csvReader.parseCSVData(' ');
		
	    ApplicationContext ctx = AppContext.getApplicationContext();
	    assertNotNull(ctx);
	    
	    RemoteAccessService<CountryBean> countryService = (RemoteAccessService<CountryBean>)ctx.getBean("countryService"); 
	    assertNotNull(countryService);

		int count = 0;
		
		for (Object o : csvReader.getData()) {

			String[] data = (String[]) o;
			
			if (data[0].trim().length() > 0) {			

				StringBuffer name = new StringBuffer();
				
				for (int i = 4; i < data.length;i++) {
					name.append(data[i]+ " ");
				}
				
				CountryBean country = new CountryBean();

				country.setName(name.toString().trim());
				country.setCode(data[1]);
				
				countryService.create(country);
				
				count++;
				
			}
		
		}
		
		System.out.println("found "+count+" countries.");
	}
	
}
