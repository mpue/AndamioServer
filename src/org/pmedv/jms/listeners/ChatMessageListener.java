package org.pmedv.jms.listeners;

import java.io.StringReader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.context.AppContext;
import org.pmedv.jms.chat.ChatMessage;
import org.pmedv.session.ChatUserList;
import org.springframework.context.ApplicationContext;

public class ChatMessageListener implements MessageListener {

	private static final ApplicationContext ctx = AppContext.getApplicationContext();
	
	private static final Log log = LogFactory.getLog(ChatMessageListener.class); 
	
	private static final String TYPE_JOIN  = "join";
	private static final String TYPE_LEAVE = "leave";
	private static final String TYPE_CHAT  = "chat";
	
	@Override
	public void onMessage(Message m) {

		ChatUserList userlist = (ChatUserList)ctx.getBean("chatUserList");
		
		try {

			String propertyName = "message";
			String messageBody = m.getStringProperty(propertyName);

			Unmarshaller u = (Unmarshaller)JAXBContext.newInstance(ChatMessage.class).createUnmarshaller();			
			ChatMessage cm = (ChatMessage)u.unmarshal(new StringReader(messageBody));
			
			if (cm.getType().equalsIgnoreCase(TYPE_JOIN)) {

				if (!userlist.getUserNames().contains(cm.getFrom())) {
					userlist.getUserNames().add(cm.getFrom());
				}
				
			}
			else if (cm.getType().equalsIgnoreCase(TYPE_LEAVE)) {
				
				if (userlist.getUserNames().contains(cm.getFrom())) {
					userlist.getUserNames().remove(cm.getFrom());
				}
				
				
			}
			else if (cm.getType().equalsIgnoreCase(TYPE_CHAT)) {
				
			}
			
			log.debug("Received chat message : ");
			log.debug("Type : "+ cm.getType());
			log.debug("From : "+cm.getFrom());
			log.debug("Content : "+  cm.getValue());

			if (log.isDebugEnabled()) {
				
				log.debug("Userlist contains now the following users : ");
				
				for (String username : userlist.getUserNames()) {
					log.debug(username);
				}
				
			}
			
		}
		catch (JMSException e) {
			e.printStackTrace();
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}
