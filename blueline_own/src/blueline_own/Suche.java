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
	
	@SuppressWarnings("deprecation")
	public IDocument[] searchDocument() throws BlueLineException, NumberFormatException, IOException{
		System.out.println("normal search");
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
		
		Date d = new Date();
		Date e = new Date();
		d.setYear(1900);d.setDate(2);d.setMonth(0);e.setYear(2500);e.setDate(2);e.setMonth(0);
		//server.query(arg0, session);
		
		for(String s : server.searchAttributeValues(session, "SELECT Kundenname FROM DMS", false, d, e, 20)){
			System.out.println(s);
		}
		
		for(String s : session.getDatabaseNames() ){
			System.out.println(s);
		}
		
		
		// alternative Herangehensweise
				HashMap<String, String> searchDescriptors = new HashMap<String, String>();
		        searchDescriptors.put(descriptor.getId(), "Rheinwerk Group");
		        
		        IQueryParameter queryParameter = query(session, dialog, searchDescriptors);
				
				
				IDocumentHitList result = executeQuery(session, queryParameter);
				System.out.println(result.getDocumentObjects().length);
		// bis hier
		
		
		
		// Such-Parameter zusammenbasteln MEHRERE MOEGLICH
		IQueryParameter param = factory.getQueryParameterInstance(session, dialog);
		param.addValueDescriptor(valueDescriptor);
		//param.setOrderByExpression(arg0);
		System.out.println(param.getQueryToExecute());

		//param.setExpression(factory.getExpressionInstance("SELECT Kundennummer FROM DMS WHERE (Kundennummer = '4567891');"));

		IDocumentHitList hitList = server.query(param, session);

		System.out.println(hitList.getTotalHitCount());
		System.out.println(hitList.getInformationObjects().length + " <- Hitlist l�nge");

		return hitList.getDocumentObjects();
	}
	
	 public IDocumentHitList executeQuery(ISession session, IQueryParameter queryParameter)
	            throws BlueLineException
	    {
	        IDocumentHitList result = server.query(queryParameter, session);
	        return result;
	    }
	
	public IQueryParameter query(ISession session, IQueryDlg queryDlg, HashMap<String, String> descriptorValues)
            throws BlueLineException
    {
        IQueryParameter queryParameter = null;
        IQueryExpression expression = null;
        // Retrieve all components from the query dialog
        IComponent components[] = queryDlg.getComponents();
        int i;

        // Create the query expression by traversing over all components of the dialog.
        for (i = 0; i < components.length; i++)
        {
            // If the component is a masked edit field, check for the assigned descriptor.
            if (components[i].getType() == IMaskedEdit.TYPE)
            {
                // If the component is of type "masked edit", the component might be casted to
                // IControl or IMaskedEdit.
                IControl control = (IControl) components[i];

                // Get the descriptor ID from the control.
                String descriptorId = control.getDescriptorID();

                // Get the value for this descriptor.
                String value = (String) descriptorValues.get(descriptorId);

                // If the value is not null and not an empty string, add this descriptor to the
                // query expression. Descriptors on documents must not be null or empty strings.
                if (value != null && value.trim().length() > 0)
                {
                    IQueryValueDescriptor queryValueDescriptor;

                    // Get the descriptor instance from the document server.
                    IDescriptor descriptor = server.getDescriptor(descriptorId, session);

                    // Create a value descriptor for the descriptor instance and add the value.
                    queryValueDescriptor = factory.getQueryValueDescriptorInstance(descriptor);
                    queryValueDescriptor.addValue(value);

                    // Create an expression instance for this query value descriptor.
                    IQueryExpression expr = queryValueDescriptor.getExpression();

                    // If expression has been built during the previous loops, combine the existing
                    // expression with the new one using the AND operator.
                    if (expression != null)
                        expression = factory.getExpressionInstance(expression, expr, IQueryOperator.AND);
                    // Otherwise just initialize expression with expr.
                    else
                        expression = expr;
                }
            }
        }

        if (expression != null)
        {
            // Create a query parameter instance from the session, the query dialog to use and
            // the constructed expression.
            queryParameter = factory.getQueryParameterInstance(session, queryDlg, expression);
        }
        return queryParameter;
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
	
	
public IDocument[] descriptorsearchDocument(int searchclass, String searchword, int descriptor_Number) throws BlueLineException, NumberFormatException, IOException{
		
		IQueryClass queryClass = queryClasses[searchclass]; 
		System.out.println(queryClass.getDisplayString(session));

		// DIALOG UND DESKRIPTOR
		// suche (Default-) Dialog aus der gewählten Suchklasse
		IQueryDlg dialog = queryClass.getQueryDlg(IDialog.TYPE_DEFAULT); 
		// wähle daraus einen passenden Deskriptor aus

		// alle deskriptoren 
		IDescriptor descriptor = getDescriptors(dialog).get(descriptor_Number);
		System.out.println(descriptor.getDisplayString(session));

		IValueDescriptor valueDescriptor = factory.getValueDescriptorInstance(descriptor);
		
		valueDescriptor.addValue(searchword);
		
		System.out.println(valueDescriptor.getDisplayString(session));
		// Such-Parameter zusammenbasteln MEHRERE MOEGLICH
		IQueryParameter param = factory.getQueryParameterInstance(session, dialog);
		System.out.println("Klasse: "+searchclass + " | Suchwort: "+ searchword + " | descriptornummer: " + descriptor_Number);
		System.out.println(param.toString());
		param.addValueDescriptor(valueDescriptor);

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