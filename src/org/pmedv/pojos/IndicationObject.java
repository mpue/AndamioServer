package org.pmedv.pojos;

import java.io.Serializable;

public class IndicationObject implements Serializable, Comparable<IndicationObject> {

	private static final long serialVersionUID = 3653396106549119148L;
	private Long id;
	private String name;
	private String description;

	public IndicationObject() {

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int compareTo(IndicationObject o) {
		return getName().compareTo(o.getName());
	}

}
