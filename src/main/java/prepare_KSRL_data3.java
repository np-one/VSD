import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by prajpoot on 22/4/17.
 */
public class prepare_KSRL_data3 {

    public static void main(String args[]) throws IOException {
        String TAGGER_MODEL = "/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger";
        MaxentTagger tagger = new MaxentTagger(TAGGER_MODEL);
        Scanner stdin = new Scanner(new File("/home/prajpoot/Desktop/KsRL/wh_SRL/"+"input"));
        BufferedWriter depWriter = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/KsRL/wh_SRL/"+"pos"));
        Lemmatizer l = new Lemmatizer();
        int count=0;
        while (stdin.hasNextLine()) {
            count+=1;
            String line=stdin.nextLine();
            String sen=line.split("\t")[0];
            depWriter.write(getPOS(sen,tagger,l)+"\n");
        }
        depWriter.close();
    }


    public static String getPOS(String line, MaxentTagger tagger, Lemmatizer l){

        List<HasWord> tokens = new ArrayList();

        PTBTokenizer<Word> tokenizer = new PTBTokenizer(
                new StringReader(line), new WordTokenFactory(), "");
        for (Word label; tokenizer.hasNext(); ) {
            tokens.add(tokenizer.next());
        }


        List<TaggedWord> tagged = tagger.tagSentence(tokens);
        String[] check=String.valueOf(tagged).replace("[","").replace("]","").split(" ");
        String res="";

        for(int i=0;i<check.length;i++){
            System.out.println(check[i]);
            if(check[i].lastIndexOf(",")!=-1){
            String val=check[i].substring(0,check[i].lastIndexOf(","));
            System.out.println(val);
            res=res+" "+val.replace(val.split("/")[0]+"/",l.lemmatize(val.split("/")[0]).trim()+"/");
            }
        }

    return res.trim();
    }
}
