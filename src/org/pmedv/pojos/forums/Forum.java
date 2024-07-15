package org.pmedv.pojos.forums;

import java.util.HashSet;
import java.util.Set;

import org.pmedv.pojos.Usergroup;

/**
 * <p>
 * This class represents a forum also known as a bulletin board.
 * </p>
 * <p>
 * In a forum users can post messages and do some discussion about a specific
 * theme. Every forum has some categories in which users can discuss. 
 * </p>
 * <p>
 * For discussion users can create threads with a specific topic. Every thread
 * can contain multiple postings written by different users. See the mapping files
 * 
 * <ul>
 *     <li>Forum.hbm.xml</li>
 *     <li>Category.hbm.xml</li>
 *     <li>Thread.hbm.xml</li>
 * 	   <li>Posting.hbm.xml</li>
 * </ul>
 * 
 * for further details
 * 
 * </p>
 * @author Matthias Pueski
 * @date   21.04.2007
 * 
 * @email  pueski@gmx.de
 *
 */

public class Forum {

	private static final long serialVersionUID = 1L;
	
	private Long   id;
	private String name;
	private String description;
	private int status;
	private int position;
	private Set<Category> categories;
	protected Set<Usergroup> groups = new HashSet<Usergroup>();

	public Forum() {
	}
	
	/**
	 * @return the name of the forum
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the forum
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the description of the forum
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the forum
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the position of the forum
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the position of the forum
	 * 
	 * @param position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the status of the forum
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status of the forum
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the id of the forum
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of the forum
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the contained categories
	 */
	public Set<Category> getCategories() {
		return categories;
	}

	/**
	 * Sets the forum categories
	 * 
	 * @param categories
	 */
	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	/**
	 * @return the groups
	 */
	public Set<Usergroup> getGroups() {
		return groups;
	}

	
	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Set<Usergroup> groups) {
		this.groups = groups;
	}

	/**
	 * Determines if this forum is restricted to any group
	 * 
	 * @return
	 */
	public boolean isRestricted() {
		return getGroups().size() > 0;
	}

}