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
		
		sigeex.myanalyseText("gib mir alle Rechnungen vom Kunden Rheinwerk Group seit dem 15.12.2016");
		//Good guy anfragen
		//Gib mir alle Rechnungen vom Kunden Rheinwerk Group bis zum 17.09.2014
		//gib mir alle Rechnungen vom Kunden Rheinwerk Group
		
		//sigeex.myanalyseText("gib mir alle Rechnungen vom Kunden Rheinwerk Group ab dem 1.1.1000");
		//sigeex.myanalyseText("gib mir alle Rechnungen vom Kunden Rheinwerk Group vom 1.1.1000 bis zum 2.1.1000");
		//sigeex.myanalyseText("gib mir alle Rechnungen vom Kunden Rheinwerk Group vor dem 1.1.1000");
		//sigeex.myanalyseText("gib mir alle Rechnungen vom Kunden Rheinwerk Group nach dem 1.1.1000");
		//sigeex.myanalyseText("gib mir alle Rechnungen vom Kunden Rheinwerk Group zwischen dem 1.1.1000 und dem 2.1.1000");
		//sigeex.myanalyseText("gib mir alle Rechnungen vom Kunden Rheinwerk Group");
		
		
		try {
			TCPServer.bluelinestuff();
		} catch (NumberFormatException | BlueLineException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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