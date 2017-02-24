package Communication;

import java.io.*;
import java.net.*;

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
				
				
				bluelinestuff();
				Result res = Result.getInstance();
				String answer = "";
				
				//File gefunden
				if(res.fileCount() > 0){
					for(int i=0; i<res.fileCount();i++){
						outToClient.writeBytes(res.getFilename(i)+"?"+res.getFile(i) + "\n");
						System.out.println("Gesendet: "+res.getFilename(i) + "\n");
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
	

	
	public static void bluelinestuff(){
		try {
			Result res = Result.getInstance();
			Controller.getInstance().mysearch(res.getsearchclass(0), res.getSearchword(0));
			
			switch(Result.getInstance().evaluate()){
			case(0):{
			// TODO Volltext
			}
			case(1):{
			// TODO Strukturiert Suchklasse		
			}
			case(2):{
				// TODO Strukturiert Suchklasse + Suchwort
			}
			case(3):{
				
				int searchClass = SearchClassMapper.getSearchClassNumber(Result.getInstance().getsearchclass(0));
				int descriptor_Number = DescriptorMapper.getDescriptorNumber(Result.getInstance().getsearchclass(0), Result.getInstance().getdescriptor(0));
				String searchword = Result.getInstance().getSearchword(0);
				try {
					Controller.getInstance().descriptorsearch(searchClass, searchword, descriptor_Number);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BlueLineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
			
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} catch (BlueLineException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	




}