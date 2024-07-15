package org.pmedv.pojos.calendar;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Meeting implements Serializable {
	
	private static final long serialVersionUID = -7397253028786887212L;

	private Long id;
	
	private String shortDescription;
	private String longDescription;
	private Date start;
	private Date end;
	private Set<Calendar> calendars = new HashSet<Calendar>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}	
	public Set<Calendar> getCalendars() {
		return calendars;
	}
	public void setCalendars(Set<Calendar> calendars) {
		this.calendars = calendars;
	}
	@Override
	public int hashCode() {
		
		if (shortDescription != null)
			return shortDescription.hashCode();
		
		return super.hashCode();
		
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
}
