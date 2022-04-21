package TempFiles;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import static javax.swing.JOptionPane.*;

public class ChatServerTest 
{
	final static String IP = "192.168.137.207";
	final static int PORT = 4444;
	static Socket socket;
	static ObjectInputStream from;
	static ObjectOutputStream to;
	
	public static void setUpStreams() 
	{
		try 
		{
			socket = new Socket(IP, PORT);
			System.out.println("	Socket was setup");
			to = new ObjectOutputStream(socket.getOutputStream());
			from = new ObjectInputStream(socket.getInputStream());
			System.out.println("	Streams were setup");
		}
		catch(Exception e) 
		{
			System.err.println(e.getMessage());
		}
	}
	
	public static void closeShit() 
	{
		try 
		{
			to.close();
			from.close();
			socket.close();
		} 
		catch (IOException e) 
		{
			System.err.println(e.getMessage());
		}
	}
	
	public static void send(Object obj) 
	{
		try 
		{
			to.writeObject(obj);
		} 
		catch (IOException e) 
		{
			System.err.println(e.getMessage());
		}
	}
	
	public static Object recieve() 
	{
		try 
		{
			return from.readObject();
		} 
		catch (Exception e) 
		{
			System.err.println(e.getMessage());
			return null;
		}
	}

	
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("Setting up streams...");
		setUpStreams();
		double number = 0;
		do
		{
			String message = showInputDialog(null, "write a number to the server");
			number = Double.parseDouble(message);
			System.out.println("Sending " + number + " to server...");
			send(number);

			System.out.println("Waiting for response...");
			double answer = (double)recieve();
			System.out.println("The server answered with: " + answer);
		}while(number != 0);
		System.out.println("\nAll done\nClosing shit");
		closeShit();
	}
}





























