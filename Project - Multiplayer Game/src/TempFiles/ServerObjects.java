package TempFiles;

import java.io.Serializable;
import java.net.Socket;
public interface ServerObjects{

	class Municipality implements Serializable{
		    //our municipalities are very militant, so they have an attribute troops, they can also be cities, be valuable and have an owner!
		    private int troops = 1;
		    private boolean city;
		    //value for later implementation
		    private int value = 1;
		    private final int id;
		    private User owner;
	
		    public Municipality(int id, User owner, boolean city){
		        this.city = city;
		        this.owner = owner;
		        this.id = id;
		    }
	
		    //boring gets and sets
		    public User getOwner(){
		        return owner;
		    }
	
		    public void setOwner(User owner){
		        this.owner = owner;
		    }
	
		    public int getTroops() {
		        return troops;
		    }
	
		    public int getId() {
		        return id;
		    }
	
		    public boolean getCity(){
		        return city;
		    }
	
		    public int getValue(){
		        return  value;
		    }
		}
		    
	    class User implements Serializable{
	        private int userid;
	        private String username;
	        private int playerNr;
	        private int gIn;
	        private int gOut;
	        private Socket chatSocketOut;
	
	        public User(int userid, String username){
	            this.userid = userid;
	            this.username = username;
	        }
	        
	       /* public void setGameSocket(Socket socket) {
	        	gameSocket = socket;
	        }
	        
	        /*public void setChatSocketIn(Socket socket) {
	        	chatSocketIn = socket;
	        }*/
	        
	        public void setGIn(int gIn) {
	        	this.gIn = gIn;
	        }
	        public int getGIn() {
	        	return gIn;
	        }
	        public int getGOut() {
	        	return gOut;
	        }
	        public void setChatSockeOut(Socket socket) {
	        	chatSocketOut = socket;
	        }
	        
	       // public 
	
	        public int getUserid() {
	            return userid;
	        }
	
	        public String getUsername() {
	            return username;
	        }
	        
	        public void setPlayer(int player) {
	        	//playerNr = this.joinGame();
	        }
	        
	        public int getPlayer() {
	        	return playerNr;
	        }
	        public String toString(){
	            return "ID: " + userid + ", username: " + username;
	        }
	
			public void setGOut(int i) {
				this.gOut = i;
				
			}
	  	}
	
		    //a game class which needs further implementation
		    class Game implements Serializable{
		        private User[] users;
		        private int curUser;
		        private int turn = 0;
		        private int endturn = 0;
		        private int gameId = 0;
		        public Game(User[] users){
		            this.users = users;
		            curUser = 0;
		        }
	
				public User[] getUsers() {
					// TODO Auto-generated method stub
					return users;
				}
				
				public void setGameId(int gameId) {
					this.gameId = gameId;
				}
				
				public int getGameId() {
					return gameId;
				}
		    }
}
