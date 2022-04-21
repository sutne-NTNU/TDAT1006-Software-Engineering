package serverclasses;
// Class for making and managing connection pool
import java.sql.*;
import java.util.ArrayList;

import Logic.Cleanup;

public class ConnectionPool {

	private final int MAX_CONNECTIONS = 10;
	ArrayList<Connection> pool = new ArrayList<Connection>();
	ArrayList<Connection> usedConnection = new ArrayList<Connection>();
	String username;
	String password;
	String db_url;

	public ConnectionPool(String username, String password, String db_url){
		this.username = username;
		this.password = password;
		this.db_url = db_url;
		createPool();
	}

	/**Closes the connection pool
	 *
	 */
	//Closes the connection pool (and connections) when the server shuts down
	public void close() {
		//both the unused and used connections must be closed
		for(Connection con : pool) {
			Cleanup.closeConnection(con);
		}
		pool = null;
		if(usedConnection.size() != 0) {
			for(Connection con : usedConnection) {
				Cleanup.closeConnection(con);
			}
		}
		usedConnection = null;
	}


	private synchronized boolean poolIsFull() throws Exception{
		if(pool.size() + usedConnection.size() == MAX_CONNECTIONS){
			return true;
		}else if(pool.size() + usedConnection.size() > MAX_CONNECTIONS){
			throw new Exception ("pool is flowing over");
		}
		return false;
	}

	private void createPool(){
		try{
			while(pool.size() < MAX_CONNECTIONS) {
				Connection con = createConnection();
				
				if(!poolIsFull() && !pool.contains(con)){
					pool.add(con);
				}
			}
		}catch(Exception e){
			Cleanup.writeMessage(e, "ConnectionPool: createPool()");
		}
	}

	// create a connection with object username, password and url
	private Connection createConnection(){
		Connection con = null;
		try{
			con = DriverManager.getConnection(db_url, username, password);
		}catch(SQLException e){
			Cleanup.writeMessage(e, "createConnection()");
		}
		return con;
	}

	/**Fetches a connection from the pool, waits if the pool is full.
	 * @return Connection the connection that is fetched
	 */
	public synchronized Connection fetchConnection(){
		try{
			//When pool is empty and active-pool is full = there are no available connections
			while(pool.isEmpty() && usedConnection.size() == MAX_CONNECTIONS){ 
				//waiting...
			}
			
			Connection con = pool.get(0);
			usedConnection.add(con);
			pool.remove(con);
			
			if(!con.isValid(0) || con == null){
				con.close();
				con = createConnection();
			}
			return con;
		}catch(Exception e){
			Cleanup.writeMessage(e, "fetchConnection()");
			return null;
		}
	}

	/**Release a given connection in the pool
	 * @param con the connection to release
	 */
	// move a connection from active-pool to idle-pool
	public synchronized void releaseConnection(Connection con){
		try{
			pool.add(con);
			usedConnection.remove(con);
		}catch(Exception e){
			Cleanup.writeMessage(e, "releaseConnection()");
		}
	}
}


