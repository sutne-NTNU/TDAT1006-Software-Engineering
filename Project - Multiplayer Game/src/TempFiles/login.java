package TempFiles;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import serverclasses.*;
import Logic.User;

public class login implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	public static final String IP = "10.22.150.160";
	
	public static void main(String[] args) {
		runLogin(false, "seblastian", "9696", "�");
	}

	

	public static void runLogin(boolean registrer, String userName, String password, String eMail) {
		final int[] ports = {3737, 7373};
		ObjectInputStream inputStream = null;
		ObjectOutputStream outputStream = null;
		Socket input, output = null;
		boolean tryConnect = true , run = true;
		
		System.out.println("username: "  + userName + "\npassword: " + password + "\nE-mail: " + eMail);

		while (run) {
			while (tryConnect) {
				try {
				
					System.out.println("Setting ports");
					output = new Socket(IP, ports[0]);
					input = new Socket(IP, ports[1]);
					System.out.println("Connection successful");
					
					outputStream = new ObjectOutputStream(output.getOutputStream());
					inputStream = new ObjectInputStream(input.getInputStream());
					
					if (registrer) {
						outputStream.writeObject("register," + userName + "," + password + "," + eMail);
						tryConnect = false;
					} else {
						outputStream.writeObject(userName + "," + password);
						tryConnect = false;
					}

				} catch (Exception e) {
					Cleanup.writeMessage(e, "runLogin() - tryConnect");
					return;
				}
			}
			String serverMessage = "";
			while (serverMessage == "") {
				try {
					User bajs = (User) inputStream.readObject();
					System.out.println(bajs);
				} catch (Exception e) {
					Cleanup.writeMessage(e, "runLogin() serverMessage == \"\"");
				}
			}
			if (!serverMessage.contentEquals("Registration error") && !serverMessage.contentEquals("Wrong username/Password")) {
				run = false;
			}
		}

	}
}
