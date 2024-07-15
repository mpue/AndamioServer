package org.pmedv.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.cms.daos.DownloadCategoryDAO;
import org.pmedv.cms.daos.DownloadDAO;
import org.pmedv.pojos.Download;
import org.pmedv.pojos.DownloadCategory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class DownloadTest {

	@Test
	public void testCreateAndDeleteDownload() {

		DownloadDAO dao = DAOManager.getInstance().getDownloadDAO();
		
		Download d = new Download();
		d.setName("d1");
		d.setDescription("desc1");
		d.setFilename("f1");
		d.setHits(0);
		d.setPath("p1");
		d.setRanking(0.0f);
		d.setUploader("u1");
				
		assertNotNull(dao.createAndStore(d));
		
		d = (Download) dao.findByName("d1");
		
		assertNotNull(d);
		
		assertEquals("d1",d.getName());
		assertEquals("desc1", d.getDescription());
		assertEquals("f1",d.getFilename());
		assertEquals(0, d.getHits());
		assertEquals("p1", d.getPath());
		assertEquals(0.0f,d.getRanking(),0.0f);
		assertEquals("u1", d.getUploader());

		assertTrue(dao.delete(d));
		
	}
	
	@Test
	public void testAddCategory() {
		
		DownloadDAO ddao = DAOManager.getInstance().getDownloadDAO();
		DownloadCategoryDAO cdao = DAOManager.getInstance().getDownloadCategoryDAO();
		
		Download d = new Download();
		d.setName("d1");
		d.setDescription("desc1");
		d.setFilename("f1");
		d.setHits(0);
		d.setPath("p1");
		d.setRanking(0.0f);
		d.setUploader("u1");
				
		assertNotNull(ddao.createAndStore(d));
		
		d = (Download) ddao.findByName("d1");		
		assertNotNull(d);

		DownloadCategory c1 = new DownloadCategory();
		c1.setName("c1");
		c1.setDescription("d1");
		
		assertNotNull(cdao.createAndStore(c1));
		
		c1 = (DownloadCategory) cdao.findByName("c1");
		assertNotNull(c1);
		
		ddao.addCategory(c1.getId(), d.getId());
		
		d = (Download) ddao.findByName("d1");		
		assertNotNull(d);
		
		assertEquals(1,d.getCategories().size());

		c1 = (DownloadCategory) cdao.findByName("c1");
		assertNotNull(c1);

		assertEquals(1, c1.getDownloads().size());
		
		ddao.removeCategory(c1.getId(), d.getId());
		
		d = (Download) ddao.findByName("d1");		
		assertNotNull(d);
		
		assertEquals(0,d.getCategories().size());
		
		assertTrue(ddao.delete(d));
		assertTrue(cdao.delete(c1));
		
	}
	
}
