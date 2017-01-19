package Communication;

import java.io.*;
import java.net.*;

class TCPServer {

	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;
		System.out.println("Server l�uftt");
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {

			Socket connectionSocket = welcomeSocket.accept();

			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));

			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());

			clientSentence = inFromClient.readLine();
			if(clientSentence != null){
				System.out.println(clientSentence);
				
				
				capitalizedSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes("Server: "+capitalizedSentence);
			}
			

			
		}
	}
}
