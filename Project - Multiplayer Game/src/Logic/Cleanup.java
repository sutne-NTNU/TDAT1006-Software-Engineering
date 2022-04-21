package Logic;
import java.io.*;
import java.net.ServerSocket;
import java.sql.*;
import java.util.Properties;

public class Cleanup {


	/**
	 * Administrative class that makes sure to clean all connections
	 */


	/**
	 * Writes an errormessage to console
	 * @param e the exception to write
	 * @param location where the error occurred
	 */
	public static synchronized void writeMessage(Exception e, String location) {
		int MAX_TRACE = 10;
		int size = e.getStackTrace().length - 1;
		String errorMessage = "\n**********     Error in: " + location + "     **********\n";

		if(e.getMessage() != null){
			errorMessage += e.getMessage() + "\n" + e.getStackTrace()[0];
		}
		for(int i = 0; i < MAX_TRACE && size != 0; i++) {
			errorMessage += "\n	at " + e.getStackTrace()[size];
			size--;
		} 
		System.err.println(errorMessage + "\n");
	}

	/**
	 * fetches the database password from the .properties file
	 * @return the database password
	 */
	public static String getPassword() {
		return getProperty("password");
	}

	/**
	 * fetches a value from the .properties file
	 * @param identifier the name of the property
	 * @return Value from .properties
	 */
	public static String getProperty(String identifier) {
		String password = "";
		InputStream input = null;
		try {
//		    System.out.println(System.getProperty("user.dir"));
			File pword = new File("./password.properties");
			if(!(pword.exists())){
				Properties prop = new Properties();
				pword.getParentFile().mkdirs();
				prop.setProperty("IP", "");
				prop.setProperty("Port", "");
				FileWriter fwriter = new FileWriter(pword, true);
				prop.store(fwriter, "");
				fwriter.close();
				System.exit(-33);
				return null;
			}
			input = new FileInputStream("./password.properties");
			Properties prop = new Properties();
			prop.load(input);

			//get the property value and print it out
			password = prop.getProperty(identifier);
		} catch (IOException ex) {
			writeMessage(ex, "getProperty()");
		} finally {
			closeFileInputStream(input);
		}
		return password;
	}

	/**
	 * closes an inputstream
	 * @param fis inputstream to close
	 */
	public static void closeFileInputStream(InputStream fis) {
		try {
			if (fis != null) {
				fis.close();
			}
		} catch (IOException e) {
			writeMessage(e, "closeFileInputStream()");
		}  
	}

	
	
	
	/*
	 * SQL 
	 */


	/**
	 * closes a SQL-resultset
	 * @param res resultset to close
	 */
	public static void closeResultSet(ResultSet res) {
		try {
			if (res != null) {
				res.close();
			}
		} catch (SQLException e) {
			writeMessage(e, "closeResSet()");
		}
	}

	/**
	 * closes a SQL-statement
	 * @param stmt statement to clsoe
	 */
	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			writeMessage(e, "closeStatement()");
		}
	}

	/**
	 * closes a SQL-preparedstatement
	 * @param pstmt preparedstatement to close
	 */
	public static void closePreparedStatement(PreparedStatement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			writeMessage(e, "closeStatement()");
		}
	}

	/**
	 * closes a SQL-connection
	 * @param con connection to close
	 */
	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			writeMessage(e, "closeConnection()");
		}
	}

	/**
	 * rolls back an SQL-connection
	 * @param con connection to rollback
	 */
	public static void rollBack(Connection con) {
		try {
			if (con != null && !con.getAutoCommit() && !con.isClosed()) {
				con.rollback();
			}
		} catch (SQLException e) {
			writeMessage(e, "rollBack()");
		}
	}

	/**
	 * turns on and off autocommit
	 * @param con connection to edit
	 * @param on_off true = on, false = off
	 */
	public static void autoCommit(Connection con, boolean on_off) {
		try {
			if(con != null && con.getAutoCommit() == on_off) { 
				if(con.getAutoCommit() == false) {
					con.setSavepoint();
				}
				return;
			}
			if (con != null && on_off) {
				con.setAutoCommit(true);
			}else if(con != null && !on_off) {
				con.setAutoCommit(false);
				con.setSavepoint();
			}
		}catch (SQLException e) {
			writeMessage(e, "autoCommit()");
		}
	}
	
	
	
	
	
	/*
	 * Server related
	 */


	/**
	 * closes a serversocket
	 * @param ss socket to be closed
	 */
	public static void closeServerSocket(ServerSocket ss) {
		try {
			if (ss != null) {
				ss.close();
			}
		} catch (Exception e) {
			writeMessage(e, "closeServerSocket()");
		}
	}

	/**
	 * closes an objectinputstream
	 * @param ois inputstream to be closed
	 */
	public static void closeObjectInputStream(ObjectInputStream ois) {
		try {
			if (ois != null) {
				ois.close();
			}
		} catch (IOException e) {
			writeMessage(e, "closeObjectInputStream()");
		}
	}

	/**
	 * closes an objectoutputstream
	 * @param oos outputstream to be closed
	 */
	public static void closeObjectOutputStream(ObjectOutputStream oos) {
		try {
			if (oos != null) {
				oos.close();
			}
		} catch (IOException e) {
			writeMessage(e, "closeObjectOuputStream()");
		}
	}


	/**
	 * possible to use the same method for all fx.resources, it recognizes and closes it.
	 *
	 * @param obj an object to be closed
	 */
	public static void closeResource(Object obj) { 
		if(obj == null) {
			return;
		}else if(obj instanceof ResultSet) {
			ResultSet res = (ResultSet)obj;
			closeResultSet(res);
		}else if(obj instanceof Statement) {
			Statement stmt = (Statement)obj;
			closeStatement(stmt);
		}else if(obj instanceof PreparedStatement) {
			PreparedStatement pstmt = (PreparedStatement)obj;
			closePreparedStatement(pstmt);
		}else if(obj instanceof Connection) {
			Connection con = (Connection)obj;
			closeConnection(con);
		}else if(obj instanceof InputStream) {
			InputStream fis = (InputStream)obj;
			closeFileInputStream(fis);
		}else if(obj instanceof ObjectInputStream) {
			ObjectInputStream ois = (ObjectInputStream)obj;
			closeObjectInputStream(ois);
		}else if(obj instanceof ObjectOutputStream) {
			ObjectOutputStream oos = (ObjectOutputStream)obj;
			closeObjectOutputStream(oos);
		}else if(obj instanceof ServerSocket) {
			ServerSocket ss = (ServerSocket)obj;
			closeServerSocket(ss);
		}
	}

	/**
	 * closes two fx.resources
	 * @param obj1 an object to be closed
	 * @param obj2 an object to be closed
	 */
	public static void closeResources(Object obj1, Object obj2) {
		Object[] ressurser = {obj1, obj2};
		for(int i = 0; i < ressurser.length; i++) {
			closeResource(ressurser[i]);
		}
	}

	/**
	 * closes three fx.resources
	 * @param obj1 an object to be closed
	 * @param obj2 an object to be closed
	 * @param obj3 an object to be closed
	 */
	public static void closeResources(Object obj1, Object obj2, Object obj3) {
		Object[] ressurser = {obj1, obj2, obj3};
		for(int i = 0; i < ressurser.length; i++) {
			closeResource(ressurser[i]);
		}
	}

	/**
	 * closes four fx.resources
	 * @param obj1 an object to be closed
	 * @param obj2 an object to be closed
	 * @param obj3 an object to be closed
	 * @param obj4 an object to be closed
	 */
	public static void closeResources(Object obj1, Object obj2, Object obj3, Object obj4) {
		Object[] ressurser = {obj1, obj2, obj3, obj4};
		for(int i = 0; i < ressurser.length; i++) {
			closeResource(ressurser[i]);
		}
	}

	/**
	 * closes five fx.resources
	 * @param obj1 an object to be closed
	 * @param obj2 an object to be closed
	 * @param obj3 an object to be closed
	 * @param obj4 an object to be closed
	 * @param obj5 an object to be closed
	 */
	public static void closeResources(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) { 
		Object[] ressurser = {obj1, obj2, obj3, obj4, obj5};
		for(int i = 0; i < ressurser.length; i++) {
			closeResource(ressurser[i]);
		}
	}
}
