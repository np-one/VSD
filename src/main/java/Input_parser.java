import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.TypedDependency;
import org.apache.lucene.queryparser.classic.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

/**
 * Created by prajpoot on 23/6/16.
 */
public class Input_parser {

    public static void main(String args[]) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException, InterruptedException, ParseException {
       String datapath="";
        datapath="/home/prajpoot/Desktop/NAI/";
        String TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";
        String PARSER_MODEL = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/parser/nndep/english_UD.gz";
        DependencyParser parser = DependencyParser.loadFromModelFile(PARSER_MODEL);
        MaxentTagger tagger = new MaxentTagger(TAGGER_MODEL);
        createNER(datapath);
        createDEPS(datapath,tagger,parser);                    //create the deps file
        Scanner stdin = new Scanner(new File(datapath+"/deps"));
        Scanner stdin2 = new Scanner(new File(datapath+"/verbs"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(datapath+"/tags"));
        String line = "";
        LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
        String verblin="";
        Lemmatizer l = new Lemmatizer();
        while (stdin.hasNextLine()){
            line=stdin.nextLine();
            verblin=stdin2.nextLine();
            verblin=l.lemmatize(verblin);
            verblin=verblin.trim()+" "+"#";

            String tokens[] = line.replace("[","").replace("]","").split("\\),");




            for(int i=0;i<tokens.length;i++){

                tokens[i]=tokens[i].replace("(",", ").replace(")","");

                String vals[]=tokens[i].split(", ");

                if(map.containsKey(l.lemmatize(vals[1].split("-")[0]).replace(" ","")))
                    map.put(l.lemmatize(vals[1].split("-")[0]).replace(" ",""),map.get(l.lemmatize(vals[1].split("-")[0]).replace(" ","")).toString()+", "+vals[0]+": "+vals[2]);
                else
                    map.put(l.lemmatize(vals[1].split("-")[0]).replace(" ",""),vals[0]+": "+vals[2]);
            }
            if(map.containsKey(l.lemmatize(verblin.split(" ")[0]).trim()) ){            //changge



                if(map.get(verblin.split(" ")[0]).contains("nmod: "))
                {
                    String temp[] =map.get(verblin.split(" ")[0]).split(",");
                    for(int kl=0;kl<temp.length;kl++){
                        if(temp[kl].contains("nmod")) {
                            if(map.get(temp[kl].split(": ")[1].split("-")[0])!=null)
                            if(map.get(temp[kl].split(": ")[1].split("-")[0]).contains("case: ")){
                                String temp2[]=map.get(temp[kl].split(": ")[1].split("-")[0]).split(",");
                                for(int klm=0;klm<temp2.length;klm++){
                                    if(temp2[klm].contains("case")) {
                                        if(map.containsKey(verblin.split(" ")[0]))
                                        map.put(verblin.split(" ")[0],map.get(verblin.split(" ")[0])+", case: "+temp2[klm].split(": ")[1]);
                                    }
                                }
                            }
                        }
                    }

                }
               // System.out.println(map);
            }

            int done=0;
            for(HashMap.Entry<String,String> entry : map.entrySet()) {              //lemma fix
                if (entry.getKey().startsWith(verblin.split(" ")[0])){
                    bw.write(verblin.split(" ")[0]+" "+(map.get(entry.getKey()).replace("nmod","pobj").replace("case","prep")));
                    done=1;
                    break;
                }
            }


                  if (done==0)
                bw.write(verblin.split(" ")[0]+" "+map.get(verblin.split(" ")[0]));

            bw.newLine();



            map.clear();
        }
        bw.close();

        stdin.close();
        createOwNER(datapath);
//        System.out.println("Done: tokenization, NER, Dependency, Level1 Output");
//
//        Filter_one filter_one = new Filter_one();
//        filter_one.runFolder(datapath);
//
//        Process p = Runtime.getRuntime().exec("python /home/prajpoot/WordNetExtraction/preprocess_VB1.py "+datapath);
//
//        p.waitFor();


////pp1
      //  Test_VB2 test_vb2 = new Test_VB2();
   //     test_vb2.run(datapath);
//        test_Lucene test_lucene = new test_Lucene();
//        test_lucene.run(datapath);
//
//        Process p2 = Runtime.getRuntime().exec("python /home/prajpoot/WordNetExtraction/score_new.py "+datapath+" 31");
//        p2.waitFor();

//pp2

//        test_LuceneP2 test_luceneP2 = new test_LuceneP2();
//        test_luceneP2.run(datapath);
//        Process p3 = Runtime.getRuntime().exec("python /home/prajpoot/WordNetExtraction/temp.py "+datapath);
//        p3.waitFor();
//
//        pipeline2 pipeline2 = new pipeline2();
//        pipeline2.run(datapath);
//        Process p4 = Runtime.getRuntime().exec("python /home/prajpoot/WordNetExtraction/sum.py "+datapath);
//        p4.waitFor();
    }


    public static void createOwNER(String Folder) throws IOException {

        Scanner NER = new Scanner(new File(Folder + "/" + "NER"));
        Scanner tag = new Scanner(new File(Folder + "/" + "tags"));

        BufferedWriter Final = new BufferedWriter(new FileWriter(Folder + "/" + "final"));


        String line = "";
        String linetag = "";
        while (NER.hasNextLine()) {
            line = NER.nextLine();
            linetag = tag.nextLine();
            String finalwrite="";
            HashMap<String,String> mapNER= new HashMap<>();

            PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                    new StringReader(line), new WordTokenFactory(), "");
            for (Word label; tokenizer.hasNext(); ) {
                String id=tokenizer.next().toString();
                if(id.contains("/PERSON")||id.contains("/ORGANIZATION")||id.contains("/LOCATION")){
                    mapNER.put(id.split("/")[0],id.split("/")[1]);
                }
            }

            List<HasWord> tagtokens = new ArrayList();

            PTBTokenizer<Word> tokenizertag = new PTBTokenizer(
                    new StringReader(linetag), new WordTokenFactory(), "");
            for (Word label; tokenizertag.hasNext(); ) {
                tagtokens.add(tokenizertag.next());
            }

             Iterator<HasWord> it1 = tagtokens.iterator();
            while(it1.hasNext()){
                String checktoken=it1.next().toString();
                if(checktoken.split("-").length==0){
                    continue;
                }
                if(mapNER.containsKey(checktoken.split("-")[0])){
                    finalwrite=finalwrite+" "+checktoken.split("-")[0]+"/"+mapNER.get(checktoken.split("-")[0])+"-"+checktoken.split("-")[1];
                }
                else{
                    finalwrite=finalwrite+ " "+checktoken;
                }
//                System.out.println();
            }

            Final.write(finalwrite.trim().replace(" :",":").replace(" ,",","));
            try {
                Final.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        NER.close();
        tag.close();
        Final.close();

    }


    public static void createNER(String Filename) throws IOException, ClassNotFoundException {
        Lemmatizer l =new Lemmatizer();
       // String serializedClassifier = "/home/asshriva/Downloads/pipelineFiles/ner/english.conll.4class.distsim.crf.ser.gz";
        String serializedClassifier = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
        Scanner stdin = new Scanner(new File(Filename+"/"+"input"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(Filename+"/"+"NER"));
        String line="";
        while (stdin.hasNextLine()){
            line=stdin.nextLine();
            bw.write(classifier.classifyToString(line));
            bw.newLine();
        }
        bw.close();
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


    public static void createDEPS(String Filename, MaxentTagger tagger, DependencyParser parser) throws IOException {

        Scanner stdin = new Scanner(new File(Filename+"/"+"input"));
        BufferedWriter depWriter = new BufferedWriter(new FileWriter(Filename+"/"+"deps"));
        while (stdin.hasNextLine()) {
            String line = stdin.nextLine();

            List<HasWord> tokens = new ArrayList();

            PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                    new StringReader(line), new WordTokenFactory(), "");
            for (Word label; tokenizer.hasNext(); ) {
                tokens.add(tokenizer.next());
            }


            List<TaggedWord> tagged = tagger.tagSentence(tokens);
            Collection<TypedDependency> tdl = parser.predict(tagged).typedDependencies();
            depWriter.write(tdl.toString());
            depWriter.newLine();

        }
        try {
            depWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stdin.close();

    }
}