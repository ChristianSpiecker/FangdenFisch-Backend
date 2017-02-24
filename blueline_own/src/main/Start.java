package main;
import java.io.IOException;

import com.ser.blueline.BlueLineException;

import blueline_own.*;
import nlp.DescriptorMapper;
import nlp.Result;
import nlp.SearchClassMapper;
import nlp.SimpleGermanExample;

public class Start {
	
	public static void main(String[]args){
		//testeNLP(); 
		// Zum Test des NLP's auskommentieren
		//SimpleGermanExample sigeex = SimpleGermanExample.getInstance();
		try {
			Controller.getInstance();
		} catch (IOException | BlueLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//testeNLP("gib mir alle Rechnungen vom Meier und Müller");
		//testeNLP("gib mir alle Rechnungen von der Rheinwerk Group");
		//testeNLP("gib mir alle Rechnungen vom Kunden Rheinwerk Group");
		try {
			Controller.getInstance().search();
		} catch (NumberFormatException | BlueLineException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
	}
}
// ADJD Verb // Zeig Mach Tu
// PPER Prononem // mir mein 
// ART der die das
// NN Nomen Rechnung
// APPRART vor Eigenname
// NE Eigenname //Meier