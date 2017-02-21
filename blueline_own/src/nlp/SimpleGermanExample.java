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
	
	 
    public String analyseText(String sampleGermanText) {
    	String result = null;
        Annotation germanAnnotation = new Annotation(sampleGermanText);
       
        pipeline.annotate(germanAnnotation);
        
        for (CoreMap sentence : germanAnnotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree sentenceTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
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
                System.out.println(word + pos + ne);
            }
        }
        
        return result;
    }
    
    // Interpretiert auf Englisch. Nicht nutzen.
    public String analyseTextNew(String sampleGermanText) {
    	String result = null;
        Annotation germanAnnotation = new Annotation(sampleGermanText);
        
        pipeline.annotate(germanAnnotation);
        
        Document doc = new Document(sampleGermanText);
        for (Sentence sent : doc.sentences()) {
            
            result = sent.parse().toString();
        }
        
        return result;
    }
    
    
    
    
    public Result myanalyseText(String sampleGermanText) {
    	
    	Map<String, String> keywords = new HashMap<String, String>();
    	
    	String result = null;
        Annotation germanAnnotation = new Annotation(sampleGermanText);
       
        pipeline.annotate(germanAnnotation);
        
        for (CoreMap sentence : germanAnnotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree sentenceTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
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
                //System.out.println("w:"+word + " p:"+pos +" n:"+ ne);
                if(pos.equals("NE") || pos.equals("NN")){
 
                	keywords.put(word, pos);
                }
            }
        }
        Result res = new Result(null, null);
        for (Entry<String, String> entry : keywords.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value.equals("NE")){
            	res.setSearchword(key);
            }
            if (value.equals("NN")){
            	res.setsearchclass(key);
            }
        	
        }
        res.addTree(result);
        return res;
    }
}