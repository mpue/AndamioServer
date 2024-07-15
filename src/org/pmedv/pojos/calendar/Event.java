package org.pmedv.pojos.calendar;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Event implements Serializable {

	private static final long serialVersionUID = -6733680747101898255L;

	private Long id;
	private String title;
	private String location;
	private String description;
	private String username;
	private Date selectedDate;
	private Set<DateBean> possibleDates = new TreeSet<DateBean>();
	private Set<Participant> participants = new TreeSet<Participant>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	
	public Set<DateBean> getPossibleDates() {
		return possibleDates;
	}

	
	public void setPossibleDates(Set<DateBean> possibleDates) {
		this.possibleDates = possibleDates;
	}

	
	public Date getSelectedDate() {
		return selectedDate;
	}

	
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	
	public Set<Participant> getParticipants() {
		return participants;
	}

	
	public void setParticipants(Set<Participant> participants) {
		this.participants = participants;
	}
	
}
