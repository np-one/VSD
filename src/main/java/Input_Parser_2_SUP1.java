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
 * Created by prajpoot on 23/6/16.
 */


public class  Input_Parser_2_SUP1  {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";
        String PARSER_MODEL = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/parser/nndep/english_UD.gz";
        // DependencyParser parser = DependencyParser.loadFromModelFile(PARSER_MODEL);
        // MaxentTagger tagger = new MaxentTagger(TAGGER_MODEL);

        //createDEPS("/home/prajpoot/Desktop/test/LM_Tests2",tagger,parser);                    //create the deps file
        createNER("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)");
        Scanner stdin = new Scanner(new File("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests2_(TEST)/deps"));
        Scanner stdin2 = new Scanner(new File("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests2_(TEST)/verbs"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests2_(TEST)/tags2"));
        String line = "";
        LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
        String verblin="";

        while (stdin.hasNextLine()){
            line=stdin.nextLine();
            verblin=stdin2.nextLine();

            String tokens[] = line.replace("[","").replace("]","").split("\\),");



            for(int i=0;i<tokens.length;i++){

                tokens[i]=tokens[i].replace("(",", ").replace(")","");

                String vals[]=tokens[i].split(", ");
                Lemmatizer l = new Lemmatizer();
                //      System.out.println(l.lemmatize("mixed"));

                if(map.containsKey(l.lemmatize(vals[1].split("-")[0]).replace(" ","")))
                    map.put(l.lemmatize(vals[1].split("-")[0]).replace(" ",""),map.get(l.lemmatize(vals[1].split("-")[0]).replace(" ","")).toString()+", "+vals[0]+": "+vals[2]);
                else
                    map.put(l.lemmatize(vals[1].split("-")[0]).replace(" ",""),vals[0]+": "+vals[2]);
                //System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
            }

            //if(map.containsKey(verblin.split(" ")[0]))
            // System.out.println(line);
            // System.out.println(verblin.split(" ")[0]+" "+map);                 //get verbbb contents from map
            // if(map.get(verblin.split(" ")[0]).contains("nmod"))
//            if(map.containsKey(verblin.split(" ")[0])){
//
//
//
////                if(map.get(verblin.split(" ")[0]).contains("nmod: "))
////                {
////                    String temp[] =map.get(verblin.split(" ")[0]).split(",");
////                    for(int kl=0;kl<temp.length;kl++){
////                        if(temp[kl].contains("nmod")) {
////                            // System.out.println("sssss" + temp[kl] + "===" + map.get(temp[kl].split(": ")[1].split("-")[0]));
////                            if(map.get(temp[kl].split(": ")[1].split("-")[0])!=null)
////                                if(map.get(temp[kl].split(": ")[1].split("-")[0]).contains("case: ")){
////                                    String temp2[]=map.get(temp[kl].split(": ")[1].split("-")[0]).split(",");
////                                    for(int klm=0;klm<temp2.length;klm++){
////                                        if(temp2[klm].contains("case")) {
////                                            //System.out.println(temp2[klm].split(": ")[1]);
////                                            if(map.containsKey(verblin.split(" ")[0]))
////                                                map.put(verblin.split(" ")[0],map.get(verblin.split(" ")[0])+", case: "+temp2[klm].split(": ")[1]);
////                                        }
////                                    }
////                                }
////                            //  System.out.println("--->");
////                        }
////
//////
////
////                    }
////
////                }
//
//                // System.out.println(map);
//
//                //  }
//
//            }

            int done=0;
            for(HashMap.Entry<String,String> entry : map.entrySet()) {              //lemma fix
                if (entry.getKey().startsWith(verblin.split(" ")[0])){
                    bw.write(verblin.split(" ")[0]+" "+(map.get(entry.getKey())));
                    done=1;
                    break;
                }
            }

//            for(HashMap.Entry<String,String> entry : map.entrySet()) {
//                if(entry.getValue().contains("xcomp: "+verblin.split(" ")[0]))
//                    System.out.println(entry.getValue());
//                if(entry.getValue().contains("ccomp: "+verblin.split(" ")[0]))
//                    System.out.println(entry.getValue());
//
//            }
            //System.out.println(verblin.split(" ")[0]+" "+(map.containsKey(verblin.split(" ")[0])?map.get(verblin.split(" ")[0]).replace("nmod","pobj"):map.get(verblin.split(" ")[0])));        //nmod of verb is pobj
//            if(map.containsKey(verblin.split(" ")[0]))
//            bw.write(verblin.split(" ")[0]+" "+(map.get(verblin.split(" ")[0]).replace("nmod","pobj")));
//            else if(map.containsKey(verblin.split(" ")[0]+"ed"))
//                bw.write(verblin.split(" ")[0]+" "+(map.get(verblin.split(" ")[0]+"ed").replace("nmod","pobj")));
            if (done==0)
                bw.write(verblin.split(" ")[0]+" "+map.get(verblin.split(" ")[0]));
            //    System.out.println(verblin.split(" ")[0]+" "+map.get(verblin.split(" ")[0]));
            bw.newLine();

            //  System.out.println(verblin.split(" ")[0]+map);



            map.clear();
        }
        bw.close();

        stdin.close();
        //createOwNER("/home/prajpoot/Desktop/test/WSJ_TESTS/LM_Tests3_(TRAIN)");

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
            List<HasWord> tokens = new ArrayList();

            PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                    new StringReader(line), new WordTokenFactory(), "");
            for (Word label; tokenizer.hasNext(); ) {
                tokens.add(tokenizer.next());
            }

            List<HasWord> tagtokens = new ArrayList();

            PTBTokenizer<Word> tokenizertag = new PTBTokenizer(
                    new StringReader(linetag), new WordTokenFactory(), "");
            for (Word label; tokenizertag.hasNext(); ) {
                tagtokens.add(tokenizertag.next());
            }

            int org=0;
            int per=0;
            int loc=0;


            for (int i = 0; i < tokens.size(); i++) {
                //System.out.println(tokens.get(i).toString());
                if (tokens.get(i).toString().contains("/LOCATION") ) {
                    for (int k = 0; k < tagtokens.size(); k++) {

                        if (linetag.contains(tokens.get(i).toString().split("/")[0]) && loc!=1){
                            System.out.println(linetag+" sss "+tokens.get(i).toString().split("/")[0]);
                            linetag = linetag.replace(tokens.get(i).toString().split("/")[0], tokens.get(i).toString().split("/")[0]  + "/LOCATION");
                            loc=1;
                        }

                        //            System.out.println("LLLLLLLLLl "+"-" + String.valueOf(i+1) + "/LOCATION "+linetag+"\n\n");}
                    }
                }
                if (tokens.get(i).toString().contains("/ORGANIZATION") ) {
                    for (int k = 0; k < tagtokens.size(); k++) {
                        if (linetag.contains(tokens.get(i).toString().split("/")[0]) && org!=1){
                            System.out.println(linetag+" sss "+tokens.get(i).toString().split("/")[0]);
                            linetag = linetag.replace(tokens.get(i).toString().split("/")[0], tokens.get(i).toString().split("/")[0] + "/ORGANIZATION");
                            org=1;
                        }
                        //System.out.println("MMMMMMMMl "+"-" + String.valueOf(i+1) + "/ORG "+linetag+"\n\n");}
                    }
                }
                if (tokens.get(i).toString().contains("/PERSON") && per!=1) {
                    for (int k = 0; k < tagtokens.size(); k++) {
                        if (linetag.contains(tokens.get(i).toString().split("/")[0]) && per!=1){
                            System.out.println(linetag+" sss "+tokens.get(i).toString().split("/")[0]);
                            linetag =  linetag.replace(tokens.get(i).toString().split("/")[0], tokens.get(i).toString().split("/")[0] + "/PERSON");
                            per=1;
                        }
                        //      System.out.println("NNNNNNNNNn "+"-" + String.valueOf(i+1) + "/PER "+linetag+"\n\n");}
                    }
                }

            }


            Final.write(linetag);
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
        String serializedClassifier = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
        Scanner stdin = new Scanner(new File(Filename+"/"+"toks"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(Filename+"/"+"NER"));
        String line="";
        while (stdin.hasNextLine()){
            line=stdin.nextLine();
            bw.write(classifier.classifyToString(line));
            bw.newLine();
        }
        bw.close();
    }
    public static void createDEPS(String Filename, MaxentTagger tagger, DependencyParser parser) throws IOException {

        Scanner stdin = new Scanner(new File(Filename+"/"+"Input"));
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

