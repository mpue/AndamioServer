package org.pmedv.pojos;

import java.io.Serializable;

import org.pmedv.beans.objects.StudyType;

public class StudyObject implements Serializable, Comparable<StudyObject> {

	private Long id;
	private String name;
	private String description;
	private String studyIdent;
	private String title;
	private String icon;
	private StudyType type;
	private IndicationObject indication;
	private boolean recruiting;

	public StudyObject() {

	}

	private static final long serialVersionUID = 4231635181935564802L;

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

	/**
	 * @return the studyIdent
	 */
	public String getStudyIdent() {
		return studyIdent;
	}

	/**
	 * @param studyIdent the studyIdent to set
	 */
	public void setStudyIdent(String studyIdent) {
		this.studyIdent = studyIdent;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the type
	 */
	public StudyType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(StudyType type) {
		this.type = type;
	}
	
	/**
	 * @param indication the indication to set
	 */
	public void setIndication(IndicationObject indication) {
		this.indication = indication;
	}

	/**
	 * @return the indication
	 */
	public IndicationObject getIndication() {
		return indication;
	}

	@Override
	public int compareTo(StudyObject o) {
		return getName().compareTo(o.getName());
	}

	
	/**
	 * @return the recruiting
	 */
	public boolean isRecruiting() {
		return recruiting;
	}

	
	/**
	 * @param recruiting the recruiting to set
	 */
	public void setRecruiting(boolean recruiting) {
		this.recruiting = recruiting;
	}
	
	
}
