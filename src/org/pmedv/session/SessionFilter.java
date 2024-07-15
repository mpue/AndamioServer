package org.pmedv.session;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionFilter implements Filter {

	private static final Log log = LogFactory.getLog(SessionFilter.class); 
	
	@Override
	public void destroy() {		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		
		for (Enumeration<?> e = req.getHeaderNames();e.hasMoreElements();) {
			String header = (String)e.nextElement();
			log.info(header+"="+req.getHeader(header));
		}
		
		chain.doFilter(request, response);		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {	
	}

}
