package nlp;

import java.util.ArrayList;

public class Result {
	ArrayList<String> search = new ArrayList<>();
	ArrayList<String> searchclass = new ArrayList<>();
	String tree = null;
	
	public Result(String search, String searchclass){
		if (search != null){
			this.search.add(search);
		}
		
		if (searchclass != null){
			this.searchclass.add(searchclass);
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
	public String getsearchclass(int index){
		return searchclass.get(index);
	}
	
	public void setSearchword(String word){
		search.add(word);
	}
	public void setsearchclass(String word){
		searchclass.add(word);
	}
	
	
}
