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
		//testeNLP("gib mir alle Rechnungen vom Meier und Müller");
		//testeNLP("gib mir alle Rechnungen von der Rheinwerk Group");
		testeNLP("gib mir alle Aufträge vom Kundenname Rheinwerk Group");
	}
	
	
	public static void testeNLP(String sent){
		SimpleGermanExample sigeex = new SimpleGermanExample();
		sigeex.myanalyseText(sent);
		System.out.println(Result.getInstance().evaluate());
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
				//Result.getInstance().getSearchword()
				//descriptorsearch(int searchclass, String searchword, int descriptor_Number)	
			}
		}
		
	}
}
// ADJD Verb // Zeig Mach Tu
// PPER Prononem // mir mein 
// ART der die das
// NN Nomen Rechnung
// APPRART vor Eigenname
// NE Eigenname //Meier