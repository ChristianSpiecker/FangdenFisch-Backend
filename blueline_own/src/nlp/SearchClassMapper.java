package nlp;

import java.util.HashMap;
import java.util.Map;

public class SearchClassMapper {
	private static Map<String, Integer> searchClassMap = new HashMap<String, Integer>(){{

		put("Auftrag",0);
		put("Auftraege",0);
		
		put("Auftragsbestaetigung",1);
		put("Auftragsbestaetigungen",1);
		
		put("Prospekt",2);
		put("Prospekte",2);
		
		put("Lieferschein",3);
		put("Lieferscheine",3);
		
		put("Angebot",4);
		put("Angebote",4);
		
		put("Anfrage",6);
		put("Anfragen",6);
		
		put("Rechnung",5);
		put("Rechnungen",5);
		
	}};
	/**
	 * Sucht zu gegebenem Suchklassenname die Suchklassennummer heraus
	 * @param word Suchklassenname
	 * @return Suchklassennnummer oder -1 
	 */
	public static int getSearchClassNumber(String word){
		if (searchClassMap.get(word) == null){
			return -1;
		}
		return searchClassMap.get(word);
	}
}
