package nlp;

import java.io.IOException;
import java.util.ArrayList;

import com.ser.blueline.BlueLineException;

import blueline_own.Controller;

/*
 * 4 Faelle
 * 
 * 0 Suchwort   ->   Volltext
 * 
 * 1 Suchklasse ->	Strukturiert  maybe volltext
 * 
 * 2 Suchklasse + Suchwort -> Strukturiert maybe volltext
 * 
 * 3 Suchklasse + Suchwort + Deskriptor -> Strukturiert
 * 
 */


public class Result {
	ArrayList<String> searchword = new ArrayList<>();
	ArrayList<String> searchclass = new ArrayList<>();
	ArrayList<String> descriptor = new ArrayList<>();
	String tree = null;
	
	private static Result instance;
	public static Result getInstance (){
		    if (Result.instance == null) {
		    	Result.instance = new Result ();
		    }
		    return Result.instance;
	 }
	
	public Result(){

	}
	
	public int evaluate(){
		if(searchclass.isEmpty() && descriptor.isEmpty() && !searchword.isEmpty()){
			// 0 Suchwort   ->   Volltext
			return 0;
		}else if(!searchclass.isEmpty() && descriptor.isEmpty() && searchword.isEmpty()){
			// 1 Suchklasse ->	Strukturiert
			return 1;
		}else if(!searchclass.isEmpty() && descriptor.isEmpty() && !searchword.isEmpty()){
			// 2 Suchklasse + Suchwort -> Strukturiert
			return 2;
		}else if(!searchclass.isEmpty() && !descriptor.isEmpty() && !searchword.isEmpty()){
			//3 Suchklasse + Suchwort + Deskriptor -> Strukturiert
			return 3;
		}
		
		return -1;
	}
	
	public void addTree(String tree){
		this.tree = tree;
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
