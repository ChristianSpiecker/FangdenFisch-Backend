package main;
import java.io.IOException;

import com.ser.blueline.BlueLineException;
import Communication.TCPServer;
import blueline_own.*;
import nlp.DescriptorMapper;
import nlp.Result;
import nlp.SearchClassMapper;
import nlp.SimpleGermanExample;

public class Start {
	
	public static void main(String[]args){
		//testeNLP(); 
		// Zum Test des NLP's auskommentieren

		SimpleGermanExample.getInstance();
		SimpleGermanExample sigeex = SimpleGermanExample.getInstance();
		try {
			Controller.getInstance();
		} catch (IOException | BlueLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//testeNLP("gib mir alle Rechnungen vom Meier und Müller");
		//testeNLP("gib mir alle Rechnungen von der Rheinwerk Group");
		//testeNLP("gib mir alle Rechnungen vom Kunden Rheinwerk Group");
		
		
		sigeex.myanalyseText("gib mir alle Prospekte vom Kunden ");
		
		
		TCPServer.bluelinestuff();
	}
	
	
	public static void testeNLP(String sent){
		SimpleGermanExample sigeex = new SimpleGermanExample();
		sigeex.myanalyseText(sent);
		System.out.println(Result.getInstance().evaluate());
		
		
	}
}
// ADJD Verb // Zeig Mach Tu
// PPER Prononem // mir mein 
// ART der die das
// NN Nomen Rechnung
// APPRART vor Eigenname
// NE Eigenname //Meier