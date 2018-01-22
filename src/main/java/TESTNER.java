import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prajpoot on 21/6/16.
 */
public class TESTNER {
    public static void main(String args[]) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
        VB_tools p = new VB_tools();
        // p.getClasses("rise");
        Map<String, ArrayList<String>> map = new HashMap<>();
        map = p.Create_map("/home/prajpoot/Downloads/new_vn/");


        ArrayList<String> verbs = new ArrayList<>();
        for (String key: map.keySet()) {
            for (String val: map.get(key)) {
                if (verbs.contains(val))
                        continue;
                else
                    verbs.add(val);
            }
        }
        System.out.println(verbs);

        /*String TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";
        String PARSER_MODEL = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/parser/nndep/english_UD.gz";
       // DependencyParser parser = DependencyParser.loadFromModelFile(PARSER_MODEL);
        MaxentTagger tagger = new MaxentTagger(TAGGER_MODEL);
        String datapath="/home/prajpoot/Desktop/LM_Tests4_(VALID)/1";
        Scanner stdin = new Scanner(new File(datapath+"/verbs"));
        Scanner stdin2 = new Scanner(new File(datapath+"/input"));
        Scanner stdin3 = new Scanner(new File(datapath+"/doc_word"));
        BufferedWriter bwverb = new BufferedWriter(new FileWriter(datapath+"/verbs_1"));
        BufferedWriter bwinput = new BufferedWriter(new FileWriter(datapath+"/input_1"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(datapath+"/test"));

        String vline = "";
       // LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
        String lin="";
        String lin2="";
        Lemmatizer l = new Lemmatizer();
        while (stdin.hasNextLine()) {
            vline = stdin.nextLine();
            lin=stdin2.nextLine();
            lin2=stdin3.nextLine();
            if (getPOS(vline,tagger ).startsWith("VB")){
                bwverb.write(vline+"\n");
                bwinput.write(lin+"\n");
                bw.write(lin2+"\n");
            }
        }
        bwinput.close();
        bwverb.close();
        stdin.close();
        stdin2.close();
        bw.close();*/

    }


    public static String getPOS(String line, MaxentTagger tagger){

        List<HasWord> tokens = new ArrayList();

        PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                new StringReader(line), new WordTokenFactory(), "");
        for (Word label; tokenizer.hasNext(); ) {
            tokens.add(tokenizer.next());
        }


        List<TaggedWord> tagged = tagger.tagSentence(tokens);
        return String.valueOf(tagged.get(0).tag());

    }

//    String serializedClassifier = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
//    AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
//
//    String[] example = {"Good afternoon Rajat Raina, how are you today?",
//            "I go to school at Stanford University, which is located in California." };
//    for (String str : example) {
//        System.out.println(classifier.classifyToString(str));
//    }
//    }
}
