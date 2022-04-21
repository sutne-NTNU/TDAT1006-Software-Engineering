package fx.controllers;

import Clientclasses.Client;
import Logic.Objects.ChatMessage;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GuiChat {

    String bold 	= "-fx-font-weight: bold;"; 
    String size  	= "-fx-font-size: 16;";

	/** Method that formats the message that a user sends, as [timestamp: -- username: -- messagetext: --].
	 * @param allmessages TextFlow where all message components are added to make a complete message (timestamp, username, messagetext). The method gets timestamp and username from a different class.
	 * @param themessage String that contains the text a user sends.
	 * @param messagetosend TextField where the user writes the message text.
	 * @return returns a ChatMessage-object that is sent to the server.
	 */
    public ChatMessage sendMessage(TextFlow allmessages, String themessage, TextField messagetosend){
       
    	//Containers that will hold message components (time, name, partyname, message)
        Text timestamp 	= new Text();
        Text sender 	= new Text();
//      Text partyname 	= new Text(); 
        Text message 	= new Text();
  
        //getting local time 
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime now = LocalTime.now();
        String time = now.format(dtf);
        
        //getting sender name
        String name = Client.getUser().getUsername(); 

        //Set texts in containers initialized above
        timestamp.setText(time + " ");
        sender 	 .setText(name);
//      partyname.setText("[" + Client.getUser().getParty().getPartyName() + "]"); //set party name
        message  .setText(": " + themessage);

        //Set styles of texts
        timestamp.setStyle(size); 
        sender	 .setStyle(bold + size); 
//      partyname.setStyle(msgfont); 
        message	 .setStyle(size); 	
//      Party Colour for name and partyname
        sender	 .setFill(Color.web(Client.getUser().getParty().getColour())); 
        sender	 .setStroke(Color.web("#868686"));
        sender	 .setStrokeWidth(0.1);
//      partyname.setFill(Color.web(Client.getUser().getParty().getColour()));

        allmessages.getChildren().addAll(timestamp, sender, /*partyname, */message); // adding all messages to textflow in scrollpane
        messagetosend.setText(""); //after it is sent, remove what is written in textbox for the user

        return new ChatMessage(time, Client.getUser(), themessage);
    }

	/** Method that formats the message that a user recieves, and displays it in the chat-area along previously sent messages
	 * @param recievedMessage A ChatMessage-object with the timestamp, user-object and messagetext of the recieved message
	 * @param allmessages TextFlow where all message components are added to make a complete message (timestamp, username, messagetext).
	 */
    public void messageReceived(ChatMessage recievedMessage, TextFlow allmessages){
    	
    	//Containers that will hold message components (time, name, partyname, message)
        Text timesent 	 = new Text();
        Text sender 	 = new Text();
        //Text senderParty = new Text();
        Text message 	 = new Text();

        //Gets the strings from the ChatMessage Object
    	String time 			 = recievedMessage.getTimestamp();
        String senderName 	 	 = recievedMessage.getUser().getUsername();
        //String senderPartyName	 = recievedMessage.getUser().getParty().getPartyName();
        String theMessage 				 = recievedMessage.getMessagetext();

        //Set texts in containers initialized above
        timesent.setText(time + " ");
        sender 	.setText(senderName);
        //senderParty	.setText("[" + senderPartyName + "]"); //set party name
        message	.setText(": " + theMessage);
      

        // set styles of texts
        timesent.setStyle(size); 
        sender.setStyle(bold + size);
        //senderParty	.setStyle(msgfont); 
        message.setStyle(size);
        //Party Colour for name and partyname
        sender.setFill(Color.web(recievedMessage.getUser().getParty().getColour()));
        sender.setStroke(Color.web("#868686"));
        sender.setStrokeWidth(0.1);
        //senderParty .setFill(Color.web(recievedMessage.getUser().getParty().getColour())); 

        allmessages.getChildren().addAll(timesent, sender, /*senderParty, */message); // adding all messages to textflow in scrollpane
    }

	/** Method that transforms a message if it is a keyword for emotes, or if it needs to be senored. Empty or null-messages are not sent
	 * @param message The message a user sends
	 * @return returns a message transformed if it fulfills requirements for transformations
	 */
    public String transformMessage(String message){
    	if(message.equals("") || message == null) {
    		return null;
    	}
    	else if(message.contentEquals("bigchung"))
        {
        	message = "\n ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣧⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⣧⠀⠀⠀⢰⡿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⡟⡆⠀⠀⣿⡇⢻⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⠀⣿⠀⢰⣿⡇⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⡄⢸⠀⢸⣿⡇⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣿⡇⢸⡄⠸⣿⡇⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⣿⢸⡅⠀⣿⢠⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿⣥⣾⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⡿⡿⣿⣿⡿⡅⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠉⠀⠉⡙⢔⠛⣟⢋⠦⢵⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⣄⠀⠀⠁⣿⣯⡥⠃⠀⢳⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⡇⠀⠀⠀⠐⠠⠊⢀⠀⢸⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⣿⣿⡿⠀⠀⠀⠀⠀⠈⠁⠀⠀⠘⣿⣄⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⣠⣿⣿⣿⣿⣿⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣷⡀⠀⠀⠀ ⠀⠀⠀⠀⣾⣿⣿⣿⣿⣿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿⣧⠀⠀ ⠀⠀⠀⡜⣭⠤⢍⣿⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⢛⢭⣗⠀ ⠀⠀⠀⠁⠈⠀⠀⣀⠝⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠠⠀⠀⠰⡅ ⠀⠀⠀⢀⠀⠀⡀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠔⠠⡕⠀ ⠀⠀⠀⠀⣿⣷⣶⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠀⠀⠀⠀ ⠀⠀⠀⠀⠘⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠰⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⠀⠈⢿⣿⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠊⠉⢆⠀⠀⠀⠀ ⠀⢀⠤⠀⠀⢤⣤⣽⣿⣿⣦⣀⢀⡠⢤⡤⠄⠀⠒⠀⠁⠀⠀⠀⢘⠔⠀⠀⠀⠀ ⠀⠀⠀⡐⠈⠁⠈⠛⣛⠿⠟⠑⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠉⠑⠒⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀";
        }
    	else if(message.contentEquals("gnome"))
        {
        	message = "\n████████▓▓████████▓▓█████████████\n" + 
        			"██████▓▓█████████████▓███████████\n" + 
        			"█████▓███████████████▓███████████\n" + 
        			"████▓█████████████████▒██████████\n" + 
        			"████▓▓▓▒░░▒▒▒▒▒▒▒▒▒▒▒▒▒██████████\n" + 
        			"████▒▒░▒▒░▒▒▗▒▒▒▒▒▒▗▒▒▒██████████\n" + 
        			"████▒▒▒░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██████████\n" + 
        			"████░▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█████████\n" + 
        			"█████▒▒▒▒▒▒▒▒▃▒░░░▒▃▒▒███████████\n" + 
        			"██████▓▒▒▒▒▒▃▒▒▒▒▒▒▒▃░▒▒█████████\n" + 
        			"███████▒▒▒▒░▒▒▒▒▒▒▒▒▒▒░██████████\n" + 
        			"█████████▒░░░░░░░░░░░░░██████████\n" + 
        			"██████████░▒░░░░░░░░░░██▓▒▒▒▒▒▓▒▓\n" + 
        			"▓▓█████████████▒░░░▒████▒▒▒▒▒▓▓▒▓\n" + 
        			"▒▒▒▒▒▒▓███████████████████▒░░▒▒▒█\n" + 
        			"▒▒▒▒▒▓▓▒▒█████████████▒████░▒████\n" + 
        			"█▒▒▒▓▓▒▒▒████████████▒███████████\n" + 
        			"███▓▒▓▓▒█████████████████████████\n" + 
        			"░░█▀▀░░█▄░█░█▀▀█░█▄░▄█░█▀▀░█▀▄\n" + 
        			"░░█░▀█░█░▀█░█░░█░█░▀░█░█▀▀░█░█\n" + 
        			"░░▀▀▀░░▀░░▀░▀▀▀▀░▀░░░▀░▀▀▀░▀▀░";
        }
    	else if(message.contentEquals("lennygun"))
        {
        	message = "̿̿ ̿̿ ̿̿ ̿'̿'\\̵͇̿̿\\з= ( ▀ ͜͞ʖ▀) =ε/̵͇̿̿/’̿’̿ ̿ ̿̿ ̿̿ ̿̿";
        }
    	else if(message.contentEquals("lenny"))
        {
        	message = "( ͡◉ ͜ʖ ͡◉)";
        }else if(message.contentEquals("actually"))
		{
			message = "(°ロ°)☝";
		}else if(message.contentEquals("ganggang")){
    		message = "( ͡°( ͡° ͜ʖ( ͡° ͜ʖ ͡°)ʖ ͡°) ͡°)";
		}else if(message.contentEquals("sans")){
    		message = "░░░░░▄▄▀▀▀▀▀▀▀▀▀▄▄░░░░░\n" +
					  "░░░░█░░░░░░░░░░░░░█░░░░\n" +
					  "░░░█░░░░░░░░░░▄▄▄░░█░░░\n" +
					  "░░░█░░▄▄▄░░▄░░███░░█░░░\n" +
					  "░░░▄█░▄░░░▀▀▀░░░▄░█▄░░░\n" +
					  "░░░█░░▀█▀█▀█▀█▀█▀░░█░░░\n" +
					  "░░░▄██▄▄▀▀▀▀▀▀▀▄▄██▄░░░\n" +
					  "░▄█░█▀▀█▀▀▀█▀▀▀█▀▀█░█▄░";
		}else if(message.contentEquals("fightz"))
		{
			message = "🗡"; //"\uD83D\uDDE1"
		}else if(message.contentEquals("peachez"))
		{
			message = "🍑"; //"\uD83C\uDF51"
		}else if(message.contentEquals(("snakez")))
		{
			message = "🐍"; //"\uD83D\uDC0D"
		}else if(message.contentEquals("18+"))
		{
			message = "\uD83D\uDD1E";
		}else if(message.contentEquals("smallchung"))
		{
			message = "🐰"; //"\uD83D\uDC30"
		}else if(message.contentEquals("diamondz"))
		{
			message = "💎"; //"\uD83D\uDC8E"
		}else if(message.contentEquals("momoneymoproblems"))
		{
			message = "💸"; //"\uD83D\uDCB8"
		}else if(message.contentEquals("raygun"))
		{
			message = "\uD83D\uDD2B";
		}else if(message.contentEquals(("cig")))
		{
			message = "\uD83D\uDEAC";
		}else if(message.contentEquals("pleasedont"))
		{
			message = "\uD83E\uDD22";
		}else if(message.contentEquals("syringe"))
		{
    		message = "\uD83D\uDC89";
		}else if(message.contentEquals(("thumbsup")))
		{
			message = "\uD83D\uDC4D";
		}else if(message.contentEquals("thumbsdown"))
		{
			message = "\uD83D\uDC4E";
		}else if(message.contentEquals("FU"))
		{
			message = "\uD83D\uDCA9";
		}else if(message.contentEquals("eggplant"))
		{
			message = "\uD83C\uDF46";
		}else if(message.contentEquals("squidward"))
		{
			message = "\uD83E\uDD91";
		}

		if(message.toLowerCase().contains("shit"))	{message = message.replace("shit", "s***");}
    	if(message.toLowerCase().contains("hore"))  {message = message.replace("hore", "h***");}
    	if(message.toLowerCase().contains("cunt"))	{message = message.replace("cunt", "c***");}
    	if(message.toLowerCase().contains("fuck"))	{message = message.replace("fuck", "f***");}
    	if(message.toLowerCase().contains("fitte"))	{message = message.replace("fitte", "f***e");}
    	if(message.toLowerCase().contains("faen"))	{message = message.replace("faen", "f***");}
    	if(message.toLowerCase().contains("hei"))		{message = message.replace("hei", "\nThe Industrial Revolution and its consequences have been a disaster for the" +
    			"   human race. They have greatly increased the life-expectancy of those of us" + 
    			"   who live in “advanced” countries, but they have destabilized society, have" + 
    			"   made life unfulfilling, have subjected human beings to indignities, have led" + 
    			"   to widespread psychological suffering (in the Third World to physical" + 
    			"   suffering as well) and have inflicted severe damage on the natural world. The" + 
    			"   continued development of technology will worsen the situation. It will" + 
    			"   certainly subject human beings to greater indignities and inflict greater" + 
    			"   damage on the natural world, it will probably lead to greater social" + 
    			"   disruption and psychological suffering, and it may lead to increased physical" + 
    			"   suffering even in “advanced” countries.");}
    
    	return message + "\n";
    }
}

