package blueline_own;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.ser.blueline.BlueLineException;
import com.ser.blueline.IDescriptor;
import com.ser.blueline.IDocument;
import com.ser.blueline.IDocumentHitList;
import com.ser.blueline.IDocumentServer;
import com.ser.blueline.IFulltextEngine;
import com.ser.blueline.IProperties;
import com.ser.blueline.IQueryExpression;
import com.ser.blueline.IQueryOperator;
import com.ser.blueline.IQueryParameter;
import com.ser.blueline.IQueryValueDescriptor;
import com.ser.blueline.ISerClassFactory;
import com.ser.blueline.ISession;
import com.ser.blueline.ISystem;
import com.ser.blueline.ITicket;
import com.ser.blueline.IValueDescriptor;
import com.ser.blueline.MetaDataException;
import com.ser.blueline.metaDataComponents.IComponent;
import com.ser.blueline.metaDataComponents.IControl;
import com.ser.blueline.metaDataComponents.IDate;
import com.ser.blueline.metaDataComponents.IDialog;
import com.ser.blueline.metaDataComponents.IMaskedEdit;
import com.ser.blueline.metaDataComponents.IQueryClass;
import com.ser.blueline.metaDataComponents.IQueryDlg;
import com.ser.sedna.client.bluelineimpl.SEDNABluelineAdapterFactory;

import nlp.DescriptorMapper;
import nlp.SearchClassMapper;

public class Suche {
	private IDocumentServer server;
	private ISession session;
	private IQueryClass[] queryClasses;
	private ISerClassFactory factory;
	
	public Suche(ISession session, IDocumentServer server, ISerClassFactory factory) throws MetaDataException{
		this.server = server;
		this.factory = factory;
		this.session = session;
		// holt sich alle Suchklassen vom Server
		this.queryClasses = server.getQueryClasses(session);
	}
	
	public IDocument[] searchDocument() throws BlueLineException, NumberFormatException, IOException{
		// DOKUMENTENKLASSE
		System.out.println("Dokumentenklasse auswaehlen!");
		IQueryClass queryClass = chooseQueryClass();
		
		// DIALOG UND DESKRIPTOR
		// suche (Default-) Dialog aus der gewählten Suchklasse

		IQueryDlg dialog = queryClass.getQueryDlg(IDialog.TYPE_DEFAULT); 
		// wähle daraus einen passenden Deskriptor aus
		IDescriptor descriptor = chooseDescriptor(dialog);
		
		// IValueDescriptor generieren
		IValueDescriptor valueDescriptor = factory.getValueDescriptorInstance(descriptor);
		// Suchanfrage anfuegen

		String anfrage = inputStreamString();
		
		valueDescriptor.addValue(anfrage);

		// Such-Parameter zusammenbasteln MEHRERE MOEGLICH
		IQueryParameter param = factory.getQueryParameterInstance(session, dialog);
		param.setStartDate(null);
		param.addValueDescriptor(valueDescriptor);
		
		//param.setOrderByExpression(arg0);
		System.out.println(param.getQueryToExecute());

		//param.setExpression(factory.getExpressionInstance("SELECT Kundennummer FROM DMS WHERE (Kundennummer = '4567891');"));

		IDocumentHitList hitList = server.query(param, session);

		return hitList.getDocumentObjects();
	}
	
	 public IDocumentHitList executeQuery(ISession session, IQueryParameter queryParameter)
	            throws BlueLineException
	    {
	        IDocumentHitList result = server.query(queryParameter, session);
	        return result;
	    }
	

	private IQueryClass getQueryClass(String searchclass){
		for(IQueryClass queryClass : queryClasses){
			if (searchclass.equals(queryClass))return queryClass;
		}
		return null;
	}
	public IDocument[] mysearchDocument(String searchclass, String searchword) throws BlueLineException, NumberFormatException, IOException{
		System.out.println("MY");
		// DOKUMENTENKLASSE
		System.out.println("Dokumentenklasse auswaehlen!");
		
		// Hole die Richtige Suchklasse
		IQueryClass queryClass = myChooseQueryClass(searchclass); 
		
		
		
		
		// DIALOG UND DESKRIPTOR
		// suche (Default-) Dialog aus der gewählten Suchklasse
		IQueryDlg dialog = queryClass.getQueryDlg(IDialog.TYPE_DEFAULT); 
		// wähle daraus einen passenden Deskriptor aus

		// alle deskriptoren 
		ArrayList<IDescriptor>descriptors = getDescriptors(dialog);
		// IValueDescriptorlist
		ArrayList<IValueDescriptor> valuedescriptors = new ArrayList<IValueDescriptor>();
		
		// alle IValueDescriptor generieren
		for(IDescriptor descr : descriptors){
			valuedescriptors.add(factory.getValueDescriptorInstance(descr));
		} 
		
		// Suchanfrage anfuegen
		//System.out.println(descriptor.getName() + ": ");
		//String anfrage = inputStreamString();
		
		// Jedem Valuedescriptor das suchwort adden
		/*for(IValueDescriptor descr : valuedescriptors){
			descr.addValue(searchword);
		}*/ 
		//cheeese
		valuedescriptors.get(9).addValue(searchword);
		
		// Such-Parameter zusammenbasteln MEHRERE MOEGLICH
		IQueryParameter param = factory.getQueryParameterInstance(session, dialog);
		
		// alle ValueDescriptors adden
		/*for(IValueDescriptor descr : valuedescriptors){
			param.addValueDescriptor(descr);
		}*/ 
		param.addValueDescriptor(valuedescriptors.get(9));

		// Server anfragen und das Ergebnis speichern
		IDocumentHitList hitList = server.query(param, session);
		return hitList.getDocumentObjects();
	}
	
	
public IDocument[] descriptorsearchDocument(int searchclass, String searchword, int descriptor_Number, Date firstDate, Date secondDate, int dateState) throws BlueLineException, NumberFormatException, IOException{
		
		IQueryClass queryClass = queryClasses[searchclass]; 


		// DIALOG UND DESKRIPTOR
		// suche (Default-) Dialog aus der gewählten Suchklasse
		IQueryDlg dialog = queryClass.getQueryDlg(IDialog.TYPE_DEFAULT); 
		// wähle daraus einen passenden Deskriptor aus

		// alle deskriptoren 
		IDescriptor descriptor = getDescriptors(dialog).get(descriptor_Number);
		System.out.println(descriptor.getDisplayString(session));

		IValueDescriptor valueDescriptor = factory.getValueDescriptorInstance(descriptor);
		
		valueDescriptor.addValue(searchword);
		
		
		// Such-Parameter zusammenbasteln MEHRERE MOEGLICH
		IQueryParameter param = factory.getQueryParameterInstance(session, dialog);
		param.setStartDate(null);
		System.out.println("Klasse: "+searchclass + " | Suchwort: "+ searchword + " | descriptornummer: " + descriptor_Number);
		param.addValueDescriptor(valueDescriptor);
		
		// zum Testen
		param.setHitLimit(1);
		param.setHitLimitThreshold(1);
		
		if(firstDate != null && secondDate != null){
			if(firstDate.getTime() < secondDate.getTime()){
				param.setStartDate(firstDate);
				param.setEndDate(secondDate);
			}else{
				param.setStartDate(secondDate);
				param.setEndDate(firstDate);
			}
		}
		if(firstDate != null){
			if(dateState == 0){
				/*param.addValueDescriptor(getDateDescriptor());
				IDescriptor datedescriptor = getDescriptors(dialog).get(descriptor_Number);
				System.out.println(descriptor.getDisplayString(session));

				IValueDescriptor valueDescriptor = factory.getValueDescriptorInstance(descriptor);
				valueDescriptor.addValue(searchword);
				*/
				param.setEndDate(firstDate);
			}else if(dateState == 1){
				param.setStartDate(firstDate);
			}
			
		}
		
		
		
		// Server anfragen und das Ergebnis speichern
		IDocumentHitList hitList = server.query(param, session);
		return hitList.getDocumentObjects();
	}
	
	public int inputStreamInteger() throws NumberFormatException, IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return Integer.parseInt(reader.readLine());
	}
	
	public String inputStreamString() throws NumberFormatException, IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader.readLine();
	}
	
	public IQueryClass chooseQueryClass() throws MetaDataException, NumberFormatException, IOException {
		// auswahl der Klassen ausgeben
		int i = 0;
		for(IQueryClass queryClass : queryClasses){
			System.out.println(i + ": " + queryClass);
			i++;
		}
		return queryClasses[inputStreamInteger()]; 
	}
	public IQueryClass myChooseQueryClass(String searchclass) throws MetaDataException, NumberFormatException, IOException {
		// auswahl der Klassen ausgeben
		int i = SearchClassMapper.getSearchClassNumber(searchclass);
		if (i == -1){
			return null;
		}		
		return queryClasses[i]; 
	}
	
	public IDescriptor chooseDescriptor(IQueryDlg dialog) throws BlueLineException, NumberFormatException, IOException{
		ArrayList<IDescriptor>descriptors = getDescriptors(dialog);
		int i = 0;
		for(IDescriptor descriptor : descriptors){
			System.out.println(i + ": " + descriptor.getName());
			i++;
		}

		// Benutzer darf einen Deskriptor auswählen...
		int number = inputStreamInteger();
		return descriptors.get(number);
	}
	
	 

	
	public ArrayList<IDescriptor> getDescriptors(IQueryDlg dialog) throws BlueLineException{
		// alle Komponenten dieses Eingabe-Dialoges zwischenspeichern
		IComponent[] components = dialog.getComponents();
		// hier kommen später die passenden Deskriptoren rein
		ArrayList<IDescriptor> descriptors = new ArrayList<IDescriptor>();
		// die Komponenten durchlaufen
		for (IComponent component : components) {
			// wenn die Komponente eine Eingabe-Komponente ist, kann man sie
			// als IControl casten und den dazugehörigen Deskriptor herausbekommen.
			// So bekommt man nur Deskriptoren, die in dieser Suchklasse wirklich Sinn ergeben. */
			if (component.getType() == (IDate.TYPE) || component.getType() == (IMaskedEdit.TYPE)) {
				// casten
				IControl control = (IControl) component;
				// Deskriptor-ID abfragen
				String descriptorId = control.getDescriptorID();
            	// aus der ID den wirklichen Deskriptor generieren...
            	IDescriptor descriptor = server.getDescriptor(descriptorId, session);
            	// wenn Deskriptor existiert und vom Typ String ist
            	if (descriptor != null && (
            			descriptor.getDescriptorType() == IDescriptor.TYPE_STRING  || 
            			descriptor.getDescriptorType() == IDescriptor.TYPE_DATE || 
            			descriptor.getDescriptorType() == IDescriptor.TYPE_INTEGER ||
            			descriptor.getDescriptorType() == IDescriptor.TYPE_LONG ||
            			descriptor.getDescriptorType() == IDescriptor.TYPE_INTEGER ||
            			descriptor.getDescriptorType() == IDescriptor.TYPE_DOUBLE ||
            			descriptor.getDescriptorType() == IDescriptor.TYPE_FLOAT
            			)) {
            		// in einer Array-List zwischenspeichern
            		descriptors.add(descriptor);
            	}
			}
			
		}
		return descriptors;		
	}
	
 	public IQueryClass[] getDocumentClasses(){
		return this.queryClasses;
	}
	
	public IQueryClass getDocumentClassByName(String name){
		// auswahl der Klassen ausgeben
		int i = 0;
		for(IQueryClass queryClass : queryClasses){
			if(queryClass.getName() == name){
				return queryClass;
			}
		}
		return null;
	}
}