package org.pmedv.pojos;

import java.io.Serializable;


public class UsergroupProxy implements Serializable {

	private static final long serialVersionUID = 644577929745177877L;

	private Long id;
	private String name;
	private String description;
	
	public UsergroupProxy(Usergroup group) {		
		this.id = group.getId();
		this.name = group.getName();
		this.description = group.getDescription();		
	}

	
	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public String getDescription() {
		return description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
