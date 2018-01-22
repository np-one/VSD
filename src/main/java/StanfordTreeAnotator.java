import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.TreeBinarizer;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;


import java.io.*;
import java.util.*;


/**
 * Created by prajpoot on 29/5/16.
 */
public class StanfordTreeAnotator {

    static boolean val= false;
    static BufferedWriter dataset;
    public static void main(String[] args) throws IOException {
        dataset = new BufferedWriter(new FileWriter(new File("/home/prajpoot/Desktop/datasetSentences.txt")));

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        BufferedReader bufr = new BufferedReader(new FileReader("/home/prajpoot/Desktop/metlife_test"));
        BufferedWriter dict = new BufferedWriter(new FileWriter(new File("/home/prajpoot/Desktop/dictionary.txt")));
        BufferedWriter sent_labels = new BufferedWriter(new FileWriter(new File("/home/prajpoot/Desktop/sentiment_labels.txt")));
        BufferedWriter sTree = new BufferedWriter(new FileWriter(new File("/home/prajpoot/Desktop/STree.txt")));
        BufferedWriter sosTree = new BufferedWriter(new FileWriter(new File("/home/prajpoot/Desktop/SOSTree.txt")));
        String sCurrentLine;

        dataset.write("sentence_index\tsentence");
        dataset.newLine();
        while ((sCurrentLine = bufr.readLine()) != null) {
            String text = sCurrentLine.split("\t")[1];
            // create an empty Annotation just with the given text
            Annotation document = new Annotation(text);

            // run all Annotators on this text
            pipeline.annotate(document);


        /* a CoreMap is essentially a Map that uses class objects as keys and has values with custom types */
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

            for (CoreMap sentence : sentences) {
                String restoken = "";
                // traversing the words in the current sentence
                // a CoreLabel is a CoreMap with additional token-specific methods
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    // this is the text of the token
                    restoken  = restoken+"|"+token.get(CoreAnnotations.TextAnnotation.class);
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    // this is the POS tag of the token
//                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//                    // this is the NER label of the token
//                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                }
                //System.out.println(restoken);
                sosTree.write(restoken);
                sosTree.newLine();

                // this is the parse tree of the current sentence
                Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//                tree.
                HeadFinder h = new SemanticHeadFinder();
                TreebankLanguagePack tlp = new PennTreebankLanguagePack();
                TreeBinarizer t = new TreeBinarizer(h,tlp,true,false,1,false,false,2.0,true,true,false);
                tree = t.transformTree(tree);


//            System.out.println(Sentence.listToString(tree.yield()));
                val=false;
//                List<Tree> list_leaves = tree.getLeaves();
//               // System.out.println(tree.flatten());
//               /* for (Tree t : list_leaves){
//                    t.+-
//                }*/
                nodecount = 0;
                dfs(tree,"ROOT",Integer.parseInt(sCurrentLine.split("\t")[0]));

                for (String node: parentmap.keySet()){
                    if(parentmapTemp.containsKey(node) && parentmapTemp.containsKey(parentmap.get(node))){
//                    System.out.println("Parent of "+ parentmapTemp.get(node)+": "+parentmapTemp.get(parentmap.get(node)));
                    finalmap.put(Integer.parseInt(parentmapTemp.get(node).toString()),Integer.parseInt(parentmapTemp.get(parentmap.get(node)).toString()));
                    }
                    else{
                        //System.out.println("Parent of "+parentmapTemp.get(node)+": 0");
                        finalmap.put(Integer.parseInt(parentmapTemp.get(node).toString()),Integer.parseInt("0"));
                    }


                }

                String resMap = "";
                SortedSet<Integer> keys = new TreeSet<Integer>(finalmap.keySet());
                for (Integer key : keys) {
                    Integer value = finalmap.get(key);

                    resMap = resMap +"|"+ value;

                    // do something
                }
              //  System.out.println(resMap);

                sTree.write(resMap);
                sTree.newLine();
               // System.out.println(parentmapTemp);
               //System.out.println("##########"+parentmap);
                parentmapTemp.clear();
                parentmap.clear();
                finalmap.clear();

        }
            // this is the Stanford dependency graph of the current sentence
           // SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
        }
        //System.out.println(result);
        sent_labels.write("phrase ids|sentiment values");
        sent_labels.newLine();

        int line=0;
        for(String keys:result.keySet()){
            //System.out.println(keys+"\t"+result.get(keys));
            dict.write(keys+"|"+line);
            dict.newLine();
            sent_labels.write(line+"|"+result.get(keys));
            sent_labels.newLine();
            line+=1;
        }
        sent_labels.close();
        dict.close();
        dataset.close();
        sTree.close();
        sosTree.close();
    }
    static HashMap<String,String> result = new HashMap<>();
    static int sentenceCount = 1;
    static int nodecount = 0;
    static HashMap<String,Integer> parentmapTemp  = new HashMap<>();
    static HashMap<String,String> parentmap  = new HashMap<>();
    static HashMap<Integer,Integer> finalmap = new HashMap<>();

    public static void dfs(Tree node, String parent, int score) throws IOException {

        if (node == null || node.isLeaf()) {
            return;
        }
//        System.out.println(node.parent());
        //if node is a NP - Get the terminal nodes to get the words in the NP

            List<Tree> leaves = node.getLeaves();
            String res = "";
            for(Tree leaf : leaves) {
                //System.out.print(leaf.toString()+" ");
                res = res +" "+ leaf.toString();
        }
        if(!result.containsKey(res)) {
            //if(val==false)
//            parentmapTemp.put(node.toString(),nodecount);
//            //TreeGraphNode sentence = new TreeGraphNode(node);
//            parentmap.put(node.toString(),parent.toString());

            result.put(res, String.valueOf(score));
        }
        parentmapTemp.put(node.toString(),nodecount);
        //TreeGraphNode sentence = new TreeGraphNode(node);
        parentmap.put(node.toString(),parent.toString());
        nodecount+=1;
        if(val==false) {
            dataset.write(sentenceCount+"\t"+res);
            dataset.newLine();
            sentenceCount+=1;
        }

        System.out.print(res+"\t"+val+"\n");
        for(Tree child : node.children()) {
            val=true;
            dfs(child, node.toString(), 0);
        }

    }
}
