package nlp;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.util.TypesafeMap;

import java.util.*;
import java.util.Map.Entry;

public class SimpleGermanExample {
	
	private static SimpleGermanExample instance;
	private Properties germanProperties;
	private StanfordCoreNLP pipeline;
	
	
	public SimpleGermanExample(){
		this.germanProperties = StringUtils.argsToProperties(
                new String[]{"-props", "StanfordCoreNLP-german.properties"});
		
		this.pipeline = new StanfordCoreNLP(germanProperties);
        
	}
	// Singleton
	 public static SimpleGermanExample getInstance () {
		    if (SimpleGermanExample.instance == null) {
		    	SimpleGermanExample.instance = new SimpleGermanExample ();
		    }
		    return SimpleGermanExample.instance;
	 }
	
    
    /**
     * Erstellt den Baum zu dem Eingegebenen String
     * @param sampleGermanText Der zu analysierende Satz
     */
    public void myanalyseText(String sampleGermanText) {
    	
    	Tree sentenceTree = null;
    	String result = null;
        Annotation germanAnnotation = new Annotation(sampleGermanText);
       
        pipeline.annotate(germanAnnotation);
        
        for (CoreMap sentence : germanAnnotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            sentenceTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            result = sentenceTree.toString();
            
            pipeline.getConstituentTreePrinter().printTree(sentenceTree);
            
            // http://stanfordnlp.github.io/CoreNLP/api.html
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(NamedEntityTagAnnotation.class);
                
            }
        }
        
        handleTree(sentenceTree);
        
        Result.getInstance().addTree(result);
    }
    /**
     * Traversiert durch den Baum und fügt erhaltene Informationen in das Resultobjekt
     * @param tree Der Baum
     */
    private void handleTree(Tree tree){
    	traverse(tree);
    }

    /**
     * Überprüft Datum und Zahl und fügt sie zum Resultobjekt hinzu
     * @param card Teilbaum ab CARD
     */
    private void checkCARD(Tree card){
    	Tree children [] = card.children();
    	for(int i=0; i < children.length; i++){
    		//Datum
	    	if (children[i].nodeString().startsWith("CARD")){
	
				String date =children[i].firstChild().nodeString();
				if(date.contains(".")){
					Result.getInstance().addDate(date);
				}else if(date.contains("-")){
					Result.getInstance().setSearchword(date);
				}else{
					Result.getInstance().setSearchword(date);
				}
				

				// Überprüft ob ab oder bis zu dem Datum gesucht werden soll
	    	} else if (children[i].nodeString().equals("APPR")){
	    		String temp_preposition = children[i].firstChild().nodeString();
	    		System.out.println("preposition geaddet: " + temp_preposition);
	    		Result.getInstance().addTemp_Preposition(temp_preposition);
	    	}
    	}
    }
    /**
     * Überprüft Suchwörter und Descriptoren und fügt sie zum Resultobjekt hinzu
     * @param pp Teilbaum ab PP
     */
    private void checkPPinNP(Tree pp){
    	Tree children [] = pp.children();
    	for(int i=0; i < children.length; i++){
    		// Nomen
			if (children[i].nodeString().startsWith("NN")){
				// NP PP NN Deskriptor
				String descriptor =children[i].firstChild().nodeString();
				System.out.println("descriptor geaddet: " + descriptor);
				Result.getInstance().setdescriptor(descriptor);
				//Eigenname
			}else if (children[i].nodeString().startsWith("NE")){
				// NP PP NE
				String searchword = children[i].firstChild().nodeString();
				System.out.println("Suchwort geaddet: " + searchword);
				Result.getInstance().setSearchword(searchword);

				// Zusamengesetzter Eigenname bsp. Rheinwerk Group
			} else if (children[i].nodeString().startsWith("MPN")){
				// NP PP NE
				String erstesNE = children[i].getChild(0).toString().split(" ")[1];
				String zweitesNE = children[i].getChild(1).toString().split(" ")[1];
				if ( erstesNE.substring(0,erstesNE.length()-1).equals("Rheinberg")){
					erstesNE="Rheinwerk ";
				}
				String searchword = erstesNE.substring(0,erstesNE.length()-1).concat(" ").concat(zweitesNE.substring(0,zweitesNE.length()-1));		
				System.out.println("Suchwort geaddet: " + searchword);
				Result.getInstance().setSearchword(searchword);

				// Eigennnamenkombination - ungenutzt
			} else if (children[i].nodeString().startsWith("CNP")){
				// NP PP CNP
				Tree cnp []= children[i].children();
				for (int j=0; j < cnp.length; j++){
					if(cnp[j].nodeString().startsWith("NE")){
						System.out.println("Suchwort geaddet: " + cnp[j].firstChild().nodeString());
						String searchword = cnp[j].firstChild().nodeString();
						Result.getInstance().setSearchword(searchword);
					}
				}
				
				Result.getInstance().setSearchword(children[i].firstChild().nodeString());
				
			}
		}
    	
    }
    /**
     * Überprüft Suchklsase und fügt sie zum Resultobjekt hinzu
     * Ruft ggf. die PPinNP Überprüfung auf
     * @param np teilbaum ab NP
     */
    private void checkNP(Tree np){
    	Tree children [] = np.children();
    	for(int i=0; i < children.length; i++){
			if (children[i].nodeString().startsWith("NN")){
				// NP NN Suchklasse
				System.out.println("Suchklasse geaddet: " + children[i].firstChild().nodeString());
				Result.getInstance().setsearchclass(children[i].firstChild().nodeString());
			}else if (children[i].nodeString().startsWith("PP")){
				
				checkPPinNP(children[i]);
				
			}
		}
    	
    	
    	
    }

    /**
     * Überprüft PP's außerhalb von NP's
     * @param pp Teilbaum ab pp
     */
    private void checkPP(Tree pp){
    	Tree children [] = pp.children();
    	for(int i=0; i < children.length; i++){
    		// Eigenwort
			if (children[i].nodeString().startsWith("NE")){
				String searchword = children[i].firstChild().nodeString();
				System.out.println("Suchwort geaddet: " + searchword);
				Result.getInstance().setSearchword(searchword);
			//zusammengesetzes Eigenwort
			}else if (children[i].nodeString().startsWith("MPN")){
				String erstesNE = children[i].getChild(0).toString().split(" ")[1];
				String zweitesNE = children[i].getChild(1).toString().split(" ")[1];
				String searchword = erstesNE.substring(0,erstesNE.length()-1).concat(" ").concat(zweitesNE.substring(0,zweitesNE.length()-1));
				System.out.println("Suchwort geaddet: " + searchword);
				Result.getInstance().setSearchword(searchword);
				
			// ADJA ist eine ortsbezogene Angabe und bezieht sich immer auf ein darauffolgendes Nomen 
			//somit ist i+1 niemals außerhalb des Baumes | Beispiel: Frankfurter Zoo, Bremer Warenhaus
			}else if(children[i].nodeString().startsWith("ADJA")){

				if(children[i+1].nodeString().startsWith("NN")){
					String erstesNE = children[i].firstChild().nodeString();
					String zweitesNE = children[i+1].firstChild().nodeString();
					String searchword = erstesNE.substring(0,erstesNE.length()).concat(" ").concat(zweitesNE.substring(0,zweitesNE.length()));
					System.out.println("Suchwort geaddet: " + searchword);
					Result.getInstance().setSearchword(searchword);
					
				}
			}
		}
    	
    	
    	
    }
    /**
     * Traversiert durch den Baum
     * @param tree Baum
     */
    private void traverse(Tree tree){;
    	
    	if (tree.nodeString().startsWith("NP")){
    		checkNP(tree);
    	} else if(tree.nodeString().startsWith("PP")){
    		checkCARD(tree);
    		checkPP(tree);
    	}
    	Tree children [] = tree.children();
    	if(children != null){
    		for(int i=0; i < children.length; i++){
    			traverse(children[i]);
    		}
    	}
    }
}