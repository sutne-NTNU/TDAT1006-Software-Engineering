package TempFiles;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

public class clientloginTest implements Serializable {
	
	public static void main(String[] args) {
		String userName = "sebbern";
		String hi = "h";
		String passWord = "12345678";
		String eMail = "seb@sebmail.no";
		int[] ports = {7777, 3333};
		ObjectInputStream sIn = null;
		ObjectOutputStream sOut = null;
		Socket in = null;
		Socket out = null;
		boolean register = false;
		boolean tryConnect = true;
		boolean run = true;
		String serverMessage = "";
		
		while(run) {
			while(tryConnect) {
				try {
					out= new Socket(InetAddress.getLocalHost().getHostAddress(), ports[0]);
			   		in= new Socket(InetAddress.getLocalHost().getHostAddress(), ports[1]);
			   		System.out.println("Ports set");
			   		System.out.println("Connection successful");
			   		sOut = new ObjectOutputStream(out.getOutputStream());
			   		sIn = new ObjectInputStream(in.getInputStream());
					if(register) {
				   		sOut.writeObject(userName+","+passWord+","+eMail);
				   		tryConnect = false;
					}else {
						sOut.writeObject(userName+","+passWord);
						tryConnect = false;
					}
					
				}catch(Exception e) {
					
				}
			}while(serverMessage == "") {
				try {
					serverMessage = (String)sIn.readObject();
					System.out.println(serverMessage);
				}catch(Exception e) {
				
				}
			}
			if(serverMessage == "Registration error" || serverMessage == "Wrong username/Password"){
			}else{
				run = false;
			}
		}
	}
}
