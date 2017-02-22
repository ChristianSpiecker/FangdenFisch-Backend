package Communication;

import java.io.*;
import java.net.*;

import com.ser.blueline.BlueLineException;

import blueline_own.Controller;
import nlp.Result;
import nlp.SimpleGermanExample;

class TCPServer {

	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(420);
		SimpleGermanExample sigeex = SimpleGermanExample.getInstance();
		System.out.println("Server rennt");
		Controller.getInstance();
		while (true) {

			Socket connectionSocket = welcomeSocket.accept();

			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));

			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());

			clientSentence = inFromClient.readLine().replace('+', ' ');
			
			if(clientSentence != null){
				System.out.println(clientSentence);
				
				
				sigeex.myanalyseText(clientSentence);
				
				
				//bluelinestuff(res);
				
				//outToClient.writeBytes("Server: "+ answer + "\n");
				//System.out.println("Gesendet: "+answer + "\n");
			}
			

			
		}
	}
	

	
	public static void bluelinestuff(Result res){
		try {
			
			Controller.getInstance().mysearch(res.getsearchclass(0), res.getSearchword(0));
			
			
			
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} catch (BlueLineException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	




}