package org.pmedv.plugins.objects;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "Interview", propOrder = {
    "text",
    "name",
    "questions",
    "selectedQuestion",
    "participatedUsers"
})

public class Interview {

	private String text;
	private String name;
	private ArrayList<Question> questions;
	private Question selectedQuestion;
	
	private ArrayList<String> participatedUsers;
	
	public Interview() {
		questions = new ArrayList<Question>();
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
	
	/**
	 * @return the questions
	 */
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	
	/**
	 * @param questions the questions to set
	 */
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	/**
	 * @return the selectedQuestion
	 */
	public Question getSelectedQuestion() {
		return selectedQuestion;
	}

	
	/**
	 * @param selectedQuestion the selectedQuestion to set
	 */
	public void setSelectedQuestion(Question selectedQuestion) {
		this.selectedQuestion = selectedQuestion;
	}

	/**
	 * @return the participatedUsers
	 */
	public ArrayList<String> getParticipatedUsers() {
	
		return participatedUsers;
	}

	
	/**
	 * @param participatedUsers the participatedUsers to set
	 */
	public void setParticipatedUsers(ArrayList<String> participatedUsers) {
	
		this.participatedUsers = participatedUsers;
	}

}
