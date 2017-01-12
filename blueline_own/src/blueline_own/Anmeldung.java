package blueline_own;

import java.io.IOException;
import java.util.Arrays;

import com.ser.blueline.BlueLineException;
import com.ser.blueline.IDocumentServer;
import com.ser.blueline.IProperties;
import com.ser.blueline.IRole;
import com.ser.blueline.ISerClassFactory;
import com.ser.blueline.IServerInfo;
import com.ser.blueline.ISession;
import com.ser.blueline.ISystem;
import com.ser.blueline.ITicket;
import com.ser.blueline.IUser;
import com.ser.sedna.client.bluelineimpl.SEDNABluelineAdapterFactory;

public class Anmeldung {
	
	/* Diese beiden Variablen werden im Laufe des Login-/Logout-Prozesses immer
	 * wieder gebraucht und werden deshalb als Instanzvariablen gespeichert. */
	private ISerClassFactory factory;
	private IDocumentServer server;

	
	/**
	 * Framework initialisieren
	 */
	public void initFramework(){
		System.out.println();
		System.out.println("> Initialisiere das Framework...");
		factory = SEDNABluelineAdapterFactory.getInstance();
		System.out.println("> Initialisierung erfolgreich!");
		System.out.println("> Typ und Version der Blueline-Implementation: " + factory.getVersion());
	}
	
	/**
	 * Verbindung mit Dokumentenserver herstellen
	 * @param propertiesFile Konfigurationsdatei für Dokumentserver
	 * @throws IOException
	 * @throws BlueLineException
	 */
	public void initDocumentServer(String configFile) throws IOException, BlueLineException{
		System.out.println();
		System.out.println("> Parse Konfigurationsdatei...");
		IProperties props = factory.getPropertiesInstance(configFile);
		props.setProperty("Global", "ArchivServerName", "172.26.90.201");
        props.setProperty("Global", "SeratioServerName", "172.26.90.201");
        props.setProperty("Global", "ArchivPort", "8080");
        props.setProperty("Global", "SeratioPort", "8080");
        props.setProperty("Global", "TmpDir", "C:/temp");
		System.out.println();
		System.out.println("> Verbinde mit Server...");
		server = factory.getDocumentServerInstance(props);
		//server = factory.getDocumentServerInstance();
		System.out.println("> Verbindung erfolgreich...");
		
		System.out.println();
		System.out.println("> Rufe Informationen über den Server ab...");
		IServerInfo serverInfo = server.getServerInfo();
		System.out.println("> Server-Version: " + serverInfo.getServerVersion());
		System.out.println("> Server-Release: " + serverInfo.getRelease());
		System.out.println("> Server-Charset: " + serverInfo.getServerCharSet());
		System.out.println("> Server-UID: " + serverInfo.getUID());
		System.out.println("> Server-Produkt-ID: " + serverInfo.getProductID());
		System.out.println("> Kundenname: " + serverInfo.getCustomerName());
	}
	
	/**
	 * Benutzeranmeldung am Dokumentenserver 
	 * @param userName Benutzername
	 * @param password Passwort des Benutzers
	 * @param system Systemname (Mandant)
	 * @return ISessionobjekt
	 * @throws BlueLineException
	 */
	public ISession login(String userName, String password, String systemName)
	  throws BlueLineException{

		System.out.println();
		System.out.println("> Rufe das System ab...");
		ISystem system = server.getSystem(systemName);
		System.out.println("> Systemname: " + system.getName());
		
		System.out.println();
		System.out.println("> Beginne mit Login...");
		ITicket ticket = server.login(system, userName, password.toCharArray());
		
		if (!ticket.isValid()) {
			System.out.println("> Login fehlgeschlagen!");
			switch (ticket.getState()) {
			case ITicket.STATE_MAXIMUM_NUMBER_OF_USERS_REACHED:
				System.out.println("> Der Server ist schon voll!");
				break;
			case ITicket.STATE_UNKNOWN_USER_OR_PW:
				System.out.println("> Unbekannter Benutzer oder falsches Passwort!");
				break;
			default:
				break;
			}
			return null;
		}

		System.out.println("> Login erfolgreich!");
		
		
		System.out.println();
		System.out.println("> Erzeuge eine Benutzersession...");
		ISession session = server.createSession(ticket);
		System.out.println("> Darf das Passwort geändert werden: " + session.canChangePassword());
		System.out.println("> Mögliche Rollen: " + Arrays.toString(session.getRoles()));
		
		System.out.println();
		IRole role = session.getCurrentRole();
		System.out.println("> Rollen-Name: " + role.getName());
		System.out.println("> Rollen-Beschreibung: " + role.getDescription());
		System.out.println("> Rollen-ID: " + role.getId());
		
		System.out.println();
		IUser user = session.getUser();
		System.out.println("> Benutzer-Vorname: " + user.getFirstName());
		System.out.println("> Benutzer-Nachname: " + user.getLastName());
		System.out.println("> Benutzer-Beschreibung: " + user.getDescription());
		System.out.println("> Benutzer-Email-Adresse: " + user.getEMailAddress());
		System.out.println("> Benutzer-Login-Name: " + user.getLogin());
		System.out.println("> Benutzer-ID: " + user.getId());
		System.out.println("> Benutzer-Access-Identifier: " + user.getAccessIdentifier().getName());
		System.out.println("> Zuletzt eingeloggt am: " + user.getLastLoginDate());
		
		return session;
	}
	
	
	//================================================================
	// FERTIGE FUNKTIONEN
	//================================================================
	
	/**
	 * Abmelden des Benutzers
	 * @param session Session des Benutzers
	 * @throws BlueLineException
	 */
	public void logout(ISession session) throws BlueLineException{
		if(session != null && server!=null) {
			System.out.println("\n> Benutzer wird abgemeldet...");
			server.logout(session);
		}
	}
	
	/**
	 * Verbindung vom Dokumentenserver trennen
	 * @throws BlueLineException
	 */
	public void close() throws BlueLineException{
		System.out.println("\n> verbindung zum Server wird beendet...");
		if(server!=null)
			server.close();
	}
	
	public IDocumentServer get_server(){
		return server;
	}
	
	public ISerClassFactory get_factory(){
		return factory;
	}
	
}
