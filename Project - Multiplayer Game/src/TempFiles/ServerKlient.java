package TempFiles;

import java.io.*;
	import java.net.*;
	import java.util.*;
	 //This is an example
	public class ServerKlient implements Serializable {

	   public static void main(String argv[])
	      {
		   try{
			    Socket socketClient= new Socket("10.22.149.33", 8888);
			    System.out.println("Client: "+"Connection Established");

			    BufferedReader reader =
			    		new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

			    while(true) {
			    	ObjectOutputStream writer = new  ObjectOutputStream(socketClient.getOutputStream());
			    	writer.writeObject((Object)"Hello?");
			    }
			    //writer.write("Hello?");
			   /// writer.write("120\r\n");
	            //writer.flush();
				/*while((serverMsg = reader.readLine()) != null){
					System.out.println("Client: " + serverMsg);
				}*/

		   }catch(Exception e){e.printStackTrace();}
	      }
	}
