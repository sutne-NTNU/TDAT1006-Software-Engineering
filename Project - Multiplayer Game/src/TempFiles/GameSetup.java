package TempFiles;

import serverclasses.*;
import java.sql.*;
import java.util.Properties;
import Logic.User;

public class GameSetup {
    Connection con;
    PreparedStatement pstmt;
    PreparedStatement pstmt_the_sequal;
    ResultSet res;
    int g_id;
    User[] users = new User[2];

    public GameSetup(){
        final String database = "sebastai";
        final String username = "sebastai";
        final String password =  Cleanup.getProperty("sebpassword");
        
        try{
            con = DriverManager.getConnection("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/" + database, username, password);
            con.setAutoCommit(false);
        }catch(Exception e){
        	Cleanup.writeMessage(e,  "GameSetup()");
            Cleanup.closeConnection(con);
        }
    }
    
    public boolean game_IsActive() {
    	try{
    		con.setSavepoint();
        	pstmt = con.prepareStatement("SELECT game_status FROM game");
            res = pstmt.executeQuery();
            con.commit();
            if(res.next() && res.getInt("game_status") == 1) {
            	return true;
            }else {
            	return false; 
            }
        }catch(SQLException e){
        	Cleanup.rollBack(con);
        	Cleanup.writeMessage(e,  "game_IsActive()");
        }finally{
        	Cleanup.closeResources(res, pstmt);
        }
    	return false;
    }
    
    public void startGame() {
    	String stmt = "Select max(g_id) from GameData;";
        try{
            con.setSavepoint();
            pstmt = con.prepareStatement("UPDATE game SET game_status = ?");
            pstmt.setInt(1, 1);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(stmt);
            res = pstmt.executeQuery();
            res.next();
            g_id = res.getInt("g_id") +1;
            con.commit();
        }catch(SQLException e){
        	Cleanup.rollBack(con);
        	Cleanup.writeMessage(e,  "startGame()");
        }finally {
            Cleanup.closeResources(pstmt, res);
        }
    }
    
    public boolean player_IsActive(int player){
		try{
			con.setSavepoint();
			pstmt = con.prepareStatement("SELECT active FROM player WHERE player_nr = ?");
			pstmt.setInt(1, player);
			res = pstmt.executeQuery();
			con.commit();
			if(res.next() && res.getInt("active") == 1) {
				return true;
			}else{
				return false;
			}
		}catch(SQLException e){
			Cleanup.rollBack(con);
			Cleanup.writeMessage(e,  "player_IsActive()");
			return false;
		}finally {
			Cleanup.closeResources(pstmt, res);
		}
	}
    public void waitForOpponent() {
    	while(!game_IsActive()) {} //probably not how you're supposed to do this
    }
    
    public int joinGame() {
    	int player_nr = -1;
    	try{ 
	    	con.setSavepoint();
	    	pstmt_the_sequal = con.prepareStatement("UPDATE player SET active = 1 WHERE player_nr = ?");
	    	if(!player_IsActive(1)) {
	            pstmt_the_sequal.setInt(1, 1);
	            pstmt_the_sequal.executeUpdate();
	            player_nr = 1; 
	        }else if(player_IsActive(1) && !player_IsActive(2)){
	        	pstmt_the_sequal.setInt(1, 2);
	        	pstmt_the_sequal.executeUpdate();
	            player_nr = 2;
	        }
	    	con.commit();
	    	if(player_IsActive(1) && player_IsActive(2) && player_nr > 0) {
	    		startGame();
	    	}
        }catch(SQLException e){
        	Cleanup.rollBack(con);
        	Cleanup.writeMessage(e,  "joinGame()");
        }finally {
            Cleanup.closeResource(pstmt_the_sequal);
            
        }
    	return player_nr;
    }

	public void sendMessage(int player, String message){
		try{
			con.setSavepoint();
			pstmt = con.prepareStatement("UPDATE player SET message = ? WHERE player_nr = ?");
			pstmt.setString(1, message);
			pstmt.setInt(2,  player);
			pstmt.execute();
			con.commit();
		}catch(SQLException e){
			Cleanup.rollBack(con);
			Cleanup.writeMessage(e,  "sendMessage()");
		}finally {
			Cleanup.closeResources(res, pstmt);
		}
	}

	public String getMessage(int player){
		String message = "";
		try{
			con.setSavepoint();
			pstmt = con.prepareStatement("SELECT message FROM player WHERE player_nr = ?");
			pstmt.setInt(1, player);
			res = pstmt.executeQuery();
			if(res.next()) {
				message = res.getString("message");
			}
			con.commit();
		}catch(SQLException e){
			Cleanup.rollBack(con);
			Cleanup.writeMessage(e,  "getMessage()");
		}finally{
			Cleanup.closeResources(res, pstmt);
		}
		return message;
	}

	public void waitForChangeFrom(int player) {
		String last;
		last = getMessage(player);
		while(getMessage(player).equals(last)) {}
	}

	Statement stmt;
	public void endGame(){
		String setning1 = "UPDATE player SET message = \"\"";
		String setning2 = "UPDATE player SET active = 0";
		String setning3 = "UPDATE game SET game_status = 0";
		try{
			con.setSavepoint();
			stmt = con.createStatement();
			stmt.executeUpdate(setning1);
			stmt = con.createStatement();
			stmt.executeUpdate(setning2);
			stmt = con.createStatement();
			stmt.executeUpdate(setning3);
			con.commit();
		}catch(SQLException e){
			Cleanup.rollBack(con);
			Cleanup.writeMessage(e,  "endGame()");
		}finally {
			Cleanup.closeResources(pstmt, res);
			Cleanup.closeConnection(con);
		}
	}
}
