/**
 * WeberknechtCMS - Open Source Content Management Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.pmedv.context.AppContext;
import org.pmedv.plugins.IPlugin;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
import org.pmedv.session.SessionCollector;
import org.pmedv.session.UserSession;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("unchecked")
public class MainpageForm extends ActionForm {

	private static final ApplicationContext ctx = AppContext.getApplicationContext();
	private static final SessionCollector collector = (SessionCollector) ctx.getBean("sessionCollector");

	private static final long serialVersionUID = 1L;

	private List menus = null;
	private Content content = null;
	private Vector menuitems = null;
	private String keywords = null;
	private String stylesheet = null;
	private Set submenus = null;
	private List daysOfMonth = null;
	private String sitename = null;
	private String logo = null;
	private String background = null;

	private String remoteIP = null;
	private String userAgent = null;
	private String userid = null;

	private int month = 0;
	private int year = 0;
	private int day = 0;
	private int lastMonth = 0;
	private int nextMonth = 0;
	private int lastYear = 0;
	private int nextYear = 0;

	private int weekOfYear = 0;
	private long mainCategory = 0;

	private Config config;
	private List randomImages;

	// Show module content instead of regular content

	private boolean showModule = false;
	private String moduleName = "";
	private String moduleDescription = "";

	private ArrayList<IPlugin> pluginList;

	private String lastModified;
	private String search;

	private String currentNode;
	private Node node;

	private ArrayList<String> searchResults;

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Reset all properties to their default values.
	 * 
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.menus = null;
		this.content = null;
		this.menuitems = null;
		this.keywords = null;
		this.stylesheet = null;
		this.submenus = null;
		this.daysOfMonth = null;
		this.background = null;
		this.logo = null;

		this.remoteIP = null;
		this.userAgent = null;
		this.userid = null;

		this.month = 0;
		this.year = 0;
		this.day = 0;
		this.lastMonth = 0;
		this.nextMonth = 0;
		this.lastYear = 0;
		this.nextYear = 0;

		this.weekOfYear = 0;
		this.mainCategory = 0;
		this.lastModified = null;
		this.search = null;
	}

	/**
	 * @return Returns the content.
	 */
	public Content getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContent(Content content) {
		this.content = content;
	}

	/**
	 * @return Returns the menus.
	 */
	public List getMenus() {
		return menus;
	}

	/**
	 * @param menus The menus to set.
	 */
	public void setMenus(List menus) {
		this.menus = menus;
	}

	/**
	 * @return Returns the menuitems.
	 */
	public Vector getMenuitems() {
		return menuitems;
	}

	/**
	 * @param menuitems The menuitems to set.
	 */
	public void setMenuitems(Vector menuitems) {
		this.menuitems = menuitems;
	}

	/**
	 * @return Returns the keywords.
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords The keywords to set.
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return Returns the stylesheet.
	 */
	public String getStylesheet() {
		return stylesheet;
	}

	/**
	 * @param stylesheet The stylesheet to set.
	 */
	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}

	/**
	 * @return Returns the submenus.
	 */
	public Set getSubmenus() {
		return submenus;
	}

	/**
	 * @param submenus The submenus to set.
	 */
	public void setSubmenus(Set submenus) {
		this.submenus = submenus;
	}

	/**
	 * @return Returns the day.
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day The day to set.
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return Returns the daysOfMonth.
	 */
	public List getDaysOfMonth() {
		return daysOfMonth;
	}

	/**
	 * @param daysOfMonth The daysOfMonth to set.
	 */
	public void setDaysOfMonth(List daysOfMonth) {
		this.daysOfMonth = daysOfMonth;
	}

	/**
	 * @return Returns the month.
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month The month to set.
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return Returns the weekOfYear.
	 */
	public int getWeekOfYear() {
		return weekOfYear;
	}

	/**
	 * @param weekOfYear The weekOfYear to set.
	 */
	public void setWeekOfYear(int weekOfYear) {
		this.weekOfYear = weekOfYear;
	}

	/**
	 * @return Returns the year.
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year The year to set.
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return Returns the lastMonth.
	 */
	public int getLastMonth() {
		return lastMonth;
	}

	/**
	 * @param lastMonth The lastMonth to set.
	 */
	public void setLastMonth(int lastMonth) {
		this.lastMonth = lastMonth;
	}

	/**
	 * @return Returns the nextMonth.
	 */
	public int getNextMonth() {
		return nextMonth;
	}

	/**
	 * @param nextMonth The nextMonth to set.
	 */
	public void setNextMonth(int nextMonth) {
		this.nextMonth = nextMonth;
	}

	/**
	 * @return Returns the lastYear.
	 */
	public int getLastYear() {
		return lastYear;
	}

	/**
	 * @param lastYear The lastYear to set.
	 */
	public void setLastYear(int lastYear) {
		this.lastYear = lastYear;
	}

	/**
	 * @return Returns the nextYear.
	 */
	public int getNextYear() {
		return nextYear;
	}

	/**
	 * @param nextYear The nextYear to set.
	 */
	public void setNextYear(int nextYear) {
		this.nextYear = nextYear;
	}

	/**
	 * @return Returns the remoteIP.
	 */
	public String getRemoteIP() {
		return remoteIP;
	}

	/**
	 * @param remoteIP The remoteIP to set.
	 */
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	/**
	 * @return Returns the user.
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return Returns the userAgent.
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent The userAgent to set.
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public long getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(long mainCategory) {
		this.mainCategory = mainCategory;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Collection<UserSession> getSessions() {
		return collector.getSessions().values();
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public boolean isShowModule() {
		return showModule;
	}

	public void setShowModule(boolean showModule) {
		this.showModule = showModule;
	}

	public List getRandomImages() {
		return randomImages;
	}

	public void setRandomImages(List randomImages) {
		this.randomImages = randomImages;
	}

	public String getModuleDescription() {
		return moduleDescription;
	}

	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	/**
	 * @return the pluginList
	 */
	public ArrayList<IPlugin> getPluginList() {
		return pluginList;
	}

	/**
	 * @param pluginList the pluginList to set
	 */
	public void setPluginList(ArrayList<IPlugin> pluginList) {
		this.pluginList = pluginList;
	}

	/**
	 * @return the lastModified
	 */
	public String getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the search
	 */
	public String getSearch() {
		return search;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * @return the currentNode
	 */
	public String getCurrentNode() {
		return currentNode;
	}

	/**
	 * @param currentNode the currentNode to set
	 */
	public void setCurrentNode(String currentNode) {
		this.currentNode = currentNode;
	}

	public ArrayList<String> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(ArrayList<String> searchResults) {
		this.searchResults = searchResults;
	}

}
