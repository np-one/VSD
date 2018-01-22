/**
 * Created by prajpoot on 6/7/16.
 */

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test_VB2 {






    public static void run(String datapath) throws ParserConfigurationException, SAXException, IOException {
        File file = new File(datapath+"/final-input-frames");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        BufferedReader bufr = new BufferedReader(new FileReader(datapath+"/final-input1"));
        SentenceAnalysis s = new SentenceAnalysis("/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger");
        String sCurrentLine;
        get_WN_words gwn= new get_WN_words();
        gwn.init2();
        Dictionary dict;
        URL url;
        String path = "/home/prajpoot/Downloads/WordNet-3.0"+File.separator + "dict";
        url = new URL("file", null, path);
        dict = new Dictionary(url);
        dict.open();
        while ((sCurrentLine = bufr.readLine()) != null) {
            test(sCurrentLine,bw,s,gwn,dict);

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



    public static void test(String sentence, BufferedWriter bw,SentenceAnalysis s,get_WN_words gwn,Dictionary dict) throws IOException, SAXException, ParserConfigurationException {

//        Dictionary dict;
//        URL url;
//        String path = "/home/prajpoot/Downloads/WordNet-3.0"+File.separator + "dict";
//        url = new URL("file", null, path);
//        dict = new Dictionary(url);
//        dict.open();
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
        for(int l=0;l<1;l++){
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
                Verbmap=LookUpMap(map,s.Lemmetize((POSword[0])));
                //Verbmap=LookUpMap(map,(verbc.split("/")[0].trim()));                  OLD
                trimVerbmap=p.trim_map(Verbmap);
               // List<IWordID> map2= getIndexedWordNEW((verbc.split("/")[0].trim()),getPosType("VB"));
//                bw.write("$$$$$"+sentence+" @@@@@"+String.valueOf(trimVerbmap.size())+" *****"+POSword[0]);
                bw.write("$$$$$"+sentence+" @@@@@"+String.valueOf(Verbmap.size())+" *****"+POSword[0]);
                bw.newLine();
                //bw.write("@@@@@"+String.valueOf(trimVerbmap.size()));
                //bw.newLine();

////////////////////////////////////test///////////////////////////////////////////

                //////////////////////////////////////////////


                  for(HashMap.Entry<String,ArrayList<String>> entry : trimVerbmap.entrySet()) {
                      List<String> res = new ArrayList<>();
           //     for(int kk=0;kk<Verbmap.size();kk++) {
//                    String key = entry.getKey();
//                    Object value = entry.getValue();
                    //String key =

                    System.out.println("#####"+entry.getKey());
                    bw.write("#####"+entry.getKey());
                    bw.newLine();
                       res=gwn.getwN2(POSword[0].toString(),entry.getKey());                           //  OLD
                        if (res.size()==0)
                            continue;
                      //res=gwn.getwN3(POSword[0].toString(),map2.get(kk));
                    for(int i=0;i<res.size();i++) {
//                        if(LookUpMap(map,res.get(i)).size()!=1)
//                            continue;
                        //BufferedReader bufr = new BufferedReader(new FileReader("/home/prajpoot/WordNetExtraction/rise_data.csv"));

                        // bw.write(sentence.replace(POSword[0],past));;
                        //  bw.newLine();
                        bw.write(sentence.replace(POSword[0],res.get(i)));
                        bw.newLine();

                    }
                    // ...
              //  }

                done=1;
            }
            }

        }
//        if(done==0){
//            bw.write("$$$$$"+sentence+" @@@@@FAIL"+s.Lemmetize(POS.get(0).toString().split("/")[0].trim())+"%"+verbc.split("/")[0]);
//            bw.newLine();
//        }

        //  bw.close();
        //bufr.close();

    }

    public static void testWSD(String sentence, BufferedWriter bw,SentenceAnalysis s,get_WN_words gwn,Dictionary dict) throws IOException, SAXException, ParserConfigurationException {


        String verbc=sentence.split(" ")[0];



        //SentenceAnalysis s = new SentenceAnalysis("/home/prajpoot/Downloads/stanford-postagger-2015-01-30/models/english-left3words-distsim.tagger");
        List<HasWord> te=s.GetTokens(sentence);
        List<TaggedWord> POS=s.GetPOS(te);
        int done=0;
//        System.out.print(POS);
        for(int l=0;l<1;l++){
            String[] POSword=(POS.get(l).toString()).split(" ")[0].split("/");
            //System.out.print(s.Lemmetize(POSword[0]));
//            System.out.println(POSword[1]);
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
                //Verbmap=LookUpMap(map,(verbc.split("/")[0].trim()));                  OLD
                //trimVerbmap=
                System.out.println((verbc.split("/")[0].trim()));
                 List<IWordID> map2= getIndexedWordNEW((verbc.split("/")[0].trim()),getPosType("VB"));
//                bw.write("$$$$$"+sentence+" @@@@@"+String.valueOf(trimVerbmap.size())+" *****"+POSword[0]);
                bw.write("$$$$$"+sentence+" @@@@@"+String.valueOf(map2.size())+" *****"+POSword[0]);
                bw.newLine();
                //bw.write("@@@@@"+String.valueOf(trimVerbmap.size()));
                //bw.newLine();

////////////////////////////////////test///////////////////////////////////////////

                //////////////////////////////////////////////


//                for(HashMap.Entry<String,ArrayList<String>> entry : trimVerbmap.entrySet()) {
                    List<String> res = new ArrayList<>();
                         for(int kk=0;kk<map2.size();kk++) {
//                    String key = entry.getKey();
//                    Object value = entry.getValue();
                    //String key = Verbmap.get(kk).getSynsetID().toString();
//                    System.out.println("#####"+entry.getKey());
                    bw.write("#####class-"+String.valueOf(kk+1)+"\n");
//                    bw.newLine();
//                    res=gwn.getwN2(POSword[0].toString(),entry.getKey());                           //  OLD
//                    if (res.size()==0)
//                        continue;
                    res=gwn.getwN3(POSword[0].toString(),map2.get(kk));
                    for(int i=0;i<res.size();i++) {
                        //BufferedReader bufr = new BufferedReader(new FileReader("/home/prajpoot/WordNetExtraction/rise_data.csv"));

                        // bw.write(sentence.replace(POSword[0],past));;
                        //  bw.newLine();
                        bw.write(sentence.replace(POSword[0],res.get(i)));
                        bw.newLine();

                    }
                    // ...
                    //  }

                    done=1;
                }
            }

        }
//        if(done==0){
//            bw.write("$$$$$"+sentence+" @@@@@FAIL"+s.Lemmetize(POS.get(0).toString().split("/")[0].trim())+"%"+verbc.split("/")[0]);
//            bw.newLine();
//        }

        //  bw.close();
        //bufr.close();

    }


    public static List<IWordID> getIndexedWordNEW(String word, POS pos) throws IOException {
        IIndexWord idxWord;
        String wordNetDictPath= "/home/prajpoot/Downloads/WordNet-3.0";

        String path = wordNetDictPath + File.separator + "dict";
        ArrayList<String> words = null;
        //  try {
        URL url = new URL("file", null, path);
        // construct the dictionary object and open it
        Dictionary dict = new Dictionary(url);
        dict.open();

        try {
            idxWord = dict.getIndexWord(word, pos);
            dict.close();
        } catch (NullPointerException e) {
            return null;
        }


        return idxWord.getWordIDs();

        // dict.close();
    }




    public static POS getPosType( String pos) {
        switch (pos) {
            case "NNS":
            case "NNP":
            case "NN":
            case "NNPS":
                return POS.NOUN;
            case "JJ":
            case "JJR":
            case "JJS":
                return POS.ADJECTIVE;
            case "RB":
            case "RBR":
            case "RBS":
                return POS.ADVERB;
            case "VB":
            case "VBD":
            case "VBG":
            case "VBN":
            case "VBP":
            case "VBZ":
                return POS.VERB;
            default:
                return POS.NOUN;
        }
    }



}

