package blueline_own;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.ser.blueline.BlueLineException;
import com.ser.blueline.IDateValue;
import com.ser.blueline.IDescriptor;
import com.ser.blueline.IDocument;
import com.ser.blueline.IDocumentHitList;
import com.ser.blueline.IDocumentServer;
import com.ser.blueline.IFulltextEngine;
import com.ser.blueline.IOrderByExpression;
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
	
	public List<IDocument> getDocumentsFromPrimaryKeys(List<String> primaryHitList, String primaryKey, int descriptor_Number, String searchClassString) throws NumberFormatException, IOException, BlueLineException{
		IQueryClass queryClass = myChooseQueryClass(searchClassString);
		IQueryDlg dialog = queryClass.getQueryDlg(IDialog.TYPE_DEFAULT);
		List <IDocument> docList = new ArrayList<IDocument>();
		
		
		for(String hit : primaryHitList){
			//Primary key nummer vom desc
			IDescriptor descriptor = getDescriptors(dialog).get( DescriptorMapper.getDescriptorNumber(searchClassString, primaryKey)     );

			IValueDescriptor valueDescriptor = factory.getValueDescriptorInstance(descriptor);
			System.out.println("Hit: '"+hit+"' valueDescriptor:'"+valueDescriptor.getDisplayString()+"'");
			valueDescriptor.addValue(hit);
			
			IQueryParameter param = factory.getQueryParameterInstance(session, dialog);
			param.setStartDate(null);
			param.addValueDescriptor(valueDescriptor);
			System.out.println("Query to execute: "+param.getQueryToExecute());
			IDocumentHitList hitList = server.query(param, session);
			for(IDocument act_doc : hitList.getDocuments()){
				docList.add(act_doc);
			}
			
		}
		
		
		return docList;
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

public List<IDocument> fulltextSearch(String searchword) throws BlueLineException, NumberFormatException, IOException{
		
	// Suchklasse mit Deskriptor mit Fulltextunterstuetzung
	IQueryClass queryClass = queryClasses[5]; 

	IQueryDlg dialog = queryClass.getQueryDlg(IDialog.TYPE_DEFAULT); 
	// Einziger Deskriptor mit Fulltextunterstuetzung
	IDescriptor descriptor = getDescriptors(dialog).get(10);
	

	IValueDescriptor valueDescriptor = factory.getValueDescriptorInstance(descriptor);
	
	valueDescriptor.addValue(searchword);
	valueDescriptor.isFulltext();
	
	
	// Such-Parameter zusammenbasteln MEHRERE MOEGLICH
	IQueryParameter param = factory.getQueryParameterInstance(session, dialog);
	param.setStartDate(null);
	
	param.addValueDescriptor(valueDescriptor);
	
	IDocumentHitList hitList = server.query(param, session);
		
		List<IDocument> documents = new ArrayList<IDocument>();	
		for(IDocument act_doc : hitList.getDocuments()){
			documents.add(act_doc);
		}

		return documents;
	}

	public List<IDocument> searchclassSearchwordSearch(String searchclass, int searchClass_Number, String searchword) throws BlueLineException, NumberFormatException, IOException{
	
	IQueryClass queryClass = queryClasses[searchClass_Number]; 

	IQueryDlg dialog = queryClass.getQueryDlg(IDialog.TYPE_DEFAULT); 
	
	ArrayList<IDescriptor>descriptors = getDescriptors(dialog);
	String expression = "";
	// Alle Descriptoren der Suchklasse des Typs String durchgehen und verodern. 
	for(IDescriptor descriptor : descriptors){
		// Wenn der Typ des Descriptors ein String ist. 1 ist Datum
		if(descriptor.getType() == 10){
			// zusammengesetze Deskriptoren m�ssen mit Unterstrichen verbunden werden
			expression += " ("+descriptor.getName().replace(' ', '_')+"='"+ searchword +"') OR";
		}
		
	}
	// letztes OR entfernen
	expression = expression.substring(0, expression.length()-3);
	
	String primaryKey = getPrimaryKey(searchClass_Number);
	String query = "SELECT DISTINCT "+primaryKey+ " FROM DMS WHERE"+expression+";";
	
	System.out.println("Die Query ist: "+query);
	
	
	List<String> primaryHitList = server.searchAttributeValues(session, query, true, null, null, 20);
	for(String a : primaryHitList){
		System.out.println(a);
	}
	int descriptor_Number = DescriptorMapper.getDescriptorNumber(searchclass, primaryKey);

	List<IDocument> hitList = getDocumentsFromPrimaryKeys(primaryHitList, primaryKey, descriptor_Number, searchclass);
	//addDateDescriptor(searchclass, firstDate, secondDate, dateState, param);

	return hitList;
	}

public List<IDocument> searchClassSearch(String searchclass) throws BlueLineException, NumberFormatException, IOException{
	
	
	
	String primaryKey = getPrimaryKey(getSearchClassNumber(searchclass));
	String expression = "";
	if(primaryKey.equals("Anfragedatum1")){
		expression += "("+primaryKey+">'10000101')";
	}else{
		expression += "("+primaryKey+" LIKE '*')";
	}
	String query = "SELECT "+primaryKey+ " FROM DMS WHERE "+expression+";";
	System.out.println("Die Query ist: "+query);
	
	
	List<String> primaryHitList = server.searchAttributeValues(session, query, true, null, null, 20);
	for(String a : primaryHitList){
		System.out.println(a);
	}
	int descriptor_Number = DescriptorMapper.getDescriptorNumber(searchclass, primaryKey);

	List<IDocument> hitList = getDocumentsFromPrimaryKeys(primaryHitList, primaryKey, descriptor_Number, searchclass);
	//addDateDescriptor(searchclass, firstDate, secondDate, dateState, param);

	return hitList;
	}

	
public List<IDocument> descriptorsearchDocument(int searchclass, String searchClassString, String searchword, String descriptor, int descriptor_Number, Date firstDate, Date secondDate, int dateState) throws BlueLineException, NumberFormatException, IOException{
		
		/*
		 * IQueryClass queryClass = queryClasses[searchclass]; 
		IOrderByExpression a = factory.getOrderByExpressionInstance();
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
		*/
		String query = getQuery(firstDate, secondDate, dateState, descriptor, searchword, searchclass);
		
		List<String> primaryHitList = server.searchAttributeValues(session, query, true, null, null, 20);
		for(String a : primaryHitList){
			System.out.println(a);
		}
		List<IDocument> hitList = getDocumentsFromPrimaryKeys(primaryHitList, getPrimaryKey(searchclass), descriptor_Number, searchClassString);
		//addDateDescriptor(searchclass, firstDate, secondDate, dateState, param);

		return hitList;
	}


	
	public String getQuery(Date firstDate, Date secondDate, int dateState, String descriptor, String searchword, int searchclass){
		
		String expression = getExpression(firstDate, secondDate, dateState, descriptor, searchword, searchclass);
		String primaryKey = getPrimaryKey(searchclass);
		String query = "SELECT "+primaryKey+ " FROM DMS WHERE "+expression+";";
		System.out.println("Die Query ist: "+query);
		return query;
	}
	
	public String getPrimaryKey(int searchclass){
		Map<Integer, String> prim_Key = new HashMap<Integer, String>();
		prim_Key.put(0, "Auftragsnummer");
		prim_Key.put(1, "Angebotsnummer");
		prim_Key.put(2, "Typenbezeichnungen");
		prim_Key.put(3, "Lieferscheinnummer");
		prim_Key.put(4, "Angebotnummer");
		prim_Key.put(5, "Rechnungsnummer");
		prim_Key.put(6, "Anfragedatum1");
		return  prim_Key.get(searchclass);
	}

	public String getExpression(Date firstDate, Date secondDate, int dateState, String descriptor, String searchword, int searchclass){
		String standartExpression ="(" + descriptor + "=" + "'"+searchword+"')";
		String expression = "";
		Map<Integer, String> dateType = new HashMap<Integer, String>();
		dateType.put(0, "Bestelldatum");
		dateType.put(1, "Angebotsdatum");
		dateType.put(2, null);
		dateType.put(3, "Lieferscheindatum");
		dateType.put(4, "Angebotsdatum");
		dateType.put(5, "Rechnungsdatum");
		dateType.put(6, "Anfragedatum1");
		
		if(firstDate != null && secondDate != null){
			if(firstDate.getTime() < secondDate.getTime()){
				Calendar cal = Calendar.getInstance();
				cal.setTime(firstDate);
				String year = Integer.toString((cal.get(Calendar.YEAR)));
				String month = Integer.toString((cal.get(Calendar.MONTH)+1));
				String day = Integer.toString((cal.get(Calendar.DAY_OF_MONTH)));
				System.out.println("Datum: "+year+month+day);
				if(day.length() < 2){
					day = "0"+day;
				}
				if(month.length() < 2){
					month = "0"+month;
				}
				
				if(dateType.get(searchclass) != null){
					String specificDateType = dateType.get(searchclass);
					String dateValue = year+month+day;
					expression =" AND (" + specificDateType + ">=" + "'"+dateValue+"')";
					
					
					cal.setTime(firstDate);
					year = Integer.toString((cal.get(Calendar.YEAR)));
					month = Integer.toString((cal.get(Calendar.MONTH)+1));
					day = Integer.toString((cal.get(Calendar.DAY_OF_MONTH)));
					System.out.println("Datum: "+year+month+day);
					if(day.length() < 2){
						day = "0"+day;
					}
					if(month.length() < 2){
						month = "0"+month;
					}
					dateValue = year+month+day;
					expression =expression + " AND (" + specificDateType + "<=" + "'"+dateValue+"')";
					
				}else{
					//TODO RaiseException es gibt kein Datum zu der Suchklasse -> Falsche Suchanfrage
				}
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTime(firstDate);
				String year = Integer.toString((cal.get(Calendar.YEAR)));
				String month = Integer.toString((cal.get(Calendar.MONTH)+1));
				String day = Integer.toString((cal.get(Calendar.DAY_OF_MONTH)));
				System.out.println("Datum: "+year+month+day);
				if(day.length() < 2){
					day = "0"+day;
				}
				if(month.length() < 2){
					month = "0"+month;
				}
				
				if(dateType.get(searchclass) != null){
					String specificDateType = dateType.get(searchclass);
					String dateValue = year+month+day;
					expression =" AND (" + specificDateType + "<=" + "'"+dateValue+"')";
					
					
					cal.setTime(firstDate);
					year = Integer.toString((cal.get(Calendar.YEAR)));
					month = Integer.toString((cal.get(Calendar.MONTH)+1));
					day = Integer.toString((cal.get(Calendar.DAY_OF_MONTH)));
					System.out.println("Datum: "+year+month+day);
					if(day.length() < 2){
						day = "0"+day;
					}
					if(month.length() < 2){
						month = "0"+month;
					}
					dateValue = year+month+day;
					expression =expression + " AND (" + specificDateType + ">=" + "'"+dateValue+"')";
				}
					
			}
		}
		else if(firstDate != null){
			System.out.println("Datestate: "+dateState);
			if(dateState >= 0){
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(firstDate);
				String year = Integer.toString((cal.get(Calendar.YEAR)));
				String month = Integer.toString((cal.get(Calendar.MONTH)+1));
				String day = Integer.toString((cal.get(Calendar.DAY_OF_MONTH)));
				System.out.println("Datum: "+year+month+day);
				if(day.length() < 2){
					day = "0"+day;
				}
				if(month.length() < 2){
					month = "0"+month;
				}
				
				if(dateType.get(searchclass) != null){
					String specificDateType = dateType.get(searchclass);
					String dateValue = year+month+day;
					expression =" AND (" + specificDateType + "#=" + "'"+dateValue+"')";
				}else{
					//TODO RaiseException es gibt kein Datum zu der Suchklasse -> Falsche Suchanfrage
				}
			}
			//bis
			if(dateState == 0){
				expression = expression.replace('#', '<');
			// ab
			}else if(dateState == 1){
				expression = expression.replace('#', '>');
			}
			
		}
		System.out.println("Die Expression ist: "+standartExpression + expression);
		return standartExpression + expression;
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
	
	public int getSearchClassNumber(String searchclass) throws MetaDataException, NumberFormatException, IOException {
		int i = SearchClassMapper.getSearchClassNumber(searchclass);
		if (i == -1){
			return (Integer) null;
		}		
		return i; 
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