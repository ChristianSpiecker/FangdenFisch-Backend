package nlp;

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
			put("Kunde",0);
			put("Kunden",0);
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
			put("Kunde",0);
			put("Kunden",0);
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
			put("Kunde",1);
			put("Kunden",1);
			put("Hundename",1);
			put("Hundenamen",1);
			put("Angebotsdatum",2);
			put("Angebotsnummer",3);
			put("Angebotnummer",3);
			put("Angebotsnummern",3);
			put("Angebotnummern",3);
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
			put("Kunde",1);
			put("Kunden",1);
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
			put("Kundenname",0);
			put("Kundennamen",0);
			put("Kunde",0);
			put("Kunden",0);
			put("Hundename",0);
			put("Hundenamen",0);	
			put("Lieferadresse",1);
			put("Lieferadressen",1);
			put("Auftragsnummer Kunde",2);
			put("Auftragsnummer Kunden",2);	
			put("Auftragsnummern Kunde",2);
			put("Auftragsnummern Kunden",2);
			put("Kundenauftragsnummer",2);
			put("Kundenauftragsnummern",2);	
			put("Artikelnummer",3);
			put("Artikelnummern",3);
			put("Lieferscheindatum",4);
			put("Lieferscheinnummer",5);
			put("Lieferscheinnummern",5);
		}});
		put("Lieferscheine",new HashMap<String,Integer>(){{
			put("Kundenname",0);
			put("Kundennamen",0);
			put("Kunde",0);
			put("Kunden",0);
			put("Hundename",0);
			put("Hundenamen",0);	
			put("Lieferadresse",1);
			put("Lieferadressen",1);
			put("Auftragsnummer Kunde",2);
			put("Auftragsnummer Kunden",2);	
			put("Auftragsnummern Kunde",2);
			put("Auftragsnummern Kunden",2);
			put("Kundenauftragsnummer",2);
			put("Kundenauftragsnummern",2);	
			put("Artikelnummer",3);
			put("Artikelnummern",3);
			put("Lieferscheindatum",4);
			put("Lieferscheinnummer",5);
			put("Lieferscheinnummern",5);
		}});
		/*	0: Angebotsdatum
			1: Angebotnummer
			2: Kundenname 
		 */
		put("Angebot",new HashMap<String,Integer>(){{
			put("Angebotsdatum",0);
			put("Angebotnummer",1);
			put("Angebotsnummer",1);
			put("Angebotnummern",1);
			put("Angebotsnummern",1);
			put("Kundenname",2);
			put("Kundennamen",2);
			put("Kunde",2);
			put("Kunden",2);
			put("Hundename",2);
			put("Hundenamen",2);
		}});
		put("Angebote",new HashMap<String,Integer>(){{
			put("Angebotsdatum",0);
			put("Angebotnummer",1);
			put("Angebotsnummer",1);
			put("Angebotnummern",1);
			put("Angebotsnummern",1);
			put("Kundenname",2);
			put("Kundennamen",2);
			put("Kunde",2);
			put("Kunden",2);
			put("Hundename",2);
			put("Hundenamen",2);
		}});
		/*	0: Kundenname
			1: Artikelname
			2: Anfragedatum
		 */
		put("Anfrage",new HashMap<String,Integer>(){{
			put("Kundenname",0);
			put("Kundennamen",0);
			put("Kunde",0);
			put("Kunden",0);
			put("Hundename",0);
			put("Hundenamen",0);
			put("Artikelnamen",1);
			put("Artikelname",1);
			put("Anfragedatum",2);
		}});
		put("Anfragen",new HashMap<String,Integer>(){{
			put("Kundenname",0);
			put("Kundennamen",0);
			put("Kunde",0);
			put("Kunden",0);
			put("Hundename",0);
			put("Hundenamen",0);
			put("Artikelnamen",1);
			put("Artikelname",1);
			put("Anfragedatum",2);			
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
			put("Angebotnummer",0);
			put("Angebotsnummer",0);
			put("Angebotnummern",0);
			put("Angebotsnummern",0);
			put("Angebotsdatum",1);
			put("Auftragsnummer",2);
			put("Auftragsnummern",2);
			put("Auftragnummer",2);
			put("Auftragnummern",2);
			put("Bestelldatum",3);			
			put("Bruttopreis",4);
			put("Bruttogesamtpreis",4);
			put("Brutto",4);
			put("Preis",4);
			put("Gesamtpreis",4);
			put("Nettopreis",4);
			put("Nettogesamtpreis",4);
			put("Netto",4);		
			put("Kundennummer",6);
			put("Kundennummern",6);
			put("Kundenummer",6);
			put("Kundenummern",6);
			put("Hundennummer",6);
			put("Hundennummern",6);
			put("Hundenummer",6);
			put("Hundenummern",6);
			put("Rechnungsdatum",7);
			put("Rechnungsnummer",8);
			put("Rechnungsnummern",8);
			put("Rechnungnummer",8);
			put("Rechnungnummern",8);
			put("Sachbearbeiter",9);
			put("Sachbearbeiterin",9);
			put("Kundenname",10);
			put("Kundennamen",10);
			put("Kunde",10);
			put("Kunden",10);
			put("Hundename",10);
			put("Hundenamen",10);
		}});
		put("Rechnungen",new HashMap<String,Integer>(){{
			put("Angebotnummer",0);
			put("Angebotsnummer",0);
			put("Angebotnummern",0);
			put("Angebotsnummern",0);
			put("Angebotsdatum",1);
			put("Auftragsnummer",2);
			put("Auftragsnummern",2);
			put("Auftragnummer",2);
			put("Auftragnummern",2);
			put("Bestelldatum",3);			
			put("Bruttopreis",4);
			put("Bruttogesamtpreis",4);
			put("Brutto",4);
			put("Preis",4);
			put("Gesamtpreis",4);
			put("Nettopreis",4);
			put("Nettogesamtpreis",4);
			put("Netto",4);		
			put("Kundennummer",6);
			put("Kundennummern",6);
			put("Kundenummer",6);
			put("Kundenummern",6);
			put("Hundennummer",6);
			put("Hundennummern",6);
			put("Hundenummer",6);
			put("Hundenummern",6);
			put("Rechnungsdatum",7);
			put("Rechnungsnummer",8);
			put("Rechnungsnummern",8);
			put("Rechnungnummer",8);
			put("Rechnungnummern",8);
			put("Sachbearbeiter",9);
			put("Sachbearbeiterin",9);
			put("Kundenname",10);
			put("Kundennamen",10);
			put("Kunde",10);
			put("Kunden",10);
			put("Hundename",10);
			put("Hundenamen",10);
		}});
	}};
	public static int getDescriptorNumber(String searchClass, String word){
		if (descriptorMap.get(searchClass) == null){
			return -1;
			//TODO: switch case0
		}
		if (descriptorMap.get(searchClass).get(word) == null){
			return -1;
			//TODO: switch case2
		}
		
		return descriptorMap.get(searchClass).get(word);
	}
}
