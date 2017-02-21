package nlp;

import java.util.ArrayList;

public class Result {
	ArrayList<String> search = new ArrayList<>();
	ArrayList<String> descriptor = new ArrayList<>();
	String tree = null;
	
	public Result(String search, String descriptor){
		if (search != null){
			this.search.add(search);
		}
		
		if (descriptor != null){
			this.descriptor.add(descriptor);
		}
	}
	public void addTree(String tree){
		this.tree = tree;
	}
	
	public String getTree(){
		return tree;
	}
	public String getSearchword(int index){
		return search.get(index);
	}
	public String getDescriptor(int index){
		return descriptor.get(index);
	}
	
	public void setSearchword(String word){
		search.add(word);
	}
	public void setDescriptor(String word){
		descriptor.add(word);
	}
	
	
}
