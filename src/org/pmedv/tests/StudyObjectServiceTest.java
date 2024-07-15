package org.pmedv.tests;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.StudyObjectBean;
import org.pmedv.beans.objects.StudyType;
import org.pmedv.context.AppContext;
import org.pmedv.services.RemoteAccessService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/WEB-INF/applicationContext.xml" })
public class StudyObjectServiceTest extends TestCase {

	private ApplicationContext ctx;
	private RemoteAccessService<StudyObjectBean> studyObjectService;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		ctx = AppContext.getApplicationContext();
		assertNotNull(ctx);
		studyObjectService = (RemoteAccessService<StudyObjectBean>) ctx.getBean("studyObjectService");
		assertNotNull(studyObjectService);

	}

	@Test
	public void testCreate() {

		ctx = AppContext.getApplicationContext();
		assertNotNull(ctx);
		studyObjectService = (RemoteAccessService<StudyObjectBean>) ctx.getBean("studyObjectService");
		assertNotNull(studyObjectService);

		StudyObjectBean s1 = new StudyObjectBean();

		s1.setDescription("description1");
		s1.setIcon("icon1");
		s1.setName("name1");
		s1.setStudyIdent("abcde");
		s1.setTitle("title1");
		s1.setType(StudyType.NIS);

		Long id = studyObjectService.create(s1);
		assertNotNull(id);
		assertEquals(id, Long.valueOf(1L));

		StudyObjectBean s2 = new StudyObjectBean();

		s2.setDescription("description2");
		s2.setIcon("icon2");
		s2.setName("name2");
		s2.setStudyIdent("bcdef");
		s2.setTitle("title2");
		s2.setType(StudyType.REGISTER);

		id = studyObjectService.create(s2);
		assertNotNull(id);
		assertEquals(id, Long.valueOf(2L));

	}

	@Test
	public void testFindAndCompare() {

		ctx = AppContext.getApplicationContext();
		assertNotNull(ctx);
		studyObjectService = (RemoteAccessService<StudyObjectBean>) ctx.getBean("studyObjectService");
		assertNotNull(studyObjectService);

		ArrayList<StudyObjectBean> studies = studyObjectService.findAll();
		assertNotNull(studies);

		assertEquals(2, studies.size());

		StudyObjectBean c1 = studyObjectService.findById(1L);
		assertNotNull(c1);

		assertEquals("name1", c1.getName());
		assertEquals("description1",c1.getDescription());
		assertEquals("icon1",c1.getIcon());
		assertEquals("abcde",c1.getStudyIdent());
		assertEquals("title1",c1.getTitle());
		assertEquals(StudyType.NIS, c1.getType());

		StudyObjectBean c2 = studyObjectService.findById(2L);
		assertNotNull(c2);

		assertEquals("name2", c2.getName());
		assertEquals(StudyType.REGISTER, c2.getType());
		assertEquals("description2",c2.getDescription());
		assertEquals("icon2",c2.getIcon());
		assertEquals("bcdef",c2.getStudyIdent());
		assertEquals("title2",c2.getTitle());

	}

	@Test
	public void testDelete() {

		ctx = AppContext.getApplicationContext();
		assertNotNull(ctx);
		studyObjectService = (RemoteAccessService<StudyObjectBean>) ctx.getBean("studyObjectService");
		assertNotNull(studyObjectService);

		StudyObjectBean c1 = studyObjectService.findById(1L);
		assertNotNull(c1);

		assertEquals(true, studyObjectService.delete(c1));

		StudyObjectBean c2 = studyObjectService.findById(2L);
		assertNotNull(c2);

		assertEquals(true, studyObjectService.delete(c2));

		ArrayList<StudyObjectBean> studies = studyObjectService.findAll();
		assertNotNull(studies);

		assertEquals(0, studies.size());

	}

}
