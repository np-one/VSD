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

import java.io.*;
import java.util.*;

/**
 * Created by prajpoot on 21/4/17.
 */
public class prepare_KSRL_data {
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String datapath="";
        datapath="/home/prajpoot/Desktop/KsRL/wh_SRL/SVm";
        String TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";
        String PARSER_MODEL = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/parser/nndep/english_UD.gz";
        DependencyParser parser = DependencyParser.loadFromModelFile(PARSER_MODEL);
        MaxentTagger tagger = new MaxentTagger(TAGGER_MODEL);
        //createNER(datapath);
        createDEPS(datapath,tagger,parser);
        Scanner stdin = new Scanner(new File(datapath+"/deps"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(datapath+"/tags"));
        String line="";
        String verblin="";
        Lemmatizer l = new Lemmatizer();
        LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();

        while(stdin.hasNextLine()){

            String linenow=stdin.nextLine();
            String subline[]=linenow.split("\t");
            if(subline.length<2){
                bw.write(linenow+"\n");
                continue;}
            String srl="frame:"+subline[subline.length-1];
            for(int i=2;i<subline.length;i++){
                if(subline[i].contains("AM-") || subline[i].contains("A0:") ||subline[i].contains("A1:")|| subline[i].contains("A2:") || subline[i].contains("A3:") ||subline[i].contains("A4:") || subline[i].contains("A5:"))
                    srl=srl+" "+subline[i];
            }
            line=subline[0];
            verblin=subline[subline.length-1].split("\\.")[0];
            verblin=l.lemmatize(verblin).replace("frame :","");
            verblin=verblin.trim()+" "+"#";

            String tokens[] = line.replace("[","").replace("]","").split("\\),");




            for(int i=0;i<tokens.length;i++){

                tokens[i]=tokens[i].replace("(",", ").replace(")","");

                String vals[]=tokens[i].split(", ");

                if(map.containsKey(l.lemmatize(vals[1].split("-")[0]).replace(" ","")))
                    map.put(l.lemmatize(vals[1].split("-")[0]).replace(" ",""),map.get(l.lemmatize(vals[1].split("-")[0]).replace(" ","")).toString()+", "+vals[0]+": "+l.lemmatize(vals[2]));
                else
                    map.put(l.lemmatize(vals[1].split("-")[0]).replace(" ",""),vals[0]+": "+l.lemmatize(vals[2]));
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
                                                map.put(verblin.split(" ")[0],map.get(verblin.split(" ")[0])+", case: "+l.lemmatize(temp2[klm].split(": ")[1]));
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
                if (l.lemmatize(entry.getKey()).trim().equals(l.lemmatize(verblin.split(" ")[0]).trim())){
                    bw.write(verblin.split(" ")[0]+" "+(map.get(entry.getKey()).replace("nmod","pobj").replace("case","prep")).replace(": ",":").replace(", ","")+" ");
                    done=1;
                    break;
                }
            }


            if (done==0)
                bw.write(verblin.split(" ")[0]+" "+map.get(verblin.split(" ")[0])+" ");

            for(int ind=1;ind<subline.length;ind++) {
                bw.write(subline[ind]+" ");
            }
            bw.newLine();



            map.clear();
        }
        bw.close();
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
            String linenow=stdin.nextLine();
            String subline[] = linenow.split("\t");
            if(subline.length<2){
                depWriter.write(linenow+"\n");
                continue;}
            String line=subline[0];
            List<HasWord> tokens = new ArrayList();

            PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                    new StringReader(line), new WordTokenFactory(), "");
            for (Word label; tokenizer.hasNext(); ) {
                tokens.add(tokenizer.next());
            }


            List<TaggedWord> tagged = tagger.tagSentence(tokens);
            String srl=subline[1];
            for(int i=2;i<subline.length;i++){
//                if(subline[i].contains("AM-") || subline[i].contains("A0:") ||subline[i].contains("A1:")|| subline[i].contains("A2:") || subline[i].contains("A3:") ||subline[i].contains("A4:") || subline[i].contains("A5:") || subline[i].contains("frame:"))
                srl=srl+"\t"+subline[i];
            }
            Collection<TypedDependency> tdl = parser.predict(tagged).typedDependencies();
            depWriter.write(tdl.toString()+"\t"+srl);
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
