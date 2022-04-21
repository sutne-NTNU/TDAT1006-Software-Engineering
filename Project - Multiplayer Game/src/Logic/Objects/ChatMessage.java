package Logic.Objects;

import java.io.Serializable;

/**
 * A message to be sent in the chat
 */
public class ChatMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	
		private int messageID;
		private User user;
		private String timestamp;
		private String messagetext;
		public ChatMessage(String timestamp, User user, String messagetext) {
			this.messagetext = messagetext;
			this.user = user;
			this.timestamp = timestamp;  // guiChat-class creates this
		}
		public int getMessageID() {return messageID;}
		public User getUser(){return user;}
		public String getTimestamp(){return timestamp;}
		public String getMessagetext(){return messagetext;}

	/**
	 *
	 * @return the message with timestamp, user who sent it and the party of the user as a string
	 */
		@Override
		public String toString(){
			return timestamp + " " + user.getUsername() + " - " + user.getParty().getPartyName() + " - " + messagetext;
		}
	}
