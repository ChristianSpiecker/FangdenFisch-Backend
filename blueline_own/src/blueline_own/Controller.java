package blueline_own;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Date;
import java.util.List;

import com.ser.blueline.BlueLineException;
import com.ser.blueline.IDocument;
import com.ser.blueline.IDocumentPart;
import com.ser.blueline.IRepresentation;
import com.ser.blueline.ISession;
import nlp.Result;

public class Controller {
	Anmeldung meine_anmeldung;
	ISession session;
	
	// Singleton
	private static Controller instance;
	public static Controller getInstance () throws IOException, BlueLineException {
		    if (Controller.instance == null) {
		    	Controller.instance = new Controller ();
		    }
		    return Controller.instance;
	 }
	 
	public Controller() throws IOException, BlueLineException{
		// initalisieren und einloggen
		this.meine_anmeldung = new Anmeldung();
		meine_anmeldung.initFramework();	
		meine_anmeldung.initDocumentServer("blueline_own.ini");
		this.session = meine_anmeldung.login("Supervisor", "Supervisor", "km4");
		

	}
	
	public ISession getSession(){
		return session;
	}
	/**
	 * Meldet sich neu an und erstellt die Session
	 * @throws IOException
	 * @throws BlueLineException
	 */
	public void updateSession() throws IOException, BlueLineException{
		this.meine_anmeldung = new Anmeldung();
		meine_anmeldung.initFramework();	
		meine_anmeldung.initDocumentServer("blueline_own.ini");
		this.session = meine_anmeldung.login("Supervisor", "Supervisor", "km4");
	}
	
	/**
	 * Ausloggen
	 * @throws BlueLineException
	 */
	public void logout() throws BlueLineException{
		// Benutzer abmelden + Verbindung von Dokumentenserver trennen
		meine_anmeldung.logout(session);
		meine_anmeldung.close();
	}
	/**
	 * Volltextsuche - Sucht mit einem Suchwort Dokumente und fügt sie dem Resultobjekt hinzu
	 * @param searchword Suchwort der Volltextsuche
	 * @throws BlueLineException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void fulltextSearch(String searchword) throws BlueLineException, NumberFormatException, IOException{
		
		Suche suche = new Suche(session, meine_anmeldung.get_server(), meine_anmeldung.get_factory());
		List<IDocument> documents = suche.fulltextSearch(searchword);
		for(IDocument document : documents){
			System.out.println("DOKUMENT ERHALTEN");
			// alle Repraesentationen dieses Dokuments abrufen
			IRepresentation[] representationList = document.getRepresentationList();
			int representationNr = document.getDefaultRepresentation();
					
			// aus der Repraesentationsliste auslesen
			IRepresentation defaultRepresentation = representationList[representationNr];
					
			// erstes Teildokument der Repraesentation herunterladen
			IDocumentPart documentPart = defaultRepresentation.getPartDocument(0);
					
			// was soll gelesen werden?
			InputStream inputStream = documentPart.getRawDataAsStream();
			String filename = documentPart.getFilename().split("\\\\")[documentPart.getFilename().split("\\\\").length-1];
			filename = normalizeFilename(filename);
			
			// File im Resultobjekt adden
			Result.getInstance().addFile(inputStream, filename);
		}
	}
	
	/**
	 * Sucht mithilfe von Suchklasse und einem Suchwort Dokumente und fügt sie dem Resultobjekt hinzu
	 * @param searchclass Suchklasse
	 * @param searchClass_Number Integer-Representation der Suchklasse
	 * @param searchword Suchwort
	 * @throws BlueLineException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void searchclassSearchwordSearch(String searchclass, int searchClass_Number, String searchword) throws BlueLineException, NumberFormatException, IOException{
		
		
		Suche suche = new Suche(session, meine_anmeldung.get_server(), meine_anmeldung.get_factory());
		List<IDocument> documents = suche.searchclassSearchwordSearch(searchclass, searchClass_Number, searchword);
		for(IDocument document : documents){
			System.out.println("DOKUMENT ERHALTEN");
			// alle Repraesentationen dieses Dokuments abrufen
			IRepresentation[] representationList = document.getRepresentationList();
			int representationNr = document.getDefaultRepresentation();
					
			// aus der Repraesentationsliste auslesen
			IRepresentation defaultRepresentation = representationList[representationNr];
					
			// erstes Teildokument der Repraesentation herunterladen
			IDocumentPart documentPart = defaultRepresentation.getPartDocument(0);
					
			// was soll gelesen werden?
			InputStream inputStream = documentPart.getRawDataAsStream();
			String filename = documentPart.getFilename().split("\\\\")[documentPart.getFilename().split("\\\\").length-1];
			filename = normalizeFilename(filename);
			
			// File im Resultobjekt adden
			
			Result.getInstance().addFile(inputStream, filename);
		}
	}
	
/**
 * Sucht mithilfe von Suchklasse Dokumente und fügt sie dem Resultobjekt hinzu
 * @param searchClass Suchklasse
 * @throws BlueLineException
 * @throws NumberFormatException
 * @throws IOException
 */
public void searchClassSearch(String searchClass) throws BlueLineException, NumberFormatException, IOException{
		
		
		Suche suche = new Suche(session, meine_anmeldung.get_server(), meine_anmeldung.get_factory());
		List<IDocument> documents = suche.searchClassSearch(searchClass);
		for(IDocument document : documents){
			System.out.println("DOKUMENT ERHALTEN");
			// alle Repraesentationen dieses Dokuments abrufen
			IRepresentation[] representationList = document.getRepresentationList();
			int representationNr = document.getDefaultRepresentation();
					
			// aus der Repraesentationsliste auslesen
			IRepresentation defaultRepresentation = representationList[representationNr];
					
			// erstes Teildokument der Repraesentation herunterladen
			IDocumentPart documentPart = defaultRepresentation.getPartDocument(0);
			// was soll gelesen werden?
			InputStream inputStream = documentPart.getRawDataAsStream();
			
			String filename = documentPart.getFilename().split("\\\\")[documentPart.getFilename().split("\\\\").length-1];
			filename = normalizeFilename(filename);
			
			// File im Resultobjekt adden
			Result.getInstance().addFile(inputStream, filename);
		}
	}
	
	/**
	 *  Sucht mithilfe von Suchklasse, Deskriptor und Suchwort Dokumente und fügt sie dem Resultobjekt hinzu
	 * @param searchclass Integer-Representation der Suchklasse
	 * @param searchClassString Suchklasse
	 * @param searchword Suchwort
	 * @param descriptor Deskriptor 
	 * @param descriptor_Number Integer-Representation des Deskriptors
	 * @param firstDate Erstes Datum
	 * @param secondDate Zweites Datum
	 * @param dateState Datumszustand(1 == vor, 0 == nach, -1 == Kein Datum)
	 * @throws NumberFormatException
	 * @throws BlueLineException
	 * @throws IOException
	 */
	public void descriptorsearch(int searchclass,String  searchClassString, String searchword, String descriptor, int descriptor_Number, Date firstDate, Date secondDate, int dateState) throws NumberFormatException, BlueLineException, IOException{
		Suche suche = new Suche(session, meine_anmeldung.get_server(), meine_anmeldung.get_factory());
		
		List<IDocument> documents = suche.descriptorsearchDocument(searchclass,searchClassString, searchword, descriptor,descriptor_Number, firstDate, secondDate, dateState);
		
		for(IDocument document : documents){
			System.out.println("DOKUMENT ERHALTEN");
			// alle Repraesentationen dieses Dokuments abrufen
			IRepresentation[] representationList = document.getRepresentationList();
			int representationNr = document.getDefaultRepresentation();
					
			// aus der Repraesentationsliste auslesen
			IRepresentation defaultRepresentation = representationList[representationNr];
					
			// erstes Teildokument der Repraesentation herunterladen
			IDocumentPart documentPart = defaultRepresentation.getPartDocument(0);
					
			// was soll gelesen werden?
			InputStream inputStream = documentPart.getRawDataAsStream();
			String filename = documentPart.getFilename().split("\\\\")[documentPart.getFilename().split("\\\\").length-1];
			filename = normalizeFilename(filename);
			// File im Resultobjekt adden
			
			Result.getInstance().addFile(inputStream, filename);
			
		}
	}
	/**
	 * entfernt ungewollte Zeichen aus dem Dateinamen
	 * @param f Dateiname
	 * @return veränderter Dateiname
	 */
	public String normalizeFilename(String f){
		char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
		
		for (int i=0; i< ILLEGAL_CHARACTERS.length; i++){
			f.replace(ILLEGAL_CHARACTERS[i], ' ');
		}
		
		f = f.replace("ä", "ae");
		f = f.replace("?", " ");
		f = f.replace("Ì", " ");
		f = f.replace("ä", "ae");
		f = f.replace("ü", "ue")
                .replace("ö", "oe")
                .replace("ä", "ae")
                .replace("ß", "ss");

		f = f.replace("Ü", "UE")
          .replace("Ö", "OE")
          .replace("Ä", "AE");
		
		f = Normalizer.normalize(f, Normalizer.Form.NFKC);
		return f;
	}

}
