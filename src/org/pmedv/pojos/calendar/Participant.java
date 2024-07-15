package org.pmedv.pojos.calendar;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;


public class Participant implements Serializable, Comparable<Participant> {
	
	private static final long serialVersionUID = -8025316322885023095L;

	private Long id;
	private String firstname;
	private String lastname;
	private Set<DateBean> possibleDates = new TreeSet<DateBean>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public Set<DateBean> getPossibleDates() {
		return possibleDates;
	}
	
	public void setPossibleDates(Set<DateBean> possibleDates) {
		this.possibleDates = possibleDates;
	}

	@Override
	public int compareTo(Participant o) {
		return lastname.compareToIgnoreCase(o.getLastname());
	}

}
