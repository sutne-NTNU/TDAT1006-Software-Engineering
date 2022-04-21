package serverclasses.serverObjects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Logic.Cleanup;

/**
 * a class for a client that's connected but not logged in
 */
public class ConnectedClient
{
	/**
	 * the socket the client is connected through
	 */
	private Socket socket;
	/**
	 * the clients IP-address
	 */
	private InetAddress ip;
	/**
	 * the outputstream to the client
	 */
	private ObjectOutputStream outputStream;
	/**
	 * the inputstream from the client
	 */
	private ObjectInputStream inputStream;

	/**
	 * this constructor creates a new instance and a new socket
	 * @param socket a new socket belonging to the client
	 */
	public ConnectedClient(Socket socket) 
	{
		this.ip = socket.getInetAddress();
		this.socket = socket;
		try 
		{
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(Exception e) 
		{
			Cleanup.writeMessage(e, "User()");
		}
	}

	/**
	 * this constructor creates a new ConnectedClient based on another ConnectedClient, and copies the old socket, streams and ip
	 * @param client used for transferring the old data to the new instance
	 */
	public ConnectedClient(ConnectedClient client)
	{
		this.ip = client.getIP();
		this.socket = client.getSocket();
		this.outputStream = client.getOutputStream();
		this.inputStream = client.getInputStream();
	}

	/**
	 * same as above but with ConnectedClient instead
	 * @param user transfers the data to the new instance
	 */
	public ConnectedClient(ConnectedUser user)
	{
		this.ip = user.getIP();
		this.socket = user.getSocket();
		this.outputStream = user.getOutputStream();
		this.inputStream = user.getInputStream();
	}

	/**
	 *
	 * @return the socket attached to this client
	 */
	public Socket getSocket() 					{return socket;}

	/**
	 *
	 * @return the clients IP adress
	 */
	public InetAddress getIP() 					{return ip;}

	/**
	 *
	 * @return the outputstream connected to this client
	 */
	public ObjectOutputStream getOutputStream() {return outputStream;}

	/**
	 *
	 * @return the input stream from this client
	 */
	public ObjectInputStream getInputStream() 	{return inputStream;}

	/**
	 * resets and flushes the outputStream, so that nothing weird happens
	 */
	public void resetStreams()
	{
		try {
			outputStream.reset();
			outputStream.flush();
		} catch (IOException e) {
			Cleanup.writeMessage(e, "resetStreams");
		}
	}

	/**
	 * closes all streams, ending communication with this client
	 */
	public void closeStreams() 
	{
		try 
		{
			if(!socket.isClosed())
			{
				outputStream.close();
				inputStream.close();
				socket.close();
			}
		} 
		catch (IOException e) 
		{
			if(e.getMessage() != null && e.getMessage().contains("Broken pipe")) {}
			else
			{
				Cleanup.writeMessage(e, "send()");
			}
		}
	}

	/**
	 * send an object to this client
	 * @param obj the object to send
	 * @return whether the object was sent successfully or not
	 */
	public boolean send(Object obj) //sends an object to this client
	{ 	
		try 
		{
			//javax.swing.JOptionPane.showMessageDialog(null,obj + " " + (obj instanceof ChatMessage) + " " + ip);
			outputStream.writeObject(obj);
			outputStream.flush();
			outputStream.reset();
			return true;
		}
		catch(Exception e) 
		{
			if(e.getMessage() != null && e.getMessage().contains("Broken pipe")) {}
			else
			{
				Cleanup.writeMessage(e, "send()");
			}
			return false;
		}
	}

	/**
	 * read an object from the client
	 * @return an object sent by the client
	 */
	public Object recieve() //reads an object from this client
	{	
		Object object;
		try 
		{
			object = inputStream.readObject();
			return object;
		}
		catch(Exception e) 
		{
			if(e.getMessage() != null && !e.getMessage().equals("Connection reset") && !e.getMessage().contentEquals("Socket closed"))
			{
				Cleanup.writeMessage(e, "ConnectedClient - recieve()");
			}
			return null;
		}
	}

	/**
	 *
	 * @return the most important attribute of the object
	 */
	public String toString() 
	{
		return "IP: " + ip;
	}
}
