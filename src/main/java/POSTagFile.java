import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * Created by prajpoot on 2/2/17.
 */
public class POSTagFile {

    public static void main(String args[]) throws IOException {
        Scanner stdin = null;
        try {
            stdin = new Scanner(new File("/home/prajpoot/Desktop/treelstm_QA/dev/trainWithAns.txt"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SentenceAnalysis s = new SentenceAnalysis("/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger");

        String sCurrentLine[];
        String line="";
        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/prajpoot/Desktop/treelstm_QA/dev/trainWithAnsPOS.txt"));
        while (stdin.hasNextLine()){
            line=stdin.nextLine();
            if(line.split("\t").length<5){
                System.out.println(line.split("\t")[0]);
                continue;
            }
            sCurrentLine=line.split("\t");
            String fact=sCurrentLine[1];
            String ans=sCurrentLine[4];
            List<HasWord> tokens = s.GetTokens(fact.split("\t")[0]);
            List<TaggedWord> POS= s.GetPOS(tokens);
            String res="";
            int count=0;
            for(TaggedWord tag: POS){

            res=res+" "+tag.toString();
            }

            Boolean flag=false;
            String temp[]=ans.trim().split(" ");
            for(int y=0;y<temp.length;y++){
                if(res.toLowerCase().contains(temp[y]+"/vb"))
                               flag=true;
            }


            if(res!="" && !flag)
            {bw.write(line);
            bw.newLine();}
          //  System.out.println(sCurrentLine+"\t"+s.Lemmetize(res.trim().replace("\\n","")));
    }
        bw.close();
}

}
