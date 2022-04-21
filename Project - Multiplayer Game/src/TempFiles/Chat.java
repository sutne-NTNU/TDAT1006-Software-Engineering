package TempFiles;

import java.util.ArrayList;

import serverclasses.ChatMessage;
import serverclasses.Database;

//public class Chat{
//	private int gameID;
//	volatile boolean exit;
//	
//	public Chat(int gameID){ 
//		this.gameID = gameID;
//	}
//	
//	
//	
//	//sends a message to the chat table
//	public static boolean sendMessage(int userID, String messagetext, String timestamp){
////		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
////		Date date = new Date();
////		String timestamp = dateFormat.format(date);
//		
//		messagetext = messagetext.trim();
//		if(messagetext.contentEquals("")) {return true;} //if message is empty, will return true because nothing will be sent
//		int gameID = Database.select_GameID(userID);
//		
//		return Database.insert_ChatMessage(gameID, userID, messagetext, timestamp);
//	}
//	
//	public ArrayList<ChatMessage> getChat() {
//		return Database.select_AllMessagesFromGame(gameID);
//	}
//}


