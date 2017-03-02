package nlp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.ser.blueline.BlueLineException;

import blueline_own.Controller;

/*
 * 4 Faelle
 * 
 * 0 Suchwort   ->   Volltext
 * 
 * 1 Suchklasse ->	Strukturiert 
 * 
 * 2 Suchklasse + Suchwort -> Strukturiert
 * 
 * 3 Suchklasse + Suchwort + Deskriptor -> Strukturiert
 * 
 */

public class Result {
	
	ArrayList<String> searchword = new ArrayList<>();
	ArrayList<String> searchclass = new ArrayList<>();
	ArrayList<String> descriptor = new ArrayList<>();
	ArrayList<InputStream> files = new ArrayList<>();
	ArrayList<String> filenames = new ArrayList<>();
	String tree = null;
	ArrayList<Date> date = new ArrayList<>();
	
	private int datestate = -1; // 0 == vor : 1 == ab
	public int getDatestate() {
		return datestate;
	}
	
	//Singleton
	private static Result instance;
	public static Result getInstance (){
		    if (Result.instance == null) {
		    	Result.instance = new Result ();
		    }
		    return Result.instance;
	 }
	
	public Result(){

	}
	
	public Date getDate(int index) {
		if(index >= date.size()){
			return null;
		}
		return date.get(index);
	}
	/**
	 * Resetet Resultobjekt
	 */
	public void reset(){
		searchword.clear();
		searchclass.clear();
		descriptor.clear();
		files.clear();
		filenames.clear();
		tree = null;
		datestate = -1;
	}
	
/** 
 * Macht aus einem String ein Datum und fügt dieses hinzu
 * @param d Datumsstring
 */
	public void addDate(String d){
		// TODO abfangen wenn kein jahr gegeben
		
		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
		try {
		    Date da = f.parse(d);
		    date.add(da);
		    System.out.println("Date geaddet: "+da);
		} catch (ParseException e) {
			e.printStackTrace();
			//Date dt = new Date(Integer.parseInt(d.split("\\.")[2]),Integer.parseInt(d.split("\\.")[1]), Integer.parseInt(d.split("\\.")[0]));
			//date.add(dt);
		}
		
	}
	
	/**
	 * Fügt eine temporäre Preposition hinzu
	 * @param temp_preposition temporäre Preposition
	 * @return gibt den neuen DateState zurück
	 */
	public int addTemp_Preposition(String temp_preposition){
		String [] before = {"bis","vor"};
		String [] after = {"ab","seit"};
		for(String word : before){
			if(temp_preposition.equals(word)){
				datestate = 0;
				return 0;
			}
		}
		
		for(String word : after){
			if(temp_preposition.equals(word)){
				datestate = 1;
				return 1;
			}
		}
		return -1;
	}
	/**
	 * Errechnet den Fall aus den gegeben Informationen
	 * @return Gibt die Fallnummer zurück 0-3
	 */
	public int evaluate(){
		if(searchclass.isEmpty() && descriptor.isEmpty() && !searchword.isEmpty()){
			// 0 Suchwort   ->   Volltext
			System.out.println("nullter fall");
			return 0;
		}else if(!searchclass.isEmpty() && descriptor.isEmpty() && searchword.isEmpty()){
			System.out.println("erster fall");
			// 1 Suchklasse ->	Strukturiert
			return 1;
		}else if(!searchclass.isEmpty() && descriptor.isEmpty() && !searchword.isEmpty()){
			System.out.println("zweiter fall");
			// 2 Suchklasse + Suchwort -> Strukturiert
			return 2;
		}else if(!searchclass.isEmpty() && !descriptor.isEmpty() && !searchword.isEmpty()){
			//3 Suchklasse + Suchwort + Deskriptor -> Strukturiert
			System.out.println("dritter fall");
			return 3;
		}
		
		return -1;
	}
	
	public void addTree(String tree){
		this.tree = tree;
	}
	
	public void addFile(InputStream stream, String filename){
		files.add(stream);
		filenames.add(filename);
	}
	public int fileCount(){
		return files.size();
	}
	public InputStream getFile(int i){
		if(i >= 0 && i < files.size()){
			return files.get(i);
		}
		return null;
	}
	public String getFilename(int i){
		if(i >= 0 && i < filenames.size()){
			return filenames.get(i).intern();
		}
		return null;
	}
	
	
	public String getTree(){
		return tree;
	}
	public String getSearchword(int index){
		return searchword.get(index);
	}
	public String getsearchclass(int index){
		return searchclass.get(index);
	}
	public String getdescriptor(int index){
		return descriptor.get(index);
	}
	
	public void setdescriptor(String desc){
		descriptor.add(desc);
	}
	public void setSearchword(String word){
		searchword.add(word);
	}
	public void setsearchclass(String word){
		searchclass.add(word);
	}
	
	
}
