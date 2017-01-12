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

    public static void main(String[] args) {
        String sampleGermanText = "...";
        Annotation germanAnnotation = new Annotation(sampleGermanText);
        Properties germanProperties = StringUtils.argsToProperties(
                new String[]{"-props", "StanfordCoreNLP-german.properties"});
        StanfordCoreNLP pipeline = new StanfordCoreNLP(germanProperties);
        pipeline.annotate(germanAnnotation);
        for (CoreMap sentence : germanAnnotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree sentenceTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            System.out.println(sentenceTree);
        }
    }
}