/**
 * Created by prajpoot on 30/5/16.
 */

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Lemmatizer {

    protected StanfordCoreNLP pipeline;
public Lemmatizer() {

        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");


        this.pipeline = new StanfordCoreNLP(props);
        }

    public String lemmatize(String documentText)
        {
        List<String> lemmas = new LinkedList<String>();


        Annotation document = new Annotation(documentText);


        this.pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
        // Iterate over all tokens in a sentence
         for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
        // Retrieve and add the lemma for each word into the list of lemmas
                 lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
             }
        }
            String answer="";
        for (String words: lemmas){
            answer=answer+" "+words;
        }
           // System.out.print(answer);
        return answer;
        }


}
