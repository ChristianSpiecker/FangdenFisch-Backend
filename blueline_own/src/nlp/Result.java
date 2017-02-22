package nlp;

import java.io.IOException;
import java.util.ArrayList;

import com.ser.blueline.BlueLineException;

import blueline_own.Controller;

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
