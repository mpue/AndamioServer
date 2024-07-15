package org.pmedv.plugins.objects;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Question", propOrder = {
		"name",
	    "text",
	    "type",
	    "answers",
	    "selectedAnswer"
	})

public class Question {
	
	private String name;
	private String text;	
	private QuestionType type;	
	private ArrayList<Answer> answers;	
	private Answer selectedAnswer;
	
	public Question() {
		answers = new ArrayList<Answer>();
	}
	
	public Question(String text) {
		this();
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
	 * @return the type
	 */
	public QuestionType getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(QuestionType type) {
		this.type = type;
	}
	
	/**
	 * @return the answers
	 */
	public ArrayList<Answer> getAnswers() {
		return answers;
	}
	
	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}

	/**
	 * @return the selectedAnswer
	 */
	public Answer getSelectedAnswer() {
		return selectedAnswer;
	}

	
	/**
	 * @param selectedAnswer the selectedAnswer to set
	 */
	public void setSelectedAnswer(Answer selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
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
			
			Question q = (Question) obj;		
			
			if (q.getName() != null && this.name != null)
				if (this.getName().equals(q.getName())) {
					return true;
				}				
			
		}
		return false;
	}
	
}
