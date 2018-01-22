import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajpoot on 8/6/16.
 */
public class SentenceAnalysis {
    public static String TAGGER_MODEL;
    public static MaxentTagger tagger;
    public static  Lemmatizer l;
    public SentenceAnalysis(String POSTagger) {

            TAGGER_MODEL = POSTagger;
         tagger = new MaxentTagger(TAGGER_MODEL);
        l = new Lemmatizer();
       //  TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";
    }

    public List<HasWord> GetTokens(String sentence){
        List<HasWord> tokens = new ArrayList();
        for (String word : sentence.split(" ")) {
            tokens.add(new Word(word));
        }
        return tokens;
    }
    public List<TaggedWord> GetPOS(List<HasWord> tokens ){

        return tagger.tagSentence(tokens);

    }
    public String Lemmetize(String sentence){
        //Lemmatizer l = new Lemmatizer();
        return l.lemmatize(sentence);
    }
}
