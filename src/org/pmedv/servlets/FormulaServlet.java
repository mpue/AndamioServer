package org.pmedv.servlets;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.Icon;
import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.ugent.caagt.jmathtex.TeXConstants;
import be.ugent.caagt.jmathtex.TeXFormula;

/**
 * <p>
 * This is an  implementation of a formula generator servlet.The according
 * url mapping has to be placed inside the web.xml. 
 * </p>
 * <p>
 * This Servlet is based on the JMathTex library which can be found at 
 * </p>
 * <p>
 * <a href="http://jmathtex.sourceforge.net/">http://jmathtex.sourceforge.net/</a>
 * </p>
 * <p>
 * 	The formula has to be written in latex math notation and passed as url parameter named &quot;formula&quot;
 * such that the call to the servlet looks somewhat like:
 * </p>
 * <p>
 * <b>http://pathToYourWebApp//FormulaServlet?formula=\frac{a}{b}</b> 
 * </p>

 * @author Matthias Pueski (16.05.2010)
 *
 */
public class FormulaServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = -2292677373810767618L;

	public static final Log log = LogFactory.getLog(FormulaServlet.class);

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public FormulaServlet() {
		super();
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String formulaString = request.getParameter("formula");

		if (formulaString != null && formulaString.length() > 0) {

			log.info("Got formula : "+formulaString);
			
			TeXFormula formula = new TeXFormula(formulaString);


			Icon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 25);
			BufferedImage image = new BufferedImage(icon.getIconWidth()+5, icon.getIconHeight()+5, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = image.createGraphics();
			icon.paintIcon(new JLabel(), g2, 0, 0); // component can't be null

			response.setContentType("image/png");

			try {
				ImageIO.write(image, "png", response.getOutputStream());
			}
			catch (IOException ex) {
				log.error("An I/O exception occured : ");
				log.error(ex.getMessage());
			}

		}

	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}