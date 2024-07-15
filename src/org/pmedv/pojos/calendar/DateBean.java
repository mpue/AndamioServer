package org.pmedv.pojos.calendar;

import java.io.Serializable;
import java.util.Date;


public class DateBean implements Serializable, Comparable<DateBean> {
	
	private static final long serialVersionUID = -5280463118769739909L;

	private Long id;
	private Date date;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int compareTo(DateBean o) {
		return date.compareTo(o.getDate());
	}

}
