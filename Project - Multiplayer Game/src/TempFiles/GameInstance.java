package TempFiles;
import Logic.*;
import serverclasses.*;
import java.sql.SQLException;
import java.util.ArrayList;
import Logic.*;


//Extends gamesetup as it also utilizes the DB
//This class is meant to describe an instance of a game to the klient.

public class GameInstance extends Database {
	ArrayList<Municipality> mToTable = new ArrayList<>();
	ArrayList<Municipality> mFromTable = new ArrayList<>();
   
	//Constructor for an instance of a game
	//Typically used at the start and end of each game
    public GameInstance(ArrayList<Municipality> municipalities, int player){
//    	this.mToTable = municipalities;
////    	ArrayList<Municipality>prevTable = this.fetchTable();
//    	if(prevTable != null) {
//    		this.mFromTable = prevTable;
//    	}else {
//    		mFromTable = mToTable;
//    	}
    }
    //Fetches the table currently in the database
//    public ArrayList<Municipality> fetchTable() {
//    	String stmt = "SELECT m_id, owner, troops, value from municipalities where g_is ="+g_id+";";
//    	ArrayList<Municipality> fetched = new ArrayList<>();
//    	try {
//    		pstmt = con.prepareStatement(stmt);
//    		res = pstmt.executeQuery();
//    		Municipality fetchedM;
//    		while(res.next()) {
//    			fetchedM = new Municipality(res.getInt("m_id"), users[res.getInt("owner")], res.getBoolean("city"));
//    			fetchedM.setTroops(res.getInt("troops"));
//    			fetched.add(fetchedM);
//    		}
//    		return fetched;
//    	}catch(Exception e) {
//    		Cleanup.writeMessage(e, "fetchTable()");
//    		return null;
//    	}finally {
//    		Cleanup.closeResources(res, pstmt);
//    	}
//    }
//    //Converts objects to table values
//   /* private int[][] breakDown(ArrayList<Municipality> list) {
//    	int[][] simplified = new int[list.size()][3];
//    	for(int i = 0; i < list.size(); i++) {
//    		simplified[i][0] = list.get(i).getId();
//    		simplified[i][1] = list.get(i).getOwner().getPlayer();
//    		if(list.get(i).getCity()) {
//    			simplified[i][2] = 1;
//    		}else {
//    			simplified[i][2] = 0;
//    		}
//    		
//    	}
//    	return simplified;
//    }*/
//    
//    //Converts table values to objects
//    private ArrayList<Municipality> rebuild(int[][] simplified){
//    	ArrayList<Municipality> rebuilt = new ArrayList<>();
//    	Municipality toList;
//    	for(int i = 0; i < simplified.length; i++) {
//    		boolean cityVal = false;
//    		if(simplified[i][2] == 1) {
//    			cityVal = true;
//    		}
//    		toList = new Municipality(simplified[i][0], users[simplified[i][1]], cityVal);
//    		rebuilt.add(toList);
//    	}
//    	return rebuilt;
//    }
//    //Returns an update string to the corresponding object
//    private String updateStatement(Municipality in) {
//    	ArrayList<Municipality> toValue = new ArrayList<>();
//    	int[][] values = this.breakDown(toValue);
//    	String stmt = "Update municipalities(owner, city) set values("+values[0][1]+", "+values[0][2]+")Where m_id = "+ values[0][0]+" and g_id = "+g_id+";";
//    	return stmt;
//    }
//    
//   //Returns a delete string to the corresponding object
//    private String deleteStatement(Municipality in) {
//    	ArrayList<Municipality> toValue = new ArrayList<>();
//    	int[][] values = this.breakDown(toValue);
//    	String stmt = "DELETE FROM municipalities Where m_id ="+ values[0][0]+" and g_id = "+g_id+";";
//    	return stmt;
//    	
//    }
//    
//    //Returns an insert string to the corresponding object
//    private String insertStatement(Municipality in) {
//    	ArrayList<Municipality> toValue = new ArrayList<>();
//    	int[][] values = this.breakDown(toValue);
//    	String stmt = "INSERT INTO municipalities(m_id, owner, city) Values("+values[0][0]+", "+values[0][1]+", "+values[0][2]+");";
//    	return stmt;
//    	
//    }
//    
//    //Updates the SQL table
//    public boolean updateTable() {
//    	ArrayList<Municipality> update = this.mToUpdate();
//    	ArrayList<Municipality> delete = this.mToDelete();
//    	ArrayList<Municipality> insert = this.mToInsert();
//    	String totStatement = "";
//    	for(int i = 0; i < update.size(); i++) {
//    		totStatement += this.updateStatement(update.get(i)) + "\n";
//    	}
//    	for(int i = 0; i < delete.size(); i++) {
//    		totStatement += this.deleteStatement(delete.get(i)) + "\n";
//    	}
//    	for(int i = 0; i < insert.size(); i++) {
//    		totStatement += this.insertStatement(insert.get(i)) + "\n";
//    	}
//    	
//    	try {
//    		con.setSavepoint();
//    		pstmt = con.prepareStatement(totStatement);
//    		pstmt.executeBatch();
//    		con.commit();
//    		return true;
//    	}catch(SQLException e) {
//    		Cleanup.writeMessage(e, "updateTable()");
//    		return false;
//    	}finally {
//    		Cleanup.closeResource(pstmt);
//    	}
//    	
//    }
//    
//    //Checks what rows to delete from table
//    private ArrayList<Municipality> mToDelete(){
//    	ArrayList<Municipality> changedM = new ArrayList<>();
//    	boolean[] changes = new boolean[mFromTable.size() -1];
//    	
//    	for(int z = 0; z < changes.length; z++) {
//    		changes[z] = true;
//    	}
//    	for(int i = 0; i < mFromTable.size(); i++) {
//    		for(int y = 0; y < mToTable.size(); y++) {
//    			if(mToTable.get(y).getId() == mFromTable.get(i).getId()) {
//    				changes[i] = false;
//    			}
//    		}
//    	}
//    	
//    	for(int x = 0; x < changes.length; x++) {
//    		if(changes[x] == true) {
//    			changedM.add(mToTable.get(x));
//    		}
//    	}
//    	return changedM;
//    	
//    }
//    //Checks what rows to insert
//    private ArrayList<Municipality> mToInsert(){
//    	ArrayList<Municipality> changedM = new ArrayList<>();
//    	boolean[] changes = new boolean[mToTable.size() -1];
//    	
//    	for(int z = 0; z < changes.length; z++) {
//    		changes[z] = true;
//    	}
//    	for(int i = 0; i < mToTable.size(); i++) {
//    		for(int y = 0; y < mFromTable.size(); y++) {
//    			if(mToTable.get(i).getId() == mFromTable.get(y).getId()) {
//    				changes[i] = false;
//    			}
//    		}
//    	}
//    	
//    	for(int x = 0; x < changes.length; x++) {
//    		if(changes[x] == true) {
//    			changedM.add(mToTable.get(x));
//    		}
//    	}
//    	return changedM;
//    }
//    
//    //Checks what rows to update
//    private ArrayList<Municipality> mToUpdate() {
//    	ArrayList<Municipality> changedM = new ArrayList<>();
//    	boolean[] changes = new boolean[mToTable.size() -1];
//    	int[][] valuesTo = this.breakDown(mToTable);
//    	int[][] valuesFrom = this.breakDown(mFromTable);
//    	
//    	for(int z = 0; z < changes.length; z++) {
//    		changes[z] = true;
//    	}
//    	for(int i = 0; i < valuesTo.length; i++) {
//    		for(int y = 0; y < valuesFrom.length; y++) {
//    			if(valuesTo[i][2] == valuesFrom[y][2]) {
//    				changes[i] = false;
//    			}
//    		}
//    	}
//    	
//    	for(int x = 0; x < changes.length; x++) {
//    		if(changes[x] == true) {
//    			changedM.add(mToTable.get(x));
//    		}
//    	}
//    	return changedM;
//    }

}
