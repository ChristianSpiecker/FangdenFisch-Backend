package nlp;

import java.util.HashMap;
import java.util.Map;

public class SearchClassMapper {
	private static Map<String, Integer> searchClassMap = new HashMap<String, Integer>(){{

		put("Auftrag",0);
		put("Aufträge",0);
		
		put("Auftragsbestätigung",1);
		put("Auftragsbestätigungen",1);
		
		put("Prospekt",2);
		put("Prospekte",2);
		
		put("Lieferschein",3);
		put("Lieferscheine",3);
		
		put("Angebot",4);
		put("Angebote",4);
		
		put("Anfrage",5);
		put("Anfragen",5);
		
		put("Rechnung",6);
		put("Rechnungen",6);
		
	}};

	public static int getSearchClassNumber(String word){
		if (searchClassMap.get(word) == null){
			return -1;
		}
		return searchClassMap.get(word);
	}
}
