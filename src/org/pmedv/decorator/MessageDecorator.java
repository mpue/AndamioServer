package org.pmedv.decorator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.pmedv.beans.objects.MessageBean;
import org.pmedv.cms.common.CMSProperties;


public class MessageDecorator extends CustomizableTableDecorator {
	
	public String getDeleteLink() {
		
		MessageBean message = (MessageBean)getCurrentRowObject();
		String protocol = CMSProperties.getInstance().getAppProps().getProperty("protocol");
		String imageUrl = protocol+"://"+myConfig.getHostname()+"/"+myConfig.getLocalPath()+"admin/themes/experience/icons/small/delete.png";		
		String image = "<img src=\""+imageUrl+"\" border=\"0\" align=\"middle\" onClick=\"confirmDelete("+message.getId()+");\">";

		return image;
	}
	
	
	public String getReceivedDate() {
		
		MessageBean message = (MessageBean)getCurrentRowObject();
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
		return dateFormat.format(message.getReceived());
			
	}
	
	public String getMessageLink() {
		
		MessageBean message = (MessageBean)getCurrentRowObject();
		
		String href = "<a href=\"#\" onClick=\"showMessage("+message.getId()+");\">"+message.getSubject()+"</a>";
		
		StringBuffer messageEntry = new StringBuffer();		
		messageEntry.append(href);
		
//		messageEntry.append("<ul class=\"messageEntry\">");
//		messageEntry.append("<li>");
//		messageEntry.append(message.getSubject());
//		messageEntry.append("<ul class=\"reply\">");
//		messageEntry.append("<li>");
//		messageEntry.append(message.getBody());
//		messageEntry.append("</li>");
//		messageEntry.append("<li>");
//		messageEntry.append("<form id=\"messageForm_"+message.getId()+"\" name=\"messageForm_"+message.getId()+"\">");
//		messageEntry.append("<input type=\"hidden\" name=\"receiver\" value=\""+message.getFrom()+"\">");
//		messageEntry.append("<input type=\"hidden\" name=\"subject\" value=\"Re : "+message.getSubject()+"\">");
//		messageEntry.append("<input type=\"hidden\" name=\"message_id\" value=\""+message.getId()+"\">");
//		messageEntry.append("<div id=\"messageBox\">");
//		messageEntry.append("<textarea rows=\"12\" cols=\"70\" name=\"message\" id=\"message\"></textarea>");		
//		messageEntry.append("</div");
//		messageEntry.append("<div id=\"submitButton\">");
//		messageEntry.append("<input type=\"button\" value=\"Abschicken\" onClick=\"replyMessage("+message.getId()+");\"/>");
//		messageEntry.append("</div");		
//		messageEntry.append("</form>");		
//		messageEntry.append("</li>");
//		messageEntry.append("</ul>");		
//		messageEntry.append("</li>");
//		messageEntry.append("</ul>");
		
		return messageEntry.toString();
		
	}
}
