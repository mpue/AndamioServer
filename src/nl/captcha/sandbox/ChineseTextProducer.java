/*
 * Created on Sep 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package nl.captcha.sandbox;

import nl.captcha.text.imp.DefaultTextCreator;



/**
 * @author you
 *
 * TextProducer Implementation that will return chinese characters..
 * 
 */
public class ChineseTextProducer extends DefaultTextCreator {

	private String[] simplfiedC = new String[] {
		"sching schang schung",
		};
		
		
	public String getText(){
		return simplfiedC[generator.nextInt(simplfiedC.length) ];
	}

	 
	
}
