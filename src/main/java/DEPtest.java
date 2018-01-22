import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.TypedDependency;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by prajpoot on 29/9/16.
 */
public class DEPtest {

    public static void main(String args[]){

        String TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";
        String PARSER_MODEL = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/parser/nndep/english_UD.gz";
        DependencyParser parser = DependencyParser.loadFromModelFile(PARSER_MODEL);
        MaxentTagger tagger = new MaxentTagger(TAGGER_MODEL);

        String line = "Hello. I finished setting up my quote online. I have a letter with a personal id that allows for no down payment. How do I apply that to my online quote?";
        List<HasWord> tokens = new ArrayList();

        PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                new StringReader(line), new WordTokenFactory(), "");
        for (Word label; tokenizer.hasNext(); ) {
            tokens.add(tokenizer.next());
        }


        List<TaggedWord> tagged = tagger.tagSentence(tokens);
        Collection<TypedDependency> tdl = parser.predict(tagged).typedDependencies();

        System.out.println(tdl.toString());
    }
}
