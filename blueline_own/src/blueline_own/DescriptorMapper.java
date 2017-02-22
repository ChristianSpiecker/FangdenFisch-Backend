package blueline_own;

import java.util.HashMap;
import java.util.Map;

public class DescriptorMapper {
	private static Map<String, Map<String, Integer>> descriptorMap = new HashMap<String, Map<String, Integer>>(){{
		/*	0: Kundenname
			1: Bestelldatum
			2: Angebotsnummer
			3: Auftragsnummer
		*/	
		put("Auftrag",new HashMap<String,Integer>(){{
			put("Kundenname",0);
			put("Kundennamen",0);
			put("Hundename",0);
			put("Hundenamen",0);
			put("Bestelldatum",1);
			put("Angebotnummer",2);
			put("Angebotsnummer",2);
			put("Angebotnummern",2);
			put("Angebotsnummern",2);
			put("Auftragsnummer",3);
			put("Auftragsnummern",3);
			put("Auftragnummer",3);
			put("Auftragnummern",3);
		}});
		
		put("Aufträge",new HashMap<String,Integer>(){{
			put("Kundenname",0);
			put("Kundennamen",0);
			put("Hundename",0);
			put("Hundenamen",0);
			put("Bestelldatum",1);
			put("Angebotnummer",2);
			put("Angebotsnummer",2);
			put("Angebotnummern",2);
			put("Angebotsnummern",2);
			put("Auftragsnummer",3);
			put("Auftragsnummern",3);
			put("Auftragnummer",3);
			put("Auftragnummern",3);	
		}});
		/*	0: Auftragsnummer Kunde
			1: Kundenname
			2: Angebotsdatum
			3: Angebotsnummer
		*/		
		put("Auftragsbestätigung",new HashMap<String,Integer>(){{
			put("Auftragsnummer Kunde",0);
			put("Auftragsnummer Kunden",0);	
			put("Auftragsnummern Kunde",0);
			put("Auftragsnummern Kunden",0);
			put("Kundenauftragsnummer",0);
			put("Kundenauftragsnummern",0);	
			put("Kundenname",1);
			put("Kundennamen",1);
			put("Hundename",1);
			put("Hundenamen",1);
			put("Angebotsdatum",2);
			put("Angebotsnummer",3);
		}});
		put("Auftragsbestätigungen",new HashMap<String,Integer>(){{
			put("Auftragsnummer Kunde",0);
			put("Auftragsnummer Kunden",0);	
			put("Auftragsnummern Kunde",0);
			put("Auftragsnummern Kunden",0);
			put("Kundenauftragsnummer",0);
			put("Kundenauftragsnummern",0);	
			put("Kundenname",1);
			put("Kundennamen",1);
			put("Hundename",1);
			put("Hundenamen",1);
			put("Angebotsdatum",2);
			put("Angebotsnummer",3);
		}});
		/*	0: Artikelnamen
			1: Typenbezeichnungen
		*/
		put("Prospekt",new HashMap<String,Integer>(){{
			put("Artikelnamen",0);
			put("Artikelname",0);
			put("Typenbezeichnungen",1);
			put("Typenbezeichnung",1);
			put("Typbezeichnungen",1);
			put("Typbezeichnung",1);
		}});
		put("Prospekte",new HashMap<String,Integer>(){{
			put("Artikelnamen",0);
			put("Artikelname",0);
			put("Typenbezeichnungen",1);
			put("Typenbezeichnung",1);
			put("Typbezeichnungen",1);
			put("Typbezeichnung",1);			
		}});
		/*	0: Kundenname
			1: Lieferadresse
			2: Auftragsnummer Kunde
			3: Artikelnummer
			4: Lieferscheindatum
			5: Lieferscheinnummer
		 */
		put("Lieferschein",new HashMap<String,Integer>(){{
			
		}});
		put("Lieferscheine",new HashMap<String,Integer>(){{
			
		}});
		/*	0: Angebotsdatum
			1: Angebotnummer
			2: Kundenname 
		 */
		put("Angebot",new HashMap<String,Integer>(){{
			
		}});
		put("Angebote",new HashMap<String,Integer>(){{
			
		}});
		/*	0: Kundenname
			1: Artikelname
			2: Anfragedatum
		 */
		put("Anfrage",new HashMap<String,Integer>(){{
			
		}});
		put("Anfragen",new HashMap<String,Integer>(){{
			
		}});
		/*	0: Angebotnummer
			1: Angebotsdatum
			2: Auftragsnummer
			3: Bestelldatum
			4: Gesamtpreis Brutto
			5: Gesamtpreis Netto
			6: Kundennummer
			7: Rechnungsdatum
			8: Rechnungsnummer
			9: Sachbearbeiter
			10: Kundenname
		 */
		put("Rechnung",new HashMap<String,Integer>(){{
			
		}});
		put("Rechnungen",new HashMap<String,Integer>(){{
			
		}});
		

	}};
}
