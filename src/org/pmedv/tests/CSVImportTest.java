package org.pmedv.tests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.ContactBean;
import org.pmedv.context.AppContext;
import org.pmedv.importer.CSVReader;
import org.pmedv.services.ContactService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class CSVImportTest {

	@Test
	
	public void testCreate() {
	
		
		CSVReader csvReader = new CSVReader("/home/mpue/Desktop/algroup/","ADRESSEN.csv");
		csvReader.parseCSVData(';');
		
	    ApplicationContext ctx = AppContext.getApplicationContext();
	    assertNotNull(ctx);
	    
	    ContactService contactService = (ContactService)ctx.getBean("contactService");
	    assertNotNull(contactService);

		int count = 0;
		
		for (Object o : csvReader.getData()) {

			String[] data = (String[]) o;
			
			System.out.println(data.length);
			
			if (data[16].trim().length() > 0) {			
				System.out.println(data[2]+" "+data[10]+" "+data[16]);
				
				ContactBean contact = new ContactBean();
				
				contact.setCompany(data[2]);
				contact.setLetterSalutation(data[10]);
				contact.setEmail(data[16]);
				
				String[] names = data[10].split(" ");
				
				contact.setLastname(names[names.length-1]);
				
				contactService.createContact(contact);
				
				count++;
			}
			
		
		}
		
		System.out.println("found "+count+" adresses.");
	}
	
}
