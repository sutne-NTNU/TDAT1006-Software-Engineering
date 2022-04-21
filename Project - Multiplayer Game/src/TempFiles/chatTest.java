package TempFiles;

import serverclasses.*;
import java.util.ArrayList;

//small test klient
public class chatTest {
	static ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
	
	//a local method that the user needs on their computer, 
	//gets the entire gamechat for the game they ar ein
	
	public static void sendChat(ArrayList<ChatMessage> chat) { 
		boolean newM = true;
		for(ChatMessage chatM : chat) {
			newM = true;
			for(ChatMessage messagesM : messages) {
				if(chatM.getMessageID() == messagesM.getMessageID()) {
					newM = false;
				}	
			}
			if(newM) {
				System.out.println(chatM); 
				messages.add(chatM);
			}
		}
	}
	
	/*
	public static void main(String[] args) throws Exception{
		Long start = System.currentTimeMillis();
		Database db = new Database(); //is only used when testing to boot up the connectionPools
		int pause = 1000;//affects the pauses between messages
		
		int userID = 1;
		int user2ID = 4;
		int gameID = Database.select_GameID(userID);

		Chat testChat = new Chat(gameID);
		Thread chat = new Thread(testChat);
		System.out.println(" *** Starter chat ****");
		Thread.sleep(pause); 
		chat.start(); //starts the chat thread
		
		System.out.println(Chat.sendMessage(userID, "HeiHei!"));
		Thread.sleep(pause); 

		System.out.println(Chat.sendMessage(user2ID, "fack off"));
		Thread.sleep(pause);
		
		System.out.println(Chat.sendMessage(userID, ":,("));
		Thread.sleep(pause); 
		
		System.out.println(Chat.sendMessage(user2ID, "buhuu"));
		Thread.sleep(pause); 
		
		System.out.println(Chat.sendMessage(userID, "dette er ikke særlig hyggelig"));
		Thread.sleep(pause);//gives time for last message to be sent and registered

		System.out.println("\n\nStopping Thread\n\n");
		testChat.stop();
		chat.interrupt();
		
		Thread.sleep(pause);//gives time for thread to pull out before shutting down the database

		System.out.println("\n\nShutting down Database\n\n");
		db.shutDown();



		Long finish = System.currentTimeMillis();
		long time = finish-start;
		double workingTime = (((double)time-(7*pause))/1000); //takes away the thread.sleep times
		System.out.println("Time to complete chatTest: " + workingTime + " seconds");
	}*/
}
