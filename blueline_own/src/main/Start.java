package main;
import java.io.IOException;

import com.ser.blueline.BlueLineException;

import blueline_own.*;
import nlp.Result;
import nlp.SimpleGermanExample;

public class Start {
	
	public static void main(String[]args){
		//testeNLP(); 
		// Zum Test des NLP's auskommentieren
		SimpleGermanExample sigeex = SimpleGermanExample.getInstance();
		testeNLP("Zeig mir die Rechnung vom Meier");
	}
	
	
	public static void testeNLP(String sent){
		SimpleGermanExample sigeex = new SimpleGermanExample();
		Result res = sigeex.myanalyseText(sent);
		
	}
}
// ADJD Verb // Zeig Mach Tu
// PPER Prononem // mir mein 
// ART der die das
// NN Nomen Rechnung
// APPRART vor Eigenname
// NE Eigenname //Meier