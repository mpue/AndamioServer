package org.pmedv.tests.sandbox;

import org.apache.commons.lang.StringEscapeUtils;


public class EscapeTest {

	
	static String metatags = 
	"<meta name=\"author\" content=\"\">"+"\n"+
	"<meta name=\"publisher\" content=\"Schorf Sanueder\">"+"\n"+
	"<meta name=\"copyright\" content=\"Schorf Sanueder\">"+"\n"+
	"<meta name=\"description\" content=\"Blah\">"+"\n"+
	"<meta name=\"keywords\" content=\"\">"+"\n"+
	"<meta name=\"page-topic\" content=\"\">"+"\n"+
	"<meta name=\"page-type\" content=\"\">"+"\n"+
	"<meta name=\"audience\" content=\"Alle\">"+"\n"+
	"<meta http-equiv=\"content-language\" content=\"de\">"+"\n"+
	"<meta name=\"robots\" content=\"index, follow\">"+"\n"+
	"<meta name=\"DC.Creator\" content=\"Schorf Sanueder\">"+"\n"+
	"<meta name=\"DC.Publisher\" content=\"Schorf Sanueder\">"+"\n"+
	"<meta name=\"DC.Rights\" content=\"Schorf Sanueder\">"+"\n"+
	"<meta name=\"DC.Description\" content=\"\">"+"\n"+
	"<meta name=\"DC.Language\" content=\"de\">"+ "\n";
	
	public static void main(String[] args) {
		
		System.out.println(StringEscapeUtils.escapeHtml(metatags));
		
	}
	
}
