import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prajpoot on 8/6/16.
 */
public class Test_VB {






    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {
        File file = new File("/home/prajpoot/Desktop/LM_Tests4_(VALID)/final-input-frames");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        BufferedReader bufr = new BufferedReader(new FileReader("/home/prajpoot/Desktop/LM_Tests4_(VALID)/final-input"));
        SentenceAnalysis s = new SentenceAnalysis("/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger");
        String sCurrentLine;
        while ((sCurrentLine = bufr.readLine()) != null) {
            test(sCurrentLine,bw,s);

        }
        bw.close();
    }



    public static Map<String,ArrayList<String>> LookUpMap(Map<String, ArrayList<String>> map, String word){

        Map<String, ArrayList<String>> Verbmap = new HashMap<>();
        //Check which classes verb belongs
        for(HashMap.Entry<String,ArrayList<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
           // System.out.println(entry.getValue()+"------------------------"+word.trim());
            if(entry.getValue().contains(word.trim()))
            {  //System.out.println(key);
            Verbmap.put(key,entry.getValue());
                }
            // ...
        }

        return Verbmap;

    }

    public static String GetPAST(String word, BufferedReader bf) throws IOException {
            String sCurrentLine;
        while ((sCurrentLine = bf.readLine()) != null) {
            String[] toks=sCurrentLine.split(",");
            if(toks[0].equals(word))
                return toks[1];
        }
        return word;
    }



    public static void test(String sentence, BufferedWriter bw,SentenceAnalysis s) throws IOException, SAXException, ParserConfigurationException {


        String verbc=sentence.split(" ")[0];
        VB_tools p = new VB_tools();
       // p.getClasses("rise");
        Map<String, ArrayList<String>> map = new HashMap<>();
        map = p.Create_map("/home/prajpoot/Downloads/new_vn/");


     //SentenceAnalysis s = new SentenceAnalysis("/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger");
        List<HasWord> te=s.GetTokens(sentence);
        List<TaggedWord> POS=s.GetPOS(te);
        int done=0;
    System.out.print(POS);
        for(int l=0;l<POS.size();l++){
            String[] POSword=(POS.get(l).toString()).split(" ")[0].split("/");
            //System.out.print(s.Lemmetize(POSword[0]));
            System.out.print(POSword[1]);
            //if(POSword[1].contains("VB"))

                if(s.Lemmetize(POS.get(0).toString().split("/")[0].trim()).contains(s.Lemmetize(verbc.split("/")[0].trim())) && done ==0)    //Variable
            //        if(POSword[0].contains("rose"))//Variable
            {
                //bw.write("$$$$$"+sentence+"@@@@@"+String.valueOf(trimVerbmap.size()));
                //bw.newLine();
               // System.out.println(s.Lemmetize(POSword[0])+" "+(POSword[1]));
                Map<String, ArrayList<String>> Verbmap = new HashMap<>();
                Map<String, ArrayList<String>> trimVerbmap = new HashMap<>();
                //Verbmap=LookUpMap(map,s.Lemmetize((POSword[0])));
                Verbmap=LookUpMap(map,(verbc.split("/")[0].trim()));
                trimVerbmap=p.trim_map(Verbmap);
                bw.write("$$$$$"+sentence+" @@@@@"+String.valueOf(trimVerbmap.size())+" *****"+POSword[0]);
                bw.newLine();
                //bw.write("@@@@@"+String.valueOf(trimVerbmap.size()));
                //bw.newLine();

                for(HashMap.Entry<String,ArrayList<String>> entry : trimVerbmap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println("#####"+key);
                    bw.write("#####"+key);
                    bw.newLine();
                    for(int i=0;i<entry.getValue().size();i++) {
                        //BufferedReader bufr = new BufferedReader(new FileReader("/home/prajpoot/WordNetExtraction/rise_data.csv"));

                       // bw.write(sentence.replace(POSword[0],past));;
                      //  bw.newLine();
                        bw.write(sentence.replace(POSword[0],entry.getValue().get(i)));;
                        bw.newLine();

                    }
                    // ...
                }

                done=1;
            }

        }
        if(done==0){
            bw.write("$$$$$"+sentence+" @@@@@FAIL"+s.Lemmetize(POS.get(0).toString().split("/")[0].trim())+"%"+verbc.split("/")[0]);
            bw.newLine();
        }

      //  bw.close();
        //bufr.close();

    }




}
