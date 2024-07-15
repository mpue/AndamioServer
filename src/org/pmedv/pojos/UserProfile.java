package org.pmedv.pojos;

import java.io.Serializable;

public class UserProfile implements Serializable {

	private static final long serialVersionUID = 2385220720481658859L;

	private String pageContent;
	private User user;
	private Long userId;

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
