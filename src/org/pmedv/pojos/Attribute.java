package org.pmedv.pojos;

import java.io.Serializable;

import org.pmedv.beans.objects.DataType;


public class Attribute implements Serializable, Comparable<Attribute> {

	private static final long serialVersionUID = 4996422489506581263L;

	private Long id;	
	private String key;
	private String value;
	private String description;
	private DataType dataType;
	
	public Attribute() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public DataType getDataType() {
		return dataType;
	}

	
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	public boolean equals(Attribute obj) {

		if (this == obj) {
			return true;
		}
		if (obj instanceof Attribute) {
			Attribute attribute = (Attribute) obj;
			if (this.id.equals(attribute.getId()))
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(Attribute o) {
		return getKey().compareTo(o.getKey());
	}
	
	
}
