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
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.*;
import java.util.*;

/**
 * Created by prajpoot on 27/2/17.
 */
public class bootStrap {

    static HashSet<String> set = new LinkedHashSet<>();

    public static String getQuerycount(IndexSearcher searcher, String quer, QueryParser parser,BufferedWriter depWriter,int type) throws IOException, ParseException {



        ArrayList<String> queries = new ArrayList<>();
        if(!quer.contains("/"))
            queries.add(quer);
        else {
            String[] line = quer.replace("AND ", "").split(" ");
            ArrayList<String> NERs = new ArrayList<>();

            for (int j = 0; j < line.length; j++) {
                if (line[j].contains("/PERSON") || line[j].contains("/LOCATION") || line[j].contains("/ORGANIZATION"))
                    NERs.add(line[j]);
            }
            for (String ner : NERs) {
                String tempquer = "";
                String end = " AND ";
                for (int j = 0; j < line.length; j++) {
                    if (j == line.length - 1)
                        end = "";
                    if (line[j].equals(ner))
                        tempquer = tempquer + ner.split(":")[0] + ":" + ner.split("/")[1].toLowerCase() + end;
                    else
                        tempquer = tempquer + line[j].split("/")[0] + end;
                }
                if(!queries.contains(tempquer))
                    queries.add(tempquer);
            }
            String tempquer="";
            String end=" AND ";
            for (int j = 0; j < line.length; j++) {
                if (j == line.length - 1)
                    end = "";
                tempquer = tempquer + line[j].split("/")[0] + end;
            }
            if(!queries.contains(tempquer))
                queries.add(tempquer);
            tempquer="";
            end=" AND ";
            for (int j = 0; j < line.length; j++) {
                if (j == line.length - 1)
                    end = "";
                if(line[j].contains("/"))
                    tempquer = tempquer + line[j].split(":")[0] + ":" + line[j].split("/")[1].toLowerCase() + end;
                else
                    tempquer = tempquer + line[j] + end;
            }
            if(!queries.contains(tempquer))
                queries.add(tempquer);
        }
        for(String queryIn:queries) {
            Query query = parser.parse(queryIn);
            String count="";
            int hits= searcher.search(query,10).totalHits ;
//            if(hits > 5) {
//                hits =5;
//            }
            if(hits==0)
                continue;
            ScoreDoc[] t = (searcher.search(query, hits)).scoreDocs;
            //  System.out.println(t.length+" "+hits);
            for (int i = 0; i < hits; i++){
                count="";
                for(int j=0;j< searcher.doc(t[i].doc).getFields().size();j++) {
                    if(!searcher.doc(t[i].doc).getFields().get(j).name().contains("str") && !searcher.doc(t[i].doc).getFields().get(j).name().contains("count"))
                        count =count+" "+searcher.doc(t[i].doc).getFields().get(j).name()+":"+searcher.doc(t[i].doc).get(searcher.doc(t[i].doc).getFields().get(j).name()).split("/")[0].replace("+","").replace("-","").trim();
                }
                set.add(count.trim());
//            count= count+"\n";
                //count= count+1;
            }
        }

        return "";
    }




    public static void main(String args[]) throws IOException, ClassNotFoundException, ParseException {
        long startTime = System.currentTimeMillis();
        String datapath="";
        datapath="/home/prajpoot/vn_3.2.5_annot/seed_for_BS/";
        String TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";
        String PARSER_MODEL = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/parser/nndep/english_UD.gz";
        DependencyParser parser = DependencyParser.loadFromModelFile(PARSER_MODEL);
        MaxentTagger tagger = new MaxentTagger(TAGGER_MODEL);
        createNER(datapath);
        createDEPS(datapath,tagger,parser);                    //create the deps file
        Scanner stdin = new Scanner(new File(datapath+"/deps"));
        //Scanner stdin2 = new Scanner(new File(datapath+"/verbs"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(datapath+"/tags"));
        String line = "";
        LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
        String verblin="";
        Lemmatizer l = new Lemmatizer();
        while (stdin.hasNextLine()){
            line=stdin.nextLine();
            if(line.contains("@@@@@")){
                verblin=line.replace("@@@@@","");
                bw.write(line+"\n");
                continue;
            }
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
        System.out.println("Done: tokenization, NER, Dependency, Level1 Output");




        IndexSearcher searcher = null;
        QueryParser lparser = null;
        RAMDirectory idx=new RAMDirectory();
        searcher = new IndexSearcher(DirectoryReader.open(NIOFSDirectory.open(new File("/home/prajpoot/lucenedict/index-directory32").toPath())));
        lparser = new QueryParser("content", new WhitespaceAnalyzer());


        ////////////////////////////////////////////////////////////////////
        Scanner stdin3 = new Scanner(new File(datapath+"/final"));
        int iter =0;
        BufferedWriter depWriter = new BufferedWriter(new FileWriter(datapath+"/"+"boot_iter_"+iter));

        while (stdin3.hasNextLine()) {
            String linec=stdin3.nextLine();
            if(linec.contains("@@@@@"))
                continue;
            String[] line1 = linec.replace(": ",":").replace("  "," ").split(" ");

            String Verbname = line1[0].trim();
            String Query = "Verbname:"+Verbname ;
            for (int i =1; i<line1.length; i++) {
               if(!line1[i].contains("punct:")) {
                   String temp="";
                   temp=line1[i].split("-")[0];
                   Query = Query + " AND " + temp;

               }
            }
            getQuerycount(searcher,Query,lparser,depWriter,0);

        }

        for(String key:set) {
            depWriter.write(key+"\n");
        }
        depWriter.close();
        set.clear();
        iter++;
        ArrayList<String> depTags  = new ArrayList<>(Arrays.asList("nsubj", "pobj", "dobj", "pobj1", "pobj2", "iobj"));
        while(iter<1) {
             depWriter = new BufferedWriter(new FileWriter(datapath+"/"+"boot_iter_"+iter));
            HashSet<String> finalSet = new LinkedHashSet<>();
             stdin3=new Scanner(new File(datapath+"/"+"boot_iter_"+(iter-1)));
             while (stdin3.hasNextLine()) {
                 String[] temp = stdin3.nextLine().split(" ");

                 ArrayList<String> localTags = new ArrayList<>();
                 for (int i=0;i <temp.length; i++) {
                     if(depTags.contains(temp[i].split(":")[0].trim())) {
                         localTags.add(temp[i].split(":")[0].trim());
                     }
                 }

                 if(localTags.size()<2)
                     continue;
                     for (String key : localTags) {
                         String query = "";
                         for (int i=0;i <temp.length; i++) {
                             if(!temp[i].startsWith(key)) {
                                 query = query+" "+ temp[i];

                             }

                     }
                         query = query.trim().replace(" "," AND ");
                         getQuerycount(searcher,query,lparser,depWriter,1);
                         for(String key1:set) {
                             finalSet.add(key1);
                            // depWriter.write(key1+"\n");
                         }
                         set.clear();

                 }
                // String lineQ= stdin3.nextLine().replace(" "," AND ");
                // getQuerycount(searcher,lineQ,lparser);
             }

             stdin3.close();
            for(String key3 : finalSet) {
                depWriter.write(key3+"\n");
            }
            finalSet.clear();
             depWriter.close();

            iter++;
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000d);

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
            if(line.contains("@@@@@")) {
                Final.write(line+"\n");
                continue;
            }


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
                            //System.out.println(linetag+" sss "+tokens.get(i).toString().split("/")[0]);
                            linetag = linetag.replace(tokens.get(i).toString().split("/")[0], tokens.get(i).toString().split("/")[0]  + "/LOCATION");
                            loc=1;
                        }

                        //            System.out.println("LLLLLLLLLl "+"-" + String.valueOf(i+1) + "/LOCATION "+linetag+"\n\n");}
                    }
                }
                if (tokens.get(i).toString().contains("/ORGANIZATION") ) {
                    for (int k = 0; k < tagtokens.size(); k++) {
                        if (linetag.contains(tokens.get(i).toString().split("/")[0]) && org!=1){
                            // System.out.println(linetag+" sss "+tokens.get(i).toString().split("/")[0]);
                            linetag = linetag.replace(tokens.get(i).toString().split("/")[0], tokens.get(i).toString().split("/")[0] + "/ORGANIZATION");
                            org=1;
                        }
                        //System.out.println("MMMMMMMMl "+"-" + String.valueOf(i+1) + "/ORG "+linetag+"\n\n");}
                    }
                }
                if (tokens.get(i).toString().contains("/PERSON")/* && per!=1*/) {
                    for (int k = 0; k < tagtokens.size(); k++) {
                        if (linetag.contains(tokens.get(i).toString().split("/")[0]) /*&& per!=1*/){
                            //  System.out.println(linetag+" sss "+tokens.get(i).toString().split("/")[0]);
                            linetag =  linetag.replace(tokens.get(i).toString().split("/")[0], tokens.get(i).toString().split("/")[0] + "/PERSON");
                            //per=1;
                            break;
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
        // String serializedClassifier = "/home/asshriva/Downloads/pipelineFiles/ner/english.conll.4class.distsim.crf.ser.gz";
        String serializedClassifier = "/home/prajpoot/Downloads/stanford-english-corenlp-2016-01-10-models/edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
        Scanner stdin = new Scanner(new File(Filename+"/"+"input"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(Filename+"/"+"NER"));
        String line="";
        while (stdin.hasNextLine()){

            line=stdin.nextLine();
            if(line.contains("@@@@@")){
                bw.write(line+"\n");
                continue;
            }
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
            if(line.contains("@@@@@")){
                depWriter.write(line+"\n");
                continue;
            }

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
