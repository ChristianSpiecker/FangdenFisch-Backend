package nlp;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.StringUtils;

import java.util.*;

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
        }
        return result;
    }
}