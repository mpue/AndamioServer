package org.pmedv.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.pmedv.beans.objects.documents.BiblioBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.documents.Biblio;


public class BiblioServiceImpl implements BiblioService {

	@Override
	public Long create(BiblioBean bib) throws IllegalArgumentException {
		
		if (bib == null)
			throw new IllegalArgumentException("Entry must not be null.");
		if (bib.getIdentifier().length() < 1)
			throw new IllegalArgumentException("You must provide at least an idenitifier.");
		
		Biblio biblio = (Biblio)DAOManager.getInstance().getBiblioDAO().findByName(bib.getIdentifier());
		
		if (biblio != null) {
			throw new IllegalArgumentException("An entry with this identifier already exists.");
		}
		
		Biblio b = new Biblio();
		
		b.setAddress(bib.getAddress());
		b.setAnnote(bib.getAnnote());
		b.setAuthor(bib.getAuthor());
		b.setBooktitle(bib.getBooktitle());
		b.setChapter(bib.getChapter());
		b.setCreated(new Date());
		b.setCustom1(bib.getCustom1());
		b.setCustom2(bib.getCustom2());
		b.setCustom3(bib.getCustom3());
		b.setCustom4(bib.getCustom4());
		b.setCustom5(bib.getCustom5());
		b.setEdition(bib.getEdition());
		b.setEditor(bib.getEditor());
		b.setEdition(bib.getEdition());
		b.setHowpublish(bib.getHowpublish());
		b.setIdentifier(bib.getIdentifier());
		b.setInstitution(bib.getInstitution());
		b.setIsbn(bib.getIsbn());
		b.setJournal(bib.getJournal());
		b.setLastmodified(new Date());
		b.setLastmodifiedby(bib.getLastmodifiedby());
		b.setMonth(bib.getMonth());
		b.setNote(bib.getNote());
		b.setNumber(bib.getNumber());
		b.setOrganization(bib.getOrganization());
		b.setPages(bib.getPages());
		b.setPublisher(bib.getPublisher());
		b.setReportType(bib.getReportType());
		b.setSchool(bib.getSchool());
		b.setSeries(bib.getSeries());
		b.setSubmittedBy(bib.getSubmittedBy());
		b.setType(bib.getType());
		b.setType(bib.getType());
		b.setUrl(bib.getUrl());
		b.setVolume(bib.getVolume());
		b.setYear(bib.getYear());
		
		if (DAOManager.getInstance().getBiblioDAO().createAndStore(b) != null) {
			
			Biblio test = (Biblio)DAOManager.getInstance().getBiblioDAO().findByName(b.getIdentifier());
			
			if (test != null && test.getId() != null)
				return test.getId();
			
		}
		else throw new IllegalArgumentException("Could not create entry.");
		
		return null;
	}

	@Override
	public boolean delete(BiblioBean bib) throws IllegalArgumentException {

		if (bib == null)
			throw new IllegalArgumentException("Entry must not be null.");

		Biblio b = (Biblio)DAOManager.getInstance().getBiblioDAO().findByID(bib.getId());
		
		if (b == null) {
			throw new IllegalArgumentException("There is no biblio with this id.");
		}
		
		return DAOManager.getInstance().getBiblioDAO().delete(b);
	}

	@Override
	public ArrayList<BiblioBean> findAll() {

		ArrayList<BiblioBean> biblioList = new ArrayList<BiblioBean>();
		
		List<?> biblios = DAOManager.getInstance().getBiblioDAO().findAllItems();
		
		for (Iterator<?> it = biblios.iterator();it.hasNext();) {
			
			Biblio bib = (Biblio)it.next();
			
			BiblioBean b = new BiblioBean();
			
			b.setAddress(bib.getAddress());
			b.setAnnote(bib.getAnnote());
			b.setAuthor(bib.getAuthor());
			b.setBooktitle(bib.getBooktitle());
			b.setChapter(bib.getChapter());
			b.setCreated(bib.getCreated());
			b.setCustom1(bib.getCustom1());
			b.setCustom2(bib.getCustom2());
			b.setCustom3(bib.getCustom3());
			b.setCustom4(bib.getCustom4());
			b.setCustom5(bib.getCustom5());
			b.setEdition(bib.getEdition());
			b.setEditor(bib.getEditor());
			b.setEdition(bib.getEdition());
			b.setHowpublish(bib.getHowpublish());
			b.setIdentifier(bib.getIdentifier());
			b.setInstitution(bib.getInstitution());
			b.setIsbn(bib.getIsbn());
			b.setJournal(bib.getJournal());
			b.setLastmodified(bib.getLastmodified());
			b.setLastmodifiedby(bib.getLastmodifiedby());
			b.setMonth(bib.getMonth());
			b.setNote(bib.getNote());
			b.setNumber(bib.getNumber());
			b.setOrganization(bib.getOrganization());
			b.setPages(bib.getPages());
			b.setPublisher(bib.getPublisher());
			b.setReportType(bib.getReportType());
			b.setSchool(bib.getSchool());
			b.setSeries(bib.getSeries());
			b.setSubmittedBy(bib.getSubmittedBy());
			b.setType(bib.getType());
			b.setTitle(bib.getTitle());
			b.setUrl(bib.getUrl());
			b.setVolume(bib.getVolume());
			b.setId(bib.getId());
			b.setYear(bib.getYear());
			
			biblioList.add(b);
			
		}
		
		return biblioList;
	}

	@Override
	public BiblioBean findById(Long biblioId) throws IllegalArgumentException {
		
		if (biblioId == null)
			throw new IllegalArgumentException("Id must not be null");
		
		Biblio bib = (Biblio)DAOManager.getInstance().getBiblioDAO().findByID(1);
		
		if (bib == null)
			throw new IllegalArgumentException("There is no entry with that id");
		
		BiblioBean b = new BiblioBean();
		
		b.setId(bib.getId());
		b.setAddress(bib.getAddress());
		b.setAnnote(bib.getAnnote());
		b.setAuthor(bib.getAuthor());
		b.setBooktitle(bib.getBooktitle());
		b.setChapter(bib.getChapter());
		b.setCreated(bib.getCreated());
		b.setCustom1(bib.getCustom1());
		b.setCustom2(bib.getCustom2());
		b.setCustom3(bib.getCustom3());
		b.setCustom4(bib.getCustom4());
		b.setCustom5(bib.getCustom5());
		b.setEdition(bib.getEdition());
		b.setEditor(bib.getEditor());
		b.setEdition(bib.getEdition());
		b.setHowpublish(bib.getHowpublish());
		b.setIdentifier(bib.getIdentifier());
		b.setInstitution(bib.getInstitution());
		b.setIsbn(bib.getIsbn());
		b.setJournal(bib.getJournal());
		b.setLastmodified(bib.getLastmodified());
		b.setLastmodifiedby(bib.getLastmodifiedby());
		b.setMonth(bib.getMonth());
		b.setNote(bib.getNote());
		b.setNumber(bib.getNumber());
		b.setOrganization(bib.getOrganization());
		b.setPages(bib.getPages());
		b.setPublisher(bib.getPublisher());
		b.setReportType(bib.getReportType());
		b.setSchool(bib.getSchool());
		b.setSeries(bib.getSeries());
		b.setSubmittedBy(bib.getSubmittedBy());
		b.setType(bib.getType());
		b.setTitle(bib.getTitle());
		b.setUrl(bib.getUrl());
		b.setVolume(bib.getVolume());		
		b.setYear(bib.getYear());
		
		return b;
	}

	@Override
	public BiblioBean findByName(String name) throws IllegalArgumentException {
		
		if (name == null || name.length() < 1)
			throw new IllegalArgumentException("Name must not be null or shorter than 1.");
		
		Biblio bib = (Biblio)DAOManager.getInstance().getBiblioDAO().findByName(name);
		
		if (bib == null)
			throw new IllegalArgumentException("There is no entry with that name.");
		
		BiblioBean b = new BiblioBean();
		
		b.setId(bib.getId());
		b.setAddress(bib.getAddress());
		b.setAnnote(bib.getAnnote());
		b.setAuthor(bib.getAuthor());
		b.setBooktitle(bib.getBooktitle());
		b.setChapter(bib.getChapter());
		b.setCreated(bib.getCreated());
		b.setCustom1(bib.getCustom1());
		b.setCustom2(bib.getCustom2());
		b.setCustom3(bib.getCustom3());
		b.setCustom4(bib.getCustom4());
		b.setCustom5(bib.getCustom5());
		b.setEdition(bib.getEdition());
		b.setEditor(bib.getEditor());
		b.setEdition(bib.getEdition());
		b.setHowpublish(bib.getHowpublish());
		b.setIdentifier(bib.getIdentifier());
		b.setInstitution(bib.getInstitution());
		b.setIsbn(bib.getIsbn());
		b.setJournal(bib.getJournal());
		b.setLastmodified(bib.getLastmodified());
		b.setLastmodifiedby(bib.getLastmodifiedby());
		b.setMonth(bib.getMonth());
		b.setNote(bib.getNote());
		b.setNumber(bib.getNumber());
		b.setOrganization(bib.getOrganization());
		b.setPages(bib.getPages());
		b.setPublisher(bib.getPublisher());
		b.setReportType(bib.getReportType());
		b.setSchool(bib.getSchool());
		b.setSeries(bib.getSeries());
		b.setSubmittedBy(bib.getSubmittedBy());
		b.setType(bib.getType());
		b.setTitle(bib.getTitle());
		b.setUrl(bib.getUrl());
		b.setVolume(bib.getVolume());
		b.setYear(bib.getYear());
		
		return b;
	}

	@Override
	public boolean update(BiblioBean bib) throws IllegalArgumentException {
		
		if (bib == null)
			throw new IllegalArgumentException("Entry must not be null.");

		Biblio b = (Biblio)DAOManager.getInstance().getBiblioDAO().findByID(bib.getId());
		
		if (b == null) {
			throw new IllegalArgumentException("There is no biblio with this id.");
		}
		else {
			
			b.setAddress(bib.getAddress());
			b.setAnnote(bib.getAnnote());
			b.setAuthor(bib.getAuthor());
			b.setBooktitle(bib.getBooktitle());
			b.setChapter(bib.getChapter());
			b.setCreated(bib.getCreated());
			b.setCustom1(bib.getCustom1());
			b.setCustom2(bib.getCustom2());
			b.setCustom3(bib.getCustom3());
			b.setCustom4(bib.getCustom4());
			b.setCustom5(bib.getCustom5());
			b.setEdition(bib.getEdition());
			b.setEditor(bib.getEditor());
			b.setEdition(bib.getEdition());
			b.setHowpublish(bib.getHowpublish());
			b.setIdentifier(bib.getIdentifier());
			b.setInstitution(bib.getInstitution());
			b.setIsbn(bib.getIsbn());
			b.setJournal(bib.getJournal());
			b.setLastmodified(bib.getLastmodified());
			b.setLastmodifiedby(bib.getLastmodifiedby());
			b.setMonth(bib.getMonth());
			b.setNote(bib.getNote());
			b.setNumber(bib.getNumber());
			b.setOrganization(bib.getOrganization());
			b.setPages(bib.getPages());
			b.setPublisher(bib.getPublisher());
			b.setReportType(bib.getReportType());
			b.setSchool(bib.getSchool());
			b.setSeries(bib.getSeries());
			b.setSubmittedBy(bib.getSubmittedBy());
			b.setType(bib.getType());
			b.setTitle(bib.getTitle());
			b.setUrl(bib.getUrl());
			b.setVolume(bib.getVolume());					
			b.setYear(bib.getYear());
			
			return DAOManager.getInstance().getBiblioDAO().update(b);
			
		}

	}

}
