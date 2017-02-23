package blueline_own;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ser.blueline.BlueLineException;
import com.ser.blueline.BluelineContext;
import com.ser.blueline.IDescriptor;
import com.ser.blueline.IDocument;
import com.ser.blueline.IDocumentHitList;
import com.ser.blueline.IDocumentImportFilter;
import com.ser.blueline.IDocumentPart;
import com.ser.blueline.IDocumentServer;
import com.ser.blueline.IFDE;
import com.ser.blueline.IFileFDE;
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
import com.ser.blueline.compoundentities.DirectoryNodeType;
import com.ser.blueline.compoundentities.IDirectoryNode;
import com.ser.blueline.compoundentities.IDirectoryNodes;
import com.ser.blueline.compoundentities.IDirectoryObject;
import com.ser.blueline.compoundentities.IDirectoryType;
import com.ser.blueline.metaDataComponents.IArchiveClass;
import com.ser.blueline.metaDataComponents.IArchiveDlg;
import com.ser.blueline.metaDataComponents.IComponent;
import com.ser.blueline.metaDataComponents.IControl;
import com.ser.blueline.metaDataComponents.IMaskedEdit;
import com.ser.blueline.metaDataComponents.IMultiLineEdit;
import com.ser.blueline.metaDataComponents.IQueryClass;
import com.ser.blueline.metaDataComponents.IQueryDlg;
import com.ser.blueline.metaDataComponents.IResultControl;
import com.ser.blueline.metaDataComponents.IResultDlg;
import com.ser.foldermanager.IFolderConnection;
import com.ser.foldermanager.IFolderDefinitions;
import com.ser.sedna.client.bluelineimpl.SEDNABluelineAdapterFactory;
import com.ser.wfl.bpmline.api.EnumWBType;
import com.ser.wfl.bpmline.api.IWFFrame;
import com.ser.wfl.bpmline.api.IWFSession;
import com.ser.wfl.bpmline.api.IWFWorkBasket;
import com.ser.wfl.bpmline.api.IWFWorkItem;
import com.ser.wfl.bpmline.api.IWFWorkItems;
import com.ser.wfl.bpmline.api.WFClassFactory;

/**
 * This is an example class that demonstrates how to use the BlueLine. You will find examples for the following
 * operations: <br>
 * <ul>
 * <li> Instantiation of the class factory. </li>
 * <li> Reading properties from an ini file. </li>
 * <li> Adding/changing properties. </li>
 * <li> Instantiation of a document server. </li>
 * <li> Filing a document. </li>
 * <li> Querying the document server. </li>
 * <li> Retrieving a result list. </li>
 * <li> Executing a change of document descriptors.</li>
 * <li> Creating a new document version and locking/unlocking the document </li>
 * <li> Login to a document server. </li>
 * <li> Logout from document server. </li>
 * <li> Create an directory object, add folders and documents to it</li>
 * </ul>
 * <br>
 * 
 * The demo class assumes the following configuration of the system: <br>
 * <ul>
 * <li> The CSB server is called "demoserver". It has an organization called "democustomer". </li>
 * <li> On this server there is a user "Supervisor" with password "Supervisor" and all rights 
 *      necessary for the actions of this demo program. </li>
 * <li> There are two descriptors "DemoDescriptor1" and "DemoDescriptor2". </li>
 * <li> There is a document class "DemoDocumentClass". </li>
 * <li> A filing dialog of type "default" is defined in this document class. </li>
 * <li> There is a search class "DemoSearchClass". </li>
 * <li> A search dialog of type "default" is defined in this search class. </li>
 * <li> There is a directory class "JN_DirectoryClass1" defined, which uses a directory structure definition </li>
 * <li> There is a directory C:/temp that contains the configuration files Blueline.ini and log4j.xml as well as a
 *      file Datei1.txt which will be archived. Write access to this
 *      directory is granted. The result list will be written into the file result.csv in this directory. </li>
 * </ul>
 * <br>
 * In order to install and run the demo application on your local workstation, perform the following steps:
 * <ol>
 * <li> Set up  Blueline as described on the start page of this documentation.
 * <li> Install ant 1.6.5. You can download ant from <a href="http://ant.apache.org/bindownload.cgi" target=_blank>http://ant.apache.org/bindownload.cgi</a>.
 * <li> Add the installation directory to your system's search path.
 * <li> Copy the <code>demo</code> directory from the installation CD onto your local hard disk. Make sure that the target directory is not write-protected.
 * <li> Open the <code>blueline.ini</code> configuration file and adjust the following parameters to match your CSB Server installation: <BR>
 * ArchivServerName, SeratioServerName, ArchivPort, SeratioPort.
 * <li> Start a console and go to the target directory.
 * <li> Execute the command <code>ant</code>.
 * </ol>
 * <a href="../src-html/demo/BlueLineDemo.html" target=_blank>Display Java source code in separate window </a>
 * <br>
 */
public class BlueLineDemo
{
    private static final String iniFileName = "C:/temp/Blueline.ini";

    private static final String serverName = "km4";
    private static final String port = "8080";
    private static final String customerName = "democustomer";
    private static final String userName = "Supervisor";
    private static final String password = "Supervisor";
    private static final String workflowServerName = "demowfserver";
    private static final String workflowServerPort = "3466";

    private static Log logger = LogFactory.getLog(BlueLineDemo.class);

    private IDocumentServer documentServer;
    private ISerClassFactory classFactory;

    private String documentID;

    /**
     * This method initializes the class factory and the document server instance.
     * @param archiveServerName Host name of the web application server that hosts Doxis4 CSB.
     * @param archivePort       Port number used by the web application server, for example 8080.
     * @param seratioServerName Equal to archiveServer.
     * @param seratioPort       Equal to archivePort.
     * @throws Exception        in case of any error.
     */
    public void initServer(String archiveServerName, String archivePort)
            throws Exception
    {
        // Instantiate the class factory.
        //Class factoryClass = Class.forName("de.serac.bluelineimpl.SERACClassFactory");
        //classFactory = (ISerClassFactory) factoryClass.newInstance();
        classFactory = SEDNABluelineAdapterFactory.getInstance();

        // Instantiate a properties object filled with the values in the configuration file name.
        IProperties properties = classFactory.getPropertiesInstance(iniFileName);

        // Add/Change some properties for the connection to the server.
        properties.setProperty("Global", "ArchivServerName", archiveServerName);
        properties.setProperty("Global", "SeratioServerName", archiveServerName);
        properties.setProperty("Global", "ArchivPort", archivePort);
        properties.setProperty("Global", "SeratioPort", archivePort);
        properties.setProperty("Global", "TmpDir", "C:/temp");

        //Instantiate the DocumentServer now.
        documentServer = classFactory.getDocumentServerInstance(properties);
    }

    /**
     * Logs on a user.
     * @param systemName            The name of the organization to log on to.
     * @param userName              The name of the user to log on.
     * @param password              The password of the user.
     * @return                      A session object if login was succesful, null otherwise.
     * @throws BlueLineException    in case of any error.
     */
    public ISession login(String systemName, String userName, String password)
            throws BlueLineException
    {
        ISession session = null;

        // Retrieve the Doxis4 CSB organization for the systemName
        ISystem system = documentServer.getSystem(systemName);

        // Login to the server
        ITicket ticket = documentServer.login(system, userName, password.toCharArray());

        // If the ticker state is valid create a session object.
        // Note: Other ticket states are not handled here.
        if (ticket.isValid())
        {
            session = documentServer.createSession(ticket);
        }

        return session;
    }

    /**
     * This method searches for the dialog of type "default" in the document class
     * which is defined by its id.
     * @param session       A valid BlueLine session.
     * @param archiveClass  The document class.
     * @return              The filing dialog of type "DemoArchiveDlg" in this class or null, if
     *                      it does not exist.
     * @throws BlueLineException in case of any error.
     */
    public IArchiveDlg findArchiveDlgForDocumentClass(ISession session, IArchiveClass archiveClass)
            throws BlueLineException
    {
        IArchiveDlg dlg = null;

        if (archiveClass != null)
        {
            // Retrieves the dialog of type "default" from the archive class.
            dlg = archiveClass.getArchiveDlg("default");
        }
        return dlg;
    }

    /**
     * This methods files a document. A document is created which consists of only one file. This file is
     * the content of the first content object in the first representation.
     * @param session           A valid BlueLine-session.
     * @param archiveDlg        The filing dialog to be used.
     * @param descriptorValues  A HashMap with descriptor values.
     * @param fileName          The name of the file to be archived.
     * @throws BlueLineException in case of a BlueLine error.
     * @throws Exception  in case of other errors.
     */
    public void archive(ISession session, IArchiveDlg archiveDlg, HashMap<String, String> descriptorValues, String fileName)
            throws BlueLineException, Exception
    {
        // Create a document instance. This document instance consists of one empty representation.
        IDocument doc = classFactory.getDocumentInstance(archiveDlg, session);

        // Retrieve all components from the filing dialog
        IComponent components[] = archiveDlg.getComponents();
        int i;
        boolean oneDescriptorFilled = false;

        // Traverse over all components of the dialog.
        for (i = 0; i < components.length; i++)
        {
            // If the component is a masked edit field, check for the assigned descriptor.
            if (components[i].getType() == IMaskedEdit.TYPE || components[i].getType() == IMultiLineEdit.TYPE)
            {
                // If the component is of type "masked edit", the component might be casted to
                // IControl or IMaskedEdit.
                IControl control = (IControl) components[i];

                // Get the descriptor ID from the control.
                String descriptorId = control.getDescriptorID();

                // Get the value for this descriptor.
                String value = (String) descriptorValues.get(descriptorId);

                // If the value is not null and not an empty string, add this descriptor to the
                // document. Descriptors on documents must not be null or empty strings.
                if (value != null && value.trim().length() > 0)
                {
                    // Get the descriptor instance from the document server.
                    IDescriptor descriptor = documentServer.getDescriptor(descriptorId, session);

                    // Create a value descriptor for the descriptor instance.
                    IValueDescriptor valueDescriptor;
                    valueDescriptor = classFactory.getValueDescriptorInstance(descriptor);

                    // Add the value to the value descriptor
                    valueDescriptor.addValue(value);
                    oneDescriptorFilled = true;

                    // Add the value descriptor to the document.
                    doc.addDescriptor(valueDescriptor);
                }
            }
        }

        // Do not archive documents which have no descriptor values set.
        if (!oneDescriptorFilled)
            throw new Exception("At least one descriptor must be filled.");

        // For archiving the document create a file import filter and initialize it with the file
        // which should be archived.
        IDocumentImportFilter filter = documentServer.getDocumentImportFilter(IDocumentImportFilter.FILE);
        filter.init(new File(fileName));

        // Retrieve a content object from this filter. (Note: The filter might return multiple
        // content objects, so better use a loop).
        IDocumentPart docPart = filter.getNextDocumentPart();

        // Now add the content object to the document.
        doc.addPartDocument(docPart, 0);

        // Close the filter
        filter.close();

        // File the document on the server.
        documentServer.archiveDocument(doc, session, true);
    }

    /**
     * This method searches for the dialog of type "default" in the search class
     * which is defined by its id.
     * @param session           A valid BlueLine session.
     * @param queryClass        The ID of the searchclass.
     * @return                  The search dialog of type "DemoSearchDlg" in this class or null, if 
     *                          it does not exist.
     * @throws BlueLineException in case of any error.
     */
    public IQueryDlg findQueryDlgForQueryClass(ISession session, IQueryClass queryClass)
            throws BlueLineException
    {
        IQueryDlg dlg = null;

        if (queryClass != null)
        {
            // Retrieves the dialog of type "DemoArchiveDlg" from the archive class.
            dlg = queryClass.getQueryDlg("default");
        }
        return dlg;
    }

    /**
     * This method queries the archive.
     * @param session           A valid BlueLine session.
     * @param queryDlg          The query dialog to be used.
     * @param descriptorValues  A hash map with descriptor values.
     * @return The result of the document query.
     * @throws BlueLineException in case of a BlueLine error.
     */
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
                    IDescriptor descriptor = documentServer.getDescriptor(descriptorId, session);

                    // Create a value descriptor for the descriptor instance and add the value.
                    queryValueDescriptor = classFactory.getQueryValueDescriptorInstance(descriptor);
                    queryValueDescriptor.addValue(value);

                    // Create an expression instance for this query value descriptor.
                    IQueryExpression expr = queryValueDescriptor.getExpression();

                    // If expression has been built during the previous loops, combine the existing
                    // expression with the new one using the AND operator.
                    if (expression != null)
                        expression = classFactory.getExpressionInstance(expression, expr, IQueryOperator.AND);
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
            queryParameter = classFactory.getQueryParameterInstance(session, queryDlg, expression);
        }
        return queryParameter;
    }

    public IDocumentHitList executeQuery(ISession session, IQueryParameter queryParameter)
            throws BlueLineException
    {
        IDocumentHitList result = documentServer.query(queryParameter, session);
        return result;
    }

    /**
     * This method queries the archive. The result list is written into a file. 3 columns will be obtained
     * in the demo configuration:
     * <ul>
     * <li> The instance date </li>
     * <li> The value of DemoDescriptor1 </li>
     * <li> The value of DemoDescriptor2 </li>
     * </ul>
     * @param session       A valid BlueLine session.
     * @param queryDlg      The query dialog to be used.
     * @param result        The results of the query.
     * @param csvFileName   The name of the file into which the result list will be written.
     * @throws BlueLineException in case of a BlueLine error.
     * @throws java.io.IOException in case of errors while writing to the file.
     * @throws Exception in case of errors in demo program.
     */
    public void createResultListAsCSV(ISession session, IQueryDlg queryDlg, IDocumentHitList result, String csvFileName)
            throws BlueLineException, IOException, Exception
    {
        int i, j;

        Vector<IDescriptor> descriptors = new Vector<IDescriptor>();

        // Retrieve the result list definition from the query dialog.
        IResultDlg resultDlg = queryDlg.getResultDlg();

        // Retrieve all columents of the result list and traverse over the columns.
        IResultControl resultControls[] = resultDlg.getControls();
        for (i = 0; i < resultControls.length; i++)
        {
            // If a column is bound to a descriptor, save this descriptor
            // into a vector.
            if (resultControls[i].getID() != null)
            {
                IDescriptor descriptor = documentServer.getDescriptor(resultControls[i].getID(), session);
                if (descriptor == null)
                    descriptor = documentServer.getInternalDescriptor(session, resultControls[i].getID());
                if (descriptor != null)
                    descriptors.addElement(descriptor);
                else
                    throw new Exception("Descriptor with ID " + resultControls[i].getID() + " is missing");
            }
        }

        // If there are no columns in the result list, leave the method with
        // an exception.
        if (descriptors.size() == 0)
            throw new Exception("No descriptors available in result dialog");

        // Open a file for writing
        FileWriter fileWriter = new FileWriter(csvFileName);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        // Retrieve all documents from the result list and traverse over the documents.
        IDocument documents[] = result.getDocumentObjects();
        for (i = 0; i < documents.length; i++)
        {
            // Retrieve the instance date and write it to the file.
            Date docDate = documents[i].getDocumentID().getDocumentDate();
            fileWriter.write(sdf.format(docDate) + ";");

            // Traverse over all descriptors found in the result list.
            for (j = 0; j < descriptors.size(); j++)
            {
                IDescriptor descriptor = (IDescriptor) descriptors.get(j);
                // Retrieve the descriptor values from the document.
                IValueDescriptor vd = documents[i].getDescriptor(descriptor);

                if (vd != null)
                {
                    // Write the values to the file (for multiple values: comma separated).
                    String values[] = vd.getStringValues();
                    for (int k = 0; k < values.length; k++)
                    {
                        if (k == 0)
                            fileWriter.write(descriptor.getType() + "=" + values[k]);
                        else
                            fileWriter.write("," + descriptor.getType() + "=" + values[k]);
                    }
                }
                fileWriter.write(";");
            }
            fileWriter.write("\n");
        }

        // Close the file writer.
        fileWriter.close();
    }

    /**
     * This method demonstrates how to change document descriptors.
     * @param session A valid BlueLine session.
     * @param document The document for which the descriptor change should be executed.
     * @param descriptor The descriptor for which a new value should be defined.
     * @param newValue The new value for the descriptor.
     * @throws Exception in case of a Blueline error.
     */
    public void executeKeyChange(ISession session, IDocument document, IDescriptor descriptor, String newValue)
            throws Exception
    {
        // Check for empty values. A Blueline client application should never define empty values.
        if (newValue == null || newValue.trim().length() == 0)
            throw new Exception("Empty values for descriptors are not allowed");
        IValueDescriptor valueDescriptor = document.getDescriptor(descriptor);
        if (valueDescriptor != null)
        {
            String[] newValues = { newValue };
            valueDescriptor.setValues(newValues);
        }
        else if (newValue != null)
        {
            valueDescriptor = classFactory.getValueDescriptorInstance(descriptor);
            valueDescriptor.addValue(newValue);
            document.addDescriptor(valueDescriptor);
        }
        // So far the descriptor is changed on the memory object. Now invoke updateDocument to persist
        // this change on the document server. (Remark: applications which support ITA platforms must
        // invoke updateDocumentKeys in this case.
        documentServer.updateDocument(document, session);
    }

    /**
     * This method demonstrates how to lock a document, to create a new version and to unlock the document.
     * @param session A valid Blueline session
     * @param document The document for which a new version should be created.
     * @throws BlueLineException in case of a Blueline error.
     */
    public void createNewVersion(ISession session, IDocument document)
            throws BlueLineException
    {
        // Creating a new version usually starts with locking the document. 
        // The document will be locked by calling checkoutDocument.
        // In a real application you will first save the document content to a local directory.
        // This directory can be passed as third parameter.
        documentServer.checkoutDocument(session, document, "C:/temp");

        // Now the document is locked. In a real application the user would change
        // the document content now. After some days the user will check in a new version.
        // This can be implemented as follows:

        // First create a new document version.
        IDocument newVersion = documentServer.getNewDocumentVersion(document, session);

        // Now this new version does not contain any descriptors. In our sample we copy the descriptors from
        // the old version.

        IValueDescriptor[] valueDescriptors = document.getDescriptorList();
        for (int i = 0; i < valueDescriptors.length; i++)
        {
            IDescriptor descriptor = valueDescriptors[i].getDescriptor();
            String[] values = valueDescriptors[i].getStringValues();
            IValueDescriptor valueDescriptor = classFactory.getValueDescriptorInstance(descriptor, values);
            newVersion.addDescriptor(valueDescriptor);
        }

        // So far the new version does not contain any repesentation or content object.
        // In our sample we simply add one document, as we did before when archiving the document.

        IDocumentImportFilter filter = documentServer.getDocumentImportFilter(IDocumentImportFilter.FILE);
        String fileName = "C:/temp/Datei1.txt";
        filter.init(new File(fileName));

        // Retrieve a content object from this filter. (Note: The filter might return multiple
        // document parts, so better use a loop).
        IDocumentPart docPart = filter.getNextDocumentPart();

        // Now add the content object to the document.
        newVersion.addPartDocument(docPart, 0);

        // Close the filter
        filter.close();

        // Now we have created a memory object for our new version. We persist this document
        // by calling archiveDocument
        documentServer.archiveDocument(newVersion, session);

        // The last step is removing the lock. Here you can pass the old or the new version
        // of the document.
        documentServer.checkinDocument(session, newVersion);
    }

    /**
     * This methods finishes the user session.
     * @param session The session to be logged out.
     * @throws BlueLineException
     */
    public void logout(ISession session) throws BlueLineException
    {
        documentServer.logout(session);
    }

    /**
     * This method closes the IDocumentServer object. It must be invoked, if the
     * IDocumentServer object is no longer needed to release all used resources
     * @throws BlueLineException in case of errors
     */
    public void closeServer()
    {
        if (documentServer != null)
        {
            try
            {
                documentServer.close();
            } catch (BlueLineException e)
            {
                logger.error("Catched Exception", e);
            }
            documentServer = null;
        }
    }

    /**
     * This method exports the document's content into temporary files
     * @param session The session which exports the documents
     * @param document The document to be exported
     * @throws BlueLineException in case of errors
     */
    public void exportDocumentContent(ISession session, IDocument document) throws BlueLineException, IOException
    {
        this.documentID = document.getDocumentID().getID();
        for (int representationConter = 0; representationConter < document.getRepresentationCount(); representationConter++)
        {
            for (int partDocumentCounter = 0; partDocumentCounter < document.getPartDocumentCount(representationConter); partDocumentCounter++)
            {
                IDocumentPart partDocument = document.getPartDocument(representationConter, partDocumentCounter);
                InputStream inputStream = null;
                try
                {
                    // For better performance, the content object's raw data stream should be opened before
                    // accessing the FDE!
                    inputStream = partDocument.getRawDataAsStream();

                    IFDE fde = partDocument.getFDE();
                    if (fde.getFDEType() == IFDE.FILE)
                    {
                        FileOutputStream fileOutputStream = new FileOutputStream("C:/temp/output_" + representationConter + "_" + partDocumentCounter + "." +
                                ((IFileFDE) fde).getShortFormatDescription());
                        byte[] bytes = new byte[2048];
                        int length;
                        while ((length = inputStream.read(bytes)) > -1)
                        {
                            fileOutputStream.write(bytes, 0, length);
                        }
                        fileOutputStream.close();
                    }
                } finally
                {
                    if (inputStream != null)
                        inputStream.close();
                }
            }
        }
    }

    /**
     * The main method calls all methods of the demo class in the correct order.
     */
    private void demoMain()
    {
        IQueryParameter queryParameter = null;
        try
        {
            String archiveClassName = "DemoDokumente";
            String nameDescriptor1 = "DemoDescriptor1";
            String nameDescriptor2 = "DemoDescriptor2";
            String searchClassName = "DemoSearchClass";

            IArchiveClass archiveClass;
            IDescriptor descriptor1, descriptor2;
            IQueryClass queryClass;

            ISession session;

            // Initialize the server in the demo program
            initServer(serverName, port);

            // Log on to the server.
            session = login(customerName, userName, password);
            if (session != null)
            {
                archiveClass = documentServer.getArchiveClassByName(session, archiveClassName);
                descriptor1 = documentServer.getDescriptorByName(nameDescriptor1, session)[0];
                descriptor2 = documentServer.getDescriptorByName(nameDescriptor2, session)[0];
                queryClass = documentServer.getQueryClassByName(session, searchClassName);

                // Search for the filing dialog.
                IArchiveDlg dlg = findArchiveDlgForDocumentClass(session, archiveClass);

                /*
                 * Now it is task of a client application to display a dialog matching the 
                 * definition in dlg.
                 * In this demo class we do not show a dialog. Instead of this we create a 
                 * HashMap with the descriptor values that the user would enter in this dialog.
                 * The keys in this HashMap are descriptor names, the values in the HashMap are
                 * the values that the user would have defined.
                 * We assume that the user wants to file a document which just consists of one
                 * single file.
                 */
                HashMap<String, String> archiveDescriptors = new HashMap<String, String>();
                archiveDescriptors.put(descriptor1.getId(), "MyValue1");
                archiveDescriptors.put(descriptor2.getId(), "myvalue");

                // Now archive the document. 
                archive(session, dlg, archiveDescriptors, "C:/temp/Datei1.txt");

                // search for the query dialog.
                IQueryDlg queryDlg = findQueryDlgForQueryClass(session, queryClass);

                /*
                 * Now it is task of a client application to display a dialog matching the 
                 * definition in queryDlg.
                 * In this demo class we do not show a dialog. Instead of this we create a 
                 * HashMap with the descriptor values that the user would enter in this dialog.
                 */
                HashMap<String, String> searchDescriptors = new HashMap<String, String>();
                searchDescriptors.put(descriptor1.getId(), "MyValue*");
                searchDescriptors.put(descriptor2.getId(), "myvalu*");

                // Search for documents
                queryParameter = query(session, queryDlg, searchDescriptors);

                // If the result list is not empty, create a csv-file with the results.
                if (queryParameter != null)
                {
                    IDocumentHitList result = executeQuery(session, queryParameter);
                    createResultListAsCSV(session, queryDlg, result, "C:/temp/result.csv");
                    IDocument[] hits = result.getDocumentObjects();
                    if (hits != null && hits.length > 0)
                    {
                        IDocument document = hits[0];
                        executeKeyChange(session, document, descriptor1, "new value");

                        exportDocumentContent(session, document);

                        createNewVersion(session, document);
                    }
                    queryParameter.close();
                }

                // Log out from the server
                logout(session);
            }
        } catch (Exception e)
        {
            /*
             * As this is just an demonstration of using BlueLine, exceptions are not handled here. 
             */
            logger.error("Catched Exception", e);
        } finally
        {
            // Close the query parameter object
            try
            {
                if (queryParameter != null)
                    queryParameter.close();
            } catch (BlueLineException e)
            {
                logger.error("Catched Exception", e);
            }
            // Invoke closeServer, if IDocumentServer is no longer needed.
            closeServer();
        }
    }

    /**
     * Main method for demo application 
     * @param args Arguments passed to program.
     */
    public static void main(String[] args)
    {
        BlueLineDemo blueLineDemo = new BlueLineDemo();
        blueLineDemo.demoMain();
        blueLineDemo.connectToAllServers();
        blueLineDemo.directoryDemo();
        try
        {
            BluelineContext.getInstance().shutdown();
        } catch (Exception e)
        {
            logger.error("Closing the BluelineContext failed", e);
        }
    }

    /**
     * This method demonstrates how to use interfaces for document management, records management and workflow.
     * @throws BlueLineException in case of erros
     */
    private void connectToAllServers()
    {
        ISession session = null;
        IWFSession wfSession = null;
        IFolderConnection folderConnection = null;

        try
        {

            // Initialize the server in the demo program
            initServer(serverName, port);

            // Login to the server.
            session = login(customerName, userName, password);
            if (session != null)
            {
                // Connect to workflow server
                IWFFrame wfFrame = WFClassFactory.createFrame();
                wfFrame.setServer(workflowServerName);
                wfFrame.setSystem(workflowServerPort);
                wfSession = wfFrame.userConnect(session, documentServer);

                // Gind the user's personal workbasket.
                IWFWorkBasket workBasket = wfSession.getWorkBaskets().getItemsByFilter(EnumWBType.wfWB_PERSONAL)[0];
                IWFWorkItems workItems = workBasket.getWorkBasketContent().getWorkItems();
                for (int i = 0; i < workItems.getCount(); i++)
                {
                    IWFWorkItem workItem = workItems.getItem(i);
                    logger.info(i + ". " + workItem.getGUID());
                }

                // Establish usage of records management.
                folderConnection = session.getFolderConnection();

                // Find all record definitions
                IFolderDefinitions folderDefinitions = folderConnection.getFolderDefinitions();
                for (int i = 0; i < folderDefinitions.getCount(); i++)
                {
                    logger.info("Aktendefinition: " + folderDefinitions.getItem(i).getName());
                }

            }
        } catch (Throwable t)
        {
            logger.error("Catched Exception", t);
        } finally
        {
            // Logout from Workflow server
            if (wfSession != null)
            {
                try
                {
                    wfSession.logout();

                } catch (Exception e)
                {
                    logger.info("Logout from workflow server has failed");
                    logger.error("Catched Exception", e);
                }
            }

            if (folderConnection != null)
            {
                folderConnection.logout();
            }

            try
            {
                // Logout from the server
                if (session != null)
                    logout(session);
            } catch (BlueLineException e)
            {
                logger.info("Logout from CSB server has failed");
                logger.error("Catched Exception", e);
            }
            // Invoke closeServer, if IDocumentServer is no longer needed.
            closeServer();
        }
    }

    private void directoryDemo()
    {
        try
        {
            String directoryClassName = "JN_DirectoryClass1";

            ISession session;

            // Initialize the server in the demo program
            initServer(serverName, port);

            // Log on to the server.
            session = login(customerName, userName, password);
            if (session != null)
            {
                IDirectoryType directoryType = documentServer.getDirectoryTypeByName(session, directoryClassName);
                IDirectoryObject dir = documentServer.createDirectoryObject(session, directoryType, "TestDirectory1");
                dir.commit();

                // The directotry class uses a directotry definition
                // So nodes.getItem(0) is available
                IDirectoryNodes nodes = dir.getNodes();
                IDirectoryNode node = nodes.getItem(0);

                // Create local folders in the directory
                IDirectoryNode subnode1 = node.getChildNodes().addNew(DirectoryNodeType.STATIC, "Local1");
                subnode1.commit();

                IDirectoryNode subnode2 = node.getChildNodes().addNew(DirectoryNodeType.STATIC, "Local2");
                subnode2.commit();

                // Append a document to a folder
                if (documentID != null)
                {
                    IDocument document = documentServer.getDocument4ID(documentID, session);
                    subnode1.getDocuments().appendDocument(document);
                    logger.info("Added document to directory node");
                }
                logout(session);
            }
        } catch (Exception e)
        {
            /*
             * As this is just an demonstration of using BlueLine, exceptions are not handled here. 
             */
            logger.error("Catched Exception", e);
        } finally
        {
            // Invoke closeServer, if IDocumentServer is no longer needed.
            closeServer();
        }
    }

}
