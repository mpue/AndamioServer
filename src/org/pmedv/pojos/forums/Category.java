package org.pmedv.pojos.forums;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * This class represents a forum category.
 * </p>
 * @author Matthias Pueski
 * @date   21.04.2007
 * 
 * @email  pueski@gmx.de
 *
 */

public class Category implements Serializable, Comparable<Category> {

	private static final long serialVersionUID = 1L;
	
	private Long   id;
	private String name;
	private String description;
	private int position;
	private Forum forum;
	private Set<Thread> threads;

	public Category() {
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
	 * @return the associated forum
	 */
	public Forum getForum() {
		return forum;
	}

	/**
	 * Sets the forum
	 * 
	 * @param forum 
	 */
	public void setForum(Forum forum) {
		this.forum = forum;
	}

	/**
	 * @return the associated threads
	 */
	public Set<Thread> getThreads() {
		return threads;
	}

	/**
	 * Sets the threads of the category
	 * 
	 * @param threads
	 */
	public void setThreads(Set<Thread> threads) {
		this.threads = threads;
	}

	@Override
	public int compareTo(Category o) {
		return Integer.valueOf(position).compareTo(Integer.valueOf(o.getPosition()));
	}

}