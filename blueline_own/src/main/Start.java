package main;
import java.io.IOException;

import com.ser.blueline.BlueLineException;

import blueline_own.*;
import nlp.SimpleGermanExample;

public class Start {
	
	public static void main(String[]args){
		//testeNLP(); 
		// Zum Test des NLP's auskommentieren
		
		System.out.println("asdsa");
		try {
			Controller controller = new Controller();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} catch (BlueLineException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void testeNLP(){
		SimpleGermanExample sigeex = new SimpleGermanExample();
		System.out.println(sigeex.analyseText("Das ist ein Test."));
	}
}
