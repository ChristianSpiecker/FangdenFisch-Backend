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
		//Properties und CoreNLP laden
		SimpleGermanExample sigeex = SimpleGermanExample.getInstance();
		//Beim Server anmelden und Session erstellen
		Controller.getInstance();
		System.out.println("Server bereit");
		while (true) {
			// Client baut Verbindung auf
			Socket connectionSocket = welcomeSocket.accept();
			
			// BufferedReader zum senden der Dokumente
			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));
			// PrintWriter zur Kommunikation
			PrintWriter outPrint = new PrintWriter(connectionSocket.getOutputStream(),true);

			BufferedOutputStream outToClient = new BufferedOutputStream(
					connectionSocket.getOutputStream());
			
			// Woerter der Anfrage sind mit '+' verknuepft
			clientSentence = inFromClient.readLine().replace('+', ' ');
			
			// Wenn der Client eine Nachricht gesendet hat
			if(clientSentence != null){
				System.out.println(clientSentence);
				
				// Clientnachricht wird vom CoreNLP inteptretiert und die Baumstruktur wird im Result-Objekt gehalten.
				sigeex.myanalyseText(clientSentence);
				
				// Verarbeitung der Anfrage und speichern der Dokumente im Result-Objekt
				bluelinestuff();
				
				Result res = Result.getInstance();
				
				outPrint.println(res.fileCount());
				
				//File gefunden
				for (int i = 0; i < res.fileCount(); i++) {
					String a = "";
					//Thread.sleep(500);
					a = inFromClient.readLine();
					System.out.println(a);
					if(a.equals("done")){
						
						// Sende den Dateinamen vorraus
						outPrint.println(res.getFilename(i));

						// Erzeuge DataInputStream aus dem InputStream
						DataInputStream dis = new DataInputStream(res.getFile(i));
						// Erzeuge byte[] aus dem DataInputStream
						byte[] buffer = getBytes(dis);

						// Die Bytegroeße der Datei vorraus senden
						outPrint.println(buffer.length);
						outPrint.flush();

						// Sende die Bytes der Datei
						outToClient.write(buffer);
						outToClient.flush();
						
						
						System.out.println("######## Gesendet");
						System.out.println("Dateiname: " + res.getFilename(i));
						System.out.println("Dateigroesse: " + buffer.length);

						// Datei zuende info
						// outPrint.println("Ende");
					}
					
				}
				
			}
			
			Result.getInstance().reset();
			
		}
	}
	
	public static byte[] getBytes(InputStream is) throws IOException {

	    int len;
	    int size = 1024;
	    byte[] buf;

	    if (is instanceof ByteArrayInputStream) {
	      size = is.available();
	      buf = new byte[size];
	      len = is.read(buf, 0, size);
	    } else {
	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
	      buf = new byte[size];
	      while ((len = is.read(buf, 0, size)) != -1)
	        bos.write(buf, 0, len);
	      buf = bos.toByteArray();
	    }
	    return buf;
	  }

	
	public static void bluelinestuff() throws NumberFormatException, BlueLineException, IOException{
		Result res = Result.getInstance();
		
		if(!Controller.getInstance().getSession().isValid()){
			System.out.println("Session ist veraltet. Wird versucht zu erneuern.");
			Controller.getInstance().updateSession();
		}
		switch(res.evaluate()){
		case(0):{
		// TODO Volltext
			
			System.out.println(res.getSearchword(0));
			
			Controller.getInstance().fulltextSearch(res.getSearchword(0));

			break;
		}
		case(1):{
		// TODO Strukturiert Suchklasse	
			if(SearchClassMapper.getSearchClassNumber(res.getsearchclass(0)) != -1){
				Controller.getInstance().searchClassSearch(res.getsearchclass(0));
			}
			break;
		}
		case(2):{
			// TODO Strukturiert Suchklasse + Suchwort
			if(SearchClassMapper.getSearchClassNumber(res.getsearchclass(0)) != -1){
				int searchClass_Number = SearchClassMapper.getSearchClassNumber(res.getsearchclass(0));
				// auf -1 überprüfen
				Controller.getInstance().searchclassSearchwordSearch(res.getsearchclass(0), searchClass_Number, res.getSearchword(0));
			}
			
			break;
		}
		case(3):{
			
			if(SearchClassMapper.getSearchClassNumber(res.getsearchclass(0)) != -1 && DescriptorMapper.getDescriptorNumber(res.getsearchclass(0), res.getdescriptor(0)) != -1){
				int searchClass_Number = SearchClassMapper.getSearchClassNumber(res.getsearchclass(0));
				int descriptor_Number = DescriptorMapper.getDescriptorNumber(res.getsearchclass(0), res.getdescriptor(0));
				String descriptor = DescriptorMapper.getDescriptorName(descriptor_Number, searchClass_Number);
				String searchword = res.getSearchword(0);
				String searchClassString = res.getsearchclass(0);
							Date firstDate = null;
				if(res.getDate(0) != null) firstDate = res.getDate(0);
				Date secondDate = null;
				if(res.getDate(1) != null) firstDate = res.getDate(1);
				

				if(searchClass_Number < 0 || descriptor_Number < 0){
					//TODO ungültige anfrage
					System.out.println("Ungültige Anfrage brooo");
				}else{
					Controller.getInstance().descriptorsearch(searchClass_Number, searchClassString, searchword, descriptor,descriptor_Number, firstDate, secondDate, res.getDatestate());
				}
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