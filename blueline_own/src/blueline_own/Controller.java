package blueline_own;
import com.ser.blueline.metaDataComponents.IQueryClass;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang.ArrayUtils;

import com.ser.blueline.BlueLineException;
import com.ser.blueline.IDocument;
import com.ser.blueline.IDocumentPart;
import com.ser.blueline.IDocumentServer;
import com.ser.blueline.IRepresentation;
import com.ser.blueline.ISerClassFactory;
import com.ser.blueline.ISession;
import com.ser.blueline.MetaDataException;
import com.ser.blueline.metaDataComponents.IDialog;
import com.ser.blueline.metaDataComponents.IQueryClass;
import com.ser.blueline.metaDataComponents.IQueryDlg;

import nlp.Result;
import nlp.SimpleGermanExample;

public class Controller {
	Anmeldung meine_anmeldung;
	ISession session;
	
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
	public void logout() throws BlueLineException{
		// Benutzer abmelden + Verbindung von Dokumentenserver trennen
		meine_anmeldung.logout(session);
		meine_anmeldung.close();
	}
	public void search() throws NumberFormatException, BlueLineException, IOException{
		//Search Document
		Suche suche = new Suche(session, meine_anmeldung.get_server(), meine_anmeldung.get_factory());
		IDocument[] documents = suche.searchDocument();
		System.out.println(documents.length);
		for(IDocument document : documents){
			System.out.println("DOKUMENT GEFUNDEN");
			// alle Repraesentationen dieses Dokuments abrufen
			IRepresentation[] representationList = document.getRepresentationList();
			int representationNr = document.getDefaultRepresentation();
					
			// aus der Repraesentationsliste auslesen
			IRepresentation defaultRepresentation = representationList[representationNr];
					
			// erstes Teildokument der Repraesentation herunterladen
			IDocumentPart documentPart = defaultRepresentation.getPartDocument(0);
					
			// was soll gelesen werden?
			InputStream inputStream = documentPart.getRawDataAsStream();
				
			saveDocuments(inputStream, documentPart.getFilename().split("\\\\")[documentPart.getFilename().split("\\\\").length-1]);
		}
	}
	public List<IDocument> fulltextSearch(String searchword) throws BlueLineException, NumberFormatException, IOException{
		
		
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
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			
			Result.getInstance().addFile(bis, filename);
		}
		return documents;
	}
	
	public void mysearch(String searchclass, String searchword) throws NumberFormatException, BlueLineException, IOException{
		//Search Document
		Suche suche = new Suche(session, meine_anmeldung.get_server(), meine_anmeldung.get_factory());
		
		IDocument[] documents = suche.mysearchDocument(searchclass, searchword);
		
		for(IDocument document : documents){
			System.out.println("DOKUMENT GEFUNDEN");
			// alle Repraesentationen dieses Dokuments abrufen
			IRepresentation[] representationList = document.getRepresentationList();
			int representationNr = document.getDefaultRepresentation();
					
			// aus der Repraesentationsliste auslesen
			IRepresentation defaultRepresentation = representationList[representationNr];
					
			// erstes Teildokument der Repraesentation herunterladen
			IDocumentPart documentPart = defaultRepresentation.getPartDocument(0);
					
			// was soll gelesen werden?
			InputStream inputStream = documentPart.getRawDataAsStream();
			System.out.println("gesplittet");
				
			saveDocuments(inputStream, documentPart.getFilename().split("\\\\")[documentPart.getFilename().split("\\\\").length-1]);
		}
	}
	
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
			


			BufferedInputStream bis = new BufferedInputStream(inputStream);
			
			Result.getInstance().addFile(bis, filename);
			
			
			
			saveDocuments(inputStream, filename);
		}
	}

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

	private static void saveDocuments(InputStream inputStream, String docName) throws IOException{

		// wohin soll geschrieben werden?
		File file = new File(docName);
		System.out.println(docName);
		file.createNewFile();
		FileOutputStream writer = new FileOutputStream(file);
		// auf Festplatte schreiben
		int read;
		while ((read=inputStream.read()) != -1) {
			writer.write(read);
		}
		
		// alle Streams schliessen
		inputStream.close();
		writer.close();
	}

}
