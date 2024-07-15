/**

	WeberknechtCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2009 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.actions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.xmlgraphics.util.MimeConstants;
import org.htmlparser.filters.TagNameFilter;
import org.pmedv.beans.objects.NodeBean;
import org.pmedv.beans.objects.PageBean;
import org.pmedv.cms.common.CMSProperties;
import org.pmedv.cms.common.Params;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.forms.MainpageForm;
import org.pmedv.plugins.AbstractPlugin;
import org.pmedv.plugins.PluginHelper;
import org.pmedv.pojos.Config;
import org.pmedv.pojos.Content;
import org.pmedv.pojos.Node;
import org.pmedv.pojos.User;
import org.pmedv.services.NodeService;
import org.pmedv.util.BlogUtil;
import org.pmedv.util.DocumentBuilder;
import org.pmedv.util.NodeHelper;
import org.pmedv.util.RequestUtil;
import org.pmedv.util.StringUtil;
import org.pmedv.web.ServerUtil;
import org.springframework.context.ApplicationContext;


/**
 * This class represents the main class for the webpage and loads the content or
 * a module according to the given parameters
 * 
 * @author Matthias Pueski
 * 
 * @version 1.22 from 2007-05-03
 * 
 */
public class MainpageShowAction extends DispatchAction {
	
	// logging
	private static final Log log = LogFactory.getLog(MainpageShowAction.class);
	// message resources
	private ResourceBundle resources = ResourceBundle.getBundle("MessageResources");
	// the current selected node
	private Node node;
	// configuration
	private Config config = null;
	
	/**
	 * Displays the current selected node
	 */
	public ActionForward showNode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// reset node
		node = null;
		// template
		String template = null;
		// turn off any caching
		ServerUtil.preventBrowserCaching(response);		
		log.info("request from domain "+request.getServerName());
		// set language for current domain, if there is any
		ServerUtil.setDomainLanguage(request);						
		// get the form associated with this request		
		MainpageForm pageForm = (MainpageForm) form;
		// load configuration
		config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);		
        log.info("Config object is : "+config);
        // if there's no config redirect to the installation page
        if (config == null) {
            try {
                response.sendRedirect("./admin/install.jsp");
                return null;
            }
            catch (IOException ex) {
                log.info("Installation failed.");
            }
        }
        // set template
		if (ServerUtil.isMultiDomainEnabled(config))
			template = ServerUtil.getDomainTemplate(request);
		else
			template = config.getTemplate();			        
        // set the config to the form
		pageForm.setConfig(config);
		// get the requested node name
		String name = request.getParameter("nodeName");
		name = ServerUtil.mapNameToDomainName(name, request.getServerName(),config);
		// we need the current session for the login
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("login");
		// username available, fetch user from db
		User user = null;		
		if (username != null) {
			user = (User) DAOManager.getInstance().getUserDAO().findByName(username);
		}
		// we got an existing locale
		if (session.getAttribute("currentLocale") != null) {
			// therefore it must be inside the request
			Locale locale = (Locale)session.getAttribute("currentLocale");			
			node = NodeHelper.locateNodeByName(name+"_"+locale);					
			// TODO : This is a little bit quirky here, maybe we should indicate a non existent node with a flag			
			if (node.getName().equals(resources.getString("content.error.contentname")))
				node = NodeHelper.locateNodeByName(name);			
		}
		// node is still null, locate by name now
		if (node == null)
			node = NodeHelper.locateNodeByName(name);		
		// put it ito the page form and into the  request
		pageForm.setNode(node);
		request.getSession().setAttribute("node", node);
		// the name as well
		request.getSession().setAttribute("nodeName", name);
		// wtf?
		if (node.getParent() == null)
			request.getSession().setAttribute("currentNode", node.getName());
		// if the property first child is set, jump to the first available children
		if (node.getFirstchild() != null)
			if (node.getFirstchild() && !node.getChildren().isEmpty()) {
				for (Iterator<Node> it = node.getChildren().iterator(); it.hasNext();) {
					Node currentNode = it.next();
					node = currentNode;
					break;
				}
			}
		
		log.debug("nodetype : " + node.getType());
		// finally prepare the content
		buildContent(request, response, pageForm, config, node,user);	
		// export content?
		if (request.getParameter("export") != null) {
			if (request.getParameter("export").equalsIgnoreCase("PDF")) {
				exportPDF(response, pageForm, config, node);
				return null;				
			}
			else if (request.getParameter("export").equalsIgnoreCase("XML")) {				
				exportXML(response, pageForm, node);				
				return null;				
			}			
		}
		// some plugins (like download for example) don't need a redirect and set the content name accordingly
		if (pageForm.getContent().getContentname().equals(PluginHelper.NO_REDIRECT))
			return null;
		// raw content requested?
		if (request.getParameter("raw") != null) {		
			dumpRawContent(request, response, pageForm, config);			
			return null;
		}
		// route to the main jsp of  the current selected template
		ActionForward af = new ActionForward();

		if (template != null) {
			if (config.isMaintenanceMode())
				af.setPath("/templates/" + template + "/maintenance.jsp");
			else {
//				if (request.getHeader("User-Agent").contains("Mobile") ||
//					request.getHeader("User-Agent").contains("Android")) {
//					af.setPath("/templates/" + template + "/mobileMain.jsp");
//				}
//				else {
					af.setPath("/templates/" + template + "/main.jsp");				
//				}
				
			}
		}
		else {
			if (config.isMaintenanceMode()) {
				af.setPath("/templates/" + config.getTemplate() + "/maintenance.jsp");				
			}
			else {
//				if (request.getHeader("User-Agent").contains("Mobile") ||
//					request.getHeader("User-Agent").contains("Android")) {
//					af.setPath("/templates/" + config.getTemplate() + "/mobileMain.jsp");
//				}
//				else {
					af.setPath("/templates/" + config.getTemplate() + "/main.jsp");					
				}
//			}
		}
		
		return af;
	}
	


	private void exportPDF(HttpServletResponse response, MainpageForm pageForm, Config config, Node node) {

		String appPath = config.getBasepath();	
		String xslLocation = appPath+"xsl/xhtml2fo.xsl";
		
		response.setHeader("Content-Disposition", "attachment; filename=" + node.getName());
		response.setContentType("application/pdf");
		
		try {
			DocumentBuilder.createDocument(pageForm.getContent().getContentstring(), MimeConstants.MIME_PDF, xslLocation, response.getOutputStream());
		}
		catch (IOException e) {
			log.info("Error creating pdf.");
		}
	}

	private void exportXML(HttpServletResponse response, MainpageForm pageForm, Node node) {

		ApplicationContext ctx = AppContext.getApplicationContext();
		
		NodeService nodeService = (NodeService)ctx.getBean("nodeService");
		NodeBean nodeBean = nodeService.findNodeById(node.getId());
		
		if (nodeBean.getContentBean() != null) {
			nodeBean.getContentBean().setContentname(pageForm.getContent().getContentname());
			nodeBean.getContentBean().setDescription(pageForm.getContent().getDescription());
			nodeBean.getContentBean().setContentstring(pageForm.getContent().getContentstring());
			nodeBean.getContentBean().setCreated(pageForm.getContent().getCreated());
			nodeBean.getContentBean().setLastmodified(pageForm.getContent().getLastmodified());
			nodeBean.getContentBean().setMetatags(pageForm.getContent().getMetatags());
			nodeBean.getContentBean().setPagename(pageForm.getContent().getPagename());
		}
			
		PageBean page = new PageBean();
		page.setNode(nodeBean);
		
		response.setContentType("text/xml");
		
		try {
			Marshaller m = (Marshaller)JAXBContext.newInstance(PageBean.class).createMarshaller();					
			m.marshal(page, response.getOutputStream());					
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void dumpRawContent(HttpServletRequest request, HttpServletResponse response, MainpageForm pageForm, Config config) {

		String protocol = CMSProperties.getInstance().getAppProps().getProperty("protocol");		
		String scriptPath = protocol+"://"+ServerUtil.getHostUrl(request)+"/"+config.getLocalPath()+"scripts/style.js";
		
		StringBuffer contentBuffer = new StringBuffer();
		

//		contentBuffer.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">");
//		contentBuffer.append("\n");
//		contentBuffer.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\">");
//		contentBuffer.append("\n");
//		contentBuffer.append("<script language=\"javascript\" src=\""+scriptPath+"\" type=\"text/javascript\"></script>");
//		contentBuffer.append("\n");
		contentBuffer.append(pageForm.getContent().getContentstring());
		
		try {
			PrintWriter out = response.getWriter();
			out.write(contentBuffer.toString());
		}
		catch (IOException e) {
			log.error("Could not write to output stream.");
		}
	}
	
    /**
     * Builds up the content for web page display
     *
     * @param request
     * @param form
     * @param config
     * @param node
     */
	private void buildContent(HttpServletRequest request, HttpServletResponse response, MainpageForm form, Config config, Node node, User user) {
		
		Content content = null;
		
		if (NodeHelper.hasAccessToNode(node, user)) {
			
			if (node.getType() == Node.TYPE_PLUGIN) {

				if (!PluginHelper.isInitialized()) {
					log.info("Initializing plugin classes.");
					PluginHelper.initIPluginClasses(this.getClass().getClassLoader());
				}

				/*
				 * 
				 * Now we iterate over all given parameters an check, if its a
				 * plugin parameter, if so we append it to the plugin params
				 * list, to pass them through.
				 */

				StringBuffer pluginParams = new StringBuffer(node.getPluginparams());

				for (Enumeration<?> e = request.getParameterNames(); e.hasMoreElements();) {

					String parameterName = (String) e.nextElement();

					if (parameterName.startsWith(AbstractPlugin.PLUGIN_PREFIX)) {
						pluginParams.append(",");
						pluginParams.append(parameterName);
						pluginParams.append("=");
						pluginParams.append(request.getParameter(parameterName));
					}

				}

				node.setPluginparams(pluginParams.toString());

				synchronized (request) {
					content = PluginHelper.getPluginContent(node, request, response);	
				}

			} 
			else if (node.getType() == Node.TYPE_CONTENT){
				
				content = node.getContent();

				if (content.getLastmodified() != null) {
					DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
					form.setLastModified(dateFormat.format(content.getLastmodified()));
				}

				String contentPath = "/"+config.getLocalPath()+config.getContentpath();
				
				log.info("replacing "+contentPath);
				
				String protocol = CMSProperties.getInstance().getAppProps().getProperty("protocol");
				String fullPath = protocol+"://"+ServerUtil.getHostUrl(request)+ "/"+config.getLocalPath()+config.getContentpath();
				
				log.info("with "+fullPath);
				
				content.setContentstring(StringUtil.replace(content.getContentstring(), contentPath, fullPath));
				content.setMetatags(StringUtil.replace(StringEscapeUtils.unescapeHtml(content.getMetatags()), "<", "\n<"));
				
				if (request.getParameter("highlight") != null) {
					
					String highlight = request.getParameter("highlight");					
					String highlighted = "<span class=\"highlight\">"+highlight+"</span>";					
					content.setContentstring(content.getContentstring().replaceAll(highlight, highlighted));
					
				}
				
			}
			
			if (content != null)
				request.getSession().setAttribute("contentId", content.getId());
			
			if (content == null) {
				
				content = new Content();
				
				content.setDescription(resources.getString("content.error.description"));
				content.setContentstring(resources.getString("content.error.contentstring"));
				content.setContentname(resources.getString("content.error.contentname"));
				
			}
			
			if (node.getImage() != null && node.getImage().length() > 0)
				form.setLogo(node.getImage());
			else if (node.getImage() == null || node.getImage().length() < 1) {
				if (node.getParent() != null)
					if (node.getParent().getImage() != null && node.getParent().getImage().length() > 0)
						form.setLogo(node.getParent().getImage());
			}

			if (node.getId() != null) {
				
				if (node.getParent() != null && node.getParent().getParent() != null)
					request.getSession().setAttribute("nodeId", node.getParent().getId());					
				else	
					request.getSession().setAttribute("nodeId", node.getId());

				
			}
				
			ServerUtil.logRequest(request, content, new Date(), (String) request.getSession().getAttribute("login"));

		} 
		else {			
			content = new Content();
			content.setDescription(resources.getString("content.accessdenied.description"));
			content.setContentstring(resources.getString("content.accessdenied.contentstring"));
			content.setContentname(resources.getString("content.accessdenied.contentname"));			
		}

		if (content.getMetatags() == null || content.getMetatags().equalsIgnoreCase("null"))
			content.setMetatags("");
		
		form.setSitename(config.getSitename());
		form.setContent(content);

		log.info("Displaying content " + form.getContent().getContentname());
	}

	/**
	 * Finds all nodes which fit to the search string given
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	public ActionForward searchNodes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		MainpageForm pageForm = (MainpageForm) form;

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		Content content = null;

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("login");
		String searchString = request.getParameter("search");
		
		log.info("searching for :" + searchString);
		
		List<?> foundNodes = NodeHelper.findMatchingNodes(searchString,request,response,this.getClass().getClassLoader(),username);

		log.info("Found " + foundNodes.size() + " nodes which are visible to current user.");

		ArrayList<String> results = new ArrayList<String>();
		
		// now iterate and strip the domain part of the nodes path if multiple domains are enabled 		
		for (Iterator<?> nodeIterator = foundNodes.iterator(); nodeIterator.hasNext();) {			
			Node currentNode = (Node) nodeIterator.next();			
			if (ServerUtil.isMultiDomainEnabled(config)) {
				results.add(currentNode.getPath().substring(currentNode.getPath().indexOf("/")+1));
			}
			else {
				results.add(currentNode.getPath());
			}			
		}
				
		pageForm.setSearchResults(results);

		content = new Content();
		content.setDescription(resources.getString("content.search.description"));

		if (!foundNodes.isEmpty())
			content.setContentstring(resources.getString("content.search.contentstring"));
		else
			content.setContentstring(resources.getString("content.search.nonodesfound"));
		
		content.setContentname(resources.getString("content.search.contentname"));
		pageForm.setContent(content);

		// / We need an empty node

		Node n = new Node();
		n.setParent(null);
		n.setName("Suchergebnisse.");
		n.setContent(content);

		pageForm.setNode(n);		
		ServerUtil.preventBrowserCaching(response);

		ActionForward af = new ActionForward();
		
		String template = null;
		
        // set template
		if (ServerUtil.isMultiDomainEnabled(config))
			template = ServerUtil.getDomainTemplate(request);
		else
			template = config.getTemplate();	
		
		if (template != null) {
			af.setPath("/templates/" + template + "/main.jsp");
		}
		else {
			af.setPath("/templates/" + config.getTemplate() + "/main.jsp");	
		}

		return af;
	}

	public ActionForward createView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		RequestUtil.dumpParams(request);
			
		String xmlPath = request.getParameter("file");
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		String xmlLocation = config.getBasepath()+xmlPath;
		String xslLocation = config.getBasepath()+"xsl/views.xsl";
		
		log.info("Opening xml from : "+xmlPath);
		
		String content = null;
		
		try {
			content = FileUtils.readFileToString(new File(xmlLocation));
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			
			ServletOutputStream sos = response.getOutputStream();			
			response.setContentType("text/html");		
			DocumentBuilder.applyXsltTransform(xslLocation, content, sos);			
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
		
	}
	
	public ActionForward showCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		MainpageForm pageForm = (MainpageForm) form;
		Content content = null;		
	
		String name = request.getParameter(Params.NODE_NAME);
		
		Node node = (Node)DAOManager.getInstance().getNodeDAO().findByName(name);
		
		if (node != null) {
			
			List<Node> nodes = BlogUtil.prepareCategory(node);
			
			StringBuffer tableBuffer = new StringBuffer();
			TagNameFilter spanTagFilter = new TagNameFilter("SPAN");
			
			for (Node currentNode : nodes) {

                if (currentNode.getType() == Node.TYPE_CONTENT) {
                    BlogUtil.createBlogEntry(tableBuffer, spanTagFilter, currentNode);
                }

			}

            if (tableBuffer.toString().length() == 0) { // happens, if there are no nodes
            	
            	if (node.getContent() != null) {
            		content = node.getContent();
            		pageForm.setNode(node);
            	}
            	else {
            		content = new Content();		    			
            		content.setContentname(resources.getString("blog.overview"));
        			content.setDescription(resources.getString("blog.overview")+node.getPath());
        			content.setLastmodified(new Date());
        			content.setPagename(resources.getString("blog.overview"));
        			content.setContentstring(resources.getString("blog.noentries"));	                            		

        			Node n = new Node();
        			n.setParent(null);
        			n.setName(resources.getString("blog.overview")+" : "+node.getName());
        			n.setContent(content);

        			pageForm.setNode(n);
            	
            	}
            	
            }
            else {            	
        		content = new Content();		    			
        		content.setContentname(resources.getString("blog.overview"));
    			content.setDescription(resources.getString("blog.overview")+node.getPath());
    			content.setLastmodified(new Date());
    			content.setPagename(resources.getString("blog.overview"));
    			content.setContentstring(tableBuffer.toString());
    			
    			Node n = new Node();
    			n.setParent(null);
    			n.setName(resources.getString("blog.overview")+" : "+node.getName());
    			n.setContent(content);

    			pageForm.setNode(n);

            }

            pageForm.setContent(content);
			
		}
		else {
			
			content = new Content();
			content.setDescription(resources.getString("content.error.description"));
			content.setContentstring(resources.getString("content.error.contentstring"));
			content.setContentname(resources.getString("content.error.contentname"));

			node = new Node();
			node.setChildren(null);
			node.setContent(content);
			node.setParent(null);
			node.setName(resources.getString("content.error.contentname"));
			node.setType(Node.TYPE_CONTENT);

            pageForm.setNode(node);
			
		}

		ServerUtil.preventBrowserCaching(response);

		ActionForward af = new ActionForward();
		af.setPath("/templates/" + config.getTemplate() + "/main.jsp");

		return af;

	}

	/**
	 * Performs a prototype like autocomplete search. This search simply checks
	 * the node name and returns a list if any matches ar being found.
	 */
	public ActionForward autoComplete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
		
		String query = request.getParameter("search");
		StringBuffer result = new StringBuffer();
		
		List<?> nodes = DAOManager.getInstance().getNodeDAO().findAllItems();
				
		if (ServerUtil.isMultiDomainEnabled(config)) {
			
			for (Iterator<?> it = nodes.iterator();it.hasNext();) {				
				Node node = (Node)it.next();
				// has a part and thus must be part of a multidomain node
				// now examine which domain this node belongs to
				if (node.getPath().contains("/")) { 
					String domainName = node.getPath().substring(0,node.getPath().indexOf("/"));
					log.debug("Node belongs to domain "+domainName);
					if (!request.getServerName().equalsIgnoreCase(domainName))
						it.remove();
				}
				
			}
			
		}
		
		// we need the current session for the login
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("login");
		// username available, fetch user from db
		User user = null;		
		if (username != null) {
			user = (User) DAOManager.getInstance().getUserDAO().findByName(username);
		}		
		// next eliminate nodes shich are not visible for the current user
		for (Iterator<?> it = nodes.iterator();it.hasNext();) {				
			Node node = (Node)it.next();							
			if (!NodeHelper.hasAccessToNode(node, user)) {
				it.remove();
			}							
		}
		
		if (query != null) {
			
			log.debug("Found "+nodes.size()+" nodes.");
			result.append("<ul>");			
			for (Object o : nodes) {				
				Node n = (Node)o;
				if (n.getName().toLowerCase().contains(query.toLowerCase()))
					result.append("<li>"+n.getName()+"</li>");				
			}
			result.append("</ul>");
			
		}
		
		try {
			PrintWriter out = response.getWriter();
			out.write(result.toString());
		}
		catch (IOException e) {
			log.error("Could not write to servlet output.");
		}
		
		return null;
	}
	
}
