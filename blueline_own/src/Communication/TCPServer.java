package Communication;

import java.io.*;
import java.net.*;
import java.util.Date;

import com.ser.blueline.BlueLineException;

import blueline_own.Controller;
import nlp.DescriptorMapper;
import nlp.Result;
import nlp.SearchClassMapper;
import nlp.SimpleGermanExample;

public class TCPServer {

	public static void main(String argv[]) throws Exception {
		String clientSentence;

		ServerSocket welcomeSocket = new ServerSocket(420);
		SimpleGermanExample sigeex = SimpleGermanExample.getInstance();
		Controller.getInstance();
		System.out.println("Server rennt");
		while (true) {

			Socket connectionSocket = welcomeSocket.accept();

			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));

			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
			//WARUMMMMMMMMM
			clientSentence = inFromClient.readLine().replace('+', ' ');
			
			if(clientSentence != null){
				System.out.println(clientSentence);
				
				
				sigeex.myanalyseText(clientSentence);
				
				
				bluelinestuff();
				Result res = Result.getInstance();
				String answer = "";
				
				//File gefunden
				if(res.fileCount() > 0){
					
					for(int i=0; i<res.fileCount();i++){
						System.out.println("Start");
						//outToClient.writeBytes(res.getFilename(i)+ "\n");
						//System.out.println("Gesendet: "+res.getFilename(i) + "\n");
						BufferedInputStream act =res.getFile(i);
						int read;
						while ((read= act.read()) != -1) {
							outToClient.write(read);
						}
						outToClient.write(-1);
						outToClient.flush();
						System.out.println("\nFertig");

					}
				}else{
					//kein File gefunden
					outToClient.writeBytes("NoFile\n");
					System.out.println("Gesendet: Es wurde leider kein Fisch gefangen\n");
				}
				
			}
			
			Result.getInstance().reset();
			
		}
	}
	

	
	public static void bluelinestuff() throws NumberFormatException, BlueLineException, IOException{
		Result res = Result.getInstance();
		
		
		switch(res.evaluate()){
		case(0):{
		// TODO Volltext
			System.out.println(res.getSearchword(0));
			
			Controller.getInstance().fulltextSearch(res.getSearchword(0));

			break;
		}
		case(1):{
		// TODO Strukturiert Suchklasse	
			break;
		}
		case(2):{
			// TODO Strukturiert Suchklasse + Suchwort
			
			break;
		}
		case(3):{
			int searchClass = SearchClassMapper.getSearchClassNumber(res.getsearchclass(0));
			int descriptor_Number = DescriptorMapper.getDescriptorNumber(res.getsearchclass(0), res.getdescriptor(0));
			String descriptor = DescriptorMapper.getDescriptorName(descriptor_Number, searchClass);
			String searchword = res.getSearchword(0);
			String searchClassString = res.getsearchclass(0);
						Date firstDate = null;
			if(res.getDate(0) != null) firstDate = res.getDate(0);
			Date secondDate = null;
			if(res.getDate(1) != null) firstDate = res.getDate(1);
			

			if(searchClass < 0 || descriptor_Number < 0){
				//TODO ungültige anfrage
				System.out.println("Ungültige Anfrage brooo");
			}else{
				Controller.getInstance().descriptorsearch(searchClass,searchClassString, searchword, descriptor,descriptor_Number, firstDate, secondDate, res.getDatestate());
			}
			break;
		}
		default:{
			if (res.getsearchclass(0) == null && res.getdescriptor(0) == null && res.getSearchword(0) == null){
				System.out.println("Nix gefunden boyy");
			}
			break;
		}
		}
	}
	




}