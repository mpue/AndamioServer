package org.pmedv.plugins.objects;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Answer", propOrder = {
    "text",
    "name"
})

public class Answer {
	
	private String text;
	private String name;

	public Answer() {		
	}
	
	public Answer(String text) {
		this.text = text;
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
		    return true;
		}
		if ((obj != null) && (obj.getClass().equals(this.getClass()))) {
			
			Answer a = (Answer) obj;		
						
			if (a.getName() != null && this.name != null)
				if (this.getName().equals(a.getName())) {
					return true;
				}				
			
		}
		return false;
	}

	
}
