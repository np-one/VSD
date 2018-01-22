import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.util.*;

/**
 * Created by prajpoot on 6/4/17.
 */
public class parseGoogle {

    public static void main(String args[]) throws IOException {


        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Scanner stdin = null;

        try {
            stdin = new Scanner(new File("/home/prajpoot/Desktop/treelstm_QA/trainWithAnsPOS.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/treelstm_QA/phraseList.txt"));

        String sCurrentLine="";
        String line="";

        while (stdin.hasNextLine()) {

            line=stdin.nextLine();
            String CandidateAnswers=line.split("\t")[0];
            sCurrentLine=line.split("\t")[1];
            String text = sCurrentLine;
            String ques = line.split("\t")[2];
            Annotation document=new Annotation();
            Set<Tree> answersphrases= new HashSet<>();
            Set<Tree> quesphrases = new HashSet<>();
            for(int i=0;i<2;i++){
                if(i==0)
                    document = new Annotation(text);
                if(i==1)
                    document = new Annotation(ques);

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
                        restoken = restoken + "|" + token.get(CoreAnnotations.TextAnnotation.class);
                        String word = token.get(CoreAnnotations.TextAnnotation.class);
                        // this is the POS tag of the token
//                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//                    // this is the NER label of the token
//                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    }
                    //System.out.println(restoken);

                    // this is the parse tree of the current sentence
                    Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                    if (i==0)
                    answersphrases=new HashSet<Tree>(GetNounPhrases(tree));;
                    if(i==1)
                        quesphrases=new HashSet<Tree>(GetNounPhrases(tree));
//                for(int t=0;t<answers.size();t++){
//                    String anstemp=GetNounPhrases(tree).get(t).flatten().getLeaves().toString().replace(",","").replace("]","").replace("[","");
//                    if(!anstemp.trim().equals(line.split("\t")[4].trim()))
//                    CandidateAnswers=CandidateAnswers+"\t"+anstemp;
//                }


                }
            }
            // create an empty Annotation just with the given text
            answersphrases.removeAll(quesphrases);
          for(Tree tree1:answersphrases) {
              String anstemp=tree1.flatten().getLeaves().toString().replace(",","").replace("]","").replace("[","");
              if(!anstemp.trim().equals(line.split("\t")[4].trim()))
                  CandidateAnswers=CandidateAnswers+"\t"+anstemp;
          }
            CandidateAnswers=CandidateAnswers+"\t####"+line.split("\t")[4];
            bw.write(CandidateAnswers+'\n');
        }
        bw.close();
        stdin.close();

    }

    public static List<Tree> GetNounPhrases(Tree parse)
    {

        List<Tree> phraseList=new ArrayList<Tree>();
        for (Tree subtree: parse)
        {

            if(subtree.label().value().equals("NP"))
            {

                phraseList.add(subtree);
                System.out.println(subtree);

            }
        }

        return phraseList;

    }
}
